package com.example.emqdemo.domain;

public class emqResp {
    private String guid;
    private String code;
    private String msg;
    private String IPAddress2;
    private String IPAddress3;
    private String IPAddress4;


    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getIPAddress2() {
        return IPAddress2;
    }

    public void setIPAddress2(String IPAddress2) {
        this.IPAddress2 = IPAddress2;
    }

    public String getIPAddress3() {
        return IPAddress3;
    }

    public void setIPAddress3(String IPAddress3) {
        this.IPAddress3 = IPAddress3;
    }

    public String getIPAddress4() {
        return IPAddress4;
    }

    public void setIPAddress4(String IPAddress4) {
        this.IPAddress4 = IPAddress4;
    }
}