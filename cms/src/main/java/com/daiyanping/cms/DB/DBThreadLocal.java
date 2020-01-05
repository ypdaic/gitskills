package com.daiyanping.cms.DB;

import org.springframework.stereotype.Component;

/**
 * @ClassName DBThreadLocal
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-03
 * @Version 0.1
 */
public class DBThreadLocal {

    private static final ThreadLocal<DBTypeEnum> dbTypeEnumThreadLocal = new ThreadLocal<DBTypeEnum>();

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
        System.out.println("清除数据源");
        dbTypeEnumThreadLocal.remove();
    }

    public static void master(){
        setDBType(DBTypeEnum.MASTER);
        System.out.println("切换到主库-----------------------");
    }

    public static void slave(){
        setDBType(DBTypeEnum.SLAVE);//轮询
        System.out.println("切换到从库-----------------------");
    }
}
