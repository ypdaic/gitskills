//package com.daiyanping.cms.util;
//
//import com.alibaba.fastjson.JSONObject;
//import com.sungo.report.server.common.base.ReportServer;
//import com.sungo.report.server.common.datasource.DB;
//import com.sungo.report.server.common.datasource.DBTypeEnum;
//import com.sungo.report.server.common.enums.ReportEnum;
//import com.sungo.report.server.common.util.lang.ConstantUtil;
//import com.sungo.report.server.common.util.lang.FileUtil;
//import com.sungo.report.server.rss.entity.Report;
//import com.sungo.report.server.rss.entity.ReportGenRecord;
//import com.sungo.report.server.rss.service.IReportGenService;
//import com.sungo.report.server.smzy.mapper.PatientMapper;
//import com.sungo.report.server.smzy.mapper.ReceiveMapper;
//import com.sungo.report.server.total.service.IProjectProgressMonthReportService;
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.ss.util.CellRangeAddress;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.aop.framework.AopContext;
//import org.springframework.beans.factory.DisposableBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.stereotype.Service;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.*;
//import java.util.concurrent.BrokenBarrierException;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.function.Supplier;
//
//import static com.sungo.report.server.common.util.lang.ConstantUtil.*;
//
///**
// * @ClassName ProjectProgressMonthReportServiceImpl
// * @Description TODO
// * @Author daiyanping
// * @Date 2019-06-26
// * @Version 0.1
// */
//@Service
//@DB(DBTypeEnum.PAP)
//@ReportServer(ReportEnum.PROJECT_PROGRESS_REPORT)
//public class ProjectProgressMonthReportServiceImpl implements IProjectProgressMonthReportService, IReportGenService, DisposableBean {
//
//    Logger logger = LoggerFactory.getLogger(ProjectProgressMonthReportServiceImpl.class);
//
//    @Autowired
//    PatientMapper patientMapper;
//
//    @Autowired
//    ReceiveMapper receiveMapper;
//
//    private final String HOSPITAL_MONTH = "hospitalMonth";
//    private final String DOCTOR_MONTH = "doctorMonth";
//    private final String DRUGSTORE_MONTH = "drugstoreMonth";
//    private final String USER_MONTH = "userMonth";
//    private final String VOLUNTEER_MONTH = "volunteerMonth";
//    private final String MATERIAL_REVIEW_EVERY_MONTH = "materialReviewEveryMonth";
//    private final String PATIENT_APPLICATION_LIST = "patientApplicationList";
//    private final String DRAG_TAKING_LIST = "dragTakingList";
//    private final String MODE_IN_EVERY_MONTH = "modeInEveryMonth";
//    private final String OUTWITH_OTHER_REASON_EVERY_MONTH = "outWithOtherReasonEveryMonth";
//    private final String PATIENT_ADDED_EVERY_MONTH = "patientAddedEveryMonth";
//    private final String MATERIAL_REVIEW_MONTH = "materialReviewMonth";
//    private final String AC_MONTH = "acMonth";
//    private final String PC_MONTH = "pcMonth";
//    private final String TOTAL_LIBRARY_STORAGE_EVERY_MONTH = "totalLibraryStorageEveryMonth";
//    private final String TOTAL_LIBRARY_STORAGE_MONTH = "totalLibraryStorageMonth";
//    private final String ON_THE_WAY = "onTheWay";
//    private final String DRAG_TAKING_EVERY_MONTH = "dragTakingEveryMonth";
//    private final String DRUG_STORE_REMAINING_MONTH = "drugStoreRemainingMonth";
//    private final String DRUG_INFO = "drugInfo";
//    private final String TOTAL_LIBRARY_REMAINING_MONTH = "totalLibraryRemainingMonth";
//    private final String MONTH_TIME_LIST = "monthTimeList";
//    private final String HAS_DISPENSE_AID_FUNDS_MONTH = "hasDispenseAidFundsMonth";
//    private final String HAS_DISPENSE_AID_FUNDS_EVERY_MONTH = "hasDispenseAidFundsEveryMonth";
//    private final String PATIENT_SERVICE_EVERY_MONTH = "patientServiceEveryMonth";
//    private final String CHARITY_SITUATION_MONTH = "charitySituationMonth";
//    private final String DISPENSING_MEDICINE_EVERY_MONTH = "dispensingMedicineEveryMonth";
//    private final String DISPENSING_MEDICINE_MONTH = "dispensingMedicineMonth";
//
//    // id分批大小
//    private final int SIZE_OF_EACH_QUERY_FOR_ID = 20000;
//
//    // 月份分批大小
//    private final int SIZE_OF_EACH_QUERY_FOR_MONTH = 4;
//
//    // 允许8个库同时查询
//    @Autowired
//    ThreadPoolTaskExecutor taskExecutor;
//
//    // 允许8个SQL同时查询
//    private final ExecutorService executorService = Executors.newFixedThreadPool(8);
//
//    @Override
//    public File genReportFile(Report report, ReportGenRecord reportGenRecord, JSONObject params, String parentPath) {
//        File reportFile = FileUtil.getReportFile(report, parentPath);
//        Object proxy = AopContext.currentProxy();
//        File file = ((ProjectProgressMonthReportServiceImpl) proxy).projectMonthlyReport(reportFile, report, params, reportGenRecord);
//        return file;
//    }
//
//    @Override
//    public File projectMonthlyReport(File reportFile, Report report, JSONObject params, ReportGenRecord reportGenRecord) {
//        HSSFWorkbook wb = null;
//        FileOutputStream fileOutputStream = null;
//        try {
//            List<Map<String, Object>> projectInfo = patientMapper.queryAllProjectInfo();
//            if (CollectionUtils.isNotEmpty(projectInfo)) {
//                ArrayList<Map<String, Object>> existProjectList = new ArrayList<>();
//                projectInfo.forEach(map -> {
//                    String database = (String) map.get("database");
//                    List<Map<String, Object>> maps = patientMapper.checkDatabaseExist(database);
//                    if (CollectionUtils.isNotEmpty(maps)) {
//                        existProjectList.add(map);
//                    }
//                });
//                wb = initExcel(existProjectList);
//                ArrayList<CompletableFuture> completableFutures = new ArrayList<>();
//                if (CollectionUtils.isNotEmpty(existProjectList)) {
//
//                    for (int i = 0; i < existProjectList.size(); i++) {
//                        // 多线程查询
//                        Multithreading multithreading = new Multithreading(existProjectList, i, ((IProjectProgressMonthReportService) AopContext.currentProxy()));
//                        CompletableFuture<Void> completableFuture = CompletableFuture.supplyAsync(multithreading, taskExecutor);
//                        completableFutures.add(completableFuture);
//
//                    }
//                    // 等待查询结果
//                    List<Object> objects = new ArrayList<>();
//                    for (CompletableFuture completableFuture : completableFutures) {
//                        Object dataMap = completableFuture.get();
//                        objects.add(dataMap);
//                    }
//                    // 填充Excel
//                    populateExcel(existProjectList, wb, objects);
//
//                }
//            }
//            fileOutputStream = new FileOutputStream(reportFile);
//            wb.write(fileOutputStream);
//
//        } catch (Exception e) {
//            logger.error("导出失败！", e);
//            // 删除本地报表
//            cn.hutool.core.io.FileUtil.del(reportFile);
//            return null;
//        } finally {
//            FileUtil.close(wb);
//            FileUtil.close(fileOutputStream);
//        }
//        return reportFile;
//    }
//
//    private void populateExcel(List<Map<String, Object>> projectInfo, HSSFWorkbook wb, List<Object> objects) {
//        CellStyle projectOverviewStyle = setProjectOverviewStyle(wb);
//        CellStyle dataContentStyle = setDataContentStyle(wb);
//        CellStyle fistContentStyle = setExcelFistContentStyle(wb);
//        // 填充Excel表格
//        for (int i = 0; i < projectInfo.size(); i++) {
//            Map<String, Object> map = projectInfo.get(i);
//            String versions = (String) map.get("versions");
//            Map<String, List<Map<String, Object>>> dataMap = (Map<String, List<Map<String, Object>>>) objects.get(i);
//            List<Map<String, Object>> monthTimeListMap = dataMap.get(MONTH_TIME_LIST);
//            Map<String, Object> monthTimeMap = monthTimeListMap.get(0);
//            List<String> monthTimeList = (List<String>) monthTimeMap.get(MONTH_TIME_LIST);
//
//            HSSFSheet sheet = wb.getSheetAt(i);
//
//            // 生成时间第一列
//            generateTimeCell(sheet, projectOverviewStyle);
//
//            // 填充时间数据
//            generateTimeStatistics(sheet, monthTimeList, dataContentStyle);
//            int drugEndCellIndex = 0;
//            // 药品援助
//            if (!FINANCIAL_ASSISTANCE.equals(versions)) {
//
//                // 填充项目概况数据
//                generateProjectOverviewStatisticsWithDrug(dataMap, sheet, projectOverviewStyle, monthTimeList, dataContentStyle);
//                // 填充覆盖范围数据
//                generateCoverageStatistics(dataMap, sheet, fistContentStyle, monthTimeList, dataContentStyle, DRUG_COVERAGE_START_CELL_INDEX, DRUG_COVERAGE_END_CELL_INDEX);
//                // 填充惠及患者数据
//                int drugFlowStartCellIndex = generateBenefitPatientsStatistics(dataMap, sheet, fistContentStyle, monthTimeList, dataContentStyle, DRUG_BENEFIT_PATIENTS_START_CELL_INDEX);
//                // 填充药品流向数据
//                drugEndCellIndex = generateDrugFlowData(sheet, drugFlowStartCellIndex, fistContentStyle, monthTimeList, dataMap, dataContentStyle);
//            } else {
//                // 资金援助
//                generateProjectOverviewStatisticsWithFunds(dataMap, sheet, projectOverviewStyle, monthTimeList, dataContentStyle);
//                // 填充覆盖范围数据
//                generateCoverageStatistics(dataMap, sheet, fistContentStyle, monthTimeList, dataContentStyle, FUNDS_COVERAGE_START_CELL_INDEX, FUNDS_COVERAGE_END_CELL_INDEX);
//                // 填充惠及患者数据
//                int capitalFlowsStartCellIndex = generateBenefitPatientsStatistics(dataMap, sheet, fistContentStyle, monthTimeList, dataContentStyle, FUNDS_BENEFIT_PATIENTS_START_CELL_INDEX);
//                drugEndCellIndex = generateCapitalFlowsData(sheet, capitalFlowsStartCellIndex, fistContentStyle, monthTimeList, dataMap, dataContentStyle);
//            }
//
//            generateCharitySituation(sheet, drugEndCellIndex, fistContentStyle, monthTimeList, dataMap, dataContentStyle);
//            ExcelUtil.fillingNullCell(sheet, dataContentStyle, DATA_START_ROW_INDEX, PROJECT_OVERVIEW_START_CELL_INDEX);
//            if (!FINANCIAL_ASSISTANCE.equals(versions)) {
//
//                // 冻结前4行，前4列
//                sheet.createFreezePane(DRUG_PROJECT_OVERVIEW_END_CELL_INDEX + 1, DATA_START_ROW_INDEX, DRUG_PROJECT_OVERVIEW_END_CELL_INDEX + 2, DATA_START_ROW_INDEX + 1);
//            } else {
//                // 冻结前4行，前3列
//                sheet.createFreezePane( FUNDS_PROJECT_OVERVIEW_END_CELL_INDEX + 1, DATA_START_ROW_INDEX, FUNDS_PROJECT_OVERVIEW_END_CELL_INDEX + 2, DATA_START_ROW_INDEX + 1);
//            }
//        }
//    }
//
//    @Override
//    public void destroy() throws Exception {
//        executorService.shutdown();
//    }
//
//    private static class Multithreading implements Supplier {
//        private List<Map<String, Object>> projectInfo;
//        private int projectIndex;
//        private IProjectProgressMonthReportService projectProgressMonthReportService;
//
//        public Multithreading(List<Map<String, Object>> projectInfo, int projectIndex, IProjectProgressMonthReportService projectProgressMonthReportService) {
//            this.projectInfo = projectInfo;
//            this.projectIndex = projectIndex;
//            this.projectProgressMonthReportService = projectProgressMonthReportService;
//        }
//
//        @Override
//        public Object get() {
//            return projectProgressMonthReportService.multithreading(projectInfo, projectIndex, projectProgressMonthReportService);
//        }
//
//    }
//
//    /**
//     * 多线程处理
//     * @param projectInfo
//     * @param projectIndex
//     * @throws BrokenBarrierException
//     * @throws InterruptedException
//     */
//    @Override
//    public Object multithreading(List<Map<String, Object>> projectInfo, int projectIndex, IProjectProgressMonthReportService projectProgressMonthReportService) {
//        List<String> monthTimeList = new ArrayList<>();
//        List<Map<String, Object>> monthLastDateList = new ArrayList<>();
//        Map<String, Object> map = projectInfo.get(projectIndex);
//        String database = (String) map.get("database");
//        String versions = (String) map.get("versions");
//
//        Report report = new Report();
//        // 设置数据源
//        report.setDataBase(database);
//
//        return queryData(monthTimeList, report, monthLastDateList, versions, projectProgressMonthReportService);
//    }
//
//    /**
//     * 公益员专员概况数据
//     * @param sheet
//     * @param drugEndCellIndex
//     * @param fistContentStyle
//     */
//    private void generateCharitySituation(HSSFSheet sheet, int drugEndCellIndex, CellStyle fistContentStyle, List<String> monthTimeList, Map<String, List<Map<String, Object>>> dataMap, CellStyle dataContentStyle) {
//        int charitySituationEndCellIndex = drugEndCellIndex + 9;
//        CellStyle charitySituationStyle = setProjectOverviewStyle(sheet.getWorkbook());
//        for (int k = 0; k <= HEAD_ROW_THIRD_INDEX; k++) {
//            HSSFRow row = sheet.getRow(k);
//
//            for (int j = drugEndCellIndex + 1; j <= charitySituationEndCellIndex; j++) {
//                HSSFCell cell = row.createCell(j);
//                if (k == 0) {
//                    cell.setCellStyle(fistContentStyle);
//                } else {
//                    cell.setCellStyle(charitySituationStyle);
//                }
//                if (k == HEAD_ROW_START_INDEX && j == drugEndCellIndex + 1) {
//                    cell.setCellValue("公益专员概况");
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j == drugEndCellIndex + 1) {
//                    cell.setCellValue("已完成\n医院签署\n数量");
//                    //创建合并单元格
//                    CellRangeAddress region = new CellRangeAddress(HEAD_ROW_TWO_INDEX, HEAD_ROW_THIRD_INDEX, j, j);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                    //在sheet里增加合并单元格
//                    sheet.addMergedRegion(region);
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j == drugEndCellIndex + 2) {
//                    cell.setCellValue("患者咨询(人次)");
//                    //创建合并单元格
//                    CellRangeAddress region = new CellRangeAddress(HEAD_ROW_TWO_INDEX, HEAD_ROW_TWO_INDEX, j, j + 1);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                    //在sheet里增加合并单元格
//                    sheet.addMergedRegion(region);
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j == drugEndCellIndex + 4) {
//                    cell.setCellValue("资料预审（人次）");
//                    //创建合并单元格
//                    CellRangeAddress region = new CellRangeAddress(HEAD_ROW_TWO_INDEX, HEAD_ROW_TWO_INDEX, j, j + 1);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                    //在sheet里增加合并单元格
//                    sheet.addMergedRegion(region);
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j == drugEndCellIndex + 6) {
//                    cell.setCellValue("协助发药（人次）");
//                    //创建合并单元格
//                    CellRangeAddress region = new CellRangeAddress(HEAD_ROW_TWO_INDEX, HEAD_ROW_TWO_INDEX, j, j + 1);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                    //在sheet里增加合并单元格
//                    sheet.addMergedRegion(region);
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j == drugEndCellIndex + 8) {
//                    cell.setCellValue("医护拜访\n" +
//                            "（医院名称、次数、目的）");
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j == drugEndCellIndex + 9) {
//                    cell.setCellValue("管理者区域巡视次数、地区、目的");
//                }
//                if (k == HEAD_ROW_THIRD_INDEX && j >= drugEndCellIndex + 2 && j <= drugEndCellIndex + 7) {
//                    if ((j - drugEndCellIndex) % 2 == 0) {
//
//                        cell.setCellValue("累计");
//                    } else {
//                        cell.setCellValue("本月新增");
//                    }
//                }
//                if (k == HEAD_ROW_THIRD_INDEX && j > drugEndCellIndex + 7) {
//                    cell.setCellValue("本月");
//                }
//            }
//            if (k == HEAD_ROW_START_INDEX) {
//                //创建合并单元格
//                CellRangeAddress region = new CellRangeAddress(HEAD_ROW_START_INDEX, HEAD_ROW_START_INDEX, drugEndCellIndex + 1, drugEndCellIndex + 9);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                //在sheet里增加合并单元格
//                sheet.addMergedRegion(region);
//            }
//        }
//
//        int dataRow = 0;
//        for (String monthTime : monthTimeList) {
//
//            HSSFRow row = sheet.getRow(dataRow + DATA_START_ROW_INDEX);
//            List<Map<String, Object>> patientServiceEveryMonth = dataMap.get(PATIENT_SERVICE_EVERY_MONTH);
//            if (CollectionUtils.isNotEmpty(patientServiceEveryMonth)) {
//
//                for (Map<String, Object> map : patientServiceEveryMonth) {
//                    String mothTimeRow = (String) map.get("monthTime");
//                    Long patientConsultationTotal = (Long) map.get("patientConsultationTotal");
//                    Long preAuditTotal = (Long) map.get("preAuditTotal");
//                    Long hospitalVisitTotal = (Long) map.get("hospitalVisitTotal");
//                    if (mothTimeRow.equals(monthTime)) {
//                        generateAddedEveryMonthData(row, patientConsultationTotal, dataContentStyle, drugEndCellIndex + 3);
//                        generateAddedEveryMonthData(row, preAuditTotal, dataContentStyle, drugEndCellIndex + 5);
//                        generateAddedEveryMonthData(row, hospitalVisitTotal, dataContentStyle, drugEndCellIndex + 8);
//                    }
//                }
//            }
//            List<Map<String, Object>> dispensingMedicineEveryMonth = dataMap.get(DISPENSING_MEDICINE_EVERY_MONTH);
//            if (CollectionUtils.isNotEmpty(dispensingMedicineEveryMonth)) {
//
//                for (Map<String, Object> map : dispensingMedicineEveryMonth) {
//                    String mothTimeRow = (String) map.get("monthTime");
//                    BigDecimal drugDispenseTotal = (BigDecimal) map.get("drugDispenseTotal");
//                    if (mothTimeRow.equals(monthTime)) {
//                        generateAddedEveryMonthData(row, drugDispenseTotal, dataContentStyle, drugEndCellIndex + 7);
//                    }
//                }
//            }
//            List<Map<String, Object>> charitySituationMonth = dataMap.get(CHARITY_SITUATION_MONTH);
//            if (CollectionUtils.isNotEmpty(charitySituationMonth)) {
//                for (Map<String, Object> map : charitySituationMonth) {
//
//                    generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, drugEndCellIndex + 1, "hospitalSigningTotal");
//                    generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, drugEndCellIndex + 2, "patientConsultationTotal");
//                    generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, drugEndCellIndex + 4, "preAuditTotal");
//                }
//            }
//
//            List<Map<String, Object>> dispensingMedicineMonth = dataMap.get(DISPENSING_MEDICINE_MONTH);
//            if (CollectionUtils.isNotEmpty(dispensingMedicineMonth)) {
//                for (Map<String, Object> map : dispensingMedicineMonth) {
//
//                    generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, drugEndCellIndex + 6, "drugDispenseTotal");
//                }
//            }
//
//            dataRow++;
//        }
//
//
//    }
//
//    /**
//     * 填充药品流向数据
//     * @param sheet
//     * @param drugFlowStartCellIndex
//     * @param fistContentStyle
//     * @param monthTimeList
//     * @param dataMap
//     * @param dataContentStyle
//     * @return
//     */
//    private int generateDrugFlowData(HSSFSheet sheet, int drugFlowStartCellIndex, CellStyle fistContentStyle, List<String> monthTimeList, Map<String, List<Map<String, Object>>> dataMap, CellStyle dataContentStyle) {
//        CellStyle drugFlowStyle = setDrugFlowStyle(sheet.getWorkbook());
//        List<Map<String, Object>> drugInfo = dataMap.get(DRUG_INFO);
//        // AE报告结束cell的index
//        int AEEndCellIndex = drugFlowStartCellIndex + 1;
//
//        // 总库入库结束cell的index
//        int totalLibraryStorageEndCellIndex = 0;
//        if (CollectionUtils.isNotEmpty(drugInfo)) {
//            totalLibraryStorageEndCellIndex = AEEndCellIndex + 2 + drugInfo.size() * 2;
//        } else {
//            totalLibraryStorageEndCellIndex = AEEndCellIndex + 4;
//        }
//
//        // 总库剩余结束cell的index
//        int totalRemainingEndCellIndex = 0;
//        if (CollectionUtils.isNotEmpty(drugInfo)) {
//            totalRemainingEndCellIndex = totalLibraryStorageEndCellIndex + drugInfo.size() * 2;
//        } else {
//            totalRemainingEndCellIndex = totalLibraryStorageEndCellIndex + 2;
//        }
//
//        // 出库配送统计结束cell的index
//        int outboundDeliveryStatisticsEndCellIndex = 0;
//        if (CollectionUtils.isNotEmpty(drugInfo)) {
//            outboundDeliveryStatisticsEndCellIndex = totalRemainingEndCellIndex + 1 + drugInfo.size();
//        } else {
//            outboundDeliveryStatisticsEndCellIndex = totalRemainingEndCellIndex + 2;
//        }
//
//        // 患者已领药品结束cell的index
//        int patientReceivedDrugsEndCellIndex = 0;
//        if (CollectionUtils.isNotEmpty(drugInfo)) {
//            patientReceivedDrugsEndCellIndex = outboundDeliveryStatisticsEndCellIndex + 7 + drugInfo.size() * 2;
//        } else {
//            patientReceivedDrugsEndCellIndex = outboundDeliveryStatisticsEndCellIndex + 8;
//        }
//
//        // 药品流向结束cell的index
//        int drugEndCellIndex = 0;
//        if (CollectionUtils.isNotEmpty(drugInfo)) {
//            drugEndCellIndex = patientReceivedDrugsEndCellIndex + drugInfo.size();
//        } else {
//            drugEndCellIndex = patientReceivedDrugsEndCellIndex + 1;
//        }
//        for (int k = 0; k <= HEAD_ROW_THIRD_INDEX; k++) {
//            HSSFRow row = sheet.getRow(k);
//
//            for (int j = drugFlowStartCellIndex; j <= drugEndCellIndex; j++) {
//                HSSFCell cell = row.createCell(j);
//                if (k == 0) {
//                    cell.setCellStyle(fistContentStyle);
//                } else {
//                    cell.setCellStyle(drugFlowStyle);
//                }
//                if (k == HEAD_ROW_START_INDEX && j == drugFlowStartCellIndex) {
//                    cell.setCellValue("药品流向");
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j == drugFlowStartCellIndex) {
//                    cell.setCellValue("AE报告");
//                    //创建合并单元格
//                    CellRangeAddress region = new CellRangeAddress(HEAD_ROW_TWO_INDEX, HEAD_ROW_TWO_INDEX, j, j + 1);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                    //在sheet里增加合并单元格
//                    sheet.addMergedRegion(region);
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j == AEEndCellIndex + 1) {
//                    cell.setCellValue("总库入库");
//                    //创建合并单元格
//                    CellRangeAddress region = new CellRangeAddress(HEAD_ROW_TWO_INDEX, HEAD_ROW_TWO_INDEX, j, j + 1 + drugInfo.size() * 2);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                    //在sheet里增加合并单元格
//                    sheet.addMergedRegion(region);
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j == totalLibraryStorageEndCellIndex + 1) {
//                    cell.setCellValue("总库剩余");
//                    //创建合并单元格
//                    CellRangeAddress region = new CellRangeAddress(HEAD_ROW_TWO_INDEX, HEAD_ROW_TWO_INDEX, j, totalLibraryStorageEndCellIndex + drugInfo.size() * 2);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                    //在sheet里增加合并单元格
//                    sheet.addMergedRegion(region);
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j == totalRemainingEndCellIndex + 1) {
//                    cell.setCellValue("出库配送统计");
//                    //创建合并单元格
//                    CellRangeAddress region = new CellRangeAddress(HEAD_ROW_TWO_INDEX, HEAD_ROW_TWO_INDEX, j, totalRemainingEndCellIndex + 1 + drugInfo.size());// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                    //在sheet里增加合并单元格
//                    sheet.addMergedRegion(region);
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j == outboundDeliveryStatisticsEndCellIndex + 1) {
//                    cell.setCellValue("商业药品（支数）");
//
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j == outboundDeliveryStatisticsEndCellIndex + 2) {
//                    cell.setCellValue("免税药品（支数）");
//
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j == outboundDeliveryStatisticsEndCellIndex + 3) {
//                    cell.setCellValue("在途");
//                    //创建合并单元格
//                    CellRangeAddress region = new CellRangeAddress(HEAD_ROW_TWO_INDEX, HEAD_ROW_THIRD_INDEX, j, j);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                    //在sheet里增加合并单元格
//                    sheet.addMergedRegion(region);
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j == outboundDeliveryStatisticsEndCellIndex + 4) {
//                    cell.setCellValue("患者已领药品");
//                    //创建合并单元格
//                    CellRangeAddress region = new CellRangeAddress(HEAD_ROW_TWO_INDEX, HEAD_ROW_TWO_INDEX, j, j + 3 + drugInfo.size() * 2);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                    //在sheet里增加合并单元格
//                    sheet.addMergedRegion(region);
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j >= patientReceivedDrugsEndCellIndex + 1 && j <= drugEndCellIndex ) {
//                    if (CollectionUtils.isNotEmpty(drugInfo)) {
//                        for (Map<String, Object> map : drugInfo) {
//                            String specifications = (String) map.get("specifications");
//                            cell.setCellValue("各地药房\n库存\n总剩余\n" + specifications);
//                        }
//                    } else {
//
//                        cell.setCellValue("各地药房\n库存\n总剩余");
//                    }
//                    //创建合并单元格
//                    CellRangeAddress region = new CellRangeAddress(HEAD_ROW_TWO_INDEX, HEAD_ROW_THIRD_INDEX, j, j);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                    //在sheet里增加合并单元格
//                    sheet.addMergedRegion(region);
//                }
//                if (k == HEAD_ROW_THIRD_INDEX && j == drugFlowStartCellIndex) {
//                    cell.setCellValue("AE");
//                }
//                if (k == HEAD_ROW_THIRD_INDEX && j == drugFlowStartCellIndex + 1) {
//                    cell.setCellValue("PC");
//                }
//                if (k == HEAD_ROW_THIRD_INDEX && j == AEEndCellIndex + 1) {
//                    cell.setCellValue("商业\n（累计）");
//                }
//                if (k == HEAD_ROW_THIRD_INDEX && j>= AEEndCellIndex + 2 && j<= AEEndCellIndex + 1 + drugInfo.size()) {
//                    if (CollectionUtils.isNotEmpty(drugInfo)) {
//                        for (Map<String, Object> map : drugInfo) {
//                            String specifications = (String) map.get("specifications");
//                            cell.setCellValue("新增\n" + specifications);
//                        }
//                    } else {
//
//                        cell.setCellValue("新增");
//                    }
//                }
//                if (k == HEAD_ROW_THIRD_INDEX && j == AEEndCellIndex + 2 + drugInfo.size()) {
//                    cell.setCellValue("免税\n（累计）");
//                }
//                if (k == HEAD_ROW_THIRD_INDEX && j >= AEEndCellIndex + 3 + drugInfo.size() && j <= totalLibraryStorageEndCellIndex) {
//                    if (CollectionUtils.isNotEmpty(drugInfo)) {
//                        for (Map<String, Object> map : drugInfo) {
//                            String specifications = (String) map.get("specifications");
//                            cell.setCellValue("新增\n" + specifications);
//                        }
//                    } else {
//
//                        cell.setCellValue("新增");
//                    }
//                }
//                if (k == HEAD_ROW_THIRD_INDEX && j >= totalLibraryStorageEndCellIndex + 1 && j<= totalLibraryStorageEndCellIndex + drugInfo.size()) {
//
//                    if (CollectionUtils.isNotEmpty(drugInfo)) {
//                        for (Map<String, Object> map : drugInfo) {
//                            String specifications = (String) map.get("specifications");
//                            cell.setCellValue("商业\n（支数）\n" + specifications);
//                        }
//                    } else {
//
//                        cell.setCellValue("商业\n（支数）");
//                    }
//                }
//                if (k == HEAD_ROW_THIRD_INDEX && j >= totalLibraryStorageEndCellIndex + drugInfo.size() + 1 && j <= totalRemainingEndCellIndex) {
//
//                    if (CollectionUtils.isNotEmpty(drugInfo)) {
//                        for (Map<String, Object> map : drugInfo) {
//                            String specifications = (String) map.get("specifications");
//                            cell.setCellValue("免税\n（支数）\n" + specifications);
//                        }
//                    } else {
//
//                        cell.setCellValue("免税\n（支数）");
//                    }
//                }
//                if (k == HEAD_ROW_THIRD_INDEX && (j == totalRemainingEndCellIndex + 1 || j == outboundDeliveryStatisticsEndCellIndex + 1 || j == outboundDeliveryStatisticsEndCellIndex + 2 || j == outboundDeliveryStatisticsEndCellIndex + 4)) {
//                    cell.setCellValue("累计");
//                }
//                if (k == HEAD_ROW_THIRD_INDEX && j >= totalRemainingEndCellIndex + 2 && j <= outboundDeliveryStatisticsEndCellIndex) {
//                    if (CollectionUtils.isNotEmpty(drugInfo)) {
//                        for (Map<String, Object> map : drugInfo) {
//                            String specifications = (String) map.get("specifications");
//                            cell.setCellValue("新增\n" + specifications);
//                        }
//                    } else {
//
//                        cell.setCellValue("新增");
//                    }
//                }
//                if (k == HEAD_ROW_THIRD_INDEX && j >= outboundDeliveryStatisticsEndCellIndex + 5 && j <= outboundDeliveryStatisticsEndCellIndex + 4 + drugInfo.size()) {
//                    if (CollectionUtils.isNotEmpty(drugInfo)) {
//                        for (Map<String, Object> map : drugInfo) {
//                            String specifications = (String) map.get("specifications");
//                            cell.setCellValue("新增\n" + specifications);
//                        }
//                    } else {
//
//                        cell.setCellValue("新增");
//                    }
//                }
//                if (k == HEAD_ROW_THIRD_INDEX && j == outboundDeliveryStatisticsEndCellIndex + 5 + drugInfo.size()) {
//                    cell.setCellValue("商业支数累计");
//                }
//                if (k == HEAD_ROW_THIRD_INDEX && j >= outboundDeliveryStatisticsEndCellIndex + 6 + drugInfo.size() && j <= outboundDeliveryStatisticsEndCellIndex + 5 + drugInfo.size() * 2) {
//                    if (CollectionUtils.isNotEmpty(drugInfo)) {
//                        for (Map<String, Object> map : drugInfo) {
//                            String specifications = (String) map.get("specifications");
//                            cell.setCellValue("新增\n" + specifications);
//                        }
//                    } else {
//
//                        cell.setCellValue("新增");
//                    }
//                }
//                if (k == HEAD_ROW_THIRD_INDEX && j == outboundDeliveryStatisticsEndCellIndex + 6 + drugInfo.size() * 2) {
//                    cell.setCellValue("免税支数累计");
//                }
//                if (k == HEAD_ROW_THIRD_INDEX && j == patientReceivedDrugsEndCellIndex) {
//                    cell.setCellValue("总价值");
//                    sheet.setColumnWidth(j, (int) ((20 + 0.72) * 256));
//                }
//
//            }
//            if (k == HEAD_ROW_START_INDEX) {
//                //创建合并单元格
//                CellRangeAddress region = new CellRangeAddress(HEAD_ROW_START_INDEX, HEAD_ROW_START_INDEX, drugFlowStartCellIndex, drugEndCellIndex);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                //在sheet里增加合并单元格
//                sheet.addMergedRegion(region);
//            }
//        }
//
//        int dataRow = 0;
//        for (String monthTime : monthTimeList) {
//
//            HSSFRow row = sheet.getRow(dataRow + DATA_START_ROW_INDEX);
////            List<Map<String, Object>> ACMonth = dataMap.get(AC_MONTH);
////            if (CollectionUtils.isNotEmpty(ACMonth)) {
////
////                for (Map<String, Object> map : ACMonth) {
////                    generateMonthlyAccumulationData(map, dataRow, row, dataContentStyle, drugFlowStartCellIndex, "ACTotal");
////                }
////            }
////
////            List<Map<String, Object>> PCMonth = dataMap.get(PC_MONTH);
////            if (CollectionUtils.isNotEmpty(PCMonth)) {
////
////                for (Map<String, Object> map : PCMonth) {
////                    generateMonthlyAccumulationData(map, dataRow, row, dataContentStyle, AEEndCellIndex, "PCTotal");
////
////                }
////            }
//            generateACAndPcData(dataMap, monthTime, row, dataContentStyle, drugFlowStartCellIndex, AEEndCellIndex);
//            List<Map<String, Object>> totalLibraryStorageMonth = dataMap.get(TOTAL_LIBRARY_STORAGE_MONTH);
//            if (CollectionUtils.isNotEmpty(totalLibraryStorageMonth)) {
//
//                for (Map<String, Object> map : totalLibraryStorageMonth) {
//                    generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, AEEndCellIndex + 1, "businessInTotal");
//                    generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, AEEndCellIndex + 2 + drugInfo.size(), "dutyFreeInTotal");
//                    generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, totalRemainingEndCellIndex + 1, "allOutTotal");
//                    generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, outboundDeliveryStatisticsEndCellIndex + 1, "businessOutTotal");
//                    generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, outboundDeliveryStatisticsEndCellIndex + 2, "dutyFreeOutTotal");
//
//                }
//            }
//            List<Map<String, Object>> totalLibraryStorageEveryMonth = dataMap.get(TOTAL_LIBRARY_STORAGE_EVERY_MONTH);
//            if (CollectionUtils.isNotEmpty(totalLibraryStorageEveryMonth)) {
//                int newCommercialDrugsCellIndex = AEEndCellIndex + 2;
//                int newFreeDrugsCellIndex = AEEndCellIndex + 3 + drugInfo.size();
//                int allOutCellIndex = totalRemainingEndCellIndex + 2;
//                for (Map<String, Object> drugMap : drugInfo) {
//                    Long id = (Long) drugMap.get("id");
//                    for (Map<String, Object> map : totalLibraryStorageEveryMonth) {
//                        String mothTimeRow = (String) map.get("monthTime");
//                        BigDecimal businessInTotal = (BigDecimal) map.get("businessInTotal");
//                        BigDecimal dutyFreeInTotal = (BigDecimal) map.get("dutyFreeInTotal");
//                        BigDecimal allOutTotal = (BigDecimal) map.get("allOutTotal");
//                        Long drugId = (Long) map.get("drugId");
//
//                        if (mothTimeRow.equals(monthTime) && id.equals(drugId)) {
//                            generateAddedEveryMonthData(row, businessInTotal, dataContentStyle, newCommercialDrugsCellIndex);
//                            generateAddedEveryMonthData(row, dutyFreeInTotal, dataContentStyle, newFreeDrugsCellIndex);
//                            generateAddedEveryMonthData(row, allOutTotal, dataContentStyle, allOutCellIndex);
//                        }
//                    }
//                    newCommercialDrugsCellIndex++;
//                    newFreeDrugsCellIndex++;
//                    allOutCellIndex++;
//                }
//            }
//
//            List<Map<String, Object>> totalLibraryRemainingMonth = dataMap.get(TOTAL_LIBRARY_REMAINING_MONTH);
//            if (CollectionUtils.isNotEmpty(totalLibraryRemainingMonth)) {
//                int businessRemainingCellIndex = totalLibraryStorageEndCellIndex + 1;
//                int dutyFreeRemainingTotalCellIndex = totalLibraryStorageEndCellIndex + 1 + drugInfo.size();
//                for (Map<String, Object> drugMap : drugInfo) {
//                    Long id = (Long) drugMap.get("id");
//                    for (Map<String, Object> map : totalLibraryRemainingMonth) {
//                        Long drugId = (Long) map.get("help");
//
//                        if (id.equals(drugId)) {
//                            generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, businessRemainingCellIndex, "businessRemainingTotal");
//                            generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, dutyFreeRemainingTotalCellIndex, "dutyFreeRemainingTotal");
//                        }
//                    }
//                    businessRemainingCellIndex++;
//                    dutyFreeRemainingTotalCellIndex++;
//                }
//            }
//
//            List<Map<String, Object>> onTheWay = dataMap.get(ON_THE_WAY);
//            if (CollectionUtils.isNotEmpty(onTheWay)) {
//                for (Map<String, Object> map : onTheWay) {
//                    generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, outboundDeliveryStatisticsEndCellIndex + 3, "onTheWayTotal");
//                }
//            }
//
//            List<Map<String, Object>> dragTakingList = dataMap.get(DRAG_TAKING_LIST);
//            if (CollectionUtils.isNotEmpty(dragTakingList)) {
//                for (Map<String, Object> map : dragTakingList) {
//                    generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, outboundDeliveryStatisticsEndCellIndex + 4, "drugTakingTotal");
//                    generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, outboundDeliveryStatisticsEndCellIndex + 5 + drugInfo.size(), "businessTotal");
//                    generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, outboundDeliveryStatisticsEndCellIndex + 6 + drugInfo.size() * 2, "dutyFreeTotal");
//                    generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, patientReceivedDrugsEndCellIndex, "priceTotal");
//                }
//            }
//            List<Map<String, Object>> dragTakingEveryMonth = dataMap.get(DRAG_TAKING_EVERY_MONTH);
//            if (CollectionUtils.isNotEmpty(dragTakingEveryMonth)) {
//                int newCommercialDrugsCellIndex = outboundDeliveryStatisticsEndCellIndex + 5;
//                int newFreeDrugsCellIndex = outboundDeliveryStatisticsEndCellIndex + 6 + drugInfo.size();
//                for (Map<String, Object> drugMap : drugInfo) {
//                    Long id = (Long) drugMap.get("id");
//                    for (Map<String, Object> map : dragTakingEveryMonth) {
//                        String mothTimeRow = (String) map.get("monthTime");
//                        BigDecimal quantityTotal = (BigDecimal) map.get("quantityTotal");
//                        Integer drugId = (Integer) map.get("drugId");
//                        if (mothTimeRow.equals(monthTime) && id.intValue() == drugId.intValue()) {
//                            generateAddedEveryMonthData(row, quantityTotal, dataContentStyle, newCommercialDrugsCellIndex);
//                            generateAddedEveryMonthData(row, quantityTotal, dataContentStyle, newFreeDrugsCellIndex);
//                        }
//
//                    }
//                    newCommercialDrugsCellIndex++;
//                    newFreeDrugsCellIndex++;
//                }
//            }
//            List<Map<String, Object>> drugStoreRemainingMonth = dataMap.get(DRUG_STORE_REMAINING_MONTH);
//            if (CollectionUtils.isNotEmpty(drugStoreRemainingMonth)) {
//                int drugStoreRemainingCellIndex = patientReceivedDrugsEndCellIndex + 1;
//                for (Map<String, Object> drugMap : drugInfo) {
//                    Long id = (Long) drugMap.get("id");
//                    for (Map<String, Object> map : drugStoreRemainingMonth) {
//
//                        Integer drugId = (Integer) map.get("help");
//                        if (id.intValue() == drugId.intValue()) {
//
//                            generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, drugStoreRemainingCellIndex, "drugStoreRemainingTotal");
//                        }
//                    }
//                    drugStoreRemainingCellIndex++;
//                }
//            }
//
//            dataRow++;
//        }
//
//        return drugEndCellIndex;
//
//    }
//
//    /**
//     * 填充资金流向数据
//     * @param sheet
//     * @param capitalFlowsStartCellIndex
//     * @param fistContentStyle
//     * @param monthTimeList
//     * @param dataMap
//     * @param dataContentStyle
//     * @return
//     */
//    private int generateCapitalFlowsData(HSSFSheet sheet, int capitalFlowsStartCellIndex, CellStyle fistContentStyle, List<String> monthTimeList, Map<String, List<Map<String, Object>>> dataMap, CellStyle dataContentStyle) {
//        CellStyle drugFlowStyle = setDrugFlowStyle(sheet.getWorkbook());
//        List<Map<String, Object>> drugInfo = dataMap.get(DRUG_INFO);
//        // AE报告结束cell的index
//        int AEEndCellIndex = capitalFlowsStartCellIndex + 1;
//
//        // 援助资金入账cell的index
//        int accountEntryAidFundsEndCellIndex = AEEndCellIndex + 2;
//        int capitalFlowsEndCellIndex = accountEntryAidFundsEndCellIndex + 3;
//        for (int k = 0; k <= HEAD_ROW_THIRD_INDEX; k++) {
//            HSSFRow row = sheet.getRow(k);
//
//            for (int j = capitalFlowsStartCellIndex; j <= capitalFlowsEndCellIndex; j++) {
//                HSSFCell cell = row.createCell(j);
//                if (k == 0) {
//                    cell.setCellStyle(fistContentStyle);
//                } else {
//                    cell.setCellStyle(drugFlowStyle);
//                }
//                if (k == HEAD_ROW_START_INDEX && j == capitalFlowsStartCellIndex) {
//                    cell.setCellValue("援助资金流向");
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j == capitalFlowsStartCellIndex) {
//                    cell.setCellValue("AE报告");
//                    //创建合并单元格
//                    CellRangeAddress region = new CellRangeAddress(HEAD_ROW_TWO_INDEX, HEAD_ROW_TWO_INDEX, j, j + 1);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                    //在sheet里增加合并单元格
//                    sheet.addMergedRegion(region);
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j == AEEndCellIndex + 1) {
//                    cell.setCellValue("援助资金入账");
//                    //创建合并单元格
//                    CellRangeAddress region = new CellRangeAddress(HEAD_ROW_TWO_INDEX, HEAD_ROW_TWO_INDEX, j, j + 1);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                    //在sheet里增加合并单元格
//                    sheet.addMergedRegion(region);
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j == accountEntryAidFundsEndCellIndex + 1) {
//                    cell.setCellValue("已发放援助资金\n（累计）");
//                    //创建合并单元格
//                    CellRangeAddress region = new CellRangeAddress(HEAD_ROW_TWO_INDEX, HEAD_ROW_THIRD_INDEX, j, j);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                    //在sheet里增加合并单元格
//                    sheet.addMergedRegion(region);
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j == accountEntryAidFundsEndCellIndex + 2) {
//                    cell.setCellValue("当月\n已发放援助资金");
//                    //创建合并单元格
//                    CellRangeAddress region = new CellRangeAddress(HEAD_ROW_TWO_INDEX, HEAD_ROW_THIRD_INDEX, j, j);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                    //在sheet里增加合并单元格
//                    sheet.addMergedRegion(region);
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j == capitalFlowsEndCellIndex) {
//                    cell.setCellValue("援助资金剩余");
//                    //创建合并单元格
//                    CellRangeAddress region = new CellRangeAddress(HEAD_ROW_TWO_INDEX, HEAD_ROW_THIRD_INDEX, j, j);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                    //在sheet里增加合并单元格
//                    sheet.addMergedRegion(region);
//
//                }
//            }
//            if (k == HEAD_ROW_START_INDEX) {
//                //创建合并单元格
//                CellRangeAddress region = new CellRangeAddress(HEAD_ROW_START_INDEX, HEAD_ROW_START_INDEX, capitalFlowsStartCellIndex, capitalFlowsEndCellIndex);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                //在sheet里增加合并单元格
//                sheet.addMergedRegion(region);
//            }
//        }
//
//        int dataRow = 0;
//        for (String monthTime : monthTimeList) {
//
//            HSSFRow row = sheet.getRow(dataRow + DATA_START_ROW_INDEX);
////            List<Map<String, Object>> ACMonth = dataMap.get(AC_MONTH);
////            if (CollectionUtils.isNotEmpty(ACMonth)) {
////
////                for (Map<String, Object> map : ACMonth) {
////                    generateMonthlyAccumulationData(map, dataRow, row, dataContentStyle, drugFlowStartCellIndex, "ACTotal");
////                }
////            }
////
////            List<Map<String, Object>> PCMonth = dataMap.get(PC_MONTH);
////            if (CollectionUtils.isNotEmpty(PCMonth)) {
////
////                for (Map<String, Object> map : PCMonth) {
////                    generateMonthlyAccumulationData(map, dataRow, row, dataContentStyle, AEEndCellIndex, "PCTotal");
////
////                }
////            }
//            generateACAndPcData(dataMap, monthTime, row, dataContentStyle, capitalFlowsStartCellIndex, AEEndCellIndex);
//            List<Map<String, Object>> hasDispenseAidFundsMonth = dataMap.get(HAS_DISPENSE_AID_FUNDS_MONTH);
//            if (CollectionUtils.isNotEmpty(hasDispenseAidFundsMonth)) {
//
//                for (Map<String, Object> map : hasDispenseAidFundsMonth) {
//                    generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, accountEntryAidFundsEndCellIndex + 1, "dispenseAidFundsTotal");
//
//                }
//            }
//            List<Map<String, Object>> hasDispenseAidFundsEveryMonth = dataMap.get(HAS_DISPENSE_AID_FUNDS_EVERY_MONTH);
//            if (CollectionUtils.isNotEmpty(hasDispenseAidFundsEveryMonth)) {
//                for (Map<String, Object> map : hasDispenseAidFundsEveryMonth) {
//                    String mothTimeRow = (String) map.get("monthTime");
//                    BigDecimal dispenseAidFundsTotal = (BigDecimal) map.get("dispenseAidFundsTotal");
//                    if (mothTimeRow.equals(monthTime)) {
//                        generateAddedEveryMonthData(row, dispenseAidFundsTotal, dataContentStyle, accountEntryAidFundsEndCellIndex + 2);
//                    }
//                }
//            }
//
//            dataRow++;
//        }
//
//        return capitalFlowsEndCellIndex;
//
//    }
//
//    /**
//     * 填充AC,PC报告数据
//     * @param dataMap
//     * @param monthTime
//     * @param row
//     * @param dataContentStyle
//     * @param ACCellIndex
//     * @param PCCellIndex
//     */
//    private void generateACAndPcData(Map<String, List<Map<String, Object>>> dataMap, String monthTime, HSSFRow row, CellStyle dataContentStyle, int ACCellIndex, int PCCellIndex) {
//        List<Map<String, Object>> ACMonth = dataMap.get(AC_MONTH);
//        if (CollectionUtils.isNotEmpty(ACMonth)) {
//
//            for (Map<String, Object> map : ACMonth) {
//                generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, ACCellIndex, "ACTotal");
//            }
//        }
//
//        List<Map<String, Object>> PCMonth = dataMap.get(PC_MONTH);
//        if (CollectionUtils.isNotEmpty(PCMonth)) {
//
//            for (Map<String, Object> map : PCMonth) {
//                generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, PCCellIndex, "PCTotal");
//
//            }
//        }
//    }
//
//    /**
//     * 生成月份累计数据
//     * @return
//     */
//    private void generateMonthlyAccumulationData(Map map, String monthTime, HSSFRow row, CellStyle dataContentStyle, int cellIndex, String valueKey) {
//        Set<Map.Entry<String, Object>> entries = map.entrySet();
//        Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
//        while (iterator.hasNext()) {
//            Map.Entry<String, Object> next = iterator.next();
//            String key = next.getKey();
//            Object value = next.getValue();
//
//            if (!key.equals("help")) {
//                String checkMonthTime = key.substring(key.indexOf("_") + 1);
//                String checkKey = key.substring(0, key.indexOf("_"));
//                if (monthTime.equals(checkMonthTime) && valueKey.equals(checkKey)) {
//                    HSSFCell cell = row.createCell(cellIndex);
//                    if (value instanceof Long) {
//
//                        cell.setCellValue((Long) value);
//                    }
//                    if (value instanceof BigDecimal) {
//                        cell.setCellValue(((BigDecimal) value).setScale(4, RoundingMode.HALF_UP).doubleValue());
//                    }
//                    cell.setCellStyle(dataContentStyle);
//                }
//            }
//        }
//    }
//
//    /**
//     * 查询需要统计的数据
//     * @param monthTimeList
//     * @param report
//     * @param monthLastDateList
//     */
//    private Map<String, List<Map<String, Object>>> queryData(List<String> monthTimeList, Report report, List<Map<String, Object>> monthLastDateList, String versions, IProjectProgressMonthReportService projectProgressMonthReportService){
//        Map dataMap = new HashMap<String, List<Map<String, Object>>>();
//        // 获取患者申请月份数据
//        List<Map<String, Object>> monthList = patientMapper.queryPatientMonth(report);
//        if (CollectionUtils.isNotEmpty(monthList)) {
//            for (int j = 0; j < monthList.size(); j++) {
//                Map<String, Object> map = monthList.get(j);
//                String monthTime = (String) map.get("monthTime");
//                monthTimeList.add(monthTime);
//
//            }
//        }
//        // 获取医院每月新增数据
//        List<Map<String, Object>> hospitalMonth = patientMapper.queryHospitalMonth(report);
//        getMonth(hospitalMonth, monthTimeList);
//        dataMap.put(HOSPITAL_MONTH, hospitalMonth);
//
//        // 获取医生，护士每月新增数据
//        List<Map<String, Object>> doctorMonth = patientMapper.queryDoctorMonth(report);
//        getMonth(doctorMonth, monthTimeList);
//        dataMap.put(DOCTOR_MONTH, doctorMonth);
//
//        // 获取药房每月新增数据
//        List<Map<String, Object>> drugstoreMonth = patientMapper.queryDrugstoreMonth(report);
//        getMonth(drugstoreMonth, monthTimeList);
//        dataMap.put(DRUGSTORE_MONTH, drugstoreMonth);
//
//        // 获取药师每月新增数据
//        List<Map<String, Object>> userMonth = patientMapper.queryUserMonth(report);
//        getMonth(userMonth, monthTimeList);
//        dataMap.put(USER_MONTH, userMonth);
//
//        // 获取公益员专员每月新增数据
//        List<Map<String, Object>> volunteerMonth = patientMapper.queryVolunteerMonth(report);
//        getMonth(volunteerMonth, monthTimeList);
//        dataMap.put(VOLUNTEER_MONTH, volunteerMonth);
//
//        // 获取AC，PC数据对应的具体表名
//        List<Map<String, Object>> configForAC = patientMapper.queryConfigForAC(report);
//        String ACTableName = "";
//        String PCTableName = "";
//        if (CollectionUtils.isNotEmpty(configForAC)) {
//            for (Map<String, Object> map : configForAC) {
//
//                String key = (String) map.get("key");
//
//                if (StringUtils.isNotEmpty(key) && "adverse_form".equals(key)) {
//                    ACTableName = (String) map.get("value");
//                    ACTableName = getTableName(ACTableName);
//
//                }
//
//                if (StringUtils.isNotEmpty(key) && "product_complaint_form".equals(key)) {
//                    PCTableName = (String) map.get("value");
//                    PCTableName = getTableName(PCTableName);
//
//                }
//            }
//        }
//
//        // 获取AC月份数据
//        List<Map<String, Object>> acMonthGroup = patientMapper.queryACMonthGroup(report, ACTableName);
//        getMonth(acMonthGroup, monthTimeList);
//
//        // 获取PC月份数据
//        List<Map<String, Object>> pcMonthGroup = patientMapper.queryPCMonthGroup(report, PCTableName);
//        getMonth(pcMonthGroup, monthTimeList);
//
//        // 药品援助项目
//        if (!FINANCIAL_ASSISTANCE.equals(versions)){
//            // 获取商业药，免税药每月新增入库数据，所有药品每月出库数据
//            List<Map<String, Object>> totalLibraryStorageEveryMonth = patientMapper.queryTotalLibraryStorageEveryMonth(report);
//            getMonth(totalLibraryStorageEveryMonth, monthTimeList);
//            dataMap.put(TOTAL_LIBRARY_STORAGE_EVERY_MONTH, totalLibraryStorageEveryMonth);
//            // 获取在途月份数据
//            List<Map<String, Object>> onTheWayMonths = patientMapper.queryOnTheWayMonths(report);
//            getMonth(onTheWayMonths, monthTimeList);
//
//            // 获取患者已领药品每月新增数据
//            List<Map<String, Object>> dragTakingEveryMonth = receiveMapper.queryDragTakingEveryMonth(report);
//            getMonth(dragTakingEveryMonth, monthTimeList);
//            dataMap.put(DRAG_TAKING_EVERY_MONTH, dragTakingEveryMonth);
//            // 获取申请材料每月新增数据
//            List<Map<String, Object>> materialReviewEveryMonth = patientMapper.queryMaterialReviewEveryMonth(report);
//            getMonth(materialReviewEveryMonth, monthTimeList);
//            dataMap.put(MATERIAL_REVIEW_EVERY_MONTH, materialReviewEveryMonth);
//        } else {
//            // 每月新增援助资金数据
//            List<Map<String, Object>> hasDispenseAidFundsEveryMonth = patientMapper.queryHasDispenseAidFundsEveryMonth(report);
//            getMonth(hasDispenseAidFundsEveryMonth, monthTimeList);
//            dataMap.put(HAS_DISPENSE_AID_FUNDS_EVERY_MONTH, hasDispenseAidFundsEveryMonth);
//        }
//
//        // 公益专员每月新增数据
//        List<Map<String, Object>> patientServiceEveryMonth = patientMapper.queryPatientServiceEveryMonth(report);
//        getMonth(patientServiceEveryMonth, monthTimeList);
//        dataMap.put(PATIENT_SERVICE_EVERY_MONTH, patientServiceEveryMonth);
//
//        // 公益专员每月新增协助发药数据
//        List<Map<String, Object>> dispensingMedicineEveryMonth = patientMapper.queryDispensingMedicineEveryMonth(report);
//        getMonth(dispensingMedicineEveryMonth, monthTimeList);
//        dataMap.put(DISPENSING_MEDICINE_EVERY_MONTH, dispensingMedicineEveryMonth);
//
//        Collections.sort(monthTimeList, (o1, o2)->{
//            //升序排序
//            return o1.compareTo(o2);
//        });
//
//        monthTimeList.forEach(time -> {
//            Date date = DateUtil.strToDate(time, "yyyy_MM");
//            Map<String, Object> timeMap = new HashMap<>();
//            timeMap.put("key", time);
//            timeMap.put("value", date);
//            monthLastDateList.add(timeMap);
//
//        });
//        ArrayList<Map<String, Object>> mapArrayList = new ArrayList<>();
//        HashMap<String, Object> monthTimeMap = new HashMap<>();
//        monthTimeMap.put(MONTH_TIME_LIST, monthTimeList);
//        mapArrayList.add(monthTimeMap);
//        dataMap.put(MONTH_TIME_LIST, mapArrayList);
//
//        Integer patientCount = patientMapper.queryPatientCount(report);
//        splitMonth(monthLastDateList, (monthLastDateList1, projectProgressMonthReportService1, tableName) -> {
//            List<Map<String, Object>> maps = splitProcessingWithId(patientCount, (monthLastDateList2, report1, startIdIndex, endIdIndex) -> {
//                // 获取申请人数，在组，正常出组，拒绝数据
//                List<Map<String, Object>> patientApplicationMonthWithId = patientMapper.queryPatientApplicationMonth(monthLastDateList2, report1, startIdIndex, endIdIndex);
//                return patientApplicationMonthWithId;
//            }, monthLastDateList1, report, projectProgressMonthReportService1);
//
//            return maps;
//        }, dataMap, PATIENT_APPLICATION_LIST, projectProgressMonthReportService, null);
//
//        // 获取患者每月新增数据
//        List<Map<String, Object>> patientAddedEveryMonth = patientMapper.queryPatientAddedEveryMonth(report);
//        dataMap.put(PATIENT_ADDED_EVERY_MONTH, patientAddedEveryMonth);
//
//        splitMonth(monthLastDateList, (monthLastDateList1, projectProgressMonthReportService1, tableName) -> {
//
//            List<Map<String, Object>> maps = splitProcessingWithId(patientCount, (monthLastDateList2, report1, startIdIndex, endIdIndex) -> {
//                // 获取援助方式入组每月新增数据
//                List<Map<String, Object>> modeInEveryMonth = patientMapper.queryModeInEveryMonth(monthLastDateList2, report1, startIdIndex, endIdIndex);
//                return modeInEveryMonth;
//            }, monthLastDateList1, report, projectProgressMonthReportService1);
//
//            return maps;
//        }, dataMap, MODE_IN_EVERY_MONTH, projectProgressMonthReportService, null);
//
//        splitMonth(monthLastDateList, (monthLastDateList1, projectProgressMonthReportService1, tableName) -> {
//            List<Map<String, Object>> maps = splitProcessingWithId(patientCount, (monthLastDateList2, report1, startIdIndex, endIdIndex) -> {
//                // 获取出组，按出组方式每月新增数据
//                List<Map<String, Object>> outWithOtherReasonEveryMonth = patientMapper.queryOutWithOtherReasonEveryMonth(monthLastDateList2, report1, startIdIndex, endIdIndex);
//                return outWithOtherReasonEveryMonth;
//            }, monthLastDateList1, report, projectProgressMonthReportService1);
//
//            return maps;
//        }, dataMap, OUTWITH_OTHER_REASON_EVERY_MONTH, projectProgressMonthReportService, null);
//
//
//
////        // 获取ac月累计数据
////        List<Map<String, Object>> ACMonth = patientMapper.queryACMonth(monthLastDateList, report, ACTableName);
////        dataMap.put(AC_MONTH, ACMonth);
//        Integer aeCount = patientMapper.queryAEOrPCCount(report, ACTableName);
//        splitMonth(monthLastDateList, (monthLastDateList1, projectProgressMonthReportService1, tableName) -> {
//            List<Map<String, Object>> maps = splitProcessingWithId(aeCount, (monthLastDateList2, report1, startIdIndex, endIdIndex) -> {
//                // 获取ac月累计数据
//                List<Map<String, Object>> ACMonth = patientMapper.queryACMonth(monthLastDateList2, report1, tableName, startIdIndex, endIdIndex);
//                return ACMonth;
//            }, monthLastDateList1, report, projectProgressMonthReportService1);
//
//            return maps;
//        }, dataMap, AC_MONTH, projectProgressMonthReportService, ACTableName);
//
////        // 获取pc月累计数据
////        List<Map<String, Object>> PCMonth = patientMapper.queryPCMonth(monthLastDateList, report, PCTableName);
////        dataMap.put(PC_MONTH, PCMonth);
//        Integer pcCount = patientMapper.queryAEOrPCCount(report, PCTableName);
//        splitMonth(monthLastDateList, (monthLastDateList1, projectProgressMonthReportService1, tableName) -> {
//            List<Map<String, Object>> maps = splitProcessingWithId(pcCount, (monthLastDateList2, report1, startIdIndex, endIdIndex) -> {
//                // 获取pc月累计数据
//                List<Map<String, Object>> PCMonth = patientMapper.queryPCMonth(monthLastDateList2, report1, tableName, startIdIndex, endIdIndex);
//                return PCMonth;
//            }, monthLastDateList1, report, projectProgressMonthReportService1);
//
//            return maps;
//        }, dataMap, PC_MONTH, projectProgressMonthReportService, PCTableName);
//
//        if (!FINANCIAL_ASSISTANCE.equals(versions)) {
//            Integer patientDocumentFeedbackCount = patientMapper.queryPatientDocumentFeedbackCount(report);
//            splitMonth(monthLastDateList, (monthLastDateList1, projectProgressMonthReportService1, tableName) -> {
//                List<Map<String, Object>> maps = splitProcessingWithId(patientDocumentFeedbackCount, (monthLastDateList2, report1, startIdIndex, endIdIndex) -> {
//                    // 获取申请材料月累计数据
//                    List<Map<String, Object>> materialReviewMonth = patientMapper.queryMaterialReviewMonth(monthLastDateList2, report1, startIdIndex, endIdIndex);
//                    return materialReviewMonth;
//                }, monthLastDateList1, report, projectProgressMonthReportService1);
//
//                return maps;
//            }, dataMap, MATERIAL_REVIEW_MONTH, projectProgressMonthReportService, null);
//
//            Integer receiveCount = receiveMapper.queryReceiveCount(report);
//            splitMonth(monthLastDateList, (monthLastDateList1, projectProgressMonthReportService1, tableName) -> {
//                List<Map<String, Object>> maps = splitProcessingWithId(receiveCount, (monthLastDateList2, report1, startIdIndex, endIdIndex) -> {
//                    // 获取商业药，免税药，出库，入库月累计数据
//                    List<Map<String, Object>> dragTakingMonth = receiveMapper.queryDragTakingMonth(monthLastDateList2, report1, startIdIndex, endIdIndex);
//                    return dragTakingMonth;
//                }, monthLastDateList1, report, projectProgressMonthReportService1);
//
//                return maps;
//            }, dataMap, DRAG_TAKING_LIST, projectProgressMonthReportService, null);
//
//            // 查询药品信息
//            List<Map<String, Object>> drugInfo = patientMapper.queryDrugInfo(report);
//            dataMap.put(DRUG_INFO, drugInfo);
//
//            Integer batchStockLogCount = patientMapper.queryBatchStockLogCount(report);
//            splitMonth(monthLastDateList, (monthLastDateList1, projectProgressMonthReportService1, tableName) -> {
//                List<Map<String, Object>> maps = splitProcessingWithId(batchStockLogCount, (monthLastDateList2, report1, startIdIndex, endIdIndex) -> {
//                    // 获取商业药，免税药，出库，入库月累计数据
//                    List<Map<String, Object>> totalLibraryStorageMonth = patientMapper.queryTotalLibraryStorageMonth(monthLastDateList2, report1, startIdIndex, endIdIndex);
//                    return totalLibraryStorageMonth;
//                }, monthLastDateList1, report, projectProgressMonthReportService1);
//
//                return maps;
//            }, dataMap, TOTAL_LIBRARY_STORAGE_MONTH, projectProgressMonthReportService, null);
//
//            Integer deliveryCount = patientMapper.queryDeliveryCount(report);
//            splitMonth(monthLastDateList, (monthLastDateList1, projectProgressMonthReportService1, tableName) -> {
//                List<Map<String, Object>> maps = splitProcessingWithId(deliveryCount, (monthLastDateList2, report1, startIdIndex, endIdIndex) -> {
//                    // 获取在途月累计数据
//                    List<Map<String, Object>> onTheWayMonth = patientMapper.queryOnTheWayMonth(monthLastDateList2, report1, startIdIndex, endIdIndex);
//                    return onTheWayMonth;
//                }, monthLastDateList1, report, projectProgressMonthReportService1);
//
//                return maps;
//            }, dataMap, ON_THE_WAY, projectProgressMonthReportService, null);
//
//            Integer stockLogCount = patientMapper.queryStockLogCount(report);
//            splitMonth(monthLastDateList, (monthLastDateList1, projectProgressMonthReportService1, tableName) -> {
//                List<Map<String, Object>> maps = splitProcessingWithId(stockLogCount, (monthLastDateList2, report1, startIdIndex, endIdIndex) -> {
//                    // 获取各地药房库存剩余数据
//                    List<Map<String, Object>> drugStoreRemainingMonth = patientMapper.queryDrugStoreRemainingMonth(monthLastDateList2, report1, startIdIndex, endIdIndex);
//                    return drugStoreRemainingMonth;
//                }, monthLastDateList1, report, projectProgressMonthReportService1);
//
//                return maps;
//            }, dataMap, DRUG_STORE_REMAINING_MONTH, projectProgressMonthReportService, null);
//
//            splitMonth(monthLastDateList, (monthLastDateList1, projectProgressMonthReportService1, tableName) -> {
//                List<Map<String, Object>> maps = splitProcessingWithId(batchStockLogCount, (monthLastDateList2, report1, startIdIndex, endIdIndex) -> {
//                    // 获取总库剩余按药品规格分类
//                    List<Map<String, Object>> totalLibraryRemainingMonth = patientMapper.queryTotalLibraryRemainingMonth(monthLastDateList2, report1, startIdIndex, endIdIndex);
//                    return totalLibraryRemainingMonth;
//                }, monthLastDateList1, report, projectProgressMonthReportService1);
//
//                return maps;
//            }, dataMap, TOTAL_LIBRARY_REMAINING_MONTH, projectProgressMonthReportService, null);
//        } else {
//            Integer phaseDrugCount = patientMapper.queryPhaseDrugCount(report);
//            splitMonth(monthLastDateList, (monthLastDateList1, projectProgressMonthReportService1, tableName) -> {
//                List<Map<String, Object>> maps = splitProcessingWithId(phaseDrugCount, (monthLastDateList2, report1, startIdIndex, endIdIndex) -> {
//                    // 获取已发援助资金数据
//                    List<Map<String, Object>> hasDispenseAidFundsMonth = patientMapper.queryHasDispenseAidFundsMonth(monthLastDateList2, report1, startIdIndex, endIdIndex);
//                    return hasDispenseAidFundsMonth;
//                }, monthLastDateList1, report, projectProgressMonthReportService1);
//
//                return maps;
//            }, dataMap, HAS_DISPENSE_AID_FUNDS_MONTH, projectProgressMonthReportService, null);
//        }
//
//        Integer volunteerCount = patientMapper.queryVolunteerCount(report);
//        splitMonth(monthLastDateList, (monthLastDateList1, projectProgressMonthReportService1, tableName) -> {
//            List<Map<String, Object>> maps = splitProcessingWithId(volunteerCount, (monthLastDateList2, report1, startIdIndex, endIdIndex) -> {
//                // 公益专员每月累计患者咨询，资料预审数据
//                List<Map<String, Object>> charitySituationMonth = patientMapper.queryCharitySituationMonth(monthLastDateList2, report1, startIdIndex, endIdIndex);
//                return charitySituationMonth;
//            }, monthLastDateList1, report, projectProgressMonthReportService1);
//
//            return maps;
//        }, dataMap, CHARITY_SITUATION_MONTH, projectProgressMonthReportService, null);
//
//        Integer volunteerDrugDispenseCount = patientMapper.queryVolunteerDrugDispenseCount(report);
//        splitMonth(monthLastDateList, (monthLastDateList1, projectProgressMonthReportService1, tableName) -> {
//            List<Map<String, Object>> maps = splitProcessingWithId(volunteerDrugDispenseCount, (monthLastDateList2, report1, startIdIndex, endIdIndex) -> {
//                // 公益专员每月累计协助发药数据
//                List<Map<String, Object>> dispensingMedicineMonth = patientMapper.queryDispensingMedicineMonth(monthLastDateList2, report1, startIdIndex, endIdIndex);
//                return dispensingMedicineMonth;
//            }, monthLastDateList1, report, projectProgressMonthReportService1);
//
//            return maps;
//        }, dataMap, DISPENSING_MEDICINE_MONTH, projectProgressMonthReportService, null);
//
//        return dataMap;
//    }
//
//    /**
//     * 对查询月份进行拆分
//     * @param monthLastDateList
//     * @param splitProcessingWithMonth
//     * @param dataMap
//     * @param key
//     */
//    private void splitMonth(List<Map<String, Object>> monthLastDateList, SplitProcessingWithMonth splitProcessingWithMonth, Map<String, List<Map<String, Object>>> dataMap, String key, IProjectProgressMonthReportService projectProgressMonthReportService, String tableName) {
//        int queryCount = monthLastDateList.size() / SIZE_OF_EACH_QUERY_FOR_MONTH;
//
//        if (monthLastDateList.size() % SIZE_OF_EACH_QUERY_FOR_MONTH != 0) {
//            queryCount++;
//        }
//        List<Map<String, Object>> result = new ArrayList<>();
//        for (int i = 0; i < queryCount; i++) {
//            List<Map<String, Object>> dates = new ArrayList<>();
//            for (int j = 0; j < SIZE_OF_EACH_QUERY_FOR_MONTH; j++) {
//                int index = i * SIZE_OF_EACH_QUERY_FOR_MONTH + j;
//                if (index < monthLastDateList.size()) {
//
//                    Map<String, Object> timeMap = monthLastDateList.get(index);
//                    dates.add(timeMap);
//                }
//            }
//            List<Map<String, Object>> maps = splitProcessingWithMonth.doWith(dates, projectProgressMonthReportService, tableName);
//            // 合并结果
//            for (int k = 0; k < maps.size(); k++) {
//                Map<String, Object> map = maps.get(k);
//                if (result.size() > k ) {
//                    Map<String, Object> map1 = result.get(k);
//                    map1.putAll(map);
//                } else {
//                    result.add(map);
//                }
//            }
//
//        }
//
//        dataMap.put(key, result);
//
//
//    }
//
//    /**
//     * 一次查询月份过多，进行分批处理
//     */
//    @FunctionalInterface
//    public interface SplitProcessingWithMonth {
//
//        List<Map<String, Object>> doWith(List<Map<String, Object>> monthLastDateList, IProjectProgressMonthReportService projectProgressMonthReportService, String tableName);
//    }
//
//    /**
//     * 一次查询数据过多，通过id进行分批处理
//     */
//    @FunctionalInterface
//    public interface SplitProcessingWithId {
//
//        List<Map<String, Object>> doWith(List<Map<String, Object>> monthLastDateList, Report report, int startIdIndex, int endIdIndex);
//    }
//
//    /**
//     * 对id进行分批次查询
//     */
//    private List<Map<String, Object>> splitProcessingWithId(int count, SplitProcessingWithId splitProcessingWithId, List<Map<String, Object>> monthLastDateList, Report report, IProjectProgressMonthReportService projectProgressMonthReportService) {
//        int queryCount = count / SIZE_OF_EACH_QUERY_FOR_ID;
//        if (count % SIZE_OF_EACH_QUERY_FOR_ID != 0) {
//            queryCount++;
//        }
//
//        List<Map<String, Object>> summaryResult = new ArrayList<>();
//        List<CompletableFuture> completableFutures = new ArrayList<>();
//        for (int i = 0; i < queryCount; i++) {
//
//            int startIdIndex = i * SIZE_OF_EACH_QUERY_FOR_ID;
//            int endIdIndex = i * SIZE_OF_EACH_QUERY_FOR_ID + SIZE_OF_EACH_QUERY_FOR_ID;
//            CompletableFuture<List<Map<String, Object>>> completableFuture = CompletableFuture.supplyAsync(() -> {
//
//                return projectProgressMonthReportService.switchDataSource(splitProcessingWithId, monthLastDateList, report, startIdIndex, endIdIndex);
//            }, executorService);
//            completableFutures.add(completableFuture);
//        }
//        try {
//
//            for (CompletableFuture completableFuture : completableFutures) {
//
//                List<Map<String, Object>> maps = (List<Map<String, Object>>) completableFuture.get();
//                summaryResult(maps, summaryResult);
//            }
//        } catch (Exception e) {
//            logger.error("查询失败！",e );
//            return null;
//        }
//
//        return summaryResult;
//
//    }
//
//    /**
//     * 对id切分并行查询时，需要切换数据源
//     * @param splitProcessingWithId
//     * @param monthLastDateList
//     * @param report
//     * @param startIdIndex
//     * @param endIdIndex
//     * @return
//     */
//    public List<Map<String, Object>> switchDataSource(SplitProcessingWithId splitProcessingWithId, List<Map<String, Object>> monthLastDateList, Report report, int startIdIndex, int endIdIndex) {
//        return splitProcessingWithId.doWith(monthLastDateList, report, startIdIndex, endIdIndex);
//    }
//
//    /**
//     * 合并查询的结果
//     * @param resultList
//     * @param summaryResult
//     */
//    private void summaryResult(List<Map<String, Object>> resultList, List<Map<String, Object>> summaryResult) {
//        if (CollectionUtils.isNotEmpty(resultList)) {
//            for (Map<String, Object> map : resultList) {
//                boolean hasAdd = true;
//                for (Map<String, Object> summaryMap : summaryResult) {
//                    Object help1 = summaryMap.get("help");
//                    Set<Map.Entry<String, Object>> entries = map.entrySet();
//                    Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
//                    while (iterator.hasNext()) {
//                        Map.Entry<String, Object> next = iterator.next();
//                        String key = next.getKey();
//                        Object value = next.getValue();
//                        if ("help".equals(key) && value.toString().equals(help1.toString())) {
//                            Set<Map.Entry<String, Object>> entries2 = map.entrySet();
//                            Iterator<Map.Entry<String, Object>> iterator2 = entries2.iterator();
//                            while (iterator2.hasNext()) {
//                                Map.Entry<String, Object> next2 = iterator2.next();
//                                String key2 = next2.getKey();
//                                Object value2 = next2.getValue();
//                                Long longValue = null;
//                                BigDecimal bigDecimalValue = null;
//                                if (value2 instanceof Long) {
//                                    longValue = (Long) value2;
//                                }
//                                if (value2 instanceof BigDecimal) {
//                                    bigDecimalValue = (BigDecimal) value2;
//                                }
//                                if (!"help".equals(key2)) {
//                                    Object value3 = summaryMap.get(key2);
//                                    Long longValue2 = null;
//                                    BigDecimal bigDecimalValue2 = null;
//                                    if (value3 instanceof Long) {
//                                        longValue2 = (Long) value3;
//                                    }
//                                    if (value3 instanceof BigDecimal) {
//                                        bigDecimalValue2 = (BigDecimal) value3;
//                                    }
//
//                                    if (longValue != null && longValue2 != null) {
//                                        Long summaryValue = longValue.longValue() + longValue2.longValue();
//                                        summaryMap.put(key2, summaryValue);
//                                    }
//
//                                    if (bigDecimalValue != null && bigDecimalValue2 != null) {
//                                        BigDecimal summaryValue = bigDecimalValue.add(bigDecimalValue2);
//                                        summaryMap.put(key2, summaryValue);
//                                    }
//
//                                }
//                            }
//                            hasAdd = false;
//                        }
//
//                    }
//                }
//                if (hasAdd) {
//
//                    summaryResult.add(map);
//                }
//
//            }
//        }
//    }
//
//    /**
//     * 获取月份
//     * @param list
//     * @param monthTimeList
//     */
//    private void getMonth(List<Map<String, Object>> list, List<String> monthTimeList) {
//        if (CollectionUtils.isNotEmpty(list)) {
//            list.forEach(map -> {
//                String monthTime = (String) map.get("monthTime");
//                if (!monthTimeList.contains(monthTime)) {
//                    monthTimeList.add(monthTime);
//                }
//            });
//
//        }
//    }
//
//    /**
//     * 获取AE,PC表名
//     * @param value
//     * @return
//     */
//    private String getTableName(String value) {
//        int startIndex = value.indexOf("\"");
//        int endIndex = value.lastIndexOf("\"");
//        String tableName = value.substring(startIndex + 1, endIndex);
//        // 转为char数组
//        char[] ch = tableName.toCharArray();
//        ArrayList<Integer> indexList = new ArrayList<>();
//
//        for(int i = 0; i < ch.length ; i++){
//            if(ch[i] >= 'A' && ch[i] <= 'Z'){
//                indexList.add(tableName.indexOf(ch[i]));
//            }
//        }
//        StringBuffer stringBuffer = new StringBuffer();
//        for(int i = 0; i < ch.length ; i++){
//            if (indexList.contains(i)) {
//                stringBuffer.append("_");
//                stringBuffer.append(tableName.charAt(i));
//            } else {
//                stringBuffer.append(tableName.charAt(i));
//            }
//        }
//        return stringBuffer.toString().toLowerCase();
//    }
//
//    /**
//     * 填充惠及患者数据
//     * @param sheet
//     * @param fistContentStyle
//     * @param monthTimeList
//     * @param dataContentStyle
//     */
//    private int generateBenefitPatientsStatistics(Map<String, List<Map<String, Object>>> dataMap, HSSFSheet sheet, CellStyle fistContentStyle, List<String> monthTimeList, CellStyle dataContentStyle, int benefitPatientsStartCellIndex) {
//        ArrayList<String> modeNames = new ArrayList<>();
//        ArrayList<String> outReasons = new ArrayList<>();
//        List<Map<String, Object>> modeInEveryMonth = dataMap.get(MODE_IN_EVERY_MONTH);
//        if (CollectionUtils.isNotEmpty(modeInEveryMonth)) {
//            modeInEveryMonth.forEach(map -> {
//                String modeName = (String) map.get("help");
//                modeNames.add(modeName);
//            });
//        }
//        List<Map<String, Object>> outWithOtherReasonEveryMonth = dataMap.get(OUTWITH_OTHER_REASON_EVERY_MONTH);
//        if (CollectionUtils.isNotEmpty(outWithOtherReasonEveryMonth)) {
//            outWithOtherReasonEveryMonth.forEach(map -> {
//                String outReason = (String) map.get("help");
//                outReasons.add(outReason);
//            });
//        }
//        CellStyle benefitPatientsStyle = setBenefitPatientsStyle(sheet.getWorkbook());
//        int inEndIndex = benefitPatientsStartCellIndex + 2 + modeNames.size();
//
//        int outEndIndex = inEndIndex + 1 + outReasons.size();
//
//        int benefitPatientsEndIndex = outEndIndex + 3;
//
//        for (int j = 0; j <= HEAD_ROW_THIRD_INDEX ; j++) {
//            HSSFRow row = sheet.getRow(j);
//            for (int k = benefitPatientsStartCellIndex; k <= benefitPatientsEndIndex; k++) {
//                HSSFCell cell = row.createCell(k);
//                if (j == 0) {
//                    cell.setCellStyle(fistContentStyle);
//                } else {
//                    cell.setCellStyle(benefitPatientsStyle);
//                }
//                if (j == HEAD_ROW_START_INDEX && k == benefitPatientsStartCellIndex) {
//                    cell.setCellValue("惠及患者");
//                }
//                if (j == HEAD_ROW_TWO_INDEX && k == benefitPatientsStartCellIndex) {
//                    cell.setCellValue("申请人数");
//                    //创建合并单元格
//                    CellRangeAddress region = new CellRangeAddress(HEAD_ROW_TWO_INDEX, HEAD_ROW_TWO_INDEX, k, k + 1);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                    //在sheet里增加合并单元格
//                    sheet.addMergedRegion(region);
//                }
//                if (j == HEAD_ROW_TWO_INDEX && k == benefitPatientsStartCellIndex + 2) {
//                    cell.setCellValue("入组人数");
//                    if (CollectionUtils.isNotEmpty(modeNames)) {
//
//                        //创建合并单元格
//                        CellRangeAddress region = new CellRangeAddress(HEAD_ROW_TWO_INDEX, HEAD_ROW_TWO_INDEX, k, k + modeNames.size());// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                        //在sheet里增加合并单元格
//                        sheet.addMergedRegion(region);
//                    }
//                }
//                if (j == HEAD_ROW_TWO_INDEX && k == inEndIndex + 1) {
//                    cell.setCellValue("出组人数");
//                    if (CollectionUtils.isNotEmpty(outReasons)) {
//
//                        //创建合并单元格
//                        CellRangeAddress region = new CellRangeAddress(HEAD_ROW_TWO_INDEX, HEAD_ROW_TWO_INDEX, k, k + outReasons.size());// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                        //在sheet里增加合并单元格
//                        sheet.addMergedRegion(region);
//                    }
//                }
//                if (j == HEAD_ROW_TWO_INDEX && k == outEndIndex + 1) {
//                    cell.setCellValue("收到的申请材料");
//                    //创建合并单元格
//                    CellRangeAddress region = new CellRangeAddress(HEAD_ROW_TWO_INDEX, HEAD_ROW_TWO_INDEX, k, k + 2);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                    //在sheet里增加合并单元格
//                    sheet.addMergedRegion(region);
//                }
//                if (j == HEAD_ROW_THIRD_INDEX && k == benefitPatientsStartCellIndex) {
//                    cell.setCellValue("累计人数");
//                }
//                if (j == HEAD_ROW_THIRD_INDEX && k == benefitPatientsStartCellIndex + 1) {
//                    cell.setCellValue("本月新增");
//                }
//                if (j == HEAD_ROW_THIRD_INDEX && k > benefitPatientsStartCellIndex + 1 && k < inEndIndex) {
//
//                    String modeName = modeNames.get(k - (benefitPatientsStartCellIndex + 2));
//                    cell.setCellValue(modeName + "\n（累计）");
//                }
//                if (j == HEAD_ROW_THIRD_INDEX && k == inEndIndex) {
//                    cell.setCellValue("入组累计");
//                }
//                if (j == HEAD_ROW_THIRD_INDEX && k == inEndIndex + 1) {
//                    cell.setCellValue("领完人数（累计）");
//                }
////                if (j == HEAD_ROW_THIRD_INDEX && k == inEndIndex + 2) {
////                    cell.setCellValue("拒绝人数（累计）");
////                }
//                if (j == HEAD_ROW_THIRD_INDEX && k > inEndIndex + 1 && k <= outEndIndex) {
//
//                    String outReason = outReasons.get(k - (inEndIndex + 2));
//                    cell.setCellValue(outReason + "\n（累计）");
//                }
//                if (j == HEAD_ROW_THIRD_INDEX && k == outEndIndex + 1) {
//
//                    cell.setCellValue("累计");
//                }
//                if (j == HEAD_ROW_THIRD_INDEX && k == outEndIndex + 2) {
//
//                    cell.setCellValue("本月新增");
//                }
//                if (j == HEAD_ROW_THIRD_INDEX && k == benefitPatientsEndIndex) {
//
//                    cell.setCellValue("未审件数");
//                }
//            }
//            if (j == HEAD_ROW_START_INDEX) {
//                //创建合并单元格
//                CellRangeAddress region = new CellRangeAddress(HEAD_ROW_START_INDEX, HEAD_ROW_START_INDEX, benefitPatientsStartCellIndex, benefitPatientsEndIndex);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                //在sheet里增加合并单元格
//                sheet.addMergedRegion(region);
//            }
//        }
//        int dataRow = 0;
//        for (int j = 0; j < monthTimeList.size(); j++) {
//            String monthTime = monthTimeList.get(j);
//            HSSFRow row = sheet.getRow(dataRow + DATA_START_ROW_INDEX);
//            List<Map<String, Object>> patientApplicationList = dataMap.get(PATIENT_APPLICATION_LIST);
//            if (CollectionUtils.isNotEmpty(patientApplicationList)) {
//
//                for (Map<String, Object> map : patientApplicationList) {
//                    generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, benefitPatientsStartCellIndex, "applicationTotal");
//                    generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, inEndIndex, "inDateTotal");
//                    generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, inEndIndex + 1, "completeDrugTotal");
//                    generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, inEndIndex + 2, "refuseTotal");
//
//                }
//            }
//            List<Map<String, Object>> patientAddedEveryMonth = dataMap.get(PATIENT_ADDED_EVERY_MONTH);
//            if (CollectionUtils.isNotEmpty(patientAddedEveryMonth)) {
//                patientAddedEveryMonth.forEach(map -> {
//                    String monthTimeForAddedEveryMonth = (String) map.get("monthTime");
//                    Long patientTotal = (Long) map.get("patientTotal");
//                    if (monthTime.equals(monthTimeForAddedEveryMonth)) {
//                        generateAddedEveryMonthData(row, patientTotal, dataContentStyle, benefitPatientsStartCellIndex + 1);
//                    }
//
//                });
//            }
//            if (CollectionUtils.isNotEmpty(modeNames) && CollectionUtils.isNotEmpty(modeInEveryMonth)) {
//                int modeInEveryMonthCellIndex = benefitPatientsStartCellIndex + 2;
//                for (String modeName : modeNames) {
//                    for (Map<String, Object> map : modeInEveryMonth) {
//                        generateMonthlyAccumulationDataGroup(map, "help", modeName, monthTime, row, modeInEveryMonthCellIndex, dataContentStyle, "modeInEveryMontTotal");
//                    }
//
//                    modeInEveryMonthCellIndex++;
//                }
//
//            }
//            if (CollectionUtils.isNotEmpty(outWithOtherReasonEveryMonth) && CollectionUtils.isNotEmpty(outReasons)) {
//                int outWithOtherReasonEveryMonthCellIndex = inEndIndex + 3;
//                for (String outReason : outReasons) {
//                    for (Map<String, Object> map : outWithOtherReasonEveryMonth) {
//                        generateMonthlyAccumulationDataGroup(map, "help", outReason, monthTime, row, outWithOtherReasonEveryMonthCellIndex, dataContentStyle, "outWithOtherReasonEveryMonthTotal");
//                    }
//                    outWithOtherReasonEveryMonthCellIndex++;
//                }
//            }
//            List<Map<String, Object>> materialReviewMonth = dataMap.get(MATERIAL_REVIEW_MONTH);
//            if (CollectionUtils.isNotEmpty(materialReviewMonth)) {
//                for (Map<String, Object> map : materialReviewMonth) {
//                    generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, outEndIndex + 1, "materialReviewTotal");
//                }
//
//            }
//            List<Map<String, Object>> materialReviewEveryMonth = dataMap.get(MATERIAL_REVIEW_EVERY_MONTH);
//            if (CollectionUtils.isNotEmpty(materialReviewEveryMonth)) {
//                materialReviewEveryMonth.forEach(map -> {
//                    String materialReviewEveryMonthTotalMonth = (String) map.get("monthTime");
//                    Long materialReviewEveryMonthTotal = (Long) map.get("materialReviewEveryMonthTotal");
//                    Long moderatedTotal = (Long) map.get("moderatedTotal");
//                    if (monthTime.equals(materialReviewEveryMonthTotalMonth)) {
//                        generateAddedEveryMonthData(row, materialReviewEveryMonthTotal, dataContentStyle, outEndIndex + 2);
//                        generateAddedEveryMonthData(row, moderatedTotal, dataContentStyle, benefitPatientsEndIndex);
//                    }
//
//
//                });
//            }
//
//            dataRow++;
//        }
//
//        return benefitPatientsEndIndex + 1;
//    }
//
//    /**
//     * 生成不同类型的月累计数据
//     * @param map
//     * @param groupkey
//     * @param groupType
//     * @param monthTime
//     * @param row
//     * @param cellIndex
//     * @param dataContentStyle
//     */
//    public void generateMonthlyAccumulationDataGroup(Map map, Object groupkey, Object groupType, String monthTime, HSSFRow row, int cellIndex, CellStyle dataContentStyle, String valueKey) {
//        Set<Map.Entry<String, Object>> entries = map.entrySet();
//        Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
//        while (iterator.hasNext()) {
//            Map.Entry<String, Object> next = iterator.next();
//            String key = next.getKey();
//            Object value = next.getValue();
//            if (key.equals(groupkey) && value.equals(groupType)) {
//
//                Set<Map.Entry<String, Object>> entriesValue = map.entrySet();
//                Iterator<Map.Entry<String, Object>> iteratorValue = entriesValue.iterator();
//                while (iteratorValue.hasNext()) {
//                    Map.Entry<String, Object> nextValue = iteratorValue.next();
//                    String keyValue = nextValue.getKey();
//                    if (!keyValue.equals(groupkey)) {
//                        Long value2 = (Long) nextValue.getValue();
//
//                        String checkMonthTime = keyValue.substring(keyValue.indexOf("_") + 1);
//                        String checkKey = keyValue.substring(0, keyValue.indexOf("_"));
//
////                        String[] field = keyValue.split("_");
////                        String index = field[1];
//                        if (monthTime.equals(checkMonthTime) && valueKey.equals(checkKey)) {
//                            HSSFCell cell = row.createCell(cellIndex );
//                            cell.setCellValue(value2);
//                            cell.setCellStyle(dataContentStyle);
//                        }
////                        if (Convert.toInt(index) == dataRow && valueKey.equals(field[0])) {
////                            HSSFCell cell = row.createCell(cellIndex );
////                            cell.setCellValue(value2);
////                            cell.setCellStyle(dataContentStyle);
////                        }
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * 填充覆盖范围数据
//     * @param sheet
//     * @param fistContentStyle
//     * @param monthTimeList
//     * @param dataContentStyle
//     */
//    private void generateCoverageStatistics(Map<String, List<Map<String, Object>>> dataMap, HSSFSheet sheet, CellStyle fistContentStyle, List<String> monthTimeList, CellStyle dataContentStyle, int coverageStartCellIndex, int coverageEndCellIndex) {
//        CellStyle coverStyle = setCoverStyle(sheet.getWorkbook());
//        for (int k = 0; k <= HEAD_ROW_THIRD_INDEX; k++) {
//            HSSFRow row = sheet.getRow(k);
//
//            for (int j = coverageStartCellIndex; j <= coverageEndCellIndex; j++) {
//                HSSFCell cell = row.createCell(j);
//                if (k == 0) {
//                    cell.setCellStyle(fistContentStyle);
//                } else {
//                    cell.setCellStyle(coverStyle);
//                }
//                if (k == HEAD_ROW_START_INDEX && j == coverageStartCellIndex) {
//                    cell.setCellValue("覆盖范围（只有增量）");
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j == coverageStartCellIndex) {
//                    cell.setCellValue("省");
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j == coverageStartCellIndex + 1) {
//                    cell.setCellValue("自治区与直辖市");
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j == coverageStartCellIndex + 2) {
//                    cell.setCellValue("城市（项目自定义）");
//                    sheet.setColumnWidth(j, (int) ((20 + 0.72) * 256));
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j == coverageStartCellIndex + 3) {
//                    cell.setCellValue("医院");
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j == coverageStartCellIndex + 4) {
//                    cell.setCellValue("医生");
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j == coverageStartCellIndex + 5) {
//                    cell.setCellValue("护士");
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j == coverageStartCellIndex + 6) {
//                    cell.setCellValue("药房");
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j == coverageStartCellIndex + 7) {
//                    cell.setCellValue("药师");
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j == coverageEndCellIndex) {
//                    cell.setCellValue("公益专员");
//                }
//                if (k == HEAD_ROW_TWO_INDEX) {
//                    //创建合并单元格
//                    CellRangeAddress region = new CellRangeAddress(HEAD_ROW_TWO_INDEX, HEAD_ROW_THIRD_INDEX, j, j);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                    //在sheet里增加合并单元格
//                    sheet.addMergedRegion(region);
//                }
//            }
//            if (k == HEAD_ROW_START_INDEX) {
//                //创建合并单元格
//                CellRangeAddress region = new CellRangeAddress(HEAD_ROW_START_INDEX, HEAD_ROW_START_INDEX, coverageStartCellIndex, coverageEndCellIndex);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                //在sheet里增加合并单元格
//                sheet.addMergedRegion(region);
//            }
//        }
//
//        for (int j = 0; j < monthTimeList.size(); j++) {
//            String mothTimeRow = monthTimeList.get(j);
//            HSSFRow row = sheet.getRow(DATA_START_ROW_INDEX + j);
//            List<Map<String, Object>> hospitalMonth = dataMap.get(HOSPITAL_MONTH);
//            if (CollectionUtils.isNotEmpty(hospitalMonth)) {
//
//                for (Map<String, Object> map : hospitalMonth) {
//                    String monthTime = (String) map.get("monthTime");
//                    Long provinceTotal = (Long) map.get("provinceTotal");
//                    Long regionAndMunicipalityTotal = (Long) map.get("regionAndMunicipalityTotal");
//                    Long cityTotal = (Long) map.get("cityTotal");
//                    Long hospitalTotal = (Long) map.get("hospitalTotal");
//                    if (mothTimeRow.equals(monthTime)) {
//                        generateAddedEveryMonthData(row, provinceTotal, dataContentStyle, coverageStartCellIndex);
//                        generateAddedEveryMonthData(row, regionAndMunicipalityTotal, dataContentStyle, coverageStartCellIndex + 1);
//                        generateAddedEveryMonthData(row, cityTotal, dataContentStyle, coverageStartCellIndex + 2);
//                        generateAddedEveryMonthData(row, hospitalTotal, dataContentStyle, coverageStartCellIndex + 3);
//
//                    }
//                }
//            }
//            List<Map<String, Object>> doctorMonth = dataMap.get(DOCTOR_MONTH);
//            if (CollectionUtils.isNotEmpty(doctorMonth)) {
//                for (Map<String, Object> map : doctorMonth) {
//                    String monthTime = (String) map.get("monthTime");
//                    Long doctorTotal = (Long) map.get("doctorTotal");
//                    Long nurseTotal = (Long) map.get("nurseTotal");
//                    if (mothTimeRow.equals(monthTime)) {
//                        generateAddedEveryMonthData(row, doctorTotal, dataContentStyle, coverageStartCellIndex + 4);
//                        generateAddedEveryMonthData(row, nurseTotal, dataContentStyle, coverageStartCellIndex + 5);
//
//
//                    }
//                }
//            }
//            List<Map<String, Object>> drugstoreMonth = dataMap.get(DRUGSTORE_MONTH);
//            if (CollectionUtils.isNotEmpty(drugstoreMonth)) {
//                for (Map<String, Object> map : drugstoreMonth) {
//                    String monthTime = (String) map.get("monthTime");
//                    Long drugStoreTotal = (Long) map.get("drugStoreTotal");
//                    if (mothTimeRow.equals(monthTime)) {
//                        generateAddedEveryMonthData(row, drugStoreTotal, dataContentStyle, coverageStartCellIndex + 6);
//                    }
//                }
//            }
//            List<Map<String, Object>> userMonth = dataMap.get(USER_MONTH);
//            if (CollectionUtils.isNotEmpty(userMonth)) {
//                for (Map<String, Object> map : userMonth) {
//                    String monthTime = (String) map.get("monthTime");
//                    Long userTotal = (Long) map.get("userTotal");
//                    if (mothTimeRow.equals(monthTime)) {
//                        generateAddedEveryMonthData(row, userTotal, dataContentStyle, coverageStartCellIndex + 7);
//
//                    }
//                }
//            }
//            List<Map<String, Object>> volunteerMonth = dataMap.get(VOLUNTEER_MONTH);
//            if (CollectionUtils.isNotEmpty(volunteerMonth)) {
//                for (Map<String, Object> map : volunteerMonth) {
//                    String monthTime = (String) map.get("monthTime");
//                    Long volunteerTotal = (Long) map.get("volunteerTotal");
//                    if (mothTimeRow.equals(monthTime)) {
//                        generateAddedEveryMonthData(row, volunteerTotal, dataContentStyle, coverageEndCellIndex);
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * 药品援助项目概况数据填充
//     * @param sheet
//     * @param projectOverviewStyle
//     * @param monthTimeList
//     * @param dataContentStyle
//     */
//    private  void generateProjectOverviewStatisticsWithDrug(Map<String, List<Map<String, Object>>> dataMap, HSSFSheet sheet, CellStyle projectOverviewStyle, List<String> monthTimeList, CellStyle dataContentStyle) {
//        for (int k = HEAD_ROW_START_INDEX; k <= HEAD_ROW_THIRD_INDEX; k++) {
//            HSSFRow row = sheet.getRow(k);
//            for (int j = PROJECT_OVERVIEW_START_CELL_INDEX; j <= DRUG_PROJECT_OVERVIEW_END_CELL_INDEX; j++) {
//                HSSFCell cell = row.createCell(j);
//                if (k == HEAD_ROW_START_INDEX && j == PROJECT_OVERVIEW_START_CELL_INDEX) {
//                    cell.setCellValue("项目总概况");
//                    sheet.setColumnWidth(j, (int) ((15 + 0.72) * 256));
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j == PROJECT_OVERVIEW_START_CELL_INDEX) {
//                    cell.setCellValue("申请人数（累积）");
//                    sheet.setColumnWidth(j, (int) ((15 + 0.72) * 256));
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j ==  PROJECT_OVERVIEW_START_CELL_INDEX + 1) {
//                    cell.setCellValue("入组人数（累积）");
//                    sheet.setColumnWidth(j, (int) ((15 + 0.72) * 256));
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j ==  PROJECT_OVERVIEW_START_CELL_INDEX + 2) {
//                    cell.setCellValue("已发药品支数");
//                    sheet.setColumnWidth(j, (int) ((15 + 0.72) * 256));
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j ==  DRUG_PROJECT_OVERVIEW_END_CELL_INDEX) {
//                    cell.setCellValue("已发药品总金额");
//                    sheet.setColumnWidth(j, (int) ((20 + 0.72) * 256));
//                }
//                cell.setCellStyle(projectOverviewStyle);
//                if (k == HEAD_ROW_TWO_INDEX) {
//                    CellRangeAddress region = new CellRangeAddress(HEAD_ROW_TWO_INDEX, HEAD_ROW_THIRD_INDEX, j, j);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                    //在sheet里增加合并单元格
//                    sheet.addMergedRegion(region);
//                }
//            }
//
//            if (k == HEAD_ROW_START_INDEX) {
//                //创建合并单元格
//                CellRangeAddress region = new CellRangeAddress(HEAD_ROW_START_INDEX, HEAD_ROW_START_INDEX, PROJECT_OVERVIEW_START_CELL_INDEX, DRUG_PROJECT_OVERVIEW_END_CELL_INDEX);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                //在sheet里增加合并单元格
//                sheet.addMergedRegion(region);
//            }
//        }
//
//        int dataRow = 0;
//        for (String monthTime : monthTimeList) {
//            List<Map<String, Object>> patientApplicationList = dataMap.get(PATIENT_APPLICATION_LIST);
//            HSSFRow row = sheet.getRow(dataRow + DATA_START_ROW_INDEX);
//            if (CollectionUtils.isNotEmpty(patientApplicationList)) {
//                for (Map<String, Object> map : patientApplicationList) {
//                    generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, PROJECT_OVERVIEW_START_CELL_INDEX, "applicationTotal");
//                    generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, PROJECT_OVERVIEW_START_CELL_INDEX + 1, "inDateTotal");
//
//                }
//            }
//            List<Map<String, Object>> dragTakingList = dataMap.get(DRAG_TAKING_LIST);
//            if (CollectionUtils.isNotEmpty(dragTakingList)) {
//
//                for (Map<String, Object> map : dragTakingList) {
//                    generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, PROJECT_OVERVIEW_START_CELL_INDEX + 2, "drugTakingTotal");
//                    generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, DRUG_PROJECT_OVERVIEW_END_CELL_INDEX, "priceTotal");
//                }
//            }
//            dataRow++;
//        }
//
//
//    }
//
//    /**
//     * 资金援助项目概况数据填充
//     * @param sheet
//     * @param projectOverviewStyle
//     * @param monthTimeList
//     * @param dataContentStyle
//     */
//    private  void generateProjectOverviewStatisticsWithFunds(Map<String, List<Map<String, Object>>> dataMap, HSSFSheet sheet, CellStyle projectOverviewStyle, List<String> monthTimeList, CellStyle dataContentStyle) {
//        for (int k = HEAD_ROW_START_INDEX; k <= HEAD_ROW_THIRD_INDEX; k++) {
//            HSSFRow row = sheet.getRow(k);
//            for (int j = PROJECT_OVERVIEW_START_CELL_INDEX; j <= FUNDS_PROJECT_OVERVIEW_END_CELL_INDEX; j++) {
//                HSSFCell cell = row.createCell(j);
//                if (k == HEAD_ROW_START_INDEX && j == PROJECT_OVERVIEW_START_CELL_INDEX) {
//                    cell.setCellValue("项目总概况");
//                    sheet.setColumnWidth(j, (int) ((15 + 0.72) * 256));
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j == PROJECT_OVERVIEW_START_CELL_INDEX) {
//                    cell.setCellValue("申请人数（累积）");
//                    sheet.setColumnWidth(j, (int) ((15 + 0.72) * 256));
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j ==  PROJECT_OVERVIEW_START_CELL_INDEX + 1) {
//                    cell.setCellValue("入组人数（累积）");
//                    sheet.setColumnWidth(j, (int) ((15 + 0.72) * 256));
//                }
//                if (k == HEAD_ROW_TWO_INDEX && j ==  FUNDS_PROJECT_OVERVIEW_END_CELL_INDEX) {
//                    cell.setCellValue("已发\n援助资金\n（累积）");
//                    sheet.setColumnWidth(j, (int) ((20 + 0.72) * 256));
//                }
//                cell.setCellStyle(projectOverviewStyle);
//                if (k == HEAD_ROW_TWO_INDEX) {
//                    CellRangeAddress region = new CellRangeAddress(HEAD_ROW_TWO_INDEX, HEAD_ROW_THIRD_INDEX, j, j);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                    //在sheet里增加合并单元格
//                    sheet.addMergedRegion(region);
//                }
//            }
//
//            if (k == HEAD_ROW_START_INDEX) {
//                //创建合并单元格
//                CellRangeAddress region = new CellRangeAddress(HEAD_ROW_START_INDEX, HEAD_ROW_START_INDEX, PROJECT_OVERVIEW_START_CELL_INDEX, FUNDS_PROJECT_OVERVIEW_END_CELL_INDEX);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                //在sheet里增加合并单元格
//                sheet.addMergedRegion(region);
//            }
//        }
//
//        int dataRow = 0;
//        for (String monthTime : monthTimeList) {
//            List<Map<String, Object>> patientApplicationList = dataMap.get(PATIENT_APPLICATION_LIST);
//            HSSFRow row = sheet.getRow(dataRow + DATA_START_ROW_INDEX);
//            if (CollectionUtils.isNotEmpty(patientApplicationList)) {
//                for (Map<String, Object> map : patientApplicationList) {
//                    generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, PROJECT_OVERVIEW_START_CELL_INDEX, "applicationTotal");
//                    generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, PROJECT_OVERVIEW_START_CELL_INDEX + 1, "inDateTotal");
//
//                }
//            }
//            List<Map<String, Object>> hasDispenseAidFundsMonth = dataMap.get(HAS_DISPENSE_AID_FUNDS_MONTH);
//            if (CollectionUtils.isNotEmpty(hasDispenseAidFundsMonth)) {
//
//                for (Map<String, Object> map : hasDispenseAidFundsMonth) {
//                    generateMonthlyAccumulationData(map, monthTime, row, dataContentStyle, FUNDS_PROJECT_OVERVIEW_END_CELL_INDEX, "dispenseAidFundsTotal");
//                }
//            }
//            dataRow++;
//        }
//
//
//    }
//
//    /**
//     * 填充时间
//     * @param sheet
//     * @param monthTimeList
//     * @param dataContentStyle
//     */
//    private void generateTimeStatistics(HSSFSheet sheet, List<String> monthTimeList, CellStyle dataContentStyle) {
//        for (int j = 0; j < monthTimeList.size(); j++) {
//            HSSFRow row = sheet.createRow(ConstantUtil.DATA_START_ROW_INDEX + j);
//            HSSFCell cell = row.createCell(0);
//            String[] split = monthTimeList.get(j).split("_");
//            String month = split[1];
//            if (month.startsWith("0")) {
//                month = month.substring(1);
//            }
//            cell.setCellValue(split[0] + "年" + month + "月");
//            cell.setCellStyle(dataContentStyle);
//            sheet.setColumnWidth(0, (int) ((15 + 0.72) * 256));
//        }
//    }
//
//    /**
//     * 生成每月新增数据
//     * @param row
//     * @param value
//     * @param dataContentStyle
//     */
//    private void generateAddedEveryMonthData(HSSFRow row, Object value, CellStyle dataContentStyle, int cellIndex) {
//        HSSFCell volunteerTotalCell = row.createCell(cellIndex);
//        if (value instanceof Long) {
//
//            volunteerTotalCell.setCellValue((Long) value);
//        }
//        if (value instanceof BigDecimal) {
//            volunteerTotalCell.setCellValue(((BigDecimal) value).doubleValue());
//        }
//        volunteerTotalCell.setCellStyle(dataContentStyle);
//    }
//
//    /**
//     * 初始化excel,并设置第一行数据
//     * @param projectInfo
//     * @return
//     */
//    private HSSFWorkbook initExcel(List<Map<String, Object>> projectInfo) {
//        // 在内存当中保持 100 行 , 超过的数据放到硬盘中在内存当中保持 100 行 , 超过的数据放到硬盘中
//        HSSFWorkbook wb = new HSSFWorkbook();
//        if (CollectionUtils.isNotEmpty(projectInfo)) {
//            // 根据总记录数创建sheet并分配标题
//            CellStyle cellStyle = setExcelFistContentStyle(wb);
//            for (int i = 0; i < projectInfo.size(); i++) {
//                Map<String, Object> map = projectInfo.get(i);
//                String sheetName = (String) map.get("project");
//                Sheet sheet = wb.createSheet(sheetName);
//
//                for (int j = 0; j < 1; j++) {
//                    Row row = sheet.createRow(j);
//                    for (int k = 0; k <= ConstantUtil.DRUG_PROJECT_OVERVIEW_END_CELL_INDEX; k++) {
//                        Cell cell = row.createCell(k);
//                        if (k == 0) {
//                            cell.setCellValue("生命绿洲项目月报\n" + sheetName);
//                            cell.setCellStyle(cellStyle);
//                            row.setHeightInPoints(50f);
//                        }
//                    }
//                }
//                //创建合并单元格
//                CellRangeAddress region = new CellRangeAddress(0, 0, 0, ConstantUtil.DRUG_PROJECT_OVERVIEW_END_CELL_INDEX);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                //在sheet里增加合并单元格
//                sheet.addMergedRegion(region);
//            }
//
//        }
//
//        return wb;
//    }
//
//    /**
//     * 生成时间第一列
//     * @param sheet
//     * @param projectOverviewStyle
//     */
//    private void generateTimeCell(HSSFSheet sheet, CellStyle projectOverviewStyle) {
//
//        for (int j = HEAD_ROW_START_INDEX; j <= ConstantUtil.HEAD_ROW_THIRD_INDEX; j++) {
//            HSSFRow row = sheet.createRow(j);
//            HSSFCell cell = row.createCell(0);
//            if (j == HEAD_ROW_START_INDEX) {
//                cell.setCellValue("时间");
//                cell.setCellStyle(projectOverviewStyle);
//                //创建合并单元格
//                CellRangeAddress region = new CellRangeAddress(HEAD_ROW_START_INDEX, HEAD_ROW_THIRD_INDEX, 0, 0);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
//                //在sheet里增加合并单元格
//                sheet.addMergedRegion(region);
//            }
//        }
//    }
//
//    /**
//     * 设置第一行数据样式
//     *
//     * @param wb
//     * @return
//     */
//    private CellStyle setExcelFistContentStyle(HSSFWorkbook wb) {
//        ExcelUtil.CustomCellStyle customCellStyle = new ExcelUtil.CustomCellStyle();
//        customCellStyle.setWb(wb);
//        customCellStyle.setBackgroundCustomRGB(new byte[] {(byte) 0, (byte) 51, (byte) 102});
//        customCellStyle.setBackgroundIndexedColors(IndexedColors.LIME);
//        customCellStyle.setCreateBorderBottom(true);
//        customCellStyle.setCreateBorderLeft(false);
//        customCellStyle.setCreateBorderRight(false);
//        customCellStyle.setCreateBorderTop(true);
//        customCellStyle.setBorderBottomStyle(BorderStyle.THIN);
//        customCellStyle.setBorderTopStyle(BorderStyle.THIN);
//        customCellStyle.setFontBold(true);
//        customCellStyle.setFontSize((short) 16);
//        customCellStyle.setFontName("微软雅黑");
//        customCellStyle.setFontIndexedColors(IndexedColors.RED);
//        return ExcelUtil.createCustomCellStyle(customCellStyle);
//
//    }
//
//    /**
//     * 设置第一行数据样式
//     *
//     * @param wb
//     * @return
//     */
//    private CellStyle setProjectOverviewStyle(HSSFWorkbook wb) {
//        ExcelUtil.CustomCellStyle customCellStyle = new ExcelUtil.CustomCellStyle();
//        customCellStyle.setWb(wb);
//        customCellStyle.setBackgroundCustomRGB(new byte[] {(byte) 128, (byte) 128, (byte) 128});
//        customCellStyle.setBackgroundIndexedColors(IndexedColors.AQUA);
//        customCellStyle.setCreateBorderBottom(true);
//        customCellStyle.setCreateBorderLeft(true);
//        customCellStyle.setCreateBorderRight(true);
//        customCellStyle.setCreateBorderTop(true);
//        customCellStyle.setBorderBottomStyle(BorderStyle.THIN);
//        customCellStyle.setBorderLeftStyle(BorderStyle.THIN);
//        customCellStyle.setBorderRightStyle(BorderStyle.THIN);
//        customCellStyle.setBorderTopStyle(BorderStyle.THIN);
//        customCellStyle.setFontBold(true);
//        customCellStyle.setFontSize((short) 10);
//        customCellStyle.setFontName("微软雅黑");
//        customCellStyle.setFontIndexedColors(IndexedColors.WHITE);
//        return ExcelUtil.createCustomCellStyle(customCellStyle);
//
//    }
//
//    /**
//     * 设置第一行数据样式
//     *
//     * @param wb
//     * @return
//     */
//    private CellStyle setCoverStyle(HSSFWorkbook wb) {
//        ExcelUtil.CustomCellStyle customCellStyle = new ExcelUtil.CustomCellStyle();
//        customCellStyle.setWb(wb);
//        customCellStyle.setBackgroundCustomRGB(new byte[] {(byte) 51, (byte) 51, (byte) 147});
//        customCellStyle.setBackgroundIndexedColors(IndexedColors.BROWN);
//        customCellStyle.setCreateBorderBottom(true);
//        customCellStyle.setCreateBorderLeft(true);
//        customCellStyle.setCreateBorderRight(true);
//        customCellStyle.setCreateBorderTop(true);
//        customCellStyle.setBorderBottomStyle(BorderStyle.THIN);
//        customCellStyle.setBorderLeftStyle(BorderStyle.THIN);
//        customCellStyle.setBorderRightStyle(BorderStyle.THIN);
//        customCellStyle.setBorderTopStyle(BorderStyle.THIN);
//        customCellStyle.setFontBold(true);
//        customCellStyle.setFontSize((short) 10);
//        customCellStyle.setFontName("微软雅黑");
//        customCellStyle.setFontIndexedColors(IndexedColors.WHITE);
//        return ExcelUtil.createCustomCellStyle(customCellStyle);
//
//    }
//
//    /**
//     * 设置轮次结果样式
//     *
//     * @param wb
//     * @return
//     */
//    private CellStyle setDataContentStyle(HSSFWorkbook wb) {
//        ExcelUtil.CustomCellStyle customCellStyle = new ExcelUtil.CustomCellStyle();
//        customCellStyle.setWb(wb);
//        customCellStyle.setBackgroundIndexedColors(IndexedColors.WHITE);
//        customCellStyle.setCreateBorderBottom(true);
//        customCellStyle.setCreateBorderLeft(true);
//        customCellStyle.setCreateBorderRight(true);
//        customCellStyle.setCreateBorderTop(true);
//        customCellStyle.setBorderBottomStyle(BorderStyle.THIN);
//        customCellStyle.setBorderLeftStyle(BorderStyle.THIN);
//        customCellStyle.setBorderRightStyle(BorderStyle.THIN);
//        customCellStyle.setBorderTopStyle(BorderStyle.THIN);
//        customCellStyle.setFontBold(false);
//        customCellStyle.setFontSize((short) 11);
//        customCellStyle.setFontName("微软雅黑");
//        return ExcelUtil.createCustomCellStyle(customCellStyle);
//
//    }
//
//    /**
//     * 设置轮次结果样式
//     *
//     * @param wb
//     * @return
//     */
//    private CellStyle setBenefitPatientsStyle(HSSFWorkbook wb) {
//        ExcelUtil.CustomCellStyle customCellStyle = new ExcelUtil.CustomCellStyle();
//        customCellStyle.setWb(wb);
//        customCellStyle.setBackgroundCustomRGB(new byte[] {(byte) 221, (byte) 45, (byte) 50});
//        customCellStyle.setBackgroundIndexedColors(IndexedColors.DARK_RED);
//        customCellStyle.setCreateBorderBottom(true);
//        customCellStyle.setCreateBorderLeft(true);
//        customCellStyle.setCreateBorderRight(true);
//        customCellStyle.setCreateBorderTop(true);
//        customCellStyle.setBorderBottomStyle(BorderStyle.THIN);
//        customCellStyle.setBorderLeftStyle(BorderStyle.THIN);
//        customCellStyle.setBorderRightStyle(BorderStyle.THIN);
//        customCellStyle.setBorderTopStyle(BorderStyle.THIN);
//        customCellStyle.setFontBold(true);
//        customCellStyle.setFontSize((short) 10);
//        customCellStyle.setFontName("微软雅黑");
//        customCellStyle.setFontIndexedColors(IndexedColors.WHITE);
//        return ExcelUtil.createCustomCellStyle(customCellStyle);
//
//    }
//
//    /**
//     * 设置轮次结果样式
//     *
//     * @param wb
//     * @return
//     */
//    private CellStyle setDrugFlowStyle(HSSFWorkbook wb) {
//        ExcelUtil.CustomCellStyle customCellStyle = new ExcelUtil.CustomCellStyle();
//        customCellStyle.setWb(wb);
//        customCellStyle.setBackgroundCustomRGB(new byte[] {(byte) 242, (byte) 8, (byte) 132});
//        customCellStyle.setBackgroundIndexedColors(IndexedColors.CORAL);
//        customCellStyle.setCreateBorderBottom(true);
//        customCellStyle.setCreateBorderLeft(true);
//        customCellStyle.setCreateBorderRight(true);
//        customCellStyle.setCreateBorderTop(true);
//        customCellStyle.setBorderBottomStyle(BorderStyle.THIN);
//        customCellStyle.setBorderLeftStyle(BorderStyle.THIN);
//        customCellStyle.setBorderRightStyle(BorderStyle.THIN);
//        customCellStyle.setBorderTopStyle(BorderStyle.THIN);
//        customCellStyle.setFontBold(true);
//        customCellStyle.setFontSize((short) 10);
//        customCellStyle.setFontName("微软雅黑");
//        customCellStyle.setFontIndexedColors(IndexedColors.WHITE);
//        return ExcelUtil.createCustomCellStyle(customCellStyle);
//
//    }
//
//}
