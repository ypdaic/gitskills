package com.daiyanping.cms.DB;

import org.springframework.stereotype.Component;

/**
 * @ClassName DBThreadLocal
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-03
 * @Version 0.1
 */
@Component
public class DBThreadLocal {

    private ThreadLocal<DBTypeEnum> dbTypeEnumThreadLocal = new ThreadLocal<DBTypeEnum>();

    /**
     * 获取当前线程上的数据源类型
     * @return
     */
    public DBTypeEnum getDBType() {
        return dbTypeEnumThreadLocal.get();
    }

    /**
     * 在当前线程上设置数据源类型
     * @param dbTypeEnum
     */
    public void setDBType(DBTypeEnum dbTypeEnum) {
        dbTypeEnumThreadLocal.set(dbTypeEnum);
    }

    /**
     * 操作完成后，清除数据源类型
     */
    public void cleanDBType() {
        dbTypeEnumThreadLocal.remove();
    }

}
