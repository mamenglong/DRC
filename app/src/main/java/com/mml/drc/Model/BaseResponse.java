package com.mml.drc.Model;

/**
 * Created by 11324 on 2019/2/17
 */
public class BaseResponse {
    private int status;
    private String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
