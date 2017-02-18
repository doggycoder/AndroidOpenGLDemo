package edu.wuwang.opengl.egl;

public enum GlError {

    OK(0,"ok"),
    ConfigErr(101,"config not support");
    int code;
    String msg;
    GlError(int code, String msg){
        this.code=code;
        this.msg=msg;
    }

    public int value(){
        return code;
    }

    @Override
    public String toString() {
        return msg;
    }
}
