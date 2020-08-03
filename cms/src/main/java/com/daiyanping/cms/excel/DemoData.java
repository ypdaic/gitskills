package com.daiyanping.cms.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @ClassName DemoData
 * @Description 不使用注解指定顺序的话，默认的排序和excel里面的排序一致
 * @Author daiyanping
 * @Date 2020-01-21
 * @Version 0.1
 */
@Data
public class DemoData {

    /**
     * 可以使用index, 也可以使用列名
     */
    @ExcelProperty(index = 0)
    private String name;

    @ExcelProperty(index = 1)
    private String age;

    @ExcelProperty(index = 2)
    private String email;


}
