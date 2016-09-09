package com.example.sockettest;

public interface InitConts {
    public static int DEFAULT_PORT=49153;
    public static int MSG_UPDATE=0;
    public static int MSG_CLEAR=-1;

    public static int MSG_SERVER_BEGAIN_CONNECTION=1;
    public static int MSG_SERVER_CONNECT_SUCCESS=2;
    public static int MSG_SERVER_CONNECT_FAILED=3;
    public static int MSG_SERVER_BEGAIN_SEND_MSG=4;
    public static int MSG_SERVER_END_SEND_MSG=5;
    public static int MSG_SERVER_SEND_FAILED_MSG=6;
    public static int MSG_SERVER_BEGAIN_RECV_MSG=7;
    public static int MSG_SERVER_RECV_SUCCESS_MSG=8;
    public static int MSG_SERVER_RECV_FAILED_MSG=9;
    
    
    public static int MSG_SOCKET_SEND_OUT_CLOSE=101;
    public static int MSG_SOCKET_RECV_IN_CLOSE=102;
    public static int MSG_SOCKET_IS_CONNECTED=103;
    public static int MSG_SOCKET_CONNECTED_FAILED=104;
}
