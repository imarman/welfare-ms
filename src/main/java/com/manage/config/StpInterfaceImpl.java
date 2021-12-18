package com.manage.config;


import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.manage.model.SysUser;
import com.manage.service.SysUserService;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限验证接口扩展
 */
@Configuration    // 保证此类被SpringBoot扫描，完成Sa-Token的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {

    @Resource
    SysUserService sysUserService;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return new ArrayList<>();
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 本list仅做模拟，实际项目中要根据具体业务逻辑来查询角色
        String id = (String) StpUtil.getLoginId();
        SysUser sysUser = sysUserService.getById(id);
        // String[] split = sysUser.getRole().split(",");
        ArrayList<String> strings = new ArrayList<>();
        strings.add(sysUser.getRole());
        return strings;
    }

}