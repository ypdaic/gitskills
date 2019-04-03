import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @ClassName CodeGenerator
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-01
 * @Version 0.1
 */
public class CodeGenerator {

    private final static String PROJECT_PATH = "/Users/daiyanping/git-clone-repository/gitskills/mybatis1";

    private final static String MODULE_NAME = "test";

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator gen = new AutoGenerator();

        GlobalConfig globalConfig = new GlobalConfig();
        // 输出目录，必须填写绝对路径
        globalConfig.setOutputDir(PROJECT_PATH + "/src/main/java");
        // 是否覆盖文件
        globalConfig.setFileOverride(true);
        // 开启 activeRecord 模式
        globalConfig.setActiveRecord(true);
        // XML 二级缓存
        globalConfig.setEnableCache(false);
        // XML ResultMap
        globalConfig.setBaseResultMap(true);
        // XML columList
        globalConfig.setBaseColumnList(true);
        // 生成后打开文件夹
        globalConfig.setOpen(false);
        globalConfig.setAuthor("daiyanping");
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        globalConfig.setMapperName("%sMapper");
        globalConfig.setXmlName("%sMapper");
        globalConfig.setServiceName("I%sService");
        globalConfig.setServiceImplName("%sServiceImpl");
        globalConfig.setControllerName("%sController");

        /**
         * 全局配置
         */
        gen.setGlobalConfig(globalConfig);

        PackageConfig packageConfig = new PackageConfig();
        // 模块名
        packageConfig.setModuleName(MODULE_NAME);
        // 自定义包路径
        packageConfig.setParent("com.daiyanping.codegenerator");
        // 这里是控制器包名，默认 web
        packageConfig.setController("controller");
        // 实体类 包名
        packageConfig.setEntity("entity");
        // mapper 包名
        packageConfig.setMapper("mapper");
        // service 包名
        packageConfig.setService("service");
        // service 实现类包名
        packageConfig.setServiceImpl("service.impl");
        // mapper映射文件 包名   默认会在mapper目录下生成xml目录，并放置mapper.xml文件
//        packageConfig.setXml("xml");

        /**
         * 包配置
         */
        gen.setPackageInfo(packageConfig);

        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL);
        dataSourceConfig.setDriverName("com.mysql.jdbc.Driver");
        //如果是MySQL新版本得加上时区，否则报错
        dataSourceConfig.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=UTC");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("test1234");
        dataSourceConfig.setTypeConvert(new MySqlTypeConvert() {
            // 自定义数据库表字段类型转换【可选】
            @Override
            public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                System.out.println("转换类型：" + fieldType);
                // if ( fieldType.toLowerCase().contains( "tinyint" ) ) {
                //    return DbColumnType.BOOLEAN;
                // }
                return super.processTypeConvert(globalConfig, fieldType);
            }
        });

        /**
         * 数据库配置
         */
        gen.setDataSource(dataSourceConfig);


        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
//        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        String templatePath = "/templates/mapper.xml.vm";

        // 自定义文件输出位置（非必须）
        List<FileOutConfig> fileOutList = new ArrayList<FileOutConfig>();
        // 自定义配置会被优先输出
        fileOutList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return PROJECT_PATH + "/src/main/resources/mappers/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });

        cfg.setFileOutConfigList(fileOutList);
        gen.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        // 指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // 默认不用指定
//        templateConfig.setController("templates/controller.java");
//        templateConfig.setEntity("templates/entity.java");
//        templateConfig.setService("templates/service.java");
//        templateConfig.setMapper("templates/mapper.java");
//        templateConfig.setServiceImpl("templates/serviceImpl.java");
        // 给null，默认会在mapper包下生成xml文件夹放mapper.xml   这个xml文件我们自定义生成目录
//        templateConfig.setXml(null);
        gen.setTemplate(templateConfig);

        /**
         * 模板配置
         */
//        gen.setTemplate(
//                // 关闭默认 xml 生成 使用自定配置生成到指定目录
                new TemplateConfig().setXml(null);
//                // 自定义模板配置，模板可以参考源码 /mybatis-plus/src/main/resources/template 使用 copy
//                // 至您项目 src/main/resources/template 目录下，模板名称也可自定义如下配置：
//                // .setController("...");
//                // .setEntity("...");
//                // .setMapper("...");
//                // .setXml("...");
//                // .setService("...");
//                // .setServiceImpl("...");
//        );


        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // 全局大写命名
        strategy.setCapitalMode(false);
        // 需要生成的表的表名前缀
        strategy.setTablePrefix(new String[]{});
        // 需要生成的表的表名的策略  此处为下滑线分隔（具体给什么参数以数据库为准）
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 需要生成的表的字段名的策略  此处为下滑线分隔 （具体给什么参数以数据库为准）
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // 需要生成的表的表名
        strategy.setInclude(new String[]{"account"});
        // 需要排除生成的表的表名
//        strategy.setExclude(new String[]{"test"});
        // 设置实体类的父类 默认Model
        strategy.setSuperEntityClass("com.baomidou.mybatisplus.extension.activerecord.Model");
        // 自定义 mapper 父类 默认BaseMapper
        strategy.setSuperMapperClass("com.baomidou.mybatisplus.core.mapper.BaseMapper");
        // 自定义 service 父类 默认IService
        strategy.setSuperServiceClass("com.baomidou.mybatisplus.extension.service.IService");
        // 自定义 service 实现类父类 默认ServiceImpl
        strategy.setSuperServiceImplClass("com.baomidou.mybatisplus.extension.service.impl.ServiceImpl");
        // 自定义 controller 父类
//        strategy.setSuperControllerClass("com.kichun."+packageName+".controller.AbstractController");
        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
        strategy.setEntityColumnConstant(false);
        // 【实体】是否为lombok模型（默认 false）<a href="https://projectlombok.org/">document</a>
        strategy.setEntityLombokModel(false);
        // 【实体】是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
        strategy.setEntityBuilderModel(true);
        // 添加restController 注解
        strategy.setRestControllerStyle(true);
//        strategy.setSuperControllerClass("com.baomidou.ant.common.BaseController");
        // 自定义实体类的公共字段
        strategy.setSuperEntityColumns("id");
        // 驼峰转连字符
        strategy.setControllerMappingHyphenStyle(true);
        gen.setStrategy(strategy);

        gen.setTemplateEngine(new VelocityTemplateEngine());
        gen.execute();
    }
}
