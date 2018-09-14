package com.ykly.service.controller;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.ykly.service.remote.HelloRemote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

    private final static String lHystrixToutMs_120 = "120000"; // Histrix 长熔断时间,数据交易可能需要比较长的时间
    private final static String lHystrixToutMs_60 = "60000"; // Histrix 长熔断时间,数据交易可能需要比较长的时间
    private final static String sHystrixToutMs_6 = "6000"; // Histrix 短熔断时间
    private final static String coreSize = "200";


    @Autowired
	private HelloRemote helloRemote;

    @RequestMapping("/hello/{name}")
    public String index(@PathVariable("name") String name) {
        return helloRemote.hello(name);
    }

    @RequestMapping("/test/{name}")
    @HystrixCommand(groupKey = "dataGroup", fallbackMethod = "findOrderFallback", commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = lHystrixToutMs_60),}, threadPoolProperties = {@HystrixProperty(name = "coreSize", value = coreSize),})
    public String doTest(@PathVariable(value = "name") String name){

        return name + "我是英雄";
    }

    public String findOrderFallback(String name){
        return name + "调用失败！";
    }
}