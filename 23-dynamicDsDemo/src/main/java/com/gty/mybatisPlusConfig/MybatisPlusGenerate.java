package com.gty.mybatisPlusConfig;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;

public class MybatisPlusGenerate {


    public static void main(String[] args) {
        //数据库类型
        DbType dbType = DbType.CLICK_HOUSE;
        //数据库名,postgre不用配置
        String dbName = "fruit";
        //表名
        String[] tableList = new String[]{"t_user_two"};

        generateCode(dbType, dbName, tableList);
    }


    /*
     * 1. 修改各种类名和xml名称
     * 2. 如果是ck数据库的实体类,需要额外添加数据库名@TableName("dws.dws_market_30roi")
     * 3. 修改controller中的访问路径
     * */
    public static void generateCode(DbType dbType, String dbName, String[] tableList) {
        /*-----------------------开始生成-------------------------------------*/
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");

        gc.setOutputDir(projectPath + "/23-dynamicDsDemo/src/main/java");
        gc.setOpen(false);//生成完成后不弹出文件框
        gc.setFileOverride(true);  //文件覆盖
        gc.setActiveRecord(false);// 不需要ActiveRecord特性的请改为false
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(false);// XML columList
        gc.setAuthor("gaotainyu");// 作者
        // gc.setSwagger2(true); 实体属性 Swagger2 注解

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setControllerName("%sController");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("112233");
        dsc.setUrl("jdbc:mysql://localhost:3306/dynamic_two?serverTimezone=GMT%2B8");  //指定数据库
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.gty");
        pc.setEntity("domain");
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/23-dynamicDsDemo/src/main/resources/mapper/xml" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);

        //strategy.setSuperEntityColumns("id");     // 写于父类中的公共字段
        strategy.setTablePrefix("t_");   //配置去掉生成实体类时的前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);      // 表名生成策略
        strategy.setColumnNaming(NamingStrategy.no_change);  //字段生成策略
        strategy.setInclude(tableList);     // 需要生成的表
        strategy.setEntityLombokModel(true);  //配置使用lombok
        strategy.setChainModel(true);
        strategy.setRestControllerStyle(true);   //使用rest
        //strategy.setControllerMappingHyphenStyle(true);    //使用中划线
        strategy.setEntityTableFieldAnnotationEnable(true);  //生成字段注解
        strategy.setEntityColumnConstant(true);   //生成字段常量

        //strategy.setSuperEntityClass("com.gty.domain.BaseColumn");

        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();

    }
}
