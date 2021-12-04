package com.manage.model.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @date 2021/12/4 16:06
 */
@Data
public class SysUserReqModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;


    /**
     * 用户手机
     */
    private String mobile;

    /**
     * 验证码
     */
    private String verifyCode;


}
