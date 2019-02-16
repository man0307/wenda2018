package com.batman;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.concurrent.ArrayBlockingQueue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
@WebAppConfiguration
public class WendaApplicationTests {

	@Test
	public void contextLoads() {
		ArrayBlockingQueue<String> list = new ArrayBlockingQueue<String>(10);
		try{
		for(int i = 0; i < 114; i++){
			list.add("a");
		 }
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}
