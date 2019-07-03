package com.yequan.common.jms.service;

import javax.jms.Destination;

/**
 * @Auther: yq
 * @Date: 2019/7/3 16:31
 * @Description:
 */
public interface JmsProducerService {

    void sendMessage(Destination destination,String message);

}
