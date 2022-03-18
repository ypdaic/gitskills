package com.daiyanping.cms.hbase;

import org.apache.phoenix.queryserver.client.ThinClientUtil;

import java.sql.*;

/**
 * PhoenixTest
 *
 * @author daiyanping
 * @date 2022-03-18
 * @description
 */
public class PhoenixTest {

    public static void main(String[] args) throws SQLException {

        String connectionUrl = ThinClientUtil.getConnectionUrl("192.168.56.101", 8765);
        System.out.println(connectionUrl);
        Connection connection = DriverManager.getConnection(connectionUrl);
        PreparedStatement preparedStatement = connection.prepareStatement("select * from \"test\"");

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.println(resultSet.getString(1) + "\t" + resultSet.getString(2));
        }

        //关闭
        connection.close();

    }

}
