package one.superstack.thingstack.exception;

public class InvalidTokenException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Invalid access token or API key";
    }
}