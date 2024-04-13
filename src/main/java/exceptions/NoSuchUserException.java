package exceptions;

import java.util.NoSuchElementException;

public class NoSuchUserException extends NoSuchElementException {
    public NoSuchUserException(String s) {
        super(s);
    }
}
