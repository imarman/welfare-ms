package com.manage.controller;

import com.manage.model.comm.R;
import com.manage.model.req.TeacherReqModel;
import com.manage.service.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @date 2021/12/5 14:23
 */
@RestController
@RequestMapping("/teacher")
@Slf4j
public class TeacherController {

    @Resource
    TeacherService teacherService;

    @GetMapping("/all/{current}/{limit}")
    public R list(@PathVariable Integer current, @PathVariable Integer limit ,
                  @RequestParam(required = false) String name,
                  @RequestParam(required = false) String campus,
                  @RequestParam(required = false) String level) {
        log.info("list方法执行，参数：current:{}, limit:{}, name:{}, campus:{}, level:{}", current, limit, name, campus, level);
        TeacherReqModel reqModel = new TeacherReqModel();
        reqModel.setCurrent(current);
        reqModel.setLimit(limit);
        reqModel.setCampus(campus);
        reqModel.setLevel(level);
        reqModel.setName(name);
        log.info("list 方法执行，参数：reqModel:{}", reqModel);
        return R.ok(teacherService.selectTeachersByWrapper(reqModel));
    }

}
