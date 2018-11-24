package com.batman.annotation;

import com.batman.async.EventType;

import java.lang.annotation.*;

/**
 * @author manchaoyang
 * 2018/11/24
 * 用于对生产者关心的类型进行注解
 * 在添加新的MQ生产者的时候利用Spring的性质可以自动注入
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EvenTypeAnnotation {

    public EventType[] values();
}
