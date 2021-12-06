package com.manage.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.manage.common.VerificationCode;
import com.manage.model.SysUser;
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
import java.util.HashMap;

import static com.manage.common.ResultCodeEnum.LOGIN_AUTH;

/**
 * @date 2021/12/4 16:00
 */
@RestController
@Slf4j
public class LoginController {

    private static final HashMap<String, String> map = new HashMap<>();

    private static final String VERIFY_CODE = "verify_code";

    @Resource
    SysUserService sysUserService;

    @PostMapping("/login")
    public R login(@RequestBody SysUserReqModel userModel) {
        String verifyCode = map.get(VERIFY_CODE);
        if (verifyCode == null) {
            return R.error("网络开小差了～～");
        }
        if (userModel.getVerifyCode() == null || !StrUtil.equalsAnyIgnoreCase(verifyCode, userModel.getVerifyCode())) {
            return R.error("验证码错误");
        }
        map.remove(VERIFY_CODE);
        boolean res = sysUserService.login(userModel);
        if (res) {
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            return R.ok(tokenInfo);
        }
        return R.error("登陆失败");
    }

    @GetMapping("/logout")
    public R logout() {
        if (!StpUtil.isLogin()) {
            return R.errorMsg(LOGIN_AUTH, "请重新登录!");
        }
        StpUtil.logout(StpUtil.getLoginId());
        return R.ok("成功退出");
    }

    /**
     * 获取用户信息
     */
    @SaCheckLogin
    @GetMapping("/sysUser/getInfo")
    public R getInfo() {
        log.info("获取用户信息");
        String userID = (String) StpUtil.getLoginId();
        SysUser sysUser = sysUserService.getById(userID);
        HashMap<String,Object> userInfo = new HashMap<>();
        userInfo.put("name", sysUser.getUsername());
        userInfo.put("role", sysUser.getRole());
        userInfo.put("avatar", sysUser.getAvatar());
        return R.ok(userInfo);
    }

    @GetMapping("/verifyCode")
    public void verifyCode(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        VerificationCode code = new VerificationCode();
        BufferedImage image = code.getImage();
        String text = code.getText();
        resp.setDateHeader("Expires", 0);
        map.put(VERIFY_CODE, text);
        HttpSession session = request.getSession();
        session.setAttribute(VERIFY_CODE, text);
        log.info("验证码：{}", text);
        VerificationCode.output(image, resp.getOutputStream());
    }

}
