import entity.User;
import mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.DataSourceFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

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
        SqlSession session = sqlSessionFactory.openSession();
        try {
            // 使用映射接口去查询
            // 映射接口和映射文件的关联由映射文件的namespace属性进行关联
            // 如果namespace写的不对，使用接口将会失败
            UserMapper mapper = session.getMapper(UserMapper.class);

            User user = mapper.getAll(1);

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
        sqlSessionFactoryBean.setDataSource(simpleDriverDataSource);
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
         * sqlSessionTemplate中的sqlSessionProxy代理类，该类中
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

}
