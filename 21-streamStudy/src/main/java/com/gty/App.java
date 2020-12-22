package com.gty;

import cn.hutool.core.collection.CollUtil;
import org.junit.Test;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
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
        toStudentMap.forEach((key, value) -> System.out.println(key + "==" + value));
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
        gradeNameGroup.forEach((key, value) -> value.forEach(t -> System.out.println(key + "==" + t.toString())));
    }

    /**
     * 多重分组
     * 按照一定条件拆分list为多个list.   按照年级分组,然后再按照班级分组
     */
    @Test
    public void test09() {
        Map<String, Map<String, List<StreamTestStudent>>> groupByGradeNextClass = studentList.stream().collect(Collectors.groupingBy(temp -> temp.getGradeName(), Collectors.groupingBy(temp -> temp.getClassName())));
        groupByGradeNextClass.forEach((key1, value) -> value.forEach((key, value1) -> value1.forEach(t -> System.out.println(key1 + "==" + key + "==" + t.toString()))));
    }

    /**
     * 多重分组  然后进行分组计算
     * 按照一定条件拆分list为多个list.   按照年级分组,然后再按照班级分组,最后计算班级总分
     */
    @Test
    public void test10() {
        Map<String, Map<String, Double>> groupAndSum = studentList.stream().collect(Collectors.groupingBy(temp -> temp.getGradeName(), Collectors.groupingBy(temp -> temp.getClassName(), Collectors.summingDouble(temp -> temp.getScore()))));
        groupAndSum.forEach((key, value) -> value.forEach((key1, value1) -> System.out.println(key + "==" + key1 + "==" + value1)));

    }

    /**
     * 多重分组  然后进行分组计算
     * 按照一定条件拆分list为多个list.   按照年级分组,然后再按照班级分组,最后计算班级中分数及格(>60)的人数
     */
    @Test
    public void test11() {
        Map<String, Map<String, Long>> groupCount = studentList.stream().filter(temp -> temp.getScore() > 60).collect(Collectors.groupingBy(StreamTestStudent::getGradeName, Collectors.groupingBy(StreamTestStudent::getClassName, Collectors.counting())));
        groupCount.forEach((key, value) -> value.entrySet().forEach(t -> System.out.println(key + "==" + t.getKey() + "==" + t.getValue())));
    }

    /**
     * flatMap 方法用于映射每个元素到对应的结果，一对多。
     * 示例:从句子中得到单词
     */
    @Test
    public void test12() {
        ArrayList<String> words = CollUtil.newArrayList("The way of the future");
        List<String> collect = words.stream().flatMap(s -> Stream.of(s.split(" "))).filter(s -> s.length() > 2).collect(Collectors.toList());
        collect.forEach(System.out::println);
    }

    /**
     * limit(5) 取前5条记录
     * skip(2)  调过前两条记录
     */
    @Test
    public void test13() {
        ArrayList<String> strList = CollUtil.newArrayList("aa", "bb", "cc", "dd", "ee", "ff", "gg");
        //   2 <= i < 5
        List<String> collect = strList.stream().limit(5).skip(2).collect(Collectors.toList());
        collect.forEach(System.out::println);
    }


    /**
     * 使用peek()保存中间操作
     */
    @Test
    public void test14() {
        ArrayList<String> strList = CollUtil.newArrayList("one", "two", "three", "four", "five");
        ArrayList<Object> firstList = CollUtil.newArrayList();
        List<String> collect = strList.stream().filter(s -> s.length() > 3).peek(firstList::add).map(String::toUpperCase).peek(e -> System.out.println("第二次" + e)).collect(Collectors.toList());
        collect.forEach(System.out::println);
        System.out.println("---------------------");
        firstList.forEach(System.out::println);
    }

    /**
     * Stream流的Match使用
     * allMatch： Stream 中全部元素符合则返回 true
     * anyMatch： Stream 中只要有一个元素符合则返回 true
     * noneMatch： Stream 中没有一个元素符合则返回 true
     */
    @Test
    public void test15() {
        ArrayList<Integer> integers = CollUtil.newArrayList(1, 2, 3, 4, 5, 6, 7, 8);
        boolean all = integers.stream().allMatch(i -> i > 3);
        System.out.println("是否全部大于3的" + all);

        boolean any = integers.stream().anyMatch(i -> i > 3);
        System.out.println("是否有一个是大于3的" + any);

        boolean none = integers.stream().noneMatch(i -> i > 3);
        System.out.println("是否没有一个大于3的" + none);

    }


    @Test
    public void test16() {
        //-----------字符串连接------------
//        ArrayList<String> strings = CollUtil.newArrayList("a", "b", "c", "d");
//        String reduce = strings.stream().reduce("profix,", String::concat);
//        System.out.println(reduce);

        //-------------求和/最值------------------
        ArrayList<Double> doubles = CollUtil.newArrayList(1.1, 2.3, 0.3, 4.34, 5.33);
        //Double reduce1 = doubles.stream().reduce(Double.MAX_VALUE, Double::sum);
        Optional<Double> reduce1 = doubles.stream().reduce(Double::max);
        System.out.println(reduce1.get());


    }

    /**
     * iterate 跟 reduce 操作很像，
     * 接受一个种子值，和一个UnaryOperator（例如 f）。
     * 然后种子值seed成为 Stream 的第一个元素，
     * f(seed) 为第二个，
     * f(f(seed)) 第三个，以此类推。
     * 在 iterate 时候管道必须有 limit 这样的操作来限制 Stream 大小。
     */
    @Test
    public void test17() {
        //从首项2开始生成一个公差为3的等差数列
        Stream.iterate(2,n->n+3).limit(5).forEach(System.out::println);
    }


    /**
     * 通过实现Supplier类的方法可以自定义流计算规则。
     */
    class MySupplier implements Supplier<String>{
        //自定义随机数
        Random random = new Random();

        @Override
        public String get() {
            return "随机数"+random.nextInt(1000);
        }
    }
    @Test
    public void test18() {
        Stream.generate(new MySupplier()).limit(4).forEach(t-> System.out.println(t));
    }


    /**
     * IntSummaryStatistics 用于收集统计信息(如count、min、max、sum和average)的状态对象。
     */
    @Test
    public void test19() {
        List<Integer> numbers = Arrays.asList(1, 5, 7, 3, 9);
        IntSummaryStatistics stats = numbers.stream().mapToInt((x) -> x).summaryStatistics();

        System.out.println("列表中最大的数 : " + stats.getMax());
        System.out.println("列表中最小的数 : " + stats.getMin());
        System.out.println("所有数之和 : " + stats.getSum());
        System.out.println("平均数 : " + stats.getAverage());
    }

}
