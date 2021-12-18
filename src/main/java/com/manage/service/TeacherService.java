package com.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manage.model.Teacher;
import com.manage.model.req.TeacherReqModel;
import com.manage.model.resp.TeacherResponse;

/**
 * @date 2021/12/5 14:08
 */
public interface TeacherService extends IService<Teacher> {

    /**
     * 根据条件查询教师列表分页数据
     *
     * @param model 跳进啊
     * @return 老师列表的 page 对象
     */
    TeacherResponse selectTeachersByWrapper(TeacherReqModel model);


}