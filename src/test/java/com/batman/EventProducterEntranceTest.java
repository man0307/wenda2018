package com.batman;


import com.batman.async.EventModel;
import com.batman.async.EventType;
import com.batman.mq.producer.EventProducerEntrance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
@WebAppConfiguration
public class EventProducterEntranceTest {
    @Autowired
    private EventProducerEntrance eventProducerEntrance;

    @Test
    public void Method1(){
        EventModel eventModel = new EventModel();
        eventModel.setResendCount(11);
        eventModel.setActorId(1212);
        eventModel.setEntityId(1433434);
        eventModel.setType(EventType.ADD_QUESTION);
        eventModel.setResendCount(2);
        Map<String, String> hash = new HashMap<>();
        hash.put("参数1", "值1");
        hash.put("参数2", "值2");
        eventModel.setExts(hash);
        eventProducerEntrance.fireEvent(eventModel);
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
