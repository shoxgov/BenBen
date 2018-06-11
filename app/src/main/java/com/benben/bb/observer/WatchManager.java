package com.benben.bb.observer;

import java.util.Observable;

/**
 * Created by wangshengyin on 2017-03-08.
 * email:shoxgov@126.com
 */

public class WatchManager extends Observable {
    private static WatchManager instance = null;

    public static WatchManager getObserver() {
        if (null == instance) {
            instance = new WatchManager();
        }
        return instance;
    }

    public void setMessage(Object data) {
        //被观察者怎么通知观察者数据有改变了呢？？这里的两个方法是关键。
        setChanged();
        notifyObservers(data);
    }
}
