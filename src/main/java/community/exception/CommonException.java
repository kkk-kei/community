package community.exception;

public class CommonException extends RuntimeException{
    private Integer code;
    private String message;

    public CommonException(CommonErrorCodeImp errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMeesage();
    }
    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
