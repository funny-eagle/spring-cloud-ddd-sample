package org.nocoder.commons;

import java.util.HashMap;
import java.util.Map;

public class BaseResponse<T> {

    /**
     * 200 成功， 其他失败
     */
    private int status = 200;

    private T data;

    public BaseResponse() {

    }

    public BaseResponse(T data) {
        this.data = data;
    }

    public BaseResponse(String key, T data) {
        Map<String, T> result = new HashMap();
        result.put(key, data);
        this.data = (T) result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
