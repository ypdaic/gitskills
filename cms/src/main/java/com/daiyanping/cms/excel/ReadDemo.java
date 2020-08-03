package com.daiyanping.cms.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;

/**
 * @ClassName ReadDemo
 * @Description TODO
 * @Author daiyanping
 * @Date 2020-01-21
 * @Version 0.1
 */
public class ReadDemo {

    public static void main(String[] args) throws InterruptedException {

//       test();
        repeatedRead();
    }

    /**
     * 默认读取第一个sheet
     */
    public static void test1() {
        String fileName = "/Users/daiyanping/Documents/TEST.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        // 有个很重要的点 ExcelListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
        // 这里默认读取第一个sheet
        EasyExcel.read(fileName, DemoData.class, new ExcelListener()).sheet().doRead();

    }

    /**
     * 读取指定sheet
     */
    public static void test() {
        // 写法2：
        String fileName = "/Users/daiyanping/Documents/TEST.xlsx";
        ExcelReader excelReader = EasyExcel.read(fileName, DemoData.class, new ExcelListener()).build();
        ReadSheet readSheet = EasyExcel.readSheet(1).build();
        excelReader.read(readSheet);
        // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
        excelReader.finish();
    }

    /**
     * 读多个或者全部sheet,这里注意一个sheet不能读取多次，多次读取需要重新读取文件
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>
     * 2. 由于默认异步读取excel，所以需要创建excel一行一行的回调监听器，参照{@link ExcelListener}
     * <p>
     * 3. 直接读即可
     */
    public static void repeatedRead() {
        String fileName = "/Users/daiyanping/Documents/TEST.xlsx";
        // 读取全部sheet
        // 这里需要注意 ExcelListener的doAfterAllAnalysed 会在每个sheet读取完毕后调用一次。然后所有sheet都会往同一个ExcelListener里面写
        EasyExcel.read(fileName, DemoData.class, new ExcelListener()).doReadAll();

//        // 读取部分sheet
//        String fileName2 = "/Users/daiyanping/Documents/TEST.xlsx";
//        ExcelReader excelReader = EasyExcel.read(fileName).build();
//        // 这里为了简单 所以注册了 同样的head 和Listener 自己使用功能必须不同的Listener
//        ReadSheet readSheet1 =
//                EasyExcel.readSheet(0).head(DemoData.class).registerReadListener(new ExcelListener()).build();
//        ReadSheet readSheet2 =
//                EasyExcel.readSheet(1).head(DemoData.class).registerReadListener(new ExcelListener()).build();
//        // 这里注意 一定要把sheet1 sheet2 一起传进去，不然有个问题就是03版的excel 会读取多次，浪费性能
//        excelReader.read(readSheet1, readSheet2);
//        // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
//        excelReader.finish();
    }

}
