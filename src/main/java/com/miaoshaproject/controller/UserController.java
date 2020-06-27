package com.miaoshaproject.controller;/*
 * @Author sds
 * @Description //TODO $
 * @Date $ $
 * @Param $
 * @return $
 */

import com.miaoshaproject.controller.viewobject.UserVO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import com.oracle.jrockit.jfr.InvalidEventDefinitionException;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.omg.CORBA.ObjectHelper;
import org.springframework.http.HttpStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;


@Controller("user")
@RequestMapping("/user")
//允许跨域传输的所有header参数，将用于使用token放入header域做session共享的跨域请求
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;


    //用户登入接口
    @RequestMapping(value = "/login",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "telphone")String telphone,
                                  @RequestParam(name = "password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //入参校验
        if(org.apache.commons.lang3.StringUtils.isEmpty(telphone)||
                StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //下面通过调用校验的方法，且无异常抛出的话，就成功登入
       UserModel userModel= userService.validateLogin(telphone,this.EncodeByMd5(password));

        //将登入的凭证加入到用户登入成功的session内
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);

        //方晖给前端一个正确的信息
        return CommonReturnType.create(null);

    }


    //用户注册接口
    @RequestMapping(value = "/register",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public  CommonReturnType register(@RequestParam(name = "telphone") String telphone,
                                      @RequestParam(name = "otpCode") String otpCode,
                                      @RequestParam(name = "name") String name,
                                      @RequestParam(name = "age") Integer age,
                                      @RequestParam(name = "gender") Integer gender,
                                      @RequestParam(name = "password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {

       //验证手机号与对应的optCode相符合
        String inSessionOptCode = (String) this.httpServletRequest.getSession().getAttribute(telphone);
        if(!com.alibaba.druid.util.StringUtils.equals(otpCode,inSessionOptCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码输入错误");
        }
        //用户的注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setAge(age);
        userModel.setTelphone(telphone);
        userModel.setGender(new Byte(String.valueOf(gender.intValue())));
        userModel.setRegisterMode("byphone");
        //userModel.setEncrptPassword(MD5Encoder.encode(password.getBytes()));
        userModel.setEncrptPassword(this.EncodeByMd5(password));
        userService.register(userModel);
        return CommonReturnType.create(null);

    }

    //由于jdk里面自带的MD5加密是默认对16位的子节数进行加密，因此要改写加密方式
    public String EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //去确定加密方式
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        //对密码字符串进行加密
        String newstr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }

    //用户获取otp短信接口
    @RequestMapping(value = "/getotp",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    //访问方式：http://localhost:8081/user/getotp?telphone=12345678910
    public CommonReturnType getOtp(@RequestParam(name = "telphone")String telphone){
        //需要按照一定的规则生成otp验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);//[0-99998]
        randomInt += 10000; //[10000,109998]
        String otpCode = String.valueOf(randomInt);

        //将otp验证码同时对应用户的手机号进行管联,使用httpsession的方式绑定,一般直接用redis进行存储
        httpServletRequest.getSession().setAttribute(telphone,otpCode);


        //将otp验证码通过短信的通道发送给用户
        System.out.println("telphone"+telphone+" &otpcode"+otpCode);

        return CommonReturnType.create(null);

    }





    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException {
        //为了方便前端获得数据的状态，采用CommenReturnType,来对应请求数据的状态和数据结果
        //调用service服务获取对应id的对象返回给前端
        UserModel userModel = userService.getUserById(id);

        //若获取的对应用户信息不存在
        if(userModel==null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        //将核心模型转化为可供UI使用的viewobject
        UserVO userVO =  convertFromModel(userModel);
        //返回通用对象
        return CommonReturnType.create(userVO);
    }
    /*
    public UserVO getUser(@RequestParam(name = "id") Integer id) {
        //调用service服务获取对应id的对象返回给前端
        UserModel userModel = userService.getUserById(id);
        //将核心模型转化为可供UI使用的viewobject
        return convertFromModel(userModel);
    }*/
    //访问方式：http://localhost:8081/user/get?id=1
   /* public UserModel getUser(@RequestParam(name = "id")Integer id){
        //调用service服务获取对应id的对象返回给前端
        UserModel userModel = userService.getUserById(id);
        return userModel;
    }*/
    private UserVO convertFromModel(UserModel userModel){
        if(userModel==null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel,userVO); //copy到uservo，从而隐藏部分信息
        return userVO;
    }


}