package com.example;

import com.example.service.PushService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class MyProjectBackendApplicationTests {

    @Autowired
    private PushService pushService;

    @Test
    public void testOldPush(){
        pushService.oldPush();

    }

    @Test
    public void testNewPush(){
        pushService.pushNew();
        try {
            TimeUnit.SECONDS.sleep(20);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testNewPushCall(){
        pushService.pushNewCall();
        try {
            TimeUnit.SECONDS.sleep(20);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void test(){
        char c = '阿';
    }

}
