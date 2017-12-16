package com.hd.utils.git.common;

/**
 * ServerResponse返回码
 * @author jihang
 * @date 2017/12/12
 */

public enum ResponseCode {

    /**
     * SUCCESS返回成功消息
     */
    SUCCESS(0, "SUCCESS"),
    /**
     * ERROR返回失败消息
     */
    ERROR(1, "ERROR");

    private final Integer code;
    private final String desc;

    ResponseCode(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }
}