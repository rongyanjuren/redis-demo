package com.shiyulong.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author 石玉龙
 * @since 2025-01-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String cname;

    private Integer age;

    private String phone;

    private Integer sex;

    private Date birth;


}
