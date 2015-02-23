package com.rest.service.util;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rest.service.mongodb.UserRepository;

public class DataUtil {
    
    public static UserRepository getUserRepository() {
        ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");    
        return (UserRepository) context.getBean("userRepository");
    }
}
