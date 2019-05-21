package com.daiyanping.cms.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件校验工具类
 */
public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    //(?i)即匹配时不区分大小写。表示匹配时不区分大小写。
    public static final String XLS_REGEX = "^.+\\.(?i)(xls)$";

    public static final String XLSX_REGEX = "^.+\\.(?i)(xlsx)$";

    //文件最大20M
    public static final Long FILE_MAX_SIZE = 20l * 1024l * 1024l;

    public static final Long FILE_NULL = 0l;

    //内容纯数字
    public static final String PURE_NUMBER = "^\\d$";

    public static final int EXCEL_MAX_NUMBER = 30 * 10000;

    /**
     * 校验文件格式
     * @param regex
     * @param fileName
     * @return
     */
    public static final boolean checkExcelFileSuffix(String regex, String fileName){
        return fileName.matches(regex);
    }

    /**
     * 校验文件大小最大值
     * @param fileSize
     * @return
     */
    public static boolean chekckFileMaxSize(Long fileSize){
        if (fileSize > FILE_MAX_SIZE){
            return false;
        }
        return true;
    }

    /**
     * 校验文件内容是否为null
     * @param fileSize
     * @return
     */
    public static boolean chekckFileNull(Long fileSize){
        if (fileSize == FILE_NULL){
            return false;
        }
        return true;
    }

    /**
     * 校验消息是否为纯数字
     * @param message
     * @return
     */
    public static boolean checkMessage(String message){
        return message.matches(PURE_NUMBER);
    }

    public static boolean checkExcelNumber(String fileName, InputStream is){
        try{

            boolean isExcel2003 = true;
            if (FileUtil.checkExcelFileSuffix(FileUtil.XLSX_REGEX, fileName)){
                isExcel2003 = false;
            }
            Workbook wb = null;
            if (isExcel2003) {
                wb = new HSSFWorkbook(is);
            } else {
                wb = new XSSFWorkbook(is);
            }
            Sheet sheet = wb.getSheetAt(0);
            if(sheet != null){
               if (sheet.getLastRowNum() - 1 > EXCEL_MAX_NUMBER){
                   return false;
               }
            }
        }
        catch (Exception e){
            logger.error("快捷回复Excel表解析异常！", e);
        }
        return true;

    }

    /**
     * 关闭流
     * @param c
     */
    public static void close(Closeable c) {
        try {
            if (c != null) {
                c.close();
            }
        }
        catch (IOException e) {
            logger.error("流关闭异常！", e);
        }
    }

}
