package com.manage.model.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @date 2021/12/11 15:43
 */
@Data
public class ChangePwdReqModel implements Serializable {

    private static final long serialVersionUID = 2L;

    /**
     * 用户id
     */
    private String id;

    /**
     * 旧密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    private String newPassword;

}
