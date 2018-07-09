package org.davidmoten.text.utils;

import java.io.BufferedReader;
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

	/**
	 * Sets the source to be wrapped and returns a builder to specify more
	 * parameters.
	 * 
	 * @param reader source to be wrapped
	 * @return builder
	 */
	public static Builder from(Reader reader) {
		return from(reader, false);
	}

	/**
	 * Sets the source to be wrapped as a classpath resource which will be read
	 * using the UTF-8 character set. Returns a builder to specify more parameters.
	 * Uses an 8192 byte buffer for reading.
	 * 
	 * @param resource source to be wrapped as a classpath resource
	 * @return builder
	 */
	public static Builder fromClasspathUtf8(String resource) {
		return fromClasspath(resource, StandardCharsets.UTF_8);
	}

	/**
	 * Sets the source to be wrapped as a classpath resource to be read using the
	 * given character set. Returns a builder to specify more parameters. Uses an
	 * 8192 byte buffer for reading.
	 * 
	 * @param resource classpath resource name
	 * @param charset  charset to use for reading
	 * @return builder
	 */
	public static Builder fromClasspath(String resource, Charset charset) {
		return new Builder(
				new BufferedReader(new InputStreamReader(WordWrap.class.getResourceAsStream(resource), charset)), true);
	}

	/**
	 * Sets the the source to be wrapped and returns a builder to specify more
	 * parameters. Uses an 8192 byte buffer for reading.
	 * 
	 * @param text text to be wrapped
	 * @return builder
	 */
	public static Builder from(CharSequence text) {
		return from(new BufferedReader(new CharSequenceReader(text)), true);
	}

	/**
	 * Sets the source to be wrapped. Returns a builder to specify more parameters.
	 * Uses an 8192 byte buffer for reading.s
	 * 
	 * @param in source to be wrapped
	 * @return builder
	 */
	public static Builder fromUtf8(InputStream in) {
		return from(in, StandardCharsets.UTF_8);
	}

	/**
	 * Sets the source to be wrapped and the character set to be used to read it.
	 * Uses an 8192 byte buffer for reading. Returns a builder to specify more
	 * parameters.
	 * 
	 * @param in
	 * @param charset
	 * @return builder
	 */
	public static Builder from(InputStream in, Charset charset) {
		return from(new BufferedReader(new InputStreamReader(in, charset)));
	}

	/**
	 * Sets the source to be wrapped and the character set to be used to read it.
	 * Uses an 8192 byte buffer for reading. Returns a builder to specify more
	 * parameters.
	 * 
	 * @param file    file to be read
	 * @param charset charset of the text in the source file
	 * @return
	 */
	public static Builder from(File file, Charset charset) {
		try {
			return from(new BufferedReader(new InputStreamReader(new FileInputStream(file), charset)), true);
		} catch (FileNotFoundException e) {
			throw new IORuntimeException(e);
		}
	}

	private static Builder from(Reader reader, boolean close) {
		return new Builder(reader, close);
	}

	/**
	 * Provides method chaining for specifying parameters to word wrap.
	 */
	public static final class Builder {

		private final Reader reader;
		private final boolean closeReader;
		private Number maxWidth = 80;
		private Function<? super CharSequence, ? extends Number> stringWidth = STRING_WIDTH_DEFAULT;
		private Set<Character> extraWordChars = SPECIAL_WORD_CHARS_SET_DEFAULT;
		private String newLine = "\n";
		private boolean insertHyphens = true;
		private boolean breakWords = true;

		Builder(Reader reader, boolean closeReader) {
			this.reader = reader;
			this.closeReader = closeReader;
		}

		/**
		 * Sets the maximum width of a line using the {@code stringWidth} function. Word
		 * wrapping/splitting will be attempted for lines with greater than
		 * {@code maxWidth}. If not set the default is 80.
		 * 
		 * @param maxWidth maximum width of a line using the {@code stringWidth}
		 *                 function.
		 * @return this
		 * @throws {@link IllegalArgumentException} if {@code maxWidth} is less than or
		 *         equal to zero
		 */
		public Builder maxWidth(Number maxWidth) {
			Preconditions.checkArgument(maxWidth.doubleValue() > 0);
			this.maxWidth = maxWidth;
			return this;
		}

		/**
		 * Sets the string width function used to determine if a line is at maximum
		 * width (and therefore needing wrapping or splitting). If not set the string
		 * width function is the number of characters.
		 * 
		 * @param stringWidth function that returns the width of a sequence of
		 *                    characters
		 * @return this
		 */
		public Builder stringWidth(Function<? super CharSequence, ? extends Number> stringWidth) {
			this.stringWidth = stringWidth;
			return this;
		}

		/**
		 * Sets the newLine string to be used. If not set the default is '\n' (line feed
		 * character).
		 * 
		 * @param newLine
		 * @return this
		 */
		public Builder newLine(String newLine) {
			this.newLine = newLine;
			return this;
		}

		/**
		 * Sets all extra word characters (characters that will be treated like normal
		 * alphabetic characters for defining word boundaries).
		 * 
		 * @param extraWordChars extra word characters (in addtion to alphabetic
		 *                       characters)
		 * @return this
		 */
		public Builder extraWordChars(Set<Character> extraWordChars) {
			this.extraWordChars = extraWordChars;
			return this;
		}

		/**
		 * Sets all extra word characters (characters that will be treated like normal
		 * alphabetic characters for defining word boundaries).
		 * 
		 * @param extraWordChars extra word characters (in addtion to alphabetic
		 *                       characters)
		 * @return this
		 */
		public Builder extraWordChars(String extraWordChars) {
			return extraWordChars(toSet(extraWordChars));
		}

		/**
		 * Adds more word characters (characters that will be treated like normal
		 * alphabetic characters for defining word boundaries).
		 * 
		 * @param includeWordChars more word characters
		 * @return this
		 */
		public Builder includeExtraWordChars(String includeWordChars) {
			Set<Character> set = toSet(includeWordChars);
			this.extraWordChars.addAll(set);
			return this;
		}

		/**
		 * Adds extra word characters to be excluded. Alphabetic characters are always
		 * word characters and thus will be ignored here.
		 * 
		 * @param excludeWordChars extra word characters to be excluded
		 * @return this
		 */
		public Builder excludeExtraWordChars(String excludeWordChars) {
			Set<Character> set = toSet(excludeWordChars);
			this.extraWordChars.removeAll(set);
			return this;
		}

		/**
		 * Sets if to break words using a hyphen character. If set to false then no
		 * breaking character will be used.
		 * 
		 * @param insertHyphens whether to break hyphens
		 * @return this
		 */
		public Builder insertHyphens(boolean insertHyphens) {
			this.insertHyphens = insertHyphens;
			return this;
		}

		/**
		 * If a word is longer than {@code maxWidth} and {@code breakWords} is true then
		 * such a word will be broken across two or more lines (with or without a hyphen
		 * according to {@link Builder#insertHyphens}).
		 * 
		 * @param breakWords
		 * @return
		 */
		public Builder breakWords(boolean breakWords) {
			this.breakWords = breakWords;
			return this;
		}

		/**
		 * Performs the wrapping of the source text and writes output to the given
		 * {@link Writer}.
		 * 
		 * @param out output for wrapped text
		 */
		public void wrap(Writer out) {
			try {
				wordWrap(reader, out, newLine, maxWidth, stringWidth, extraWordChars, insertHyphens, breakWords);
			} catch (IOException e) {
				throw new IORuntimeException(e);
			} finally {
				if (closeReader) {
					close(reader);
				}
			}
		}

		/**
		 * Performs the wrapping of the source text and writes output to the given file
		 * with the given character set encoding.
		 * 
		 * @param file    file to receive wrapped output
		 * @param charset encoding to use for output
		 */
		public void wrap(File file, Charset charset) {
			try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), charset)) {
				wrap(writer);
			} catch (IOException e) {
				throw new IORuntimeException(e);
			}
		}

		/**
		 * Performs the wrapping of the source text and writes the output to the given
		 * file using UTF-8 encoding.
		 * 
		 * @param file output file for wrapped text
		 */
		public void wrapUtf8(File file) {
			wrap(file, StandardCharsets.UTF_8);
		}

		/**
		 * Performs the wrapping of the source text and writes the output to a file with
		 * the given filename.
		 * 
		 * @param filename output file for wrapped text
		 */
		public void wrapUtf8(String filename) {
			wrapUtf8(new File(filename));
		}

		/**
		 * Performs the wrapping of the source text and writes the output to a file with
		 * the given filename using the given encoding.
		 * 
		 * @param filename output file for the wrapped text
		 * @param charset  encoding to use for output
		 */
		public void wrap(String filename, Charset charset) {
			wrap(new File(filename), charset);
		}

		/**
		 * Performs the wrapping of the source text and returns output as a String.
		 * 
		 * @return
		 */
		public String wrap() {
			try (StringWriter out = new StringWriter()) {
				wrap(out);
				return out.toString();
			} catch (IOException e) {
				throw new IORuntimeException(e);
			}
		}

	}

	@VisibleForTesting
	static void close(Reader reader) {
		try {
			reader.close();
		} catch (IOException e) {
			throw new IORuntimeException(e);
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
			Function<? super CharSequence, ? extends Number> stringWidth, Set<Character> extraWordChars,
			boolean insertHyphens, boolean breakWords) throws IOException {
		StringBuilder2 line = new StringBuilder2();
		StringBuilder2 word = new StringBuilder2();
		CharSequence lineAndWordRightTrim = concatRightTrim(line, word);
		double maxWidthDouble = maxWidth.doubleValue();
		boolean broken = false;
		boolean isWordCharacter = false;
		boolean previousWasPunctuation = false;
		while (true) {
			int c = in.read();
			if (c == -1) {
				break;
			}
			char ch = (char) c;
			isWordCharacter = Character.isLetter(ch) || extraWordChars.contains(ch);
			if (ch == '\n') {
				line.append(word);
				if (tooLong(stringWidth, line, maxWidthDouble)) {
					line.rightTrim();
				}
				if (!isWhitespace(line)) {
					out.write(line.internalArray(), 0, line.length());
				}
				out.write(newLine);
				word.setLength(0);
				line.setLength(0);
				broken = false;
			} else if (ch == '\r') {
				// ignore carriage return
			} else if (isWordCharacter && !previousWasPunctuation) {
				word.append(ch);
				if (broken && line.length() == 0) {
					leftTrim(word);
				}
				if (tooLong(stringWidth, lineAndWordRightTrim, maxWidthDouble)) {
					if (line.length() > 0) {
						writeLine(out, line, newLine);
						leftTrim(word);
						if (tooLong(stringWidth, word, maxWidthDouble)) {
							if (breakWords) {
								writeBrokenWord(out, word, newLine, insertHyphens);
							} else {
								broken = true;
							}
						} else {
							broken = true;
						}
					} else {
						if (breakWords) {
							writeBrokenWord(out, word, newLine, insertHyphens);
						} else {
							broken = true;
						}
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
					Preconditions.checkArgument(line.length() > 0, "line length was zero. If this happens please" //
							+ " contribute unit test that provokes this failure to the project!");
					if (!isWhitespace(line)) {
						writeLine(out, line, newLine);
					} else {
						line.setLength(0);
					}
					broken = true;
				}
			}
			previousWasPunctuation = isPunctuation(ch) && !extraWordChars.contains(ch);
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
				out.write(word.internalArray(), 0, word.length());
			}
		}
	}

	private static CharSequence concatRightTrim(CharSequence a, CharSequence b) {
		return new CharSequenceConcatRightTrim(a, b);
	}

	private static boolean isPunctuation(char ch) {
		return PUNCTUATION.indexOf(ch) != -1;
	}

	private static boolean tooLong(Function<? super CharSequence, ? extends Number> stringWidth, CharSequence s,
			double maxWidthDouble) {
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
	static void leftTrim(StringBuilder2 word) {
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
		StringBuilder2 b = new StringBuilder2(s);
		leftTrim(b);
		return b.toString();
	}

	private static void appendWordToLine(StringBuilder2 line, StringBuilder2 word) {
		line.append(word);
		word.setLength(0);
	}

	private static void writeBrokenWord(Writer out, StringBuilder2 word, String newLine, boolean insertHyphens)
			throws IOException {
		// to be really thorough we'd check the new stringWidth with '-' but let's not
		// bother for now
		String x;
		if (insertHyphens && word.length() > 2 && !isWhitespace((x = word.substring(0, word.length() - 2)))) {
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

	private static void writeLine(Writer out, StringBuilder2 line, String newLine) throws IOException {
		out.write(line.internalArray(), 0, line.length());
		out.write(newLine);
		line.setLength(0);
	}
}
