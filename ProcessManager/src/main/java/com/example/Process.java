package com.example;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 进程类： 进程描述类
 * 进程属性：
 * int PID： 进程ID
 * int PPID :进程的父进程ID
 * string PName： 进程名字
 * List<Res> RecList：进程资源数组
 * int PState：进程状态，就绪状态W、运行状态R、等待或阻塞状态B
 * int PTTime ：进程需要执行的总时间
 * int PRTime ： 进程执行的时间
 */
public class Process implements Runnable {
    public static final int PState_W = 0x01;
    public static final int PState_R = 0x02;
    public static final int PState_B = 0x03;

    private int PID = 0;
    private int PPID = -1;
    private String PName = "name_"+PID;
    private List<Res> PResource = new ArrayList<>();
    private long PTTime = 0;
    private long PRTime = 0;
    private int PState = PState_W;
    private List<Process> PChild = new ArrayList<>();
    /* run方法的测试数据，每个进程将计算从 0 到 RunTestNumber 的和保存到RunTestAns中，
    *  RunTestStopTime是run方法每次计算的等待时间，
    * */
    public int RunTestNumber = 0;
    public int RunTestAns = 0;
    public int RunTestStopTime = 0;

    @Override
    public void run() {
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < RunTestNumber; i++) {
            try {
                Thread.sleep(RunTestStopTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            RunTestAns += i;
        }
        long t2 = System.currentTimeMillis();
        long t = t2 - t1;
        setPRTime(getPRTime() + t);

        if (PRTime>PTTime)
            setPState(PState_B);

    }


    public void setPTTime(long PTTime) {
        this.PTTime = PTTime;
    }

    public void setPID(int PID) {
        this.PID = PID;
    }

    public void setPRTime(long PRTime) {
        this.PRTime = PRTime;
    }

    public void setPState(int PState) {
        this.PState = PState;
    }

    public void setPResource(List<Res> PResource) {
        this.PResource = PResource;
    }


    public void setPPID(int PPID) {
        this.PPID = PPID;
    }

    public void setRunTestStopTime(int runTestStopTime) {
        RunTestStopTime = runTestStopTime;
    }

    public void setRunTestNumber(int runTestNumber) {
        RunTestNumber = runTestNumber;
    }


    public int getPID() {
        return PID;
    }

    public int getPPID() {
        return PPID;
    }

    public String getPName() {
        return PName;
    }

    public long getPTTime() {
        return PTTime;
    }

    public long getPRTime() {
        return PRTime;
    }

    public int getPState() {
        return PState;
    }


    public List<Process> getPChild() {
        return PChild;
    }

    /**
     * 创建子进程
     *
     * @param pro 子进程
     */
    public void creatChildProcess(Process pro) {
        pro.setPPID(this.PID);
        PChild.add(pro);
    }

    /**
     * 结束子进程
     *
     * @param PID 要结束的子进程ID
     */
    public void removeChildProcess(int PID) {
        Iterator<Process> iterator = PChild.iterator();
        while (iterator.hasNext()) {
            Process pro = iterator.next();
            if (pro.getPID() == PID)
                PChild.remove(pro);
        }
    }

    /**
     * 一次性获得所有资源
     *
     * @return
     */
    public boolean getResource() {
        Iterator<Res> ite = PResource.iterator();
        while (ite.hasNext()) {
            if (!ResManager.isFree(ite.next().getRID()))
                return false;
        }

        for (int i = 0; i < PResource.size(); i++) {
            int id = PResource.get(i).getRID();
            ResManager.getResById(id);
        }
        return true;
    }

    /**
     * 释放资源
     */
    public void releaseRes() {
        for (int i = 0; i < PResource.size(); i++) {
            int id = PResource.get(i).getRID();
            ResManager.releaseResById(id);
        }
    }

    @Override
    public String toString() {
        return "Process{" +
                "PID=" + PID +
                ", PName='" + PName + '\'' +
                ", PTTime=" + PTTime +
                ", PRTime=" + PRTime +
                ", PState=" + PState +
                ", RunTestAns=" + RunTestAns +
                '}';
    }
}
