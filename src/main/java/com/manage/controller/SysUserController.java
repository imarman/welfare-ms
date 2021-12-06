package com.manage.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.manage.model.SysUser;
import com.manage.model.comm.R;
import com.manage.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @date 2021/12/4 15:53
 */
@RestController
@RequestMapping("/sysUser")
@Slf4j
public class SysUserController {

    @Resource
    SysUserService sysUserService;


    @SaCheckLogin
    @GetMapping("all")
    public R getAllUser() {
        List<SysUser> list = sysUserService.list();
        return R.ok(list);
    }


}

