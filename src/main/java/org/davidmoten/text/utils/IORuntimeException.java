package org.davidmoten.text.utils;

import java.io.IOException;

public final class IORuntimeException extends RuntimeException{

    private static final long serialVersionUID = -6137104875062423165L;

    public IORuntimeException(IOException e) {
        super(e);
    }
    
}
