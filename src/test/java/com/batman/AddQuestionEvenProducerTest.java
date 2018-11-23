package com.batman;

import com.batman.async.EventModel;
import com.batman.mq.producer.AddQuestionEvenProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
@WebAppConfiguration
public class AddQuestionEvenProducerTest {

    @Autowired
    private AddQuestionEvenProducer addQuestionEvenProducer;

   @Test
    public void Test(){
       for(int i = 0;i < 20;i++){
          new Runnable(){
              @Override
              public void run() {
                  for(int i = 0;i<100;i++){
                      addQuestionEvenProducer.addEventCache(UUID.randomUUID().toString(),new EventModel());
                  }
              }
          }.run();
       }
   }

}
