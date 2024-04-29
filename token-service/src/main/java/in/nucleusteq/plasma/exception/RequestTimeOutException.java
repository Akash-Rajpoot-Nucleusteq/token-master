package in.nucleusteq.plasma.exception;

public class RequestTimeOutException extends RuntimeException{
    private  static final long serialVersionUID = 1L;

    public RequestTimeOutException(final String message) {
        super(message);
    }
}
