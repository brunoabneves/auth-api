package store.ojuara.authapi.shared.exception;

public class UnauthorizedException extends RuntimeException{
    private Integer status;

    public Integer status() {
        return this.status;
    }

    public UnauthorizedException(String message){
        super(message);
    }

    public UnauthorizedException(Integer status, String message){
        super(message);
        this.status = status;
    }
}
