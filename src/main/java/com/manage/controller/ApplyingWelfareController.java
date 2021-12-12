package com.manage.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manage.common.ApplyingConst;
import com.manage.common.RoleConst;
import com.manage.model.ApplyWelfare;
import com.manage.model.Campus;
import com.manage.model.SysUser;
import com.manage.model.Teacher;
import com.manage.model.comm.R;
import com.manage.service.ApplyWelfareService;
import com.manage.service.CampusService;
import com.manage.service.SysUserService;
import com.manage.service.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @date 2021/12/12 14:41
 */
@RestController
@RequestMapping("/apply")
@Slf4j
public class ApplyingWelfareController {

    @Resource
    ApplyWelfareService applyWelfareService;

    @Resource
    CampusService campusService;

    @Resource
    SysUserService sysUserService;

    @Resource
    TeacherService teacherService;

    @PostMapping("/save")
    public R save(@RequestBody ApplyWelfare applyWelfare) {
        log.info("添加审核方法执行，参数：applyWelfare:{}", applyWelfare);
        applyWelfare.setGmtModified(new Date());
        applyWelfare.setGmtCreate(new Date());
        applyWelfare.setStatus(ApplyingConst.APPLYING);
        return applyWelfareService.save(applyWelfare) ? R.ok() : R.error();
    }

    @GetMapping("/all")
    public R list() {

        SysUser sysUser = sysUserService.getById(StpUtil.getLoginId().toString());
        if (sysUser == null) {
            return R.ok(new ArrayList<String>());
        }

        // 如果是校区负责人
        if (RoleConst.MANAGER_ROLE.equals(sysUser.getRole())) {
            Campus campus = campusService.getOne(new LambdaQueryWrapper<Campus>().eq(Campus::getManager, sysUser.getId()));
            if (campus == null) {
                return R.ok(new ArrayList<String>());
            }
            List<String> teacherList = teacherService.list(new LambdaQueryWrapper<Teacher>().eq(Teacher::getCampus, campus.getId()))
                    .stream().map(teacher -> teacher.getId().toString()).collect(Collectors.toList());
            LambdaQueryWrapper<ApplyWelfare> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(ApplyWelfare::getStatus, ApplyingConst.APPLYING);
            List<ApplyWelfare> list = applyWelfareService.list(wrapper);
            List<ApplyWelfare> resList = list.stream().filter(item -> teacherList.contains(item.getTeacherId())).collect(Collectors.toList());
            resList.forEach(applyWelfare -> {
                Teacher byId = teacherService.getById(applyWelfare.getTeacherId());
                applyWelfare.setTeacherName(byId.getName());
            });
            return R.ok(resList);
        }
        // 如果是领导
        if (RoleConst.LEADER_ROLE.equals(sysUser.getRole())) {
            LambdaQueryWrapper<ApplyWelfare> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(ApplyWelfare::getStatus, ApplyingConst.NEXT);
            List<ApplyWelfare> list = applyWelfareService.list(wrapper);
            list.forEach(applyWelfare -> {
                Teacher byId = teacherService.getById(applyWelfare.getTeacherId());
                applyWelfare.setTeacherName(byId.getName());
            });
            return R.ok(list);
        }
        if (RoleConst.ADMIN_ROLE.equals(sysUser.getRole())) {
            List<ApplyWelfare> list = applyWelfareService.list();
            list.forEach(applyWelfare -> {
                Teacher byId = teacherService.getById(applyWelfare.getTeacherId());
                applyWelfare.setTeacherName(byId.getName());
            });
            return R.ok(list);
        }
        return R.ok(new ArrayList<>());
    }

    @PutMapping("/change-status")
    public R changeStatus(@RequestBody Map<String, String> map) {
        log.info("changeStatus方法执行，参数：map:{}", map);
        String id = map.get("id");
        String status = map.get("status");
        ApplyWelfare applyWelfare = applyWelfareService.getById(id);
        applyWelfare.setStatus(status);
        return applyWelfareService.updateById(applyWelfare) ? R.ok() : R.error();
    }

}
