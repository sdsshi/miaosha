package com.miaoshaproject.service;/*
 * @Author sds
 * @Description //订单服务
 * @Date $ $
 * @Param $
 * @return $
 */

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.OrderModel;

public interface OrderService {
    //使用前端url传过来的秒杀活动id，然后下单接口内校验对应的id是否属于对应商品且活动已开始
    OrderModel createOrder(Integer userId, Integer itemId,Integer promoId,Integer amount) throws BusinessException;
}
