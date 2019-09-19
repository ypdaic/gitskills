package com.daiyanping.cms.util;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

    private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    public static final String XLSX = ".xlsx";

    public static final String MESSAGE_SHEET_NAME = "快捷回复列表";

    public static final String SESSION_QUALITY_SHEET_NAME = "会话质检列表";

    public static final String SESSION_SHEET_NAME = "会话列表";

    public static final String SESSION_QUALITY_STATICS_SHEET_NAME = "会话质检统计列表";

    public static final String KNOWLEDGE_SHEET_NAME = "聊天知识列表";

    public static final String ORDER_SHEET_NAME = "工单列表";

    public static final String VISITOR_SHEET_NAME = "访客列表";

    public static final Integer DEFAULT_YEAR = 2088;

    public static final String MESSAGE_EXCEL_TMEPLATE_NAME = "客服系统-配置-公共快捷回复导入模板.xlsx";

    public static final String KNOWLEDGE_EXCEL_TMEPLATE_NAME = "客服系统-配置-聊天知识导入模板.xlsx";

    //模板文件目录
    public static final String EXCEL_TEMPLATE = "/excelTemplate/";

    public static final String DEFAULT_VALUE = "";

    public static final String FONT_TYPE_BLACK = "黑体";

    //yyyy-mm-dd HH:mm:ss  的日期格式
    public static final String DATE_FORMAT = "yyyy\\-mm\\-dd\\ hh:mm:ss";


    /**
     * 每个sheet存储的记录数 1W
     */
    public static final Integer PER_SHEET_ROW_COUNT = 10_000;

    /**
     * 每次向EXCEL写入的记录数(查询每页数据大小) 5000
     */
    public static final Integer PER_WRITE_ROW_COUNT = 5000;

    /**
     * 每个sheet的写入次数 2
     */
    public static final Integer PER_SHEET_WRITE_COUNT = PER_SHEET_ROW_COUNT / PER_WRITE_ROW_COUNT;


    /**
     * 校验日期格式
     *
     * @param cell
     * @return
     */
    public static boolean checkDateFormat(Cell cell) {
        //Excel存储日期、时间均以数值类型进行存储，读取时POI先判断是是否是数值类型，再进行判断
        if (CellType.NUMERIC == cell.getCellType()) {

            //如果是日期格式
            if (DateUtil.isCellDateFormatted(cell)) {
                String dataFormatString = cell.getCellStyle().getDataFormatString();
                if (!DATE_FORMAT.equals(dataFormatString)) {
                    return false;
                }
                return true;

            }
            return false;
        } else {
            return false;
        }

    }

    /**
     * 初始化EXCEL(sheet个数和标题)
     *
     * @param totalRowCount 总记录数
     * @param headers       表头
     * @return XSSFWorkbook对象
     */
    public static SXSSFWorkbook initExcel(Integer totalRowCount, List<String> headers, String sheetName) {

        // 在内存当中保持 100 行 , 超过的数据放到硬盘中在内存当中保持 100 行 , 超过的数据放到硬盘中
        SXSSFWorkbook wb = new SXSSFWorkbook(100);

        // 获取sheet数量
        Integer sheetCount = ((totalRowCount % PER_SHEET_ROW_COUNT == 0) ?
                (totalRowCount / PER_SHEET_ROW_COUNT) : (totalRowCount / PER_SHEET_ROW_COUNT + 1));

        // 根据总记录数创建sheet并分配标题
        for (int i = 0; i < sheetCount; i++) {
            Sheet sheet = wb.createSheet(sheetName + (i + 1));
            //第一个参数代表列id(从0开始),第2个参数代表宽度值
            for (int j = 0; j < headers.size(); j++) {

                sheet.setColumnWidth(j, (int) ((40 + 0.72) * 256));
            }
            CellStyle cellStyle = setExcelHeaderStyle(wb);
            Row headRow = sheet.createRow(0);

            //在excel表中添加表头
            for (int j = 0; j < headers.size(); j++) {
                Cell cell = headRow.createCell(j);
                cell.setCellValue(headers.get(j));
                cell.setCellStyle(cellStyle);
            }
        }

        return wb;
    }

    /**
     * 导出Excel到浏览器
     *
     * @param response
     * @param totalRowCount          总记录数
     * @param fileName               文件名称
     * @param headers                标题
     * @param writeExcelDataCallback 向EXCEL写数据回调
     * @throws Exception
     */
    public static final boolean exportExcelToWebsite(HttpServletResponse response, Integer totalRowCount, String fileName, List<String> headers, WriteExcelDataCallback writeExcelDataCallback) {

        logger.info("Start export data");

        // 初始化EXCEL
        SXSSFWorkbook wb = initExcel(totalRowCount, headers, fileName);
        //压缩临时文件
        wb.setCompressTempFiles(true);
        try {
            CellStyle cellStyle = setExcelContentStyle(wb);
            // 分批写数据
            int sheetCount = wb.getNumberOfSheets();
            for (int i = 0; i < sheetCount; i++) {
                Sheet sheet = wb.getSheetAt(i);
                for (int j = 1; j <= PER_SHEET_WRITE_COUNT; j++) {
                    int pageIndex = i * PER_SHEET_WRITE_COUNT + j;
                    int pageSize = PER_WRITE_ROW_COUNT;
                    int startRowCount = (j - 1) * PER_WRITE_ROW_COUNT + 1;
                    writeExcelDataCallback.doWith(sheet, pageIndex, pageSize, startRowCount, cellStyle);
                }
            }
            // 下载EXCEL
            setResponseHeader(response);
            wb.write(response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            logger.error("excel表格上生成失败！", e);
            return false;
        } finally {
            if (null != wb) {
                try {
                    // 销毁硬盘上的临时文件
                    wb.dispose();
                } catch (Exception e) {
                    logger.error("excel临时文件销毁失败！", e);
                }
            }
            FileUtil.close(wb);
        }
        logger.info("End export data");
        return true;
    }

    public static void insertDataToExcel(Sheet sheet, List<Map<String, Object>> data, List<String> resultSetColumnNames, CellStyle cellStyle, int startRowCount) {
        if (CollectionUtils.isNotEmpty(data)) {

            //在表中存放查询到的数据放入对应的列
            for (int i = 0; i < data.size(); i++) {
                Row row1 = sheet.createRow(i + startRowCount);
                Map<String, Object> stringObjectMap = data.get(i);
                for (int j = 0; j < resultSetColumnNames.size(); j++) {
                    Object value = stringObjectMap.get(resultSetColumnNames.get(j));
                    if (value == null) {
                        value = DEFAULT_VALUE;
                    }
                    Cell cell = row1.createCell(j);
                    if (value instanceof Date) {
                        cell.setCellValue((Date) value);
                    } else {
                        cell.setCellValue(value.toString());
                    }

                    cell.setCellStyle(cellStyle);
                }
            }
        }
    }

    /**
     * 写数据到Excel
     */
    @FunctionalInterface
    public interface WriteExcelDataCallback {

        /**
         * 查询数据库的数据添加到sheet中
         *
         * @param pageIndex     当前页
         * @param pageSize      页的大小
         * @param startRowCount sheet起始行
         */
        void doWith(Sheet sheet, int pageIndex, int pageSize, int startRowCount, CellStyle cellStyle);
    }

    /**
     * Excel导出工具类，data中的数据要和headers中的一一对应
     *
     * @param response
     * @return
     */
    @Deprecated
    public static boolean export(HttpServletResponse response, List<String> headers, List<Map<String, Object>> data, List<String> resultSetColumnNames, String sheetName) {
        Workbook wb = new XSSFWorkbook();
        try {
            Sheet sheet = wb.createSheet(sheetName);
            //第一个参数代表列id(从0开始),第2个参数代表宽度值
            for (int i = 0; i < headers.size(); i++) {

                sheet.setColumnWidth(i, (int) ((40 + 0.72) * 256));
            }
            CellStyle cellStyle = setExcelHeaderStyle(wb);
            Row row = sheet.createRow(0);
            //在excel表中添加表头
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = row.createCell(i);
                XSSFRichTextString text = new XSSFRichTextString(headers.get(i));
                cell.setCellValue(text);
                cell.setCellStyle(cellStyle);
            }

            CellStyle cellStyle1 = setExcelContentStyle(wb);
            int rowNum = 1;
            if (CollectionUtils.isNotEmpty(data)) {

                //在表中存放查询到的数据放入对应的列
                for (int i = 0; i < data.size(); i++) {
                    Row row1 = sheet.createRow(i + 1);
                    Map<String, Object> stringObjectMap = data.get(i);
                    for (int j = 0; j < resultSetColumnNames.size(); j++) {
                        Object value = stringObjectMap.get(resultSetColumnNames.get(j));
                        if (value == null) {
                            value = DEFAULT_VALUE;
                        }
                        Cell cell = row1.createCell(j);
                        if (value instanceof Date) {
                            cell.setCellValue((Date) value);
                        } else {
                            cell.setCellValue(value.toString());
                        }

                        cell.setCellStyle(cellStyle1);
                    }
                }
            }
            setResponseHeader(response);
            wb.write(response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            logger.error("excel表格上生成失败！", e);
            return false;
        } finally {
            FileUtil.close(wb);
        }
        return true;
    }

    /**
     * Excel模板导出工具方法
     *
     * @param response
     * @param filePath
     * @return
     */
    public static boolean downTemplate(HttpServletResponse response, String filePath) {
        InputStream stream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource(filePath);
            stream = classPathResource.getInputStream();
            byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = stream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            setResponseHeader(response);
            response.getOutputStream().write(byteArrayOutputStream.toByteArray());
            response.flushBuffer();
        } catch (Exception e) {
            logger.error("模板导出失败！", e);
            return false;
        } finally {
            FileUtil.close(byteArrayOutputStream);
            FileUtil.close(stream);
        }
        return true;
    }

    /**
     * 设置响应头
     *
     * @param response
     */
    public static void setResponseHeader(HttpServletResponse response) {
        StringBuffer fileName = new StringBuffer();
        fileName.append(System.currentTimeMillis()).append(ExcelUtil.XLSX);
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
    }

    /**
     * 设置Excel表头的表格样式
     *
     * @param wb
     * @return
     */
    public static CellStyle setExcelHeaderStyle(Workbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
        // 设置单元格背景色
        cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        //设置单元格边框线条类型，此次为实线
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //给单元格添加边框
        //下边框
        cellStyle.setBorderBottom(BorderStyle.THIN);
        //左边框
        cellStyle.setBorderLeft(BorderStyle.THIN);
        //上边框
        cellStyle.setBorderTop(BorderStyle.THIN);
        //右边框
        cellStyle.setBorderRight(BorderStyle.THIN);
        //水平居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //垂直居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置自动换行
//            cellStyle.setWrapText(true);
        Font font = wb.createFont();
        //设置字体类型
        font.setFontName(FONT_TYPE_BLACK);
        //设置字体大小
        font.setFontHeightInPoints((short) 16);
        //粗体显示
        font.setBold(true);
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 设置Excel表内容的表格样式
     *
     * @param wb
     * @return
     */
    public static CellStyle setExcelContentStyle(Workbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
        // 设置背景色
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //下边框
        cellStyle.setBorderBottom(BorderStyle.THIN);
        //左边框
        cellStyle.setBorderLeft(BorderStyle.THIN);
        //上边框
        cellStyle.setBorderTop(BorderStyle.THIN);
        //右边框
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置自动换行
        cellStyle.setWrapText(true);
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 12);

        cellStyle.setFont(font);
        //设置日期格式
        DataFormat dataFormat = wb.createDataFormat();
        cellStyle.setDataFormat(dataFormat.getFormat("yyyy-mm-dd HH:mm:ss"));
        return cellStyle;
    }

    /**
     * 获取富文本内容
     *
     * @param html
     * @return
     */
    public static String parseHtml(String html) {

        /*
         * <.*?>为正则表达式，其中的.表示任意字符，*?表示出现0次或0次以上，此方法可以去掉双头标签(双头针对于残缺的标签)
         * "<.*?"表示<尖括号后的所有字符，此方法可以去掉残缺的标签，及后面的内容
         * " "，若有多种此种字符，可用同一方法去除
         */
        html = html.replaceAll("<.*?>", " ").replaceAll("", "");
        html = html.replaceAll("<.*?", "");
        return html;
    }

    /**
     * 自定义cell样式
     * @param customCellStyle
     * @return
     */
    public static CellStyle createCustomCellStyle(CustomCellStyle customCellStyle) {
        CellStyle cellStyle = customCellStyle.getWb().createCellStyle();
        HSSFPalette customPalette = customCellStyle.getWb().getCustomPalette();
        if (customCellStyle.getBackgroundCustomRGB() != null && customCellStyle.getBackgroundIndexedColors() != null) {

            customPalette.setColorAtIndex(customCellStyle.getBackgroundIndexedColors().index, customCellStyle.getBackgroundCustomRGB()[0], customCellStyle.getBackgroundCustomRGB()[1], customCellStyle.getBackgroundCustomRGB()[2]);
            // 设置背景色
            cellStyle.setFillForegroundColor(customCellStyle.getBackgroundIndexedColors().index);
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }

        if (customCellStyle.getBackgroundIndexedColors() != null) {
            // 设置背景色
            cellStyle.setFillForegroundColor(customCellStyle.getBackgroundIndexedColors().getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }

        if (customCellStyle.getCreateBorderBottom()) {

            //下边框
            cellStyle.setBorderBottom(customCellStyle.getBorderBottomStyle());
        }

        if (customCellStyle.getCreateBorderLeft()) {

            //左边框
            cellStyle.setBorderLeft(customCellStyle.getBorderLeftStyle());
        }
        if (customCellStyle.getCreateBorderRight()) {

            //右边框
            cellStyle.setBorderRight(customCellStyle.getBorderRightStyle());
        }
        if (customCellStyle.getCreateBorderTop()) {
            //上边框
            cellStyle.setBorderTop(customCellStyle.getBorderTopStyle());
        }

        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置自动换行
        cellStyle.setWrapText(true);
        Font font = customCellStyle.getWb().createFont();
        if (customCellStyle.getFontBold()) {

            //粗体显示
            font.setBold(true);
        }
        font.setFontHeightInPoints(customCellStyle.getFontSize());
        if (customCellStyle.getFontCustomRGB() != null) {
            HSSFColor hssfColor = customPalette.findSimilarColor(customCellStyle.getFontCustomRGB()[0], customCellStyle.getFontCustomRGB()[1], customCellStyle.getFontCustomRGB()[2]);
            font.setColor(hssfColor.getIndex());
        }

        if (customCellStyle.getFontIndexedColors() != null) {
            font.setColor(customCellStyle.getFontIndexedColors().getIndex());
        }
        if (customCellStyle.getFontName() != null) {

            font.setFontName(customCellStyle.getFontName());
        }
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 自定义表格样式
     */
    @Data
    public static class CustomCellStyle {

        private HSSFWorkbook wb;

        /**
         * 是否创建下边框
         */
        private Boolean createBorderBottom;

        /**
         * 是否创建左边框
         */
        private Boolean createBorderLeft;

        /**
         * 是否创建上边框
         */
        private Boolean createBorderTop;

        /**
         * 是否创建右边框
         */
        private Boolean createBorderRight;

        /**
         * 字段大小
         */
        private short fontSize;

        /**
         * 字体是否加粗
         */
        private Boolean fontBold;

        /**
         * 自定义背景色
         */
        private byte[] backgroundCustomRGB;

        /**
         * 指定背景色
         */
        private IndexedColors backgroundIndexedColors;

        /**
         * 下边框样式
         */
        private BorderStyle borderBottomStyle;

        /**
         * 左边框样式
         */
        private BorderStyle borderLeftStyle;

        /**
         * 上边框样式
         */
        private BorderStyle borderTopStyle;

        /**
         * 有边框样式
         */
        private BorderStyle borderRightStyle;

        /**
         * 自定义文字颜色
         */
        private byte[] fontCustomRGB;

        /**
         * 指定文字颜色
         */
        private IndexedColors fontIndexedColors;

        /**
         * 字体名称
         */
        private String fontName;
    }
}
