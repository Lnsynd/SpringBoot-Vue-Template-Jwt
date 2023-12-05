package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by 刘千山 on 2023/9/5/005-20:22
 */
@Service
public class PushService {


    public void oldPush() {
        int dataNum = 10000;
        int[] arr = new int[dataNum];
        for (int i = 0; i < dataNum; i++) {
            arr[i] = i;
        }
        long start = System.currentTimeMillis();
        for (int i = 0; i < arr.length; i++) {
            // push
            pushSend(arr[i]);
        }
        long end = System.currentTimeMillis();
        System.out.println("共计:" + (end - start) + "ms");
    }


    public void pushNew() {
        int dataNum = 10000;
        int[] arr = new int[dataNum];
        for (int i = 0; i < dataNum; i++) {
            arr[i] = i;
        }
        long start = System.currentTimeMillis();
        // 拆分
        for (int i = 0; i < 10; i++) {
            int s = i * 1000;
            int e = i * 1000 + 1000 - 1;
            threadPush(arr, s, e);
        }
        long end = System.currentTimeMillis();
        System.out.println("共计:" + (end - start) + "ms");
    }

    @Autowired
    private ThreadPoolTaskExecutor asyncServiceExecutor;

    public void pushNewCall() {
        int dataNum = 10000;
        int[] arr = new int[dataNum];
        for (int i = 0; i < dataNum; i++) {
            arr[i] = i;
        }
        long start = System.currentTimeMillis();

        List<CompletableFuture<Integer>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int s = i * 1000;
            int e = i * 1000 + 1000 - 1;

            CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(()->{
                return threadPushNew(arr, s, e);
            }).thenApply(res->{
                System.out.println("本轮线程执行数量" + res);
                return res;
            });
//            Future<Integer> submit = asyncServiceExecutor.submit(new Callable<Integer>() {
//                @Override
//                public Integer call() throws Exception {
//                    return threadPushNew(arr, s, e);
//                }
//            });
//            list.add(submit);
        }

//        for (Future future : list) {
//            try {
//                System.out.println("本轮线程执行数量" + future.get());
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//        }
        long end = System.currentTimeMillis();
        System.out.println("共计:" + (end - start) + "ms");
    }


    @Async("asyncServiceExecutor")
    protected void threadPush(int[] arr, int s, int e) {
        long start = System.currentTimeMillis();
        for (int i = s; i < e; i++) {
            pushSend(arr[i]);
        }
        long end = System.currentTimeMillis();
        System.out.println("共计:" + (end - start) + "ms");
    }

    private int threadPushNew(int[] arr, int s, int e) {
        long start = System.currentTimeMillis();
        int count = 0;
        for (int i = s; i < e; i++) {
            pushSend(arr[i]);
            count++;
        }
        long end = System.currentTimeMillis();
        System.out.println("共计:" + (end - start) + "ms");
        return count;
    }

    private void pushSend(int i) {
        try {
            // 推送耗时1ms
            TimeUnit.MILLISECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
