package com.example.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import com.example.sockettest.InitConts;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class TCPServerService extends Service implements InitConts {
    private boolean mISServiceDestoryed=false;
    private String [] mDefinedMessages= new String[] {
            "你好啊，哈哈",
            "请问你叫什么名字",
            "今天北京天气不错呀，shy",
            "你知道吗？我可是可以和多个人同时聊天哦",
            "给你讲个笑话吧:据说爱笑的人运气不会太差，不知道真假"
    };
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        new Thread(new TCPServer()).start();
        super.onCreate();
    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        mISServiceDestoryed =true;
        super.onDestroy();
    }
    
    private class TCPServer implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            ServerSocket serverSocket =null;
            
            try {
                serverSocket =new ServerSocket(DEFAULT_PORT);
                System.out.println("server 49153");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.out.println("server err0r");
                e.printStackTrace();
            }
            
            while (!mISServiceDestoryed) {
                try {
                    System.out.println("wait 49153");
                    final Socket client = serverSocket.accept();
                    responseClient(client);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    System.out.println("wait 49153");
                    e.printStackTrace();
                }
                
            }
        }
        private void responseClient(Socket client) throws IOException {
            System.out.println("responseClient ");
            if(client ==null) return;
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            System.out.println("write TCPServer ");
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),true);
            out.println("Welcome to TCPServer");
            
            while (!mISServiceDestoryed) {
                String str = in.readLine();
                System.out.println("tds msg from client: " +str);
                if(str == null) {
                    break;
                }
                int i = new Random().nextInt(mDefinedMessages.length);
                String msg = mDefinedMessages[i];
                out.println(msg);
                System.out.println("tds msg send : " +msg);
            }
            System.out.print("client quit...");
            out.close();
            in.close();
            client.close();
            
        }
        
    }
    

}
