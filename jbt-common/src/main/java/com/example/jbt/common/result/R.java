package com.example.jbt.common.result;

import com.example.jbt.common.enums.HttpStatusEnums;
import com.example.jbt.common.utils.TraceIdUtils;

public class R<T> {

    private int code;

    private String msg;

    private String traceId;

    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTraceId() {
        return TraceIdUtils.getTraceId();
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     *
     * @param code 状态码
     * @param msg  返回内容
     */
    public R(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
    }

    /**
     *
     * @param code 状态码
     * @param msg  返回内容
     * @param data 数据对象
     */
    @SuppressWarnings("unchecked")
    public R(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = (T) data;
    }

    /**
     * 返回对应状态数据
     *
     * @return 成功消息
     */
    public static <T> R<T> status(boolean flag) {
        return flag ? R.success() : R.error();
    }


    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static <T> R<T> success() {
        return R.success(HttpStatusEnums.SUCCESS.getDesc());
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static <T> R<T> success(Object data) {
        return R.success(HttpStatusEnums.SUCCESS.getDesc(), data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static <T> R<T> success(String msg) {
        return R.success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static <T> R<T> success(String msg, Object data) {
        return new R<>(HttpStatusEnums.SUCCESS.getCode(), msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return
     */
    public static <T> R<T> error() {
        return R.error(HttpStatusEnums.ERROR.getDesc());
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static <T> R<T> error(String msg) {
        return R.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static <T> R<T> error(String msg, Object data) {
        return new R<>(HttpStatusEnums.ERROR.getCode(), msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static <T> R<T> error(int code, String msg, Object data) {
        return new R<>(code, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  返回内容
     * @return 警告消息
     */
    public static <T> R<T> error(int code, String msg) {
        return new R<>(code, msg, null);
    }

    /**
    * 返回错误消息
    *
    * @param httpStatusEnum 状态码枚举对象
    * @return 警告消息
    **/
    public static <T> R<T> error(HttpStatusEnums httpStatusEnum){
        return new R<>(httpStatusEnum.getCode(), httpStatusEnum.getDesc());
    }
}