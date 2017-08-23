package com.bqjr.model;

public class DkUserMq {
    private String serialno;

    private String username;

    private String usercode;

    private Short mqtype;

    private String mqkey;

    public String getSerialno() {
        return serialno;
    }

    public void setSerialno(String serialno) {
        this.serialno = serialno == null ? null : serialno.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode == null ? null : usercode.trim();
    }

    public Short getMqtype() {
        return mqtype;
    }

    public void setMqtype(Short mqtype) {
        this.mqtype = mqtype;
    }

    public String getMqkey() {
        return mqkey;
    }

    public void setMqkey(String mqkey) {
        this.mqkey = mqkey == null ? null : mqkey.trim();
    }
}