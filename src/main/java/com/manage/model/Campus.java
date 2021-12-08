package com.manage.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
    @date 2021/12/5 18:22
*/
@Data
@TableName(value = "campus")
public class Campus implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 校区名称
     */
    private String name;

    /**
     * 校区地址
     */
    private String address;

    /**
     * 负责人
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String manager;

    /**
     * 建造时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date buildTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModify;


}