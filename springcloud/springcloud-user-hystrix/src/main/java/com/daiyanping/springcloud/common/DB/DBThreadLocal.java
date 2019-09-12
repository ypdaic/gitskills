package com.daiyanping.springcloud.common.DB;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName DBThreadLocal
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-03
 * @Version 0.1
 */
@Slf4j
public class DBThreadLocal {

    private static ThreadLocal<DBTypeEnum> dbTypeEnumThreadLocal = new ThreadLocal<DBTypeEnum>();

    /**
     * 获取当前线程上的数据源类型
     * @return
     */
    public static DBTypeEnum getDBType() {
        return dbTypeEnumThreadLocal.get();
    }

    /**
     * 在当前线程上设置数据源类型
     * @param dbTypeEnum
     */
    public static void setDBType(DBTypeEnum dbTypeEnum) {
        dbTypeEnumThreadLocal.set(dbTypeEnum);
    }

    /**
     * 操作完成后，清除数据源类型
     */
    public static void cleanDBType() {
        log.info("清除数据源");
        dbTypeEnumThreadLocal.remove();
    }

}
