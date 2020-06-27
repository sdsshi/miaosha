package com.miaoshaproject.error;/*
 * @Author sds
 * @Description //为解决前端相应错误时的错误定位
 * @Date $ $
 * @Param $
 * @return $
 */

public interface CommonError {
    public int getErrorCode();
    public String getErrMsg();
    public CommonError setErrMsg(String errMsg);
}
