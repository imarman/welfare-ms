package com.manage.model.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @date 2021/12/7 23:22
 */
@Data
public class CampusReqModel implements Serializable {

    private static final long serialVersionUID = 2L;

    private Integer current;

    private Integer limit;

    private String name;

    private String manager;

    private String timeRange;
}
