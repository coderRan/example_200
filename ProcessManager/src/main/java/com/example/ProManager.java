package com.example;

import java.util.LinkedList;
import java.util.List;

/**
 * 进程调度类，模拟CPU控制进程之间的调度
 * Created by zdr on 16-3-13.
 */
public class ProManager {
    private List<Process> waitQueue = new LinkedList<>();
    private List<Process> blockQueue = new LinkedList<>();
    private static int ProcessCount = 0;
    private Thread runThread = null;
    private Process runProcess = null;

    /**
     * 创建进程
     *
     * @param p
     */
    public void createPro(Process p) {
        ProcessCount++;
        p.setPID(ProcessCount);
        if (runThread != null) {
            p.setPState(p.PState_W);
            waitQueue.add(p);

        } else {
            if(p.getResource()){
                runProcess = p;
                runThread = new Thread(p);
                runThread.start();
            }
        }

    }
    public void swapPro(){

    }

}
