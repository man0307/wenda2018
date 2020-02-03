package com.batman.mq.consumer;

import com.batman.async.EventModel;
import com.batman.async.EventType;
import com.batman.config.RabbitMQConfig;
import com.batman.model.HostHolder;
import com.batman.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author yehuo
 */
@Component
@Slf4j
public class AddQuestionEventConsumer implements EventConsumerInfer {


    @Autowired
    SeacherService seacherService;


    @RabbitListener(queues = RabbitMQConfig.QUEUE_ADD_QUESTION, containerFactory = "rabbitListenerContainerFactory")
    @Override
    public void process(EventModel eventModel) {
        try {
            seacherService.indexQuestion(eventModel.getEntityId()
                    , eventModel.getValue("title"), eventModel.getValue("content"));
        } catch (SolrServerException e) {
            log.error("SolrServerException" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

}
