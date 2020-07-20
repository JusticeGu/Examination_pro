package com.q7w.examination.result;

public enum ExceptionMsg {
    SUCCESS("200", "SUCCESS"),
    Upload_SUCCESS("200", "上传成功"),
    SUCCESS_GETUSER("200","DB获取用户成功"),
    SUCCESS_GETUSERR("200","Redis,获取用户成功"),
    SUCCESS_GET("200","数据获取成功-DB"),
    SUCCESS_GETR("200","数据获取成功-Redis"),
    SUCCESS_WXBIND("200","微信绑定成功"),
    SUCCESS_ER("200","进入考场成功"),
    SUCCESS_SUBMIT("200","您已成功参加考试，试卷已提交成功"),
    FAILED("801","操作失败"),
    FAILED_F("802","表单内容非法"),
    FAILED_V("803","验证失败"),
    Login_FAILED_1("4000001","操作异常-1"),
    Login_FAILED_2("4000002","用户不存在"),
    ParamError("000001", "参数错误！"),
    FileEmpty("000400","上传文件为空"),
    LimitPictureSize("000401","图片大小必须小于2M"),
    LimitPictureType("000402","图片格式必须为'jpg'、'png'、'jpge'、'gif'、'bmp'"),
    FAILED_403("403","鉴权失败，您没有对此服务的访问权限"),
    ;
    private ExceptionMsg(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    private String code;
    private String msg;

    public String getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }


}