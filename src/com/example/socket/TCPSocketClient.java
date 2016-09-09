package com.example.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import android.os.Handler;
import android.os.SystemClock;

import com.example.handler.BaseHandler;
import com.example.sockettest.InitConts;

public class TCPSocketClient extends SocketClient implements InitConts{
    PrintWriter mPrintWriter;
    BufferedReader mBufferedReader;
    Socket mClientSocket;
    Handler mHandler;
    static String hostIp;
    static int port =DEFAULT_PORT;
    public TCPSocketClient(BaseHandler handler) {
        this.mHandler = handler;
    }
    
    public void connectTCPServer(String ip,int port) {
        if(ip ==null|| "".equals(ip.trim())) {
            return ;
        }
        hostIp =ip;
        this.port = port;
        Socket socket = null;
        //while(socket == null) {
            try {
                
                mHandler.sendEmptyMessage(MSG_SERVER_BEGAIN_CONNECTION);
                socket = new Socket(ip,port);
                mClientSocket = socket;
                mPrintWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
                mBufferedReader= new BufferedReader(new BufferedReader(new InputStreamReader(socket.getInputStream())));
                mHandler.sendEmptyMessage(MSG_SERVER_CONNECT_SUCCESS);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                mHandler.sendEmptyMessage(MSG_SERVER_CONNECT_FAILED);
                SystemClock.sleep(1000);
               // e.printStackTrace();
                System.out.println(String.format("tds error:%s cause%s", e.getMessage(),e.getCause()));
            }
          
        //}
    }
    
    
    public boolean sendMsg(String msg) {
        boolean send =false;
       if(mPrintWriter!=null&&!mClientSocket.isInputShutdown()&&!mClientSocket.isOutputShutdown()&&mClientSocket.isConnected()) {
           try{
            mClientSocket.sendUrgentData(0xff);
            mHandler.sendEmptyMessage(MSG_SERVER_BEGAIN_SEND_MSG);
            mPrintWriter.println(msg);
            mPrintWriter.flush();
            mHandler.sendEmptyMessage(MSG_SERVER_END_SEND_MSG);
            send= true;
           }catch(Exception e) {
            mHandler.sendEmptyMessage(MSG_SERVER_SEND_FAILED_MSG);
            send= false;
           }
        }
       checkSocketStatus();
       return send;
    }
    
    public String readServerMsg() {
        String msg ="";
        try {
            if(mBufferedReader!=null&&mClientSocket!=null && mClientSocket.isConnected() && !mClientSocket.isInputShutdown()) {
                mHandler.sendEmptyMessage(MSG_SERVER_BEGAIN_RECV_MSG);
                msg = mBufferedReader.readLine();
                mHandler.sendEmptyMessage(MSG_SERVER_RECV_SUCCESS_MSG);
            }
        }catch(Exception e) {
            mHandler.sendEmptyMessage(MSG_SERVER_RECV_FAILED_MSG);
        }
        checkSocketStatus();
        return msg;
        
    }
    
    public void checkSocketStatus() {
        if(mClientSocket !=null) {
            if(mClientSocket.isInputShutdown()) {
                mHandler.sendEmptyMessage(MSG_SOCKET_RECV_IN_CLOSE);
            }
            if(mClientSocket.isOutputShutdown()) {
                mHandler.sendEmptyMessage(MSG_SOCKET_SEND_OUT_CLOSE);
            }
            if(mClientSocket.isConnected()) {
                mHandler.sendEmptyMessage(MSG_SOCKET_IS_CONNECTED);
            } else {
                mHandler.sendEmptyMessage(MSG_SOCKET_CONNECTED_FAILED);
            }
        }
    }
    
    public void stopConnection() {
        try {
            if(mPrintWriter !=null) {
                mPrintWriter.close();
                mPrintWriter =null;
            }
            if(mBufferedReader!=null) {
                mBufferedReader.close();
                mBufferedReader=null;
            }
            if(mClientSocket !=null) {
                    mClientSocket.shutdownOutput();
                    mClientSocket.shutdownInput();
                    mClientSocket.close();
                    mClientSocket =null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void reconnectTCPServer() {
        // TODO Auto-generated method stub
        stopConnection();
        connectTCPServer(hostIp, port);
    }
}
