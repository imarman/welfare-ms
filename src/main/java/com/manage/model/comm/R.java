package com.manage.model.comm;

import com.manage.common.ResultCodeEnum;
import lombok.Data;

/**
 * @date 2021/12/4 12:55
 */
@Data
public class R {

    private Boolean success;
    private Integer code;
    private String message;
    private Object data;

    private R(Boolean success, Integer code, String message, Object data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private R(ResultCodeEnum resultCodeEnum) {
        this.success = resultCodeEnum.getSuccess();
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }

    public static R ok() {
        return new R(ResultCodeEnum.SUCCESS);
    }

    public static R ok(String message) {
        return R.ok(message, null);
    }

    public static R ok(String message, Object data) {
        return new R(ResultCodeEnum.SUCCESS.getSuccess(), ResultCodeEnum.SUCCESS.getCode(), message, data);
    }

    public static R ok(Object data) {
        return new R(ResultCodeEnum.SUCCESS.getSuccess(), ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMessage(), data);
    }

    public static R error() {
        return R.error(ResultCodeEnum.UNKNOWN_REASON);
    }

    public static R error(String message) {
        return R.errorMsg(ResultCodeEnum.UNKNOWN_REASON, message);
    }

    public static R error(ResultCodeEnum resultCodeEnum) {
        return new R(resultCodeEnum);
    }

    public static R errorMsg(ResultCodeEnum resultCodeEnum, String message) {
        R r = new R(resultCodeEnum);
        r.setMessage(message);
        return r;
    }

}
