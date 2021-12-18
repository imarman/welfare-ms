package com.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manage.model.Campus;
import com.manage.model.SysUser;

import java.util.List;

/**
 * @date 2021/12/5 18:22
 */
public interface CampusService extends IService<Campus> {


    /**
     * 获取所有空闲的负责人
     */
    List<SysUser> getAvoidManager();
}
