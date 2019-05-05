package com.daiyanping.cms;

import com.daiyanping.cms.batchSubmission.AppConfig;
import com.daiyanping.cms.dao.UserDao;
import com.daiyanping.cms.entity.User;
import com.daiyanping.cms.service.impl.MyRunable;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

/**
 * @ClassName MybatisTests
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-30
 * @Version 0.1
 */
public class MybatisTests {

    @Test
    public void test() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
        annotationConfigApplicationContext.register(MybatisMapperScanTest.class);
        annotationConfigApplicationContext.refresh();
        UserDao bean = annotationConfigApplicationContext.getBean(UserDao.class);
        List<Map<String, Object>> list = bean.queryPage();
        System.out.println(list);
        List<Map<String, Object>> list1 = bean.queryPage("");
        System.out.println(list1);

    }

    @Test
    public void test2() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
        annotationConfigApplicationContext.register(MybatisMapperScanTest.class);
        annotationConfigApplicationContext.refresh();
        MyRunable bean = (MyRunable) annotationConfigApplicationContext.getBean(MyRunable.class);
        new Thread(bean).start();
        try {
            Thread.sleep(1000  * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
