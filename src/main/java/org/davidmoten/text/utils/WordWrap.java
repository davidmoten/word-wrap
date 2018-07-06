package org.davidmoten.text.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import com.github.davidmoten.guavamini.Preconditions;
import com.github.davidmoten.guavamini.annotations.VisibleForTesting;

public final class WordWrap {

    private WordWrap() {
        // prevent instantiation
    }

    private static final String SPECIAL_WORD_CHARS = "\"\'\u2018\u2019\u201C\u201D?./!,;:_";

    public static final Set<Character> SPECIAL_WORD_CHARS_SET_DEFAULT = toSet(SPECIAL_WORD_CHARS);

    private static final Function<CharSequence, Number> STRING_WIDTH_DEFAULT = s -> s.length();

    private static final String PUNCTUATION = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

    public static Builder from(Reader reader) {
        return from(reader, false);
    }

    public static Builder fromClasspathUtf8(String resource) {
        return fromClasspath(resource, StandardCharsets.UTF_8);
    }

    public static Builder fromClasspath(String resource, Charset charset) {
        return new Builder(
                new InputStreamReader(WordWrap.class.getResourceAsStream(resource), charset), true);
    }

    private static Builder from(Reader reader, boolean close) {
        return new Builder(reader, close);
    }

    public static Builder from(CharSequence text) {
        return from(new CharSequenceReader(text), true);
    }

    public static Builder fromUtf8(InputStream in) {
        return from(new InputStreamReader(in, StandardCharsets.UTF_8));
    }

    public static Builder from(InputStream in, Charset charset) {
        return from(new InputStreamReader(in, charset));
    }

    public static Builder from(File file, Charset charset) {
        try {
            return from(new InputStreamReader(new FileInputStream(file), charset), true);
        } catch (FileNotFoundException e) {
            throw new IORuntimeException(e);
        }
    }

    public static final class Builder {

        private final Reader reader;
        private final boolean closeReader;
        private Number maxWidth = 80;
        private Function<? super CharSequence, ? extends Number> stringWidth = STRING_WIDTH_DEFAULT;
        private Set<Character> wordChars = SPECIAL_WORD_CHARS_SET_DEFAULT;
        private String newLine = "\n";
        private boolean insertHyphens = true;

        Builder(Reader reader, boolean closeReader) {
            this.reader = reader;
            this.closeReader = closeReader;
        }

        public Builder maxWidth(Number maxWidth) {
            Preconditions.checkArgument(maxWidth.doubleValue() > 0);
            this.maxWidth = maxWidth;
            return this;
        }

        public Builder stringWidth(Function<? super CharSequence, ? extends Number> stringWidth) {
            this.stringWidth = stringWidth;
            return this;
        }

        public Builder newLine(String newLine) {
            this.newLine = newLine;
            return this;
        }

        public Builder wordChars(Set<Character> wordChars) {
            this.wordChars = wordChars;
            return this;
        }

        public Builder wordChars(String wordChars) {
            return wordChars(toSet(wordChars));
        }

        public Builder includeWordChars(String includeWordChars) {
            Set<Character> set = toSet(includeWordChars);
            this.wordChars.addAll(set);
            return this;
        }

        public Builder excludeWordChars(String excludeWordChars) {
            Set<Character> set = toSet(excludeWordChars);
            this.wordChars.removeAll(set);
            return this;
        }

        public Builder insertHyphens(boolean insertHyphens) {
            this.insertHyphens = insertHyphens;
            return this;
        }

        public void wrap(Writer out) {
            try {
                wordWrap(reader, out, newLine, maxWidth, stringWidth, wordChars, insertHyphens);
            } catch (IOException e) {
                throw new IORuntimeException(e);
            } finally {
                if (closeReader) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        throw new IORuntimeException(e);
                    }
                }
            }
        }

        public void wrap(File file, Charset charset) {
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), charset)) {
                wrap(writer);
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        }

        public void wrapUtf8(File file) {
            wrap(file, StandardCharsets.UTF_8);
        }

        public void wrapUtf8(String filename) {
            wrapUtf8(new File(filename));
        }

        public void wrap(String filename, Charset charset) {
            wrap(filename, charset);
        }

        public String wrap() {
            try (StringWriter out = new StringWriter()) {
                wrap(out);
                return out.toString();
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        }

    }

    private static Set<Character> toSet(String chars) {
        Set<Character> set = new HashSet<Character>();
        for (int i = 0; i < chars.length(); i++) {
            set.add(chars.charAt(i));
        }
        return set;
    }

    static void wordWrap(Reader in, Writer out, String newLine, Number maxWidth,
            Function<? super CharSequence, ? extends Number> stringWidth,
            Set<Character> specialWordChars, boolean insertHyphens) throws IOException {
        StringBuilder line = new StringBuilder();
        StringBuilder word = new StringBuilder();
        CharSequence lineAndWordRightTrim = concatRightTrim(line, word);
        double maxWidthDouble = maxWidth.doubleValue();
        boolean broken = false;
        boolean alphanumeric = false;
        boolean previousWasPunctuation = false;
        while (true) {
            int c = in.read();
            if (c == -1) {
                break;
            }
            char ch = (char) c;
            alphanumeric = Character.isLetter(ch) || specialWordChars.contains(ch);
            if (ch == '\n') {
                line.append(word);
                if (!isWhitespace(line)) {
                    out.write(line.toString());
                }
                out.write(newLine);
                word.setLength(0);
                line.setLength(0);
                broken = false;
            } else if (ch == '\r') {
                // ignore carriage return
            } else if (alphanumeric && !previousWasPunctuation) {
                word.append(ch);
                if (broken && line.length() == 0) {
                    leftTrim(word);
                }
                if (tooLong(stringWidth, lineAndWordRightTrim, maxWidthDouble)) {
                    if (line.length() > 0) {
                        writeLine(out, line, newLine);
                        leftTrim(word);
                        if (tooLong(stringWidth, word, maxWidthDouble)) {
                            writeBrokenWord(out, word, newLine, insertHyphens);
                        } else {
                            broken = true;
                        }
                    } else {
                        writeBrokenWord(out, word, newLine, insertHyphens);
                    }
                }
            } else {
                if (word.length() > 0 && !isWhitespace(word)) {
                    appendWordToLine(line, word);
                    if (broken) {
                        leftTrim(line);
                    }
                }
                word.append(ch);
                if (tooLong(stringWidth, lineAndWordRightTrim, maxWidthDouble)) {
                    Preconditions.checkArgument(line.length() > 0,
                            "line length was zero. If this happens please" //
                                    + " contribute unit test that provokes this failure to the project!");
                    if (!isWhitespace(line)) {
                        writeLine(out, line, newLine);
                    } else {
                        line.setLength(0);
                    }
                    broken = true;
                }
            }
            previousWasPunctuation = isPunctuation(ch) && !specialWordChars.contains(ch);
        }
        if (line.length() > 0) {
            String s = line.toString() + word.toString();
            if (broken) {
                s = leftTrim(s);
            }
            out.write(s);
        } else {
            if (broken) {
                leftTrim(word);
            }
            if (!isWhitespace(word)) {
                out.write(word.toString());
            }
        }
    }

    private static CharSequence concatRightTrim(CharSequence a, CharSequence b) {
        return new CharSequenceConcatRightTrim(a, b);
    }

    private static boolean isPunctuation(char ch) {
        return PUNCTUATION.indexOf(ch) != -1;
    }

    private static boolean tooLong(Function<? super CharSequence, ? extends Number> stringWidth,
            CharSequence s, double maxWidthDouble) {
        return stringWidth.apply(s).doubleValue() > maxWidthDouble;
    }

    @VisibleForTesting
    static CharSequence rightTrim(CharSequence s) {
        int i = s.length();
        while (i > 0) {
            if (Character.isWhitespace(s.charAt(i - 1))) {
                i--;
            } else {
                break;
            }
        }
        if (i != s.length()) {
            return s.subSequence(0, i);
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
    static void leftTrim(StringBuilder word) {
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

    private static String leftTrim(String s) {
        StringBuilder b = new StringBuilder(s);
        leftTrim(b);
        return b.toString();
    }

    private static void appendWordToLine(StringBuilder line, StringBuilder word) {
        line.append(word.toString());
        word.setLength(0);
    }

    private static void writeBrokenWord(Writer out, StringBuilder word, String newLine,
            boolean insertHyphens) throws IOException {
        // to be really thorough we'd check the new stringWidth with '-' but let's not
        // bother for now
        String x;
        if (insertHyphens && word.length() > 2
                && !isWhitespace((x = word.substring(0, word.length() - 2)))) {
            out.write(x);
            out.write("-");
            out.write(newLine);
            word.delete(0, word.length() - 2);
        } else {
            String prefix = word.substring(0, word.length() - 1);
            if (!isWhitespace(prefix)) {
                out.write(prefix);
            }
            out.write(newLine);
            word.delete(0, word.length() - 1);
        }
    }

    private static void writeLine(Writer out, StringBuilder line, String newLine)
            throws IOException {
        out.write(line.toString());
        out.write(newLine);
        line.setLength(0);
    }
}
