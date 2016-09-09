package com.example.sockettest;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.handler.BaseHandler;
import com.example.socket.SocketClient;
import com.example.socket.SocketInfo;
import com.example.socket.TCPServerService;
import com.example.socket.TCPSocketClient;

public class MainActivity extends Activity implements InitConts {
    ScrollView scrollview;
    EditText serverEdit;
    EditText msgEdit;
    TextView logView;
    Button btn;
    Button sendBtn;
    BaseHandler mHandler =new TCPSocketHandler();
    boolean stop= true;
    SocketClient mSocket;
    
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scrollview = (ScrollView) findViewById(R.id.scroll);
        serverEdit =(EditText) findViewById(R.id.server);
        msgEdit = (EditText) findViewById(R.id.msg);
        logView = (TextView) findViewById(R.id.log);
        btn = (Button)findViewById(R.id.btn);
        sendBtn =(Button)findViewById(R.id.send);
       
        mSocket = new TCPSocketClient(mHandler);
        Intent service = new Intent(this,TCPServerService.class);
        startService(service);
        clearLog();
        setSendEnable(false);
        btn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
               if(stop) {
                   mHandler.init();
                   String host = serverEdit.getText().toString();
                   if(host ==null ||"".equals(host.trim())) {
                       recordLog("请输入服务器地址 IP 或者 IP:PORT");
                       return;
                   }
                   String [] infos = host.split(":");
                   int port = DEFAULT_PORT;
                   if(infos.length >1) {
                       try{
                       port =Integer.parseInt(infos[1]);
                       }catch(Exception e) {
                           recordLog(String.format("端口异常，将使用[%d]端口",port));
                       }
                   }
                   if(!InitConfigureUtils.isIP(infos[0])) {
                       recordLog("请输入正确的服务器地址 IP 或者 IP:PORT");
                       return;
                   }
                   btn.setText(R.string.stopconnect);
                   processStart(infos[0],port);
               } else{
                   btn.setText(R.string.connect);
                   processStop();
               }
            }
        });
        
        sendBtn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                String msg = msgEdit.getText().toString();
                if(msg==null || "".equals(msg)) {
                    msg ="Welcome To China";
                    msgEdit.setText(msg);
                }
                processSendMsg(msg);
            }
        });
    }
    
    private void processStart(final String host,final int port) {
        startReCon();
        clearLog();
        setSendEnable(true);
        
        ThreadPool.runThread(new Runnable() {
            
            @Override
            public void run() {
                // TODO Auto-generated method stub
                mSocket.connectTCPServer(host, port);
            }
        });

    }
    
    private void processStop() {
        setSendEnable(false);
        stopReCon();
        mSocket.stopConnection();
        displayInfo(mHandler.getOldInfos(),mHandler.getNowInfo());
        
    }
    
    private void processSendMsg(final String msg) {
            setSendEnable(false);
            
            new Thread(new Runnable() {
                
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    int i=1;
                    boolean send =true;
                    while(!stop) {
                        if(send)recordLog(String.format("第%d次发送消息:%s",i, msg));
                        send=mSocket.sendMsg(msg);
                        if(send) {
                            i++;
                        } else {
                            i=1;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    
                    
                    }
                }
            }).start();
            
    }
    private void setSendEnable(boolean enabled) {
        msgEdit.setVisibility(enabled?View.VISIBLE:View.GONE);
        sendBtn.setVisibility(enabled?View.VISIBLE:View.GONE);
        sendBtn.setEnabled(enabled);
        msgEdit.setEnabled(enabled);
    }

    private void displayInfo(List<SocketInfo> oldinfo,SocketInfo nowinfo) {
        clearLog();
        int i=1;
        if(oldinfo !=null && oldinfo.size() >0) {
            for(SocketInfo info:oldinfo) {
                if(info!=null&&info.getStartConTime()!=-1){
                    recordLog(String.format("第%d次连接情况如下", i));
                    displayInfo(info);
                    i++;
                }
            }
        }
        if(nowinfo!=null&&nowinfo.getStartConTime()!=-1)
            recordLog(String.format("第%d次连接情况如下", i));
        displayInfo(nowinfo);
    }
    private void displayInfo(SocketInfo info) {
        if(info ==null) {
            recordLog("无状态");
        }else {
            recordLog(info.toString());
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
    private void clearLog() {
        mHandler.sendEmptyMessage(MSG_CLEAR);
    }
    private void recordLog(String msg) {
        mHandler.obtainMessage(MSG_UPDATE, msg).sendToTarget();
    }
    private void stopReCon() {
        stop =true;
    }
    private void startReCon() {
        stop = false;
    }
    private Message getMessage(int what, String msg) {
        Message obj = new Message();
        obj.what = what;
        obj.obj = msg;
        return obj;
    }
    
    class TCPSocketHandler extends BaseHandler {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
            case MSG_SERVER_CONNECT_FAILED:
            case MSG_SERVER_SEND_FAILED_MSG:
            case MSG_SERVER_RECV_FAILED_MSG:
            case MSG_SOCKET_CONNECTED_FAILED :
            case MSG_SOCKET_RECV_IN_CLOSE :
            case MSG_SOCKET_SEND_OUT_CLOSE :
                
                ThreadPool.runThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        System.out.println("stop:" +stop);
                        if(!stop)mSocket.reconnectTCPServer();
                    }
                });
                
                break;

            default:
                break;
            }
           
        }

        @Override
        public void appendLog(String msg) {
            // TODO Auto-generated method stub
            logView.append(msg +"\n");
            scrollview.fullScroll(ScrollView.FOCUS_DOWN);
        }

        @Override
        public void clearLog() {
            // TODO Auto-generated method stub
            logView.setText("");
        }
        
    }
 }
