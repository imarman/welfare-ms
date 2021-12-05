package com.manage.model.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @date 2021/12/5 14:27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TeacherReqModel extends PageBaseReq implements Serializable {

    private static final long serialVersionUID = 2L;

    /**
     * 姓名
     */
    private String name;

    /**
     * 校区
     */
    private String campus;

    /**
     * 等级
     */
    private String level;

}
