package com.gty.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author gaotainyu
 * @since 2020-12-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_user_one")
public class UserOne implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("age")
    private Integer age;


    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String AGE = "age";

}
