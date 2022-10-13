package kg.peaksoft.giftlistb6.exseptions;

public class BadCredentialsException extends RuntimeException{

    public BadCredentialsException() {
        super();
    }

    public BadCredentialsException(String message) {
        super(message);
    }
}
