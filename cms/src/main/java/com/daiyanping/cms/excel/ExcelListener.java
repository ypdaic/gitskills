package com.daiyanping.cms.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ExcelListener
 * @Description TODO
 * @Author daiyanping
 * @Date 2020-01-21
 * @Version 0.1
 */
@Slf4j
public class ExcelListener extends AnalysisEventListener<DemoData> {

    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;

    //自定义用于暂时存储data。
    //可以通过实例获取该值
    private List<DemoData> datas = new ArrayList<>();

    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     *
     */
    private DemoDao demoDAO;

    /**
     * 通过 AnalysisContext 对象还可以获取当前 sheet，当前行等数据
     */
    @Override
    public void invoke(DemoData demoData, AnalysisContext analysisContext) {
        //数据存储到list，供批量处理，或后续自己业务逻辑处理。
        datas.add(demoData);
        System.out.println(demoData);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (datas.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            datas.clear();
        }
    }

    /**
     * 第一行头数据
     *
     * @param headMap
     * @param context
     */
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        headMap.forEach((key, value) -> {
            System.out.println(value);
        });
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
         /*
           datas.clear();
           解析结束销毁不用的资源
        */
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", datas.size());
        demoDAO.save(datas);
        log.info("存储数据库成功！");
    }

    public List<DemoData> getDatas() {
        return datas;
    }

    public void setDatas(List<DemoData> datas) {
        this.datas = datas;
    }
}
