package org.davidmoten.text.utils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.function.Function;

import com.github.davidmoten.guavamini.annotations.VisibleForTesting;

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
                if (alphanumeric) {
                    word.append(ch);
                    if (stringWidth.apply(line.toString() + word.toString())
                            .doubleValue() > maxWidthDouble) {
                        if (line.length() > 0) {
                            writeLine(out, line, newLine);
                            trimLeadingSpaces(word);
                            if (stringWidth.apply(word.toString()).doubleValue() > maxWidthDouble) {
                                writeBrokenWord(out, word, newLine);
                            }
                        } else {
                            writeBrokenWord(out, word, newLine);
                        }
                    }
                } else {
                    if (word.length() > 0) {
                        appendWordToLine(line, word);
                    }
                    word.append(ch);
                    if (stringWidth.apply(line.toString() + word.toString())
                            .doubleValue() > maxWidthDouble) {
                        if (line.length() > 0) {
                            writeLine(out, line, newLine);
                            trimLeadingSpaces(word);
                        } else {
                            out.write(word.substring(0, word.length() - 1));
                            out.write(newLine);
                            word.delete(0, word.length() - 1);
                            trimLeadingSpaces(word);
                        }
                    }
                }
            }
        }
        if (line.length() > 0 || word.length() > 0) {
            out.write(line.toString() + word.toString());
        }
    }

    @VisibleForTesting
    static void trimLeadingSpaces(StringBuilder word) {
        // trim leading spaces on the word
        // because we have inserted a new line
        int i;
        for (i = 0; i < word.length(); i++) {
            if (!Character.isWhitespace(word.charAt(i))) {
                break;
            }
        }
        if (i < word.length() && i > 0) {
            word.delete(0, i);
        }
    }

    private static void appendWordToLine(StringBuilder line, StringBuilder word) {
        line.append(word.toString());
        word.setLength(0);
    }

    private static void writeBrokenWord(Writer out, StringBuilder word, String newLine)
            throws IOException {
        out.write(word.substring(0, word.length() - 1));
        out.write(newLine);
        word.delete(0, word.length() - 1);
    }

    private static void writeLine(Writer out, StringBuilder line, String newLine)
            throws IOException {
        out.write(line.toString());
        out.write(newLine);
        line.setLength(0);
    }
}
