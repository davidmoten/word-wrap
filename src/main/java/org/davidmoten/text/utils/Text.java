package org.davidmoten.text.utils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.function.Function;
import java.util.regex.Pattern;

import com.github.davidmoten.guavamini.annotations.VisibleForTesting;

public final class Text {

    public static String wordWrap(String text, int maxWidth) {
        return wordWrap(text, s -> s.length(), maxWidth);
    }

    public static String wordWrap(String text, Function<? super CharSequence, ? extends Number> stringWidth,
            Number maxWidth) {
        try (StringReader r = new StringReader(text); StringWriter w = new StringWriter()) {
            wordWrap(r, w, "\n", stringWidth, maxWidth);
            return w.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void wordWrap(Reader text, Writer out, String newLine,
            Function<? super CharSequence, ? extends Number> stringWidth, Number maxWidth) throws IOException {
        StringBuilder line = new StringBuilder();
        StringBuilder word = new StringBuilder();
        double maxWidthDouble = maxWidth.doubleValue();
        boolean broken = false;
        boolean alphanumeric = false;
        while (true) {
            int c = text.read();
            if (c == -1) {
                break;
            }
            char ch = (char) c;
            alphanumeric = Character.isAlphabetic(ch));
            if (ch == '\n') {
                line.append(word);
                out.write(line.toString());
                out.write(newLine);
                word.setLength(0);
                line.setLength(0);
                broken = false;
            } else if (ch == '\r') {
                // ignore carriage return
            } else {
                if (alphanumeric) {
                    word.append(ch);
                    if (broken && line.length() == 0) {
                        trimLeadingSpaces(word);
                    }
                    if (tooLong(stringWidth, line.toString() + word.toString(), maxWidthDouble)) {
                        if (line.length() > 0) {
                            writeLine(out, line, newLine);
                            trimLeadingSpaces(word);
                            if (tooLong(stringWidth, word.toString(), maxWidthDouble)) {
                                writeBrokenWord(out, word, newLine);
                            } else {
                                broken = true;
                            }
                        } else {
                            writeBrokenWord(out, word, newLine);
                        }
                    }
                } else {
                    if (word.length() > 0 && !isWhitespace(word)) {
                        appendWordToLine(line, word);
                        if (broken) {
                            trimLeadingSpaces(line);
                        }
                    }
                    word.append(ch);
                    if (tooLong(stringWidth, line.toString() + word.toString(), maxWidthDouble)) {
                        if (line.length() > 0) {
                            if (!isWhitespace(line)) {
                                writeLine(out, line, newLine);
                            } else {
                                line.setLength(0);
                            }
                            broken = true;
                        } else {
                            String w = word.substring(0, word.length() - 1);
                            word.delete(0, word.length() - 1);
                            if (broken) {
                                w = trimLeadingSpaces(w);
                            }
                            if (w.length() > 0) {
                                out.write(w);
                                out.write(newLine);
                                broken = false;
                            }
                        }
                    }
                }
            }
        }
        if (line.length() > 0) {
            String s = line.toString() + word.toString();
            if (broken) {
                s = trimLeadingSpaces(s);
            }
            out.write(s);
        } else {
            if (broken) {
                trimLeadingSpaces(word);
            }
            out.write(word.toString());
        }
    }

    private static boolean isPunctuation(char ch) {
        return Pattern.matches("\\p{Punct}", ch + "");
    }

    private static boolean tooLong(Function<? super CharSequence, ? extends Number> stringWidth, String s,
            double maxWidthDouble) {
        return stringWidth.apply(rtrim(s)).doubleValue() > maxWidthDouble;
    }

    @VisibleForTesting
    static String rtrim(String s) {
        int i = s.length();
        while (i > 0) {
            if (Character.isWhitespace(s.charAt(i - 1))) {
                i--;
            } else {
                break;
            }
        }
        if (i != s.length()) {
            return s.substring(0, i);
        } else {
            return s;
        }
    }

    static boolean isWhitespace(CharSequence s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
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

    private static String trimLeadingSpaces(String s) {
        StringBuilder b = new StringBuilder(s);
        trimLeadingSpaces(b);
        return b.toString();
    }

    private static void appendWordToLine(StringBuilder line, StringBuilder word) {
        line.append(word.toString());
        word.setLength(0);
    }

    private static void writeBrokenWord(Writer out, StringBuilder word, String newLine) throws IOException {
        out.write(word.substring(0, word.length() - 1));
        out.write(newLine);
        word.delete(0, word.length() - 1);
    }

    private static void writeLine(Writer out, StringBuilder line, String newLine) throws IOException {
        out.write(line.toString());
        out.write(newLine);
        line.setLength(0);
    }
}
