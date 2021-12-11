package com.manage.service;

import com.manage.model.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manage.model.req.SysUserReqModel;

/**
 * @date 2021/12/4 15:33
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 登陆
     */
    boolean login(SysUserReqModel userModel);
}
