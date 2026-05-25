package org.example.neuobject.nursinghouse.common;

import java.io.Serializable;

/**
 * <p></p>
 *
 * @author li253
 * @version 1.0
 * @date 2026/5/24 09:40
 */
public class Result implements Serializable {
    private static final long serialVersionUID = 1L;
    //状态码
    private int code;
    //提示信息message
    private String msg;
    //返回的属性类型
    private Object data;

    /**
     * 直接返回成功结果
     * @param data
     * @return
     */
    public static Result success(Object data){
        return success(200, "操作成功", data);
    }

    /**
     * 自定义返回成功结果
     * @param code
     * @param msg
     * @param data
     * @return
     */

    public static Result success(int code, String msg, Object data){
        Result r= new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    /**
     * 不需结果直接返回成功
     * @return
     */
    public static Result success(){
        Result r = new Result();
        r.setCode(200);
        r.setMsg("操作成功");
        return r;
    }

    /**
     * 直接返回失败信息
     * @return
     */
    public static Result error(){
        return error(400, "操作失败",null);
    }

    /**\
     * 带参数返回失败信息
     * @param msg
     * @return
     */
    public static Result error(String msg){
        return error(400, msg,null);
    }

    /**
     * 自定义返回失败信息
     * @param code
     * @param msg
     * @param data
     * @return
     */
    public static Result error(int code, String msg, Object data){
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    public static Result error(int code, String msg){
        return error(code, msg, null);
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
