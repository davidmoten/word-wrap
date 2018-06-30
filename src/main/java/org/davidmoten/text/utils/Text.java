package org.davidmoten.text.utils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.function.Function;

public final class Text {

    public static String wordWrap(String text, int maxWidth) {
        return wordWrap(text, s -> s.length(), maxWidth);
    }

    public static String wordWrap(String text,
            Function<? super CharSequence, ? extends Number> stringWidth, Number maxWidth) {
        try (StringReader r = new StringReader(text); StringWriter w = new StringWriter()) {
            wordWrap(r, w, "\n", stringWidth, maxWidth);
            return w.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void wordWrap(Reader text, Writer out, String newLine,
            Function<? super CharSequence, ? extends Number> stringWidth, Number maxWidth)
            throws IOException {
        StringBuilder line = new StringBuilder();
        StringBuilder word = new StringBuilder();
        double maxWidthDouble = maxWidth.doubleValue();
        while (true) {
            int c = text.read();
            if (c == -1) {
                break;
            }
            char ch = (char) c;
            boolean alphanumeric = Character.isAlphabetic(ch);
            if (ch == '\n') {
                line.append(word);
                out.write(line.toString());
                out.write(newLine);
                word.setLength(0);
                line.setLength(0);
            } else if (ch == '\r') {
                // ignore carriage return
            } else {
                if (!alphanumeric) {
                    if (word.length() > 0) {
                        // word boundary found
                        // try to add word to current line
                        if (stringWidth.apply(line.toString() + word.toString())
                                .doubleValue() < maxWidthDouble) {
                            if (line.length() == 0) {
                                line.append(word.toString().trim());
                            } else {
                                line.append(word.toString());
                            }
                            word.setLength(0);
                        } else {
                            // overflows line so write whole line and reset
                            out.write(line.toString().trim());
                            out.write(newLine);
                            line.setLength(0);
                        }
                        word.append(ch);
                    } else {
                        word.append(ch);
                        if (stringWidth.apply(line.toString() + word.toString())
                                .doubleValue() >= maxWidthDouble) {
                            if (line.length() > 0) {
                                out.write(line.toString());
                                out.write(newLine);
                                line.setLength(0);
                            } else {
                                out.write(word.substring(1, word.length() - 1));
                                out.write(newLine);
                                word.delete(0, word.length() - 1);
                            }
                        }
                    }
                } else {
                    word.append(ch);
                }
            }
        }
        if (stringWidth.apply(line.toString() + word.toString()).doubleValue() >= maxWidthDouble) {
            if (line.length() > 0) {
                out.write(line.toString());
                if (word.length() > 0) {
                    out.write(newLine);
                    out.write(word.toString().trim());
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
