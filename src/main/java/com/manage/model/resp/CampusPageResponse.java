package com.manage.model.resp;

import com.manage.model.Campus;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @date 2021/12/5 14:31
 */
@Data
public class CampusPageResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Campus> campusList;

    /**
     * 总数据
     */
    private Long total;

    /**
     * 总页数
     */
    private Long pages;

}
