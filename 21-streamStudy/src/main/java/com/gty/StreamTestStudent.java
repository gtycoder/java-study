package com.gty;

import lombok.Data;

import java.util.Objects;

@Data
public class StreamTestStudent {
    /**
     * 学号
     */
    private Integer id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 分数
     */
    private Double score;
    /**
     * 年级
     */
    private String gradeName;
    /**
     * 班级
     */
    private String className;

    public StreamTestStudent(Integer id, String name, Double score, String gradeName, String className) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.gradeName = gradeName;
        this.className = className;
    }

    public StreamTestStudent() {
    }

    /**
     * 重写equals和hashCode方法
     * 只要id相同就是同一个学生
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StreamTestStudent that = (StreamTestStudent) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
