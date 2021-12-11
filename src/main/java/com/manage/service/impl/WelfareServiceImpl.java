package com.manage.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manage.common.BusinessException;
import com.manage.common.ResultCodeEnum;
import com.manage.model.WelfareCategory;
import com.manage.model.req.WelfareReqModel;
import com.manage.model.resp.WelfareResponse;
import com.manage.service.WelfareCategoryService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manage.mapper.WelfareMapper;
import com.manage.model.Welfare;
import com.manage.service.WelfareService;

import javax.annotation.Resource;
import java.util.List;

/**
    @date 2021/12/11 13:01
*/
@Service
public class WelfareServiceImpl extends ServiceImpl<WelfareMapper, Welfare> implements WelfareService{

    @Resource
    WelfareCategoryService welfareCategoryService;

    @Override
    public WelfareResponse selectWelfareByWrapper(WelfareReqModel model) {
        if (model.getLimit() == null || model.getCurrent() == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR);
        }
        WelfareMapper baseMapper = getBaseMapper();
        LambdaQueryWrapper<Welfare> wrapper = new LambdaQueryWrapper<>();
        if (model.getName() != null && !StrUtil.isBlankIfStr(model.getName())) {
            wrapper.like(Welfare::getName, model.getName());
        }
        if (model.getCategoryId() != null && !StrUtil.isBlankIfStr(model.getCategoryId())) {
            wrapper.eq(Welfare::getCategoryId, model.getCategoryId());
        }
        Page<Welfare> page = baseMapper.selectPage(new Page<>(model.getCurrent(), model.getLimit()), wrapper);

        WelfareResponse response = new WelfareResponse();
        List<Welfare> welfareList = page.getRecords();
        welfareList.forEach(welfare -> {
            WelfareCategory welfareCategory = welfareCategoryService.getById(welfare.getCategoryId());
            welfare.setCategoryName(welfareCategory.getCategoryName());
        });
        response.setWelfareList(welfareList);
        response.setPages(page.getPages());
        response.setTotal(page.getTotal());
        return response;
    }

}
