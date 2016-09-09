package com.example.sockettest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ThreadPool {
    private static ExecutorService service = Executors.newFixedThreadPool(1);
    public static void runThread(Runnable runnable) {
        
        if(runnable!=null)service.execute(runnable);
    }
}
