import entity.User;
import mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.*;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.Test;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.jdbc.datasource.embedded.DataSourceFactory;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

import static javafx.scene.input.KeyCode.T;
import static org.apache.ibatis.session.ExecutorType.BATCH;

/**
 * 使用最原始的mybatis只需要mybatis-config.xml  mapper.xml就够了
 */
public class UserTest {

    /**
     * 验证mybatis属性配置的加载顺序
     *
     * 如果属性在不只一个地方进行了配置，那么 MyBatis 将按照下面的顺序来加载：
     * 在 properties 元素体内指定的属性首先被读取。
     * 然后根据 properties 元素中的 resource 属性读取类路径下属性文件或根据 url 属性指定的路径读取属性文件，并覆盖已读取的同名属性。
     * 最后读取作为方法参数传递的属性，并覆盖已读取的同名属性。
     * 因此，通过方法参数传递的属性具有最高优先级，resource/url 属性中指定的配置文件次之，最低优先级的是 properties 属性中指定的属性。
     * @throws Exception
     */
    @Test
    public void test1() throws Exception{
            Properties properties = new Properties();
            properties.put("username", "root");
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, properties);
            SqlSession session = sqlSessionFactory.openSession();
            try {
                //直接使用映射文件去查询
                //使用Mapper.xml的完全限定名去获取查询语句
                User user = session.selectOne("UserMapper.getAll", 1);
                System.out.println(user.getName());
            } finally {
                session.close();
            }

    }

    /**
     * 验证映射接口使用
     * @throws Exception
     */
    @Test
    public void test2() throws Exception{
        Properties properties = new Properties();
        properties.put("username", "root");
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, properties);
        SqlSession session = sqlSessionFactory.openSession(BATCH);
        try {
            // 使用映射接口去查询
            // 映射接口和映射文件的关联由映射文件的namespace属性进行关联
            // 如果namespace写的不对，使用接口将会失败
            UserMapper mapper = session.getMapper(UserMapper.class);
//
//            User user = mapper.getAll(1);
            User user = new User();
            user.setAge(94);
            user.setId(-900);
            user.setEmail("sdfsfs");
            user.setName("ssss");
            mapper.insert(user);

            System.out.println(user.getName());
            mapper.update(user);
        } finally {
            session.commit();
            session.close();
        }

    }

    /**
     * 使用mybatis-spring
     */
    @Test
    public void test3() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        ClassPathResource classPathResource = new ClassPathResource("mybatis-config.xml");
        sqlSessionFactoryBean.setConfigLocation(classPathResource);
        SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();
        simpleDriverDataSource.setDriverClass(com.mysql.jdbc.Driver.class);
        simpleDriverDataSource.setPassword("test1234");
        simpleDriverDataSource.setUsername("root");
        simpleDriverDataSource.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF8");
        TransactionAwareDataSourceProxy transactionAwareDataSourceProxy = new TransactionAwareDataSourceProxy();
        transactionAwareDataSourceProxy.setTargetDataSource(simpleDriverDataSource);
        transactionAwareDataSourceProxy.afterPropertiesSet();
        sqlSessionFactoryBean.setDataSource(transactionAwareDataSourceProxy);
        sqlSessionFactoryBean.afterPropertiesSet();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);


        /**
         *
         * @Override
         * public <T > T getMapper(Class < T > type) {
         *     return getConfiguration().getMapper(type, this);
         * }
         * 这里将sqlSessionTemplate注入到了Mapper接口的代理中，最终执行sql的是
         * sqlSessionTemplate中的sqlSessionProxy代理类，代理处理时会创建DefaultSession
         * 然后openSession时，会将SpringManagedTransactionFactory 注入进去，事物交由SpringManagedTransactionFactory处理
         *
         */
        UserMapper mapper = sqlSessionTemplate.getMapper(UserMapper.class);
        mapper.getAll(1);

    }

    /**
     * jdbc原生事物处理
     */
    @Test
    public void test4() {
        Connection connection = null;
        try {

            SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();
            simpleDriverDataSource.setDriverClass(com.mysql.jdbc.Driver.class);
            simpleDriverDataSource.setPassword("test1234");
            simpleDriverDataSource.setUsername("root");
            simpleDriverDataSource.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF8");
            connection = simpleDriverDataSource.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("update user set name = ? where id = ?");
            preparedStatement.setString(1, "daiyanping1");
            preparedStatement.setInt(2, 2);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (Exception e1) {

            }
        } finally {
            try {
                connection.commit();
                connection.close();
            } catch (Exception e2) {

            }
        }
    }

    /**
     * 以上所有都是可以调用DataSourceUtils化简代码，而JdbcTemplate又是调用DataSourceUtils的。所以在 Spring文档中要求尽量首先使用JdbcTemplate，其次是用DataSourceUtils来获取和释放连接。至于 TransactionAwareDataSourceProxy，那是下策的下策。不过可以将Spring事务管理和遗留代码无缝集成。
     * 所以如某位朋友说要使用Spring的事务管理，但是又不想用JdbcTemplate，那么可以考虑TransactionAwareDataSourceProxy。这个类是原来DataSource的代理。
     * 其次，想使用Spring事物，又不想对Spring进行依赖是不可能的。与其试图自己模拟DataSourceUtils，不如直接使用现成的。
     *
     * 下面就是验证TransactionAwareDataSourceProxy的作用
     */
    @Test
    public void test5() {
        Connection connection = null;
        DataSourceTransactionManager dataSourceTransactionManager = null;
        TransactionStatus transaction = null;
        TransactionAwareDataSourceProxy transactionAwareDataSourceProxy = null;
        try {
            //创建数据源
            SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();
            simpleDriverDataSource.setDriverClass(com.mysql.jdbc.Driver.class);
            simpleDriverDataSource.setPassword("test1234");
            simpleDriverDataSource.setUsername("root");
            simpleDriverDataSource.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF8");

            //创建数据源代理
            transactionAwareDataSourceProxy = new TransactionAwareDataSourceProxy();
            transactionAwareDataSourceProxy.setTargetDataSource(simpleDriverDataSource);
            transactionAwareDataSourceProxy.afterPropertiesSet();

            //开启spring事物管理
            dataSourceTransactionManager = new DataSourceTransactionManager();
            dataSourceTransactionManager.setDataSource(transactionAwareDataSourceProxy);
            dataSourceTransactionManager.afterPropertiesSet();
            DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();


            //开启事物并创建链接，并设置事物属性，将链接放到ThreadLocal中
            transaction = dataSourceTransactionManager.getTransaction(defaultTransactionDefinition);
            //通过spring的数据源代理，获取已经创建了的链接，将原始jdbc与spring事物关联起来
            connection = transactionAwareDataSourceProxy.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("update user set name = ? where id = ?");
            preparedStatement.setString(1, "daiyanping1");
            preparedStatement.setInt(2, 2);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            try {
                dataSourceTransactionManager.rollback(transaction);
            } catch (Exception e1) {

            }
        } finally {
            try {
                /*
                事物提交会自动关闭链接，具体由DataSourceUtils.releaseConnection(this.connection, this.dataSource);这个方法关闭
                该方法广泛用于spring的jdbcTemplate(即spring jdbc中),mybatis-spring的SpringManagedTransaction,TransactionAwareDataSourceProxy,
                DataSourceTransactionManager
                 */
                dataSourceTransactionManager.commit(transaction);
            } catch (Exception e2) {

            }
        }


    }

    /**
     * 使用DataSourceUtils将原生jdbc与spring事物结合
     */
    @Test
    public void test6() {
        Connection connection = null;
        DataSourceTransactionManager dataSourceTransactionManager = null;
        TransactionStatus transaction = null;
        TransactionAwareDataSourceProxy transactionAwareDataSourceProxy = null;
        try {
            //创建数据源
            SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();
            simpleDriverDataSource.setDriverClass(com.mysql.jdbc.Driver.class);
            simpleDriverDataSource.setPassword("test1234");
            simpleDriverDataSource.setUsername("root");
            simpleDriverDataSource.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF8");

            //开启spring事物管理
            dataSourceTransactionManager = new DataSourceTransactionManager();
            dataSourceTransactionManager.setDataSource(simpleDriverDataSource);
            dataSourceTransactionManager.afterPropertiesSet();
            DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();


            //开启事物并创建链接，并设置事物属性，将链接放到ThreadLocal中
            transaction = dataSourceTransactionManager.getTransaction(defaultTransactionDefinition);
            //直接通过DataSourceUtils获取已经创建了的链接
            connection = DataSourceUtils.getConnection(simpleDriverDataSource);
            connection = transactionAwareDataSourceProxy.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("update user set name = ? where id = ?");
            preparedStatement.setString(1, "daiyanping1");
            preparedStatement.setInt(2, 2);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            try {
                dataSourceTransactionManager.rollback(transaction);
            } catch (Exception e1) {

            }
        } finally {
            try {
                /*
                事物提交会自动关闭链接，具体由DataSourceUtils.releaseConnection(this.connection, this.dataSource);这个方法关闭
                该方法广泛用于spring的jdbcTemplate(即spring jdbc中),mybatis-spring的SpringManagedTransaction,TransactionAwareDataSourceProxy,
                DataSourceTransactionManager
                 */
                dataSourceTransactionManager.commit(transaction);
            } catch (Exception e2) {

            }
        }


    }

    /**
     * 使用TransactionAwareDataSourceProxy将原生mybatis与spring事物结合
     */
    @Test
    public void test7() throws Exception {
        //创建数据源
        SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();
        simpleDriverDataSource.setDriverClass(com.mysql.jdbc.Driver.class);
        simpleDriverDataSource.setPassword("test1234");
        simpleDriverDataSource.setUsername("root");
        simpleDriverDataSource.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF8");

        //创建数据源代理
        TransactionAwareDataSourceProxy transactionAwareDataSourceProxy = new TransactionAwareDataSourceProxy();
        transactionAwareDataSourceProxy.setTargetDataSource(simpleDriverDataSource);
        transactionAwareDataSourceProxy.afterPropertiesSet();

        //开启spring事物管理
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(transactionAwareDataSourceProxy);
        dataSourceTransactionManager.afterPropertiesSet();
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();

        //使用mybatis提供的事物工厂
        JdbcTransactionFactory jdbcTransactionFactory = new JdbcTransactionFactory();
        //将事务代理放到环境属性中
        Environment environment = new Environment("test", jdbcTransactionFactory, transactionAwareDataSourceProxy);

        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        sqlSessionFactory.getConfiguration().setEnvironment(environment);
        //开启事物并创建链接，并设置事物属性，将链接放到ThreadLocal中
        TransactionStatus transaction = dataSourceTransactionManager.getTransaction(defaultTransactionDefinition);
        SqlSession session = sqlSessionFactory.openSession();
        try {

            UserMapper mapper = session.getMapper(UserMapper.class);

            mapper.getAll(1);
        } finally {
             /*
                事物提交会自动关闭链接，具体由DataSourceUtils.releaseConnection(this.connection, this.dataSource);这个方法关闭
                该方法广泛用于spring的jdbcTemplate(即spring jdbc中),mybatis-spring的SpringManagedTransaction,TransactionAwareDataSourceProxy,
                DataSourceTransactionManager
                 */
//            dataSourceTransactionManager.commit(transaction);
        }


//        //创建数据源
//        SimpleDriverDataSource simpleDriverDataSource1 = new SimpleDriverDataSource();
//        simpleDriverDataSource1.setDriverClass(com.mysql.jdbc.Driver.class);
//        simpleDriverDataSource1.setPassword("test1234");
//        simpleDriverDataSource1.setUsername("root");
//        simpleDriverDataSource1.setUrl("jdbc:mysql://localhost:3306/test2?useUnicode=true&characterEncoding=UTF8");
//
//        //创建数据源代理
//        TransactionAwareDataSourceProxy transactionAwareDataSourceProxy1 = new TransactionAwareDataSourceProxy();
//        transactionAwareDataSourceProxy1.setTargetDataSource(simpleDriverDataSource1);
//        transactionAwareDataSourceProxy1.afterPropertiesSet();
//        //切换数据源
//        dataSourceTransactionManager.setDataSource(transactionAwareDataSourceProxy1);
        //开启事物并创建链接，并设置事物属性，将链接放到ThreadLocal中
        TransactionStatus transaction1 = dataSourceTransactionManager.getTransaction(defaultTransactionDefinition);
        SqlSession session1 = sqlSessionFactory.openSession();
        try {

            UserMapper mapper = session1.getMapper(UserMapper.class);

            mapper.getAll(1);
        } finally {
             /*
                事物提交会自动关闭链接，具体由DataSourceUtils.releaseConnection(this.connection, this.dataSource);这个方法关闭
                该方法广泛用于spring的jdbcTemplate(即spring jdbc中),mybatis-spring的SpringManagedTransaction,TransactionAwareDataSourceProxy,
                DataSourceTransactionManager
                 */
            dataSourceTransactionManager.commit(transaction1);
        }
    }
}
