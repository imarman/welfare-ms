package com.manage.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.manage.common.BusinessException;
import com.manage.common.ResultCodeEnum;
import com.manage.model.req.SysUserReqModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manage.mapper.SysUserMapper;
import com.manage.model.SysUser;
import com.manage.service.SysUserService;
/**
    @date 2021/12/4 15:33
*/
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService{

    @Override
    public boolean login(SysUserReqModel userModel) {
        if (StrUtil.isEmpty(userModel.getMobile()) || StrUtil.isEmpty(userModel.getPassword())) {
            log.info("参数缺失- userModel:{}", userModel);
            throw new BusinessException(ResultCodeEnum.LOGIN_FAIL, "邮箱或密码不能为空");
        }
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getMobile, userModel.getMobile());
        SysUser user = getBaseMapper().selectOne(queryWrapper);
        if (user == null) {
            log.info("用户不存在");
            throw new BusinessException(ResultCodeEnum.USER_NOT_EXIST);
        }
        // 判断用户是否已经激活
        // if (UserStatus.INACTIVE.getCode().equals(user.getStatus())) {
        //     log.info("用户:{}({}) 尚未激活,拒绝登陆", user.getUsername(), user.getId());
        //     throw new BusinessException(ResultCodeEnum.USER_INACTIVE);
        // }

        // 拿到数据库用户的盐值，和输入的密码做加密后对比
        String encryptPwd = MD5.create().digestHex16(userModel.getPassword());
        // 账号不匹配
        if (!StrUtil.equals(encryptPwd, user.getPassword())) {
            log.error("用户:{} 输入密码错误", user.getUsername());
            throw new BusinessException(ResultCodeEnum.BAD_PASSWORD);
        }
        // 账号匹配
        StpUtil.login(user.getId());

        // 存入到 session 时要屏蔽敏感数据
        user.setPassword(null);
        // 把用户存到 session 中，方便取出用户信息
        StpUtil.getSession().set("user", user);
        return true;
    }
}
