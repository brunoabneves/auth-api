package store.ojuara.authapi.shared.exception;

public class AuthException extends RuntimeException{

    private Integer status;

    public Integer status() {
        return this.status;
    }

    public AuthException(String message){
        super(message);
    }

    public AuthException(Integer status, String message){
        super(message);
        this.status = status;
    }
}
