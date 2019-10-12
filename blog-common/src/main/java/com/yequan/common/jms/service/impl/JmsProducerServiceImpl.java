package com.yequan.common.jms.service.impl;

import com.yequan.common.jms.service.JmsProducerService;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;
import java.io.Serializable;
import java.util.Map;

/**
 * @Auther: yq
 * @Date: 2019/7/3 16:33
 * @Description:
 */
@Service
public class JmsProducerServiceImpl implements JmsProducerService {

    @Resource
    private JmsTemplate jmsTemplate;

    public void sendMessage(Destination destination, final String message) {
        jmsTemplate.send(destination, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(message);
            }
        });
    }

    public void sendMessage(Destination destination, final Object objMessage) {
        jmsTemplate.send(destination, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createObjectMessage((Serializable) objMessage);
            }
        });
    }

    public void sendMessage(Destination destination, Map<?, ?> mapMessage) {
        jmsTemplate.send(destination, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createMapMessage();
            }
        });
    }
}
