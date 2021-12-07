package com.manage.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manage.model.Campus;
import com.manage.model.SysUser;
import com.manage.model.comm.R;
import com.manage.model.resp.CampusPageResponse;
import com.manage.service.CampusService;
import com.manage.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @date 2021/12/5 18:24
 */
@RestController
@RequestMapping("/campus")
@Slf4j
public class CampusController {

    @Resource
    CampusService campusService;

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

            // LocalDateTime began = LocalDateTimeUtil.parse(dateRange[0], "yyyy-MM-dd");
            // LocalDateTime end = LocalDateTimeUtil.parse(dateRange[1], "yyyy-MM-dd");
            wrapper.between(Campus::getBuildTime, dateRange[0], dateRange[1]);
        }
        Page<Campus> campusPage = campusService.page(page, wrapper);
        CampusPageResponse campusResponse = new CampusPageResponse();
        campusResponse.setPages(campusPage.getPages());
        campusResponse.setTotal(campusPage.getTotal());
        campusResponse.setCampusList(campusPage.getRecords());
        return R.ok(campusResponse);
    }



}
