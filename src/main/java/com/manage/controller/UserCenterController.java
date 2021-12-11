package com.manage.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.manage.common.BusinessException;
import com.manage.common.ResultCodeEnum;
import com.manage.model.Campus;
import com.manage.model.SysUser;
import com.manage.model.comm.R;
import com.manage.model.req.ChangePwdReqModel;
import com.manage.model.resp.SysUserResponse;
import com.manage.service.CampusService;
import com.manage.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @date 2021/12/11 15:36
 */
@RestController
@RequestMapping("/user-center")
@Slf4j
public class UserCenterController {

    @Resource
    SysUserService sysUserService;

    @Resource
    CampusService campusService;

    @GetMapping("/userinfo")
    public R getUserInfo(@RequestParam(value = "id", required = false) String id) {
        SysUser user = sysUserService.getById(id);
        user.setPassword(null);
        SysUserResponse sysUserResponse = new SysUserResponse();
        BeanUtil.copyProperties(user, sysUserResponse);
        Campus one = campusService.getOne(new LambdaQueryWrapper<Campus>().eq(Campus::getManager, id));
        if (one != null) {
            sysUserResponse.setManageCampus(one.getName());
        }
        return R.ok(sysUserResponse);
    }

    @PutMapping("/changePwd")
    public R changePwd(@RequestBody ChangePwdReqModel reqModel) {
        log.info("更改密码方法执行，参数：reqModel:{}", reqModel);
        SysUser sysUser = sysUserService.getById(reqModel.getId());
        // 对输入的老密码进行加密
        String encryptOldPwd = MD5.create().digestHex16(reqModel.getOldPassword());
        if (!StrUtil.equals(sysUser.getPassword(), encryptOldPwd)) {
            throw new BusinessException(ResultCodeEnum.PARAMS_MISSING, "旧密码错误，请重新输入");
        }
        sysUser.setPassword(MD5.create().digestHex16(reqModel.getNewPassword()));
        return sysUserService.updateById(sysUser) ? R.ok() : R.error();
    }

    @PutMapping("/changeInfo")
    public R changeInfo(@RequestBody SysUser sysUser) {
        log.info("更改用户信息方法执行，参数：sysUser:{}", sysUser);
        sysUser.setGmtModified(new Date());
        return sysUserService.updateById(sysUser) ? R.ok() : R.error();
    }

}
