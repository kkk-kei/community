package community.exception;


public enum CommonErrorCodeImp implements CommonErrorCode {

    Question_Not_Found(2001,"你要找的问题不存在~"),
    Comment_Missing(2002,"未选中问题或评论进行回复~"),
    No_Login(2003,"请先登录再进行操作~"),
    System_Error(2004,"服务器歇逼了~"),
    Type_Error(2005,"评论异常~"),
    Comment_Not_Found(2006,"评论不存在~"),
    Content_is_Empty(2007,"输入内容不能为空"),
    Notification_Not_Fund(2008,"你要找的通知不存在~"),
    Notification_Read_Error(2009,"这不是你的通知~");

    private Integer code;
    private String message;

    CommonErrorCodeImp(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMeesage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
