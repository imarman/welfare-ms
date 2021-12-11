package com.manage.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manage.model.WelfareCategory;
import com.manage.model.comm.R;
import com.manage.service.WelfareCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @date 2021/12/11 12:04
 */
@RestController()
@RequestMapping("/welfare-categories")
@Slf4j
public class WelfareCategoryController {

    @Resource
    WelfareCategoryService welfareCategoryService;

    @GetMapping("/list")
    public R getAllCategories(@RequestParam(value = "categoryName", required = false) String categoryName) {
        LambdaQueryWrapper<WelfareCategory> wrapper = Wrappers.lambdaQuery();
        if (categoryName != null && StrUtil.isNotBlank(categoryName)) {
            wrapper.like(WelfareCategory::getCategoryName, categoryName);
        }
        return R.ok(welfareCategoryService.list(wrapper));
    }

    @PostMapping("/save")
    public R save(@RequestBody WelfareCategory welfareCategory) {
        if (welfareCategory.getId() == null) {
            welfareCategory.setGmtCreate(new Date());
        }
        welfareCategory.setGmtModified(new Date());
        boolean res = welfareCategoryService.saveOrUpdate(welfareCategory);
        return res ? R.ok() : R.error();
    }

    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable String id) {
        boolean res = welfareCategoryService.removeById(id);
        return res ? R.ok() : R.error();

    }

}
