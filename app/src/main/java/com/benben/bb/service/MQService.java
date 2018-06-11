package com.benben.bb.service;

import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.mobileim.IYWPushListener;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.benben.bb.MyApplication;
import com.benben.bb.observer.ObMsgConstance;
import com.benben.bb.observer.ObserverBean;
import com.benben.bb.observer.WatchManager;
import com.benben.bb.utils.LogUtil;

/**
 * Created by wangshengyin on 2017-05-05.
 * email:shoxgov@126.com
 */

public class MQService extends Service {

    public final static int START_MQ_RECEIVE = 101;
    public final static int STOP_MQ_RECEIVE = 102;
    public final static int START_YUNIM = 103;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        System.out.println("BENBEN MQService onCreate");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        System.out.println("ZCP MQService onDestroy");
        super.onDestroy();
    }

    private void initYMIM(String openIm) {
        LogUtil.d("initYMIM  openIm=" + openIm);
        if (TextUtils.isEmpty(openIm)) {
            return;
        }
        YWIMKit mIMKit = YWAPI.getIMKitInstance(openIm, MyApplication.YW_APP_KEY);

        IYWConversationService conversationService = mIMKit.getConversationService();
        //如果之前add过，请清除
        conversationService.removePushListener(msgPushListener);
        //增加新消息到达的通知
        conversationService.addPushListener(msgPushListener);
        YWLoginParam loginParam = YWLoginParam.createLoginParam(openIm, MyApplication.YW_APP_COMM_PWD);
        mIMKit.getLoginService().login(loginParam, new IWxCallback() {

            @Override
            public void onSuccess(Object... obj) {
                LogUtil.d("MQService login onSuccess:"+obj.toString());
            }

            @Override
            public void onProgress(int arg0) {
            }

            @Override
            public void onError(int errCode, String description) {
                //如果登录失败，errCode为错误码,description是错误的具体描述信息
                LogUtil.d("MQService login onError:"+description);
            }
        });
    }
    IYWPushListener msgPushListener = new IYWPushListener() {
        @Override
        public void onPushMessage(IYWContact iywContact, YWMessage ywMessage) {
            LogUtil.d("initYMIM onPushMessage IYWContact=" + iywContact.getUserId());
            ObserverBean ob = new ObserverBean();
            ob.setWhat(ObMsgConstance.ALIYUN_IM_MESSAGE_RECEIVER);
            WatchManager.getObserver().setMessage(ob);
            //playNotificationAudio();
        }

        @Override
        public void onPushMessage(YWTribe ywTribe, YWMessage ywMessage) {
            //当有新消息到达时，会触发此方法的回调
            //第一个参数是新消息发送者信息
            //第二个参数是消息
            //用户在这里可以做自己的消息提醒
            LogUtil.d("onPushMessage  YWTribe=" + ywMessage.getContent());
//                adapter.notifyDataSetChanged();
        }
    };
    /**
     * 收到消息，播放声音
     */
    private static long ringTime = 0;

    private void playNotificationAudio() {
        if (System.currentTimeMillis() - ringTime < 2000) {
            return;
        }
        ringTime = System.currentTimeMillis();
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        System.out.println("BENBEN MQService onStart");
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("BENBEN MQService onStartCommand");
        if (intent != null && intent.hasExtra("command")) {
            int command = intent.getIntExtra("command", -1);
            switch (command) {
                case START_MQ_RECEIVE:
                    break;

                case START_YUNIM:
                    initYMIM(intent.getStringExtra("openIm"));
                    break;

                case STOP_MQ_RECEIVE:
                    Log.d("BENBEN", "STOP_MQ_RECEIVE RabbitMQR stopSelf");
                    try {
                        YWIMKit mIMKit = YWAPI.getIMKitInstance(intent.getStringExtra("openIm"), MyApplication.YW_APP_KEY);
                        IYWConversationService conversationService = mIMKit.getConversationService();
                        //如果之前add过，请清除
                        conversationService.removePushListener(msgPushListener);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    stopSelf();//处理好intent后再停止。
                    break;
            }
        }
        return START_NOT_STICKY;
    }

}
