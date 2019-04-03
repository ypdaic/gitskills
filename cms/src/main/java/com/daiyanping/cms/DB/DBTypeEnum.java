package com.daiyanping.cms.DB;

/**
 * 数据源类型
 */
public enum DBTypeEnum {

    TEST("test1"),TEST2("test2");
    private String dbName;

    DBTypeEnum(String dbName) {
        this.dbName = dbName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}
