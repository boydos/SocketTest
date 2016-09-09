package com.example.handler;

import java.util.ArrayList;
import java.util.List;

import com.example.socket.SocketInfo;
import com.example.sockettest.InitConts;

import android.os.Handler;
import android.os.Message;

public abstract class BaseHandler extends Handler implements InitConts{
    static List<SocketInfo> connectionInfos= new ArrayList<SocketInfo>();//old
    private static SocketInfo newInfo = new SocketInfo();//dang
    
    private boolean isNewCon = true; //下一个连接是否是新连接
    public BaseHandler() {
        init();
    }
    
    public void init() {
        connectionInfos= new ArrayList<SocketInfo>();
        newInfo = new SocketInfo();
        isNewCon = true;
    }
    
    @Override
    public void handleMessage(Message msg) {
        // TODO Auto-generated method stub
        switch (msg.what) {
        case MSG_UPDATE :
            String response =(String) msg.obj;
            if(response!=null) appendLog(response);
            break;
        case MSG_CLEAR : clearLog(); break;
        case MSG_SERVER_BEGAIN_CONNECTION :
            
            if(isNewCon) {
                appendLog("正在连接服务器");
                connectionInfos.add(newInfo);
                newInfo = new SocketInfo();
                newInfo.setStartConTime(System.currentTimeMillis());
            } else {
                appendLog(String.format("第%d次尝试连接服务器", newInfo.getRetryConTimes()+1));
            }
            newInfo.setRetryConTimes(newInfo.getRetryConTimes()+1); 
            break;//开始连接
        case MSG_SERVER_CONNECT_SUCCESS : 
            isNewCon = true;
            newInfo.setEndConTime(System.currentTimeMillis());
            newInfo.setConnected(true);
            appendLog("服务器连接成功");
            break; //连接成功
        case MSG_SERVER_CONNECT_FAILED :
            isNewCon =false;
            newInfo.setConnected(false);
            newInfo.setEndConTime(System.currentTimeMillis());
            appendLog("服务器连接失败");
            break; // 连接失败
        
        case MSG_SERVER_BEGAIN_SEND_MSG :
            if(newInfo.getStartSendTime()==-1){
               newInfo.setStartSendTime(System.currentTimeMillis());
            }
            break; //开始发送消息
        case MSG_SERVER_END_SEND_MSG :
            appendLog("发送消息成功");
            newInfo.setSucSendTimes(newInfo.getSucSendTimes()+1);
            newInfo.setEndSendTime(System.currentTimeMillis());
            break; // 发送成功
        case MSG_SERVER_SEND_FAILED_MSG : 
            appendLog("发送消息失败");
            newInfo.setFailSendTimes(newInfo.getFailSendTimes()+1);
            newInfo.setEndSendTime(System.currentTimeMillis());
            break; // 发送失败
        
        case MSG_SERVER_BEGAIN_RECV_MSG : break; //开始接收信息
        case MSG_SERVER_RECV_SUCCESS_MSG : break; //接收消息成功
        case MSG_SERVER_RECV_FAILED_MSG : break; //接收消息失败
        
        case MSG_SOCKET_IS_CONNECTED : break; //tcp 连接成功
        case MSG_SOCKET_CONNECTED_FAILED : break; //tcp 连接失败
        case MSG_SOCKET_RECV_IN_CLOSE : break; //server 断开连接
        case MSG_SOCKET_SEND_OUT_CLOSE : break; //client 断开连接
        default:
            break;
        }
        super.handleMessage(msg);
    }
    public abstract void appendLog(String msg);
    public abstract void clearLog();
    
    public List<SocketInfo> getOldInfos() {
        return connectionInfos;
    }
    public SocketInfo getNowInfo() {
        return newInfo;
    }

}
