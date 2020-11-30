package org.davidmoten.text.utils;

import java.io.IOException;

public interface LineConsumer {

    void write(char[] chars, int offset, int length) throws IOException;

    void writeNewLine() throws IOException;

    default void write(String s) throws IOException {
        char[] chars = s.toCharArray();
        write(chars, 0, chars.length);
    }
}