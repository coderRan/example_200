package com.example;

import com.sun.corba.se.spi.orb.Operation;

/**
 * 资源类
 * int RID：资源ID
 * int RState：资源状态,1 表示可用，0 表示不可用
 * Created by zdr on 16-3-13.
 */
public class Res {
    public static final int RESTATE_ONE = 1;
    public static final int RESTATE_ZERO = 0;
    private int RID = 0;
    private int RState = RESTATE_ZERO;

    public Res(int RID) {
        this.RID = RID;
    }

    public int getRState() {
        return RState;
    }

    public int getRID() {
        return RID;
    }

    public void setRState(int RState) {
        this.RState = RState;
    }
}