package com.gty.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FreeMarkerUtils {
    //@Value("${gtyfreemarker.templatePath}")
    private static String templatePath = "classpath:/freemarker/templates/";

    private String packageName = "com.gty.domain";

    public static void main(String[] args) throws IOException, TemplateException {
        FreeMarkerUtils freeMarkerUtils = new FreeMarkerUtils();
        freeMarkerUtils.generateJavaModelCode2File();
    }

    /**
     * 将文件流直接压缩后返回客户端,可以避免在服务端生成文件.
     * @param response
     * @param zipFileName
     * @throws Exception
     */
    public void generateJavaCodeFile2Client(HttpServletResponse response,String zipFileName) throws Exception {
        //构建模板引擎
        Configuration configuration = buildFreemarkerTemplate();
        Template temp = configuration.getTemplate("Model.ftl");

        Map<String, Object> infoMap01 = generateData("test111111");
        Map<String, Object> infoMap02 = generateData("test22222");
        Map<String, Object> infoMap03 = generateData("test33333");

        //使用字节流来合并模板,并且将文件输出流直接给压缩,多文件可以使用同一个流,只需要重置
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(arrayOutputStream);
        temp.process(infoMap01, writer);

        //将流转为字节数组,方便压缩输出
        List<Map<String, byte[]>> compressInfoList = new ArrayList<>();
        byte[] bytes01 = arrayOutputStream.toByteArray();

        //组建压缩信息交给压缩工具类,这里可以使用一个对象,为了简单使用了map
        Map<String, byte[]> compressInfo01 = new HashMap<>();
        compressInfo01.put(infoMap01.get("className") + ".java", bytes01);
        compressInfoList.add(compressInfo01);

        //可以使用一个流完成,只需要清空即可
        arrayOutputStream.reset();
        writer.flush();

        //组建第二个流信息
        temp.process(infoMap02, writer);
        byte[] bytes02 = arrayOutputStream.toByteArray();
        Map<String, byte[]> compressInfo02 = new HashMap<>();
        //同时可以携带文件路径
        compressInfo02.put(generateFilePath()+infoMap02.get("className") + ".java", bytes02);
        compressInfoList.add(compressInfo02);

        arrayOutputStream.reset();
        writer.flush();

        //组建第三个流信息
        temp.process(infoMap03, writer);
        byte[] bytes03 = arrayOutputStream.toByteArray();
        Map<String, byte[]> compressInfo03 = new HashMap<>();
        compressInfo03.put(generateFilePath()+infoMap03.get("className") + ".java", bytes03);
        compressInfoList.add(compressInfo03);

        //以上可以使用循环完成,为了简单,不用了
        FileCompressUtils.downloadZipStream(response,compressInfoList,zipFileName);
        writer.close();
        //writer02.close();
        //writer03.close();
    }


    /**
     *  生成代码到文件里去
     * @throws IOException
     * @throws TemplateException
     */
    public void generateJavaModelCode2File() throws IOException, TemplateException {
        //构建模板引擎
        Configuration configuration = buildFreemarkerTemplate();
        Template temp = configuration.getTemplate("Model.ftl");

        //构造k-v型数据
        Map<String, Object> infoMap = generateData("test001");

        //使用文件输出流输出成一个文件,    文件路径和文件名,    文件名同类名
        File fileDir = new File("D://test/" + generateFilePath());
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        //文件完整路径
        File outputFile = new File(fileDir,infoMap.get("className") + ".java");
        FileOutputStream stream = new FileOutputStream(outputFile);
        OutputStreamWriter writer = new OutputStreamWriter(stream);
        temp.process(infoMap, writer);

        //关闭流
        writer.close();
        stream.close();
        System.out.println("生成代码成功");
    }

    /**
     * 构建模板引擎
     * @return
     * @throws IOException
     */
    public Configuration buildFreemarkerTemplate() throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        cfg.setDirectoryForTemplateLoading(new File(generateAbsoluteTemplatePath()));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        return cfg;
    }

    /**
     * 从包路径得到文件路径
     */
    public String generateFilePath() {
        return packageName.replaceAll("\\.", "\\/") + "\\/";
    }


    /**
     * 从项目相对路径获取为磁盘绝对路径
     */
    public static String generateAbsoluteTemplatePath() {
        return FreeMarkerUtils.class.getClass().getResource("/").getFile() + templatePath.substring(ResourceUtils.CLASSPATH_URL_PREFIX.length() + 1);
    }

    /**
     * 类名将下划线转为驼峰
     * 例如 md_user_name -> MdUserName
     *
     * @param className
     */
    private String generateClassName(String className) {
        if (className != null) {
            String[] oleStrs = className.split("\\_");
            List<StringBuilder> newStrings = new ArrayList<>();

            for (int i = 0; i < oleStrs.length; i++) {
                char[] chars = oleStrs[i].toCharArray();
                chars[0] -= 32;
                newStrings.add(new StringBuilder(String.valueOf(chars)));
            }

            StringBuilder sb = new StringBuilder();
            for (StringBuilder newString : newStrings) {
                sb = sb.append(newString);
            }
            return String.valueOf(sb);
        }
        return null;
    }

    /**
     * 生成测试数据
     */
    public Map<String, Object> generateData(String salt) {
        Map<String, Object> infoMap = new HashMap<>();
        //类的一些信息,例如类名,包名,作者,时间戳之类的
        infoMap.put("packageName", packageName);
        infoMap.put("className", generateClassName(salt+"_md_cof_user_info_data"));

        //信息少可以使用位于import javax.management.Attribute;包中是一个name-value为key键值对
        //ArrayList<Attribute> fieldList = new ArrayList<>();

        //信息较多,比如字段类型,名称,注释等信息,就只用Map了.
        ArrayList<Map<String, String>> fieldList = new ArrayList<>();

        HashMap<String, String> fieldInfoMap01 = new HashMap<>();
        fieldInfoMap01.put("type", "String");
        fieldInfoMap01.put("name", salt+"name");
        fieldInfoMap01.put("note", "这是姓名");
        fieldList.add(fieldInfoMap01);

        HashMap<String, String> fieldInfoMap02 = new HashMap<>();
        fieldInfoMap02.put("type", "Long");
        fieldInfoMap02.put("name", salt+"id");
        fieldInfoMap02.put("note", "这是编号");
        fieldList.add(fieldInfoMap02);

        HashMap<String, String> fieldInfoMap03 = new HashMap<>();
        fieldInfoMap03.put("type", "Boolean");
        fieldInfoMap03.put("name", salt+"flag");
        fieldInfoMap03.put("note", "是否生效");
        fieldList.add(fieldInfoMap03);


        //字段信息统一放入map中
        infoMap.put("fields", fieldList);
        return infoMap;
    }


}
