package com.manage.controller;

import com.manage.model.Welfare;
import com.manage.model.WelfareCategory;
import com.manage.model.comm.R;
import com.manage.model.req.TeacherReqModel;
import com.manage.model.req.WelfareReqModel;
import com.manage.service.WelfareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @date 2021/12/11 13:05
 */
@RestController
@RequestMapping("welfare")
@Slf4j
public class WelfareController {

    @Resource
    WelfareService welfareService;

    @GetMapping("/all/{current}/{limit}")
    public R list(@PathVariable Integer current, @PathVariable Integer limit ,
                  @RequestParam(required = false) String name,
                  @RequestParam(required = false) String categoryId) {
        log.info("添加福利方法执行，参数：current:{}, limit:{}, name:{}, categoryId:{}", current, limit, name, categoryId);

        WelfareReqModel reqModel = new WelfareReqModel();
        reqModel.setCurrent(current);
        reqModel.setLimit(limit);
        reqModel.setName(name);
        reqModel.setCategoryId(categoryId);
        return R.ok(welfareService.selectWelfareByWrapper(reqModel));
    }

    @PostMapping("/save")
    public R save(@RequestBody Welfare welfare) {
        if (welfare.getId() == null) {
            welfare.setGmtCreate(new Date());
        }
        welfare.setGmtModified(new Date());
        boolean res = welfareService.saveOrUpdate(welfare);
        return res ? R.ok() : R.error();
    }

    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable String id) {
        boolean res = welfareService.removeById(id);
        return res ? R.ok() : R.error();
    }

}
