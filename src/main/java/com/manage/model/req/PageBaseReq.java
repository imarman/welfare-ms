package com.manage.model.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @date 2021/12/5 14:26
 */
@Data
public class PageBaseReq implements Serializable {

    private static final long serialVersionUID = 2L;

    private Integer current;

    private Integer limit;

}
