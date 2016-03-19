package com.example;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 资源管理器
 * Created by zdr on 16-3-13.
 */
public class ResManager {
    private static List<Res> Resource = new ArrayList<>();

    public static boolean isFree(int id) {
        Iterator<Res> it = Resource.iterator();
        while (it.hasNext()){
            Res r = it.next();
            if(r.getRState() == r.RESTATE_ZERO)
                return true;
        }
        return false;
    }

    /**
     * 将资源设置为占用状态
     * @param id
     */
    public static void getResById(int id) {
        Iterator<Res> it = Resource.iterator();
        while (it.hasNext()){
            Res r = it.next();
            if(r.getRID() == id)
                r.setRState(r.RESTATE_ZERO);
        }
    }

    /**
     * 将资源设置为自由状态
     * @param id
     */
    public static void releaseResById(int id) {
        Iterator<Res> it = Resource.iterator();
        while (it.hasNext()){
            Res r = it.next();
            if(r.getRID() == id)
                r.setRState(r.RESTATE_ONE);
        }
    }
    public void addRes(Res r){
        Resource.add(r);
    }

    public boolean removeRes(int id){
        Iterator<Res> it = Resource.iterator();
        while (it.hasNext()){
            Res r = it.next();
            if(r.getRID() == id) {
                Resource.remove(r);
                return true;
            }
        }
        return false;
    }
}
