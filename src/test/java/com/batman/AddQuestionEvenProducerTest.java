package com.batman;

import com.batman.async.EventModel;
import com.batman.async.EventType;
import com.batman.mq.producer.AddQuestionEvenProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
@WebAppConfiguration
public class AddQuestionEvenProducerTest {

    @Autowired
    private AddQuestionEvenProducer addQuestionEvenProducer;

    @Test
    public void Test() {
        EventModel eventModel = new EventModel();
        eventModel.setResendCount(11);
        eventModel.setActorId(40);
        eventModel.setEntityId(1433434);
        eventModel.setType(EventType.ADD_QUESTION);
        eventModel.setResendCount(2);
        eventModel.setEntityType(33);
        eventModel.setEntityOwnerId(41);
        Map<String, String> hash = new HashMap<>();
        hash.put("参数1", "值1");
        hash.put("参数2", "值2");
        eventModel.setExts(hash);
        addQuestionEvenProducer.sendEvent(eventModel);
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void Method1() throws InterruptedException {
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
