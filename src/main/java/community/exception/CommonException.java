package community.exception;

public class CommonException extends RuntimeException{
    private String message;

    public CommonException(CommonErrorCodeImp errorCode) {
        this.message = errorCode.getMeesage();
    }
    public CommonException(String message) {
        this.message = message;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
