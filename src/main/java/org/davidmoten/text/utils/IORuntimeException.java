package org.davidmoten.text.utils;

import java.io.IOException;

public final class IORuntimeException extends RuntimeException{

    public IORuntimeException(IOException e) {
        super(e);
    }
    
}
