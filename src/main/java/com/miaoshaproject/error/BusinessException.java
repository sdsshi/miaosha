package com.miaoshaproject.error;/*
 * @Author sds
 * @Description //通过抛出的异常来给定特定的错误码
 * @Date $ $
 * @Param $
 * @return $
 */
//包装器业务异常实现
public class BusinessException extends Exception implements CommonError {

    private  CommonError commonError;

    //直接接受EmBusiness的传参用于构造业务的异常
    public BusinessException(CommonError commonError){
        super();
        this.commonError = commonError;
    }

    //接受自定义的errMsg的方式来构造业务异常
    public BusinessException(CommonError commonError,String errMsg){
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg);
    }



    @Override
    public int getErrorCode() {
        return this.commonError.getErrorCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}
