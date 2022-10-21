package kg.peaksoft.giftlistb6.exceptions;

public class BadRequestException extends RuntimeException{

    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }
}
