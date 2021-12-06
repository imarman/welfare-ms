package com.manage.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.manage.common.RoleConst;
import com.manage.model.Teacher;
import com.manage.model.comm.R;
import com.manage.model.req.TeacherReqModel;
import com.manage.service.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @date 2021/12/5 14:23
 */
@RestController
@RequestMapping("/teacher")
@Slf4j
public class TeacherController {

    @Resource
    TeacherService teacherService;

    @SaCheckRole(value = {RoleConst.ADMIN_ROLE, RoleConst.MANAGER_ROLE}, mode = SaMode.OR)
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

    @DeleteMapping("/delete/{id}")
    @SaCheckLogin
    public R delete(@PathVariable Long id) {
        log.info("delete 方法执行，参数：id:{}", id);
        boolean res = teacherService.removeById(id);
        if (res) {
            return R.ok();
        }
        return R.error("删除失败");
    }

    @PostMapping("save")
    @SaCheckLogin
    public R save(@RequestBody Teacher teacher) {
        log.info("save方法执行，参数：teacher:{}", teacher);
        if (teacher.getId() == null) {
            teacher.setGmtCreate(LocalDateTime.now());
        }
        teacher.setGmtModified(LocalDateTime.now());
        boolean save = teacherService.saveOrUpdate(teacher);
        if (save) {
            return R.ok();
        }
        return R.error("新增失败");
    }

}
