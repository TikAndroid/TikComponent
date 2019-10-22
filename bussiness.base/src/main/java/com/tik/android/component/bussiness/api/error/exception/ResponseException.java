package com.tik.android.component.bussiness.api.error.exception;

/**
 * @describe :业务异常统一exception
 * @usage :
 * <p>
 * </p>
 * Created by baowei on 2018/11/16.
 */
public class ResponseException extends Exception {

    private int code;

    public ResponseException(String msg, int code) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
