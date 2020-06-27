package com.miaoshaproject.service;/*
 * @Author sds
 * @Description //秒杀场景匹配
 * @Date $ $
 * @Param $
 * @return $
 */

import com.miaoshaproject.service.model.PromoModel;

public interface PromoService {
    PromoModel getPromoByItemId(Integer itemId);
}
