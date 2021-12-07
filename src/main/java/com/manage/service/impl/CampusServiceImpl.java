package com.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manage.mapper.CampusMapper;
import com.manage.model.Campus;
import com.manage.model.SysUser;
import com.manage.service.CampusService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
/**
    @date 2021/12/5 18:22
*/
@Service
public class CampusServiceImpl extends ServiceImpl<CampusMapper, Campus> implements CampusService{

    @Resource
    CampusMapper campusMapper;

    @Override
    public List<SysUser> getAvoidManager() {
        return campusMapper.getAvoidManager();
    }
}
