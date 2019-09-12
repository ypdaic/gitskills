package com.daiyanping.springcloud.common.DB;

/**
 * 数据源类型
 */
public enum DBTypeEnum {

    DB1("db1"),DB2("db2");
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
