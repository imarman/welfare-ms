package com.manage.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manage.common.BusinessException;
import com.manage.common.ResultCodeEnum;
import com.manage.model.Campus;
import com.manage.model.Teacher;
import com.manage.model.comm.R;
import com.manage.model.resp.CampusPageResponse;
import com.manage.service.CampusService;
import com.manage.service.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @date 2021/12/5 18:24
 */
@RestController
@RequestMapping("/campus")
@Slf4j
public class CampusController {

    @Resource
    CampusService campusService;

    @Resource
    TeacherService teacherService;

    @GetMapping("/list")
    @SaCheckLogin
    public R getAll() {
        return R.ok(campusService.list());
    }

    @GetMapping("/pageList/{current}/{limit}")
    @SaCheckLogin
    public R getAllByPage(@PathVariable Integer current, @PathVariable Integer limit,
                          @RequestParam(required = false) String name,
                          @RequestParam(required = false) String manager,
                          @RequestParam(required = false, name = "timeRange") String timeRangeStr) {
        log.info("getAllByPage方法执行，参数：current:{}, limit:{}, name:{}, manager:{}, timeRangeStr:{}", current, limit, name, manager, timeRangeStr);

        Page<Campus> page = new Page<>(current, limit);
        LambdaQueryWrapper<Campus> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(name)) {
            wrapper.like(Campus::getName, name);
        }
        if (StrUtil.isNotBlank(manager)) {
            wrapper.eq(Campus::getManager, manager);
        }
        if (StrUtil.isNotBlank(timeRangeStr)) {
            String[] dateRange = timeRangeStr.split(",");
            wrapper.between(Campus::getBuildTime, dateRange[0], dateRange[1]);
        }
        Page<Campus> campusPage = campusService.page(page, wrapper);
        CampusPageResponse campusResponse = new CampusPageResponse();
        campusResponse.setPages(campusPage.getPages());
        campusResponse.setTotal(campusPage.getTotal());
        campusResponse.setCampusList(campusPage.getRecords());
        return R.ok(campusResponse);
    }

    @PostMapping("/save")
    public R saveOrUpdate( @RequestBody Campus campus) {
        log.info("saveOrUpdate方法执行，参数: campus:{}", campus);
        if (campus == null) {
            return R.error();
        }
        if (campus.getId() == null) {
            campus.setGmtCreate(new Date());
        }
        campus.setGmtModify(new Date());
        campusService.saveOrUpdate(campus);
        return R.ok();
    }

    @DeleteMapping("/delete/{id}")
    public R saveOrUpdate(@PathVariable String id) {
        log.info("saveOrUpdate方法执行，参数：id:{}", id);

        LambdaQueryWrapper<Teacher> wrapper = new LambdaQueryWrapper<>();
        Teacher one = teacherService.getOne(wrapper.eq(Teacher::getCampus, id));
        if (one != null) {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR, "已有教师在该校区，取消关联后在删除");
        }
        if (StrUtil.isNotBlank(id)) {
            campusService.removeById(id);
            return R.ok();
        }
        return R.error();
    }


}
