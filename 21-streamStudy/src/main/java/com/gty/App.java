package com.gty;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class App {
    List<StreamTestStudent> studentList = new ArrayList<>();

    {
        //先初始化一些数据
        studentList.add(new StreamTestStudent(1, "oneone", 56.1, "二年级", "一班"));
        studentList.add(new StreamTestStudent(2, "twotwo", 34.6, "二年级", "一班"));
        studentList.add(new StreamTestStudent(3, "threethree", 93.2, "二年级", "一班"));
        studentList.add(new StreamTestStudent(4, "fourfour", 80.0, "二年级", "二班"));
        studentList.add(new StreamTestStudent(5, "FiveFive", 45.4, "二年级", "二班"));
        studentList.add(new StreamTestStudent(6, "sixsix", 77.3, "一年级", "二班"));
        studentList.add(new StreamTestStudent(7, "sevenseven", 84.3, "一年级", "二班"));
        studentList.add(new StreamTestStudent(8, "eighteight", 91.2, "一年级", "二班"));
        studentList.add(new StreamTestStudent(9, "ninenine", 44.4, "一年级", "一班"));
        studentList.add(new StreamTestStudent(10, "tenten", 69.0, "一年级", "一班"));
        //重复元素,重写的equals方法,只要id同则同一位同学
        studentList.add(new StreamTestStudent(1, "tenten", 42.3, "一年级", "一班"));
        //studentList.add(new StreamTestStudent(2, "tenten22", 59.0, "一年级", "一班"));
        //studentList.add(new StreamTestStudent(3, "tenten33", 59.0, "一年级", "一班"));
    }

    /**
     * 简单的进行遍历
     */
    @Test
    public void test01() {
        studentList.forEach(temp -> System.out.println(temp));
    }

    /**
     * 去重
     */
    @Test
    public void test02() {
        List<StreamTestStudent> distinctList = studentList.stream().distinct().collect(Collectors.toList());
        System.out.println("原来长度======" + studentList.size());
        System.out.println("去重后的长度======" + distinctList.size());
        distinctList.forEach(t -> System.out.println(t));
    }

    /**
     * 排序
     * (a, b) -> a.getId() - b.getId() 升序 后大于前
     * (a, b) -> b.getId() - a.getId() 降序  前大于后  大的在后边
     * a,b是两个对象的形参
     */
    @Test
    public void test03() {
        List<StreamTestStudent> sortList = studentList.stream().sorted((a, b) -> b.getId() - a.getId()).collect(Collectors.toList());
        sortList.forEach(t -> System.out.println(t));
    }

    /**
     * 筛选
     * 按照一定条件筛选出符合要求的  (求分数大于60的同学)
     */
    @Test
    public void test04() {
        List<StreamTestStudent> filterList = studentList.stream().filter(temp -> temp.getScore() > 60).collect(Collectors.toList());
        System.out.println("筛选后长度===" + filterList.size());
        filterList.forEach(t -> System.out.println(t));
    }

    /**
     * 提取
     * 提取出对象的某一条属性   (将对象的名称组合成列表)
     */
    @Test
    public void test05() {
        List<String> nameList = studentList.stream().map(temp -> temp.getName()).collect(Collectors.toList());
        nameList.forEach(t -> System.out.println(t));
    }

    /**
     * 将列表转为map,map的key为姓名,value为当前对象
     */
    @Test
    public void test06() {
        //StreamTestStudent::getName为获取当前对象的name属性,
        // (a, b) -> b 是指当有重复的key时,使用b进行后边的替换前边的 ;;;
        // 同理(a, b) -> a  就是不进行替换;;; [其实这是实现了一个比较接口,和上边的排序一样]a
        // 若是没有这一项有重复的key时会抛出异常
        Map<String, StreamTestStudent> toStudentMap = studentList.stream().collect(Collectors.toMap(StreamTestStudent::getName, temp -> temp, (a, b) -> a));
        toStudentMap.entrySet().forEach(e -> System.out.println(e.getKey() + "==" + e.getValue()));
    }

    /**
     * 统计
     * 按照一定条件求和  求总分
     */
    @Test
    public void test07() {
        //总分
        double sum = studentList.stream().mapToDouble(temp -> temp.getScore()).sum();
        int sum1 = studentList.stream().mapToInt(temp -> temp.getId()).sum();
        System.out.println(sum1);
        System.out.println(sum);

        //大于60的人的总分
        double sum2 = studentList.stream().filter(temp -> temp.getScore() > 60).mapToDouble(temp -> temp.getScore()).sum();
        System.out.println(sum2);
    }

    /**
     * 分组
     * 按照一定条件拆分list为多个list.   按照年级分组
     */
    @Test
    public void test08() {
        Map<String, List<StreamTestStudent>> gradeNameGroup = studentList.stream().collect(Collectors.groupingBy(temp -> temp.getGradeName()));
        gradeNameGroup.entrySet().forEach(e -> {
            e.getValue().forEach(t -> System.out.println(e.getKey() + "==" + t.toString()));
        });
    }

    /**
     * 多重分组
     * 按照一定条件拆分list为多个list.   按照年级分组,然后再按照班级分组
     */
    @Test
    public void test09() {
        Map<String, Map<String, List<StreamTestStudent>>> groupByGradeNextClass = studentList.stream().collect(Collectors.groupingBy(temp -> temp.getGradeName(), Collectors.groupingBy(temp -> temp.getClassName())));
        groupByGradeNextClass.entrySet().forEach(e -> {
            Map<String, List<StreamTestStudent>> value = e.getValue();
            value.entrySet().forEach(m -> {
                m.getValue().forEach(t -> System.out.println(e.getKey() + "==" + m.getKey() + "==" + t.toString()));
            });
        });
    }

    /**
     * 多重分组  然后进行分组计算
     * 按照一定条件拆分list为多个list.   按照年级分组,然后再按照班级分组,最后计算班级总分
     */
    @Test
    public void test10() {
        Map<String, Map<String, Double>> groupAndSum = studentList.stream().collect(Collectors.groupingBy(temp -> temp.getGradeName(), Collectors.groupingBy(temp -> temp.getClassName(), Collectors.summingDouble(temp -> temp.getScore()))));
        groupAndSum.entrySet().forEach(e -> {
            e.getValue().entrySet().forEach(t -> {
                System.out.println(e.getKey() + "==" + t.getKey() + "==" + t.getValue());
            });
        });

    }

    /**
     * 多重分组  然后进行分组计算
     * 按照一定条件拆分list为多个list.   按照年级分组,然后再按照班级分组,最后计算班级中分数及格(>60)的人数
     */
    @Test
    public void test11() {
        Map<String, Map<String, Long>> groupCount = studentList.stream().filter(temp -> temp.getScore() > 60).collect(Collectors.groupingBy(StreamTestStudent::getGradeName, Collectors.groupingBy(StreamTestStudent::getClassName, Collectors.counting())));
        groupCount.entrySet().forEach(e -> {
            e.getValue().entrySet().forEach(t -> System.out.println(e.getKey() + "==" + t.getKey() + "==" + t.getValue()));
        });
    }

}
