package com.manage.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manage.common.RoleConst;
import com.manage.mapper.CampusMapper;
import com.manage.model.Campus;
import com.manage.model.SysUser;
import com.manage.service.CampusService;
import com.manage.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @date 2021/12/5 18:22
 */
@Service
public class CampusServiceImpl extends ServiceImpl<CampusMapper, Campus> implements CampusService {

    @Resource
    SysUserService sysUserService;

    @Override
    public List<SysUser> getAvoidManager() {

        List<SysUser> list = sysUserService.list(new LambdaQueryWrapper<SysUser>().eq(SysUser::getRole, RoleConst.MANAGER_ROLE));
        // 获取所有负责人
        List<String> sysIdList = list.stream().map(sysUser -> sysUser.getId().toString()).collect(Collectors.toList());

        // 获取所有已经负责校区的负责人
        List<String> campusManagerList = getBaseMapper().selectList(null).stream().map(Campus::getManager).distinct().collect(Collectors.toList());

        // 挑出暂时还没负责校区的"空闲"负责人
        List<String> collect = CollUtil.disjunction(sysIdList, campusManagerList).stream().filter(Objects::nonNull).collect(Collectors.toList());
        List<SysUser> sysUsers = new ArrayList<>();
        collect.forEach(id -> sysUsers.add(sysUserService.getById(id)));

        return sysUsers;
    }
}
