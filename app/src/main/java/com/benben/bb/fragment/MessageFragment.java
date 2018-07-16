package com.benben.bb.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWMessage;
import com.benben.bb.MyApplication;
import com.benben.bb.R;
import com.benben.bb.activity.MessageDetailActivity;
import com.benben.bb.activity.MySignupActivity;
import com.benben.bb.adapter.CustomBaseQuickAdapter;
import com.benben.bb.base.BaseFragment;
import com.benben.bb.bean.UserData;
import com.benben.bb.observer.ObMsgConstance;
import com.benben.bb.observer.ObserverBean;
import com.benben.bb.observer.WatchManager;
import com.benben.bb.utils.DateUtils;
import com.benben.bb.utils.LogUtil;
import com.benben.bb.utils.ToastUtil;
import com.benben.bb.utils.Utils;
import com.benben.bb.view.RecyclerViewSwipeLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import butterknife.Bind;

/**
 * Created by wangshengyin on 2018-05-03.
 * email:shoxgov@126.com
 */

public class MessageFragment extends BaseFragment implements Observer {
    private static final String ARG = "arg";
    private LinearLayout benbenMsgLayout;
    private TextView benbenMsg, benbenMsgDate, benbenMsgContent;

    @Bind(R.id.recyclerRefreshLayout)
    RecyclerViewSwipeLayout recyclerSwipeLayout;
    /**
     * 请求的页数，从第1页开始
     * 每一页请求数固定10
     */
    private int pageNo = 1;
    private int totalPage = -1;
    private int pageSize = 10;
    private YWIMKit mIMKit;
    private YWConversation ywMsgConversation;

    public MessageFragment() {
        LogUtil.d("MessageFragment non-parameter constructor");
    }

    public static MessageFragment newInstance(String arg) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG, arg);
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int provieResourceID() {
        return R.layout.fragment_msg;
    }

    @Override
    protected void init() {
        WatchManager.getObserver().addObserver(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        WatchManager.getObserver().deleteObserver(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View signupView = LayoutInflater.from(getActivity()).inflate(R.layout.msg_content_item, null, false);
        TextView signupMsgTitle = (TextView) signupView.findViewById(R.id.msg_title);
        TextView signupMsgContent = (TextView) signupView.findViewById(R.id.msg_content);
        signupView.findViewById(R.id.msg_count_layout).setVisibility(View.GONE);
        signupView.findViewById(R.id.msg_date).setVisibility(View.INVISIBLE);
        signupMsgTitle.setText("我的报名");
        signupMsgContent.setText("我自已的报名及就业顾问推荐的职位");
        signupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mysignup = new Intent();
                mysignup.setClass(getActivity(), MySignupActivity.class);
                startActivity(mysignup);
            }
        });
        recyclerSwipeLayout.createAdapter(R.layout.msg_content_item);
        recyclerSwipeLayout.addHeaderView(signupView);
        LinearLayout spanLine = new LinearLayout(getActivity());//LinearLayout.LayoutParams.MATCH_PARENT, Utils.dip2px(getActivity(),10));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Utils.dip2px(getActivity(), 10));
        spanLine.setBackgroundResource(R.color.mainbg);
        spanLine.setLayoutParams(params);
        recyclerSwipeLayout.addHeaderView(spanLine);
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.msg_content_item, null, false);
        benbenMsgLayout = (LinearLayout) header.findViewById(R.id.msg_count_layout);
        benbenMsg = (TextView) header.findViewById(R.id.msg_count);
        benbenMsgDate = (TextView) header.findViewById(R.id.msg_date);
        benbenMsgContent = (TextView) header.findViewById(R.id.msg_content);
        TextView titleTv = (TextView) header.findViewById(R.id.msg_title);
        titleTv.setText("系统消息");
        recyclerSwipeLayout.addHeaderView(header);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                //打开会话列表
//                Intent intent = mIMKit.getConversationActivityIntent();
//                startActivity(intent);
                loginBaiChuang("zbb10001");
                if (ywMsgConversation != null) {
                    ywMsgConversation.getMsgReadedStatusFromServer(new ArrayList<YWMessage>(), null);
                }
                benbenMsgLayout.setVisibility(View.INVISIBLE);
            }
        });
        recyclerSwipeLayout.setOnLoadMoreListener(quickAdapterListener);
        recyclerSwipeLayout.setXCallBack(callBack);
        freshUI();
    }

    private void loginBaiChuang(final String openIm) {
        //开始登录
        //此对象获取到后，保存为全局对象，供APP使用  此对象跟用户相关，如果切换了用户，需要重新获取
        IYWLoginService loginService = mIMKit.getLoginService();
        YWLoginParam loginParam = YWLoginParam.createLoginParam(UserData.getUserData().getBenbenNum() + "", MyApplication.YW_APP_COMM_PWD);
        loginService.login(loginParam, new IWxCallback() {

            @Override
            public void onSuccess(Object... obj) {
                /////////////////////--------------------------打开普通聊天窗口(Activity)
                Intent intent = mIMKit.getChattingActivityIntent(openIm, MyApplication.YW_APP_KEY);
                startActivity(intent);
            }

            @Override
            public void onProgress(int arg0) {
            }

            @Override
            public void onError(int errCode, String description) {
                //如果登录失败，errCode为错误码,description是错误的具体描述信息
                ToastUtil.showText(errCode + ":登录百川失败:" + description);
            }
        });
    }

    /**
     * 接收到阿里云旺的消息
     */
    private void ltxMsgChange() {
        if (mIMKit == null) {
            mIMKit = YWAPI.getIMKitInstance(UserData.getUserData().getBenbenNum() + "", MyApplication.YW_APP_KEY);
        }
        IYWConversationService conversationService = mIMKit.getConversationService();
        int unReadCount = 0;
        List<YWConversation> conversationList = conversationService.getConversationList();
        for (YWConversation c : conversationList) {
            if (c.getConversationId().contains("zbb10001")) {//不用是因为有可能是其它组发的 YWConversation conversation = conversationService.getConversationByConversationId("cnhhupan老同学平台");
                unReadCount = c.getUnreadCount();
                benbenMsgContent.setText(c.getLatestContent());
                benbenMsgDate.setText(DateUtils.longToDate(c.getLatestTimeInMillisecond()));
                ywMsgConversation = c;
            }
        }
        if (unReadCount > 99) {
            benbenMsg.setText("1+");
            benbenMsgLayout.setVisibility(View.VISIBLE);
        } else if (unReadCount > 0) {
            benbenMsg.setText(unReadCount + "");
            benbenMsgLayout.setVisibility(View.VISIBLE);
        } else {
            benbenMsgLayout.setVisibility(View.INVISIBLE);
        }
    }

    BaseQuickAdapter.RequestLoadMoreListener quickAdapterListener = new BaseQuickAdapter.RequestLoadMoreListener() {

        @Override
        public void onLoadMoreRequested() {
            if (pageNo < totalPage) {
                pageNo++;
            } else {
                recyclerSwipeLayout.loadComplete();
            }
        }
    };

    CustomBaseQuickAdapter.QuickXCallBack callBack = new CustomBaseQuickAdapter.QuickXCallBack() {

        @Override
        public void convert(BaseViewHolder baseViewHolder, Object itemModel) {
            baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detail = new Intent();
                    detail.setClass(getActivity(), MessageDetailActivity.class);
                    startActivity(detail);
                }
            });
        }
    };

    @Override
    public void update(Observable o, Object obj) {
        ObserverBean ob = (ObserverBean) obj;
        switch (ob.getWhat()) {
            case ObMsgConstance.ALIYUN_IM_MESSAGE_RECEIVER:
                ltxMsgChange();
                break;
        }
    }

    public void freshUI() {
        mIMKit = YWAPI.getIMKitInstance(UserData.getUserData().getBenbenNum() + "", MyApplication.YW_APP_KEY);
        ltxMsgChange();
    }
}