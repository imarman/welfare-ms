package com.manage.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @date 2021/12/4 15:34
 */
@Data
@TableName(value = "sys_user")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户手机
     */
    private String mobile;

    /**
     * 头像文件
     */
    private String avatar;

    /**
     * 性别
     */
    private String gender;

    /**
     * 角色
     */
    private String role;

    /**
     * 是否是 root 用户
     */
    private Integer root;

    /**
     * 生日
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birthday;

    /**
     * 注册时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regTime;

    /**
     * 用户状态(正常:0, 未激活:1, 冻结:2)
     */
    private Integer status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModified;
}