package com.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manage.model.Campus;
import com.manage.model.SysUser;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @date 2021/12/5 18:22
 */
public interface CampusMapper extends BaseMapper<Campus> {

    @Select("select id, username from sys_user where id not in (select DISTINCT manager from campus) and role = 'MANAGER'")
    List<SysUser> getAvoidManager();
}