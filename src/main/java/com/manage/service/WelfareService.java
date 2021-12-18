package com.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manage.model.Welfare;
import com.manage.model.req.WelfareReqModel;
import com.manage.model.resp.WelfareResponse;

/**
 * @date 2021/12/11 13:01
 */
public interface WelfareService extends IService<Welfare> {


    WelfareResponse selectWelfareByWrapper(WelfareReqModel reqModel);
}
