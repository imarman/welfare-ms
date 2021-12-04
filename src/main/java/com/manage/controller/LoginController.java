package com.manage.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.manage.model.comm.R;
import com.manage.model.req.SysUserReqModel;
import com.manage.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @date 2021/12/4 16:00
 */
@RestController
@Slf4j
public class LoginController {

    @Resource
    SysUserService sysUserService;

    @PostMapping("/login")
    public R login(@RequestBody SysUserReqModel userModel) {
        if (StpUtil.isLogin()) {
            return R.ok("该用户已经登陆");
        }
        boolean res = sysUserService.login(userModel);
        if (res) {
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            return R.ok(tokenInfo);
        }
        return R.error("登陆失败");
    }

    @SaCheckLogin
    @GetMapping("/logout")
    public R logout() {
        if (!StpUtil.isLogin()) {
            return R.error("请先登陆");
        }
        StpUtil.logout(StpUtil.getLoginId());
        return R.ok("成功退出");
    }

}
