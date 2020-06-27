package com.miaoshaproject.service;/*
 * @Author sds
 * @Description //TODO $
 * @Date $ $
 * @Param $
 * @return $
 */

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.UserModel;

public interface UserService {
    //通过用户id获取对象方法
    UserModel getUserById(Integer id);

    void register(UserModel userModel) throws BusinessException;
    //telphone是用户注册的手机，password是用户加密后的密码
    UserModel validateLogin(String telphone,String encrptPassword) throws BusinessException;
}
