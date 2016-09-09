package com.example.socket;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SocketInfo {
    private boolean connected;
    private long startConTime;
    private long endConTime;
    private int retryConTimes;
    
    private long startSendTime;
    private long endSendTime;
    private int  sucSendTimes;
    private int  failSendTimes;
    
    public SocketInfo () {
        init();
    }
    public void init() {
        startConTime =-1;
        endConTime = -1;
        retryConTimes=0;
        connected = false;
        startSendTime =-1;
        endSendTime =-1;
        sucSendTimes=0;
        failSendTimes=0;
    }
    public long getStartConTime() {
        return startConTime;
    }
    public void setStartConTime(long startConTime) {
        this.startConTime = startConTime;
    }
    public long getEndConTime() {
        return endConTime;
    }
    public void setEndConTime(long endConTime) {
        this.endConTime = endConTime;
    }
    public int getRetryConTimes() {
        return retryConTimes;
    }
    public void setRetryConTimes(int retryConTimes) {
        this.retryConTimes = retryConTimes;
    }
    public long getStartSendTime() {
        return startSendTime;
    }
    public void setStartSendTime(long startSendTime) {
        this.startSendTime = startSendTime;
    }
    public long getEndSendTime() {
        return endSendTime;
    }
    public void setEndSendTime(long endSendTime) {
        this.endSendTime = endSendTime;
    }
    public int getSucSendTimes() {
        return sucSendTimes;
    }
    public void setSucSendTimes(int sucSendTimes) {
        this.sucSendTimes = sucSendTimes;
    }
    public int getFailSendTimes() {
        return failSendTimes;
    }
    public void setFailSendTimes(int failSendTimes) {
        this.failSendTimes = failSendTimes;
    }
    
    public String formatTime(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyy/MM/dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }
    
    
    public boolean isConnected() {
        return connected;
    }
    public void setConnected(boolean connected) {
        this.connected = connected;
    }
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        if(startConTime==-1) return "无连接情况";
        String msg ="###################";
        msg += String.format("\n[开始连接时间:%s]\n[结束连接时间:%s]\n[尝试%d次后连接"+(connected?"成功":"仍然失败")+"]\n" +
                "[开始发送时间:%s]\n[结束发送时间:%s]\n[发送成功次数:%d]\n[发送失败次数:%d]\n", 
                formatTime(startConTime),formatTime(endConTime),retryConTimes,
                formatTime(startSendTime),formatTime(endSendTime),sucSendTimes,failSendTimes);
        msg +="###################";
        return msg;
    }
  
    
}
