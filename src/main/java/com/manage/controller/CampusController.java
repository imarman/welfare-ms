package com.manage.controller;

import com.manage.model.comm.R;
import com.manage.service.CampusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
    public R getAll() {
        return R.ok(campusService.list());
    }

}
