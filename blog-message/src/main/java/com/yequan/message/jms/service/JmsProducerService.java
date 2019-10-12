package com.yequan.message.jms.service;

import javax.jms.Destination;
import java.util.Map;

/**
 * @Auther: yq
 * @Date: 2019/7/3 16:31
 * @Description:
 */
public interface JmsProducerService {

    void sendMessage(Destination destination,String message);

    void sendMessage(Destination destination,Object objMessage);

    void sendMessage(Destination destination, Map<?,?> mapMessage);
}
