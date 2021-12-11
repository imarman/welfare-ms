package com.manage.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manage.common.BusinessException;
import com.manage.common.ResultCodeEnum;
import com.manage.common.RoleConst;
import com.manage.model.Campus;
import com.manage.model.SysUser;
import com.manage.model.comm.R;
import com.manage.model.resp.SysUserResponse;
import com.manage.service.CampusService;
import com.manage.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

    @Resource
    CampusService campusService;


    /**
     * 获取所有校区负责人
     */
    @SaCheckLogin
    @GetMapping("all/{current}/{limit}")
    public R getAllUser(@PathVariable Integer current, @PathVariable Integer limit,@RequestParam String name) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        Page<SysUser> sysUserPage = new Page<>(current, limit);
        wrapper.eq(SysUser::getRoot, 0);
        if (!StrUtil.isBlank(name)) {
            wrapper.like(SysUser::getUsername, name);
        }
        Page<SysUser> page = sysUserService.page(sysUserPage, wrapper);
        List<SysUserResponse> arrayList = new ArrayList<>();
        for (SysUser sysUser : page.getRecords()) {
            SysUserResponse response = new SysUserResponse();
            BeanUtil.copyProperties(sysUser, response, true);
            LambdaQueryWrapper<Campus> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Campus::getManager, sysUser.getId());
            Campus one = campusService.getOne(queryWrapper);
            if (one != null) {
                response.setManageCampus(one.getName() + "负责人");
            }
            arrayList.add(response);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("total", page.getTotal());
        map.put("pages", page.getPages());
        map.put("managerList", arrayList);
        return R.ok(map);
    }

    @PostMapping("save")
    @SaCheckLogin
    public R save(@RequestBody SysUser sysUser) {
        if (sysUser.getMobile() == null || StrUtil.isBlank(sysUser.getMobile())) {
            throw new BusinessException(ResultCodeEnum.PARAMS_MISSING, "手机号不能为空，并且唯一");
        }
        SysUser one = sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getMobile, sysUser.getMobile()));
        if (one != null) {
            throw new BusinessException(ResultCodeEnum.REGISTER_MOBILE_ERROR,"该手机号已被使用，请更换手机号～");
        }
        if (sysUser.getId() == null) {
            sysUser.setGmtCreate(new Date());
        }
        sysUser.setGmtModified(new Date());
        sysUser.setRole(RoleConst.MANAGER_ROLE);
        sysUser.setRoot(0);
        if (StrUtil.isNotBlank(sysUser.getPassword())) {
            String MD5Pwd = MD5.create().digestHex16(sysUser.getPassword());
            sysUser.setPassword(MD5Pwd);
        }
        if (sysUserService.saveOrUpdate(sysUser)) {
            return R.ok();
        }
        return R.error();
    }

    @DeleteMapping("/delete/{id}")
    @SaCheckLogin
    @Transactional(rollbackFor = Exception.class)
    public R delete(@PathVariable String id) {
        log.info("delete方法执行，参数：id:{}", id);
        Campus one = campusService.getOne(new LambdaQueryWrapper<Campus>().eq(Campus::getManager, id));
        // 如果该负责人负责了某个校区，删除前先把关联的校区的负责人字段清空
        if (one != null) {
            one.setManager(null);
            campusService.updateById(one);
        }

        return sysUserService.removeById(id) ? R.ok(): R.error();
    }

    @GetMapping("/managers")
    public R getManagers() {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getRole, RoleConst.MANAGER_ROLE);
        return R.ok(sysUserService.list(wrapper));
    }


    @GetMapping("/avoidManager/{manager}")
    public R getAvoidManager(@PathVariable("manager") String managerId) {
        log.info("getAvoidManager方法执行，参数：managerId:{}", managerId);
        SysUser user = sysUserService.getById(managerId);
        List<SysUser> resList = campusService.getAvoidManager();
        if (user != null) {
            resList.add(user);
        }
        return R.ok(resList);
    }

}

