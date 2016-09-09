package com.example.socket;

public abstract class SocketClient {
    public abstract void connectTCPServer(String ip,int port);
    public abstract void reconnectTCPServer();
    public abstract boolean sendMsg(String msg);
    public abstract String readServerMsg();
    public abstract void checkSocketStatus() ;
    public abstract void stopConnection() ;
}
