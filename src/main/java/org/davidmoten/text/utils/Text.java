package org.davidmoten.text.utils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.function.Function;

public final class Text {

    public static String wordWrap(String text, int maxWidth) {
        StringReader r = new StringReader(text);
        StringWriter w = new StringWriter();
        try {
            wordWrap(r, w, s -> (float) s.length(), maxWidth);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return w.toString();
    }

    public static void wordWrap(Reader text, Writer out,
            Function<? super CharSequence, Float> stringWidth, float maxWidth) throws IOException {
        StringBuilder line = new StringBuilder();
        StringBuilder word = new StringBuilder();
        while (true) {
            char ch = (char) text.read();
            if (ch == -1) {
                break;
            }
            boolean alphanumeric = Character.isAlphabetic(ch);
            if (ch == '\n') {
                line.append(word);
                out.write(line.toString());
                word.setLength(0);
                line.setLength(0);
            } else if (ch == '\r') {
                // ignore carriage return
            } else {
                if (!alphanumeric) {
                    if (word.length() > 0) {
                        // word boundary found
                        // try to add word to current line
                        if (stringWidth.apply(line.toString() + word.toString()) < maxWidth) {
                            if (line.length() == 0) {
                                line.append(word.toString().trim());
                            } else {
                                line.append(word.toString());
                            }
                            word.setLength(0);
                        } else {
                            // overflows line so write whole line and reset
                            out.write(line.toString().trim());
                            line.setLength(0);
                        }
                        word.append(ch);
                    } else {
                        word.append(ch);
                        if (stringWidth.apply(line.toString() + word.toString()) >= maxWidth) {
                            if (line.length() > 0) {
                                out.write(line.toString());
                                line.setLength(0);
                            } else {
                                out.write(word.substring(1, word.length() - 1));
                                word.delete(0, word.length() - 1);
                            }
                        }
                    }
                } else {
                    word.append(ch);
                }
            }
        }
        if (stringWidth.apply(line.toString() + word.toString()) >= maxWidth) {
            if (line.length() > 0) {
                out.write(line.toString());
                if (word.length() > 0) {
                    out.write(word.toString());
                }
            } else {
                if (word.length() > 0) {
                    out.write(word.toString());
                }
            }
        } else {
            out.write(line.toString() + word.toString());
        }
    }
}
