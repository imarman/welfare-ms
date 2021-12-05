package com.manage.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manage.model.Campus;
import com.manage.mapper.CampusMapper;
import com.manage.service.CampusService;
/**
    @date 2021/12/5 18:22
*/
@Service
public class CampusServiceImpl extends ServiceImpl<CampusMapper, Campus> implements CampusService{

}
