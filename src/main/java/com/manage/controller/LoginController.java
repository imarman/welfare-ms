package com.manage.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Session;
import com.manage.common.VerificationCode;
import com.manage.model.comm.R;
import com.manage.model.req.SysUserReqModel;
import com.manage.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @date 2021/12/4 16:00
 */
@RestController
@Slf4j
public class LoginController {

    @Resource
    SysUserService sysUserService;

    @PostMapping("/login")
    public R login(@RequestBody SysUserReqModel userModel, HttpServletRequest request) {
        if (StpUtil.isLogin()) {
            return R.ok("该用户已经登陆");
        }
        String verifyCode = (String) request.getSession().getAttribute("verify_code");
        if (userModel.getVerifyCode() == null || !StrUtil.equalsAnyIgnoreCase(verifyCode, userModel.getVerifyCode())) {
            return R.error("验证码错误");
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

    @GetMapping("/verifyCode")
    public void verifyCode(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        VerificationCode code = new VerificationCode();
        BufferedImage image = code.getImage();
        String text = code.getText();
        HttpSession session = request.getSession(true);
        session.setAttribute("verify_code", text);
        log.info("验证码：{}", text);
        VerificationCode.output(image, resp.getOutputStream());
    }

}
