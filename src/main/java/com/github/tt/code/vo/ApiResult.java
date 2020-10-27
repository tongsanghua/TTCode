package com.github.tt.code.vo;

import lombok.Data;

@Data
public class ApiResult {

    private Integer status;
    private String msg;
    private Object data;
    private Integer pageNum;
    private Integer pageSize;
    private Integer total;

    public static ApiResult build(Integer code, String msg, Object data, Integer pageNum, Integer pageSize, Integer total) {
        ApiResult r = new ApiResult();
        r.setStatus(code);
        r.setMsg(msg);
        r.setData(data);
        r.setPageNum(pageNum);
        r.setPageSize(pageSize);
        r.setTotal(total);
        return r;
    }

    public static ApiResult error(String msg) {
        return build(400, msg, null, null, null, null);

    }

    public static ApiResult error() {
        return error("失败");
    }

    public static ApiResult ok(String msg, Object data) {
        return build(200, msg, data, null, null, null);
    }

    public static ApiResult ok(Object data) {
        return ok("成功", data);
    }

    public static ApiResult ok(Object data, Integer pageNum, Integer pageSize, Integer total) {
        return build(200, "成功", data, pageNum, pageSize, total);
    }

    public static ApiResult ok() {
        return ok(null);
    }

}
