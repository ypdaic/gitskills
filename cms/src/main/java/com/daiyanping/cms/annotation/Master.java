package com.daiyanping.cms.annotation;

/**
 * 类说明：查询数据方法都是从从库读取数据，如果方法上用了此注解的，表示要求强制走主库
 */
public @interface Master {

}
