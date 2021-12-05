package com.manage.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manage.common.BusinessException;
import com.manage.common.ResultCodeEnum;
import com.manage.mapper.TeacherMapper;
import com.manage.model.Teacher;
import com.manage.model.req.TeacherReqModel;
import com.manage.model.resp.TeacherResponse;
import com.manage.service.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
    @date 2021/12/5 14:08
*/
@Service
@Slf4j
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService{

    @Override
    public TeacherResponse selectTeachersByWrapper(TeacherReqModel model) {
        if (model.getLimit() == null || model.getCurrent() == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR);
        }
        TeacherMapper baseMapper = getBaseMapper();
        LambdaQueryWrapper<Teacher> wrapper = new LambdaQueryWrapper<>();
        if (model.getName() != null && !StrUtil.isBlankIfStr(model.getName())) {
            wrapper.like(Teacher::getName, model.getName());
        }
        if (model.getCampus() != null && !StrUtil.isBlankIfStr(model.getCampus())) {
            wrapper.eq(Teacher::getCampus, model.getCampus());
        }
        if (model.getLevel() != null && !StrUtil.isBlankIfStr(model.getLevel())) {
            wrapper.eq(Teacher::getLevel, model.getLevel());
        }
        Page<Teacher> page = baseMapper.selectPage(new Page<>(model.getCurrent(), model.getLimit()), wrapper);
        TeacherResponse response = new TeacherResponse();
        response.setTeacherList(page.getRecords());
        response.setPages(page.getPages());
        response.setTotal(page.getTotal());
        return response;
    }
}
