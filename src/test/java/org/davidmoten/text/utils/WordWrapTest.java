package org.davidmoten.text.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.github.davidmoten.junit.Asserts;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WordWrapTest {

    ////////////////////////////////////////////
    // Word wrap tests
    ////////////////////////////////////////////

    @Test
    public void longLineSplitsOnWhiteSpace() {
        check("hello there", "hello\nthere");
    }

    @Test
    public void longLineWithTwoSpacesInMiddle() {
        check("hello  there", "hello\nthere");
    }

    @Test
    public void longLineALotOfWhiteSpaceInMiddle() {
        check("hello          there", "hello\nthere");
    }

    @Test
    public void precedingWhitespaceConserved() {
        check("  he", "  he");
    }

    @Test
    public void precedingWhitespaceLongWord() {
        check("  helloyou", "  hel-\nloyou");
    }

    @Test
    public void whitespacePreservedAfterNewLine() {
        check("hello\n  the", "hello\n  the");
    }

    @Test
    public void shortLineNoWhitespace() {
        check("hello", "hello");
    }

    @Test
    public void shortLineHasWhitespace() {
        check("hi bo", "hi bo");
    }

    @Test
    public void emptyText() {
        check("", "");
    }

    @Test
    public void oneLetter() {
        check("a", "a");
    }

    @Test
    public void spaceThenOneLetter() {
        check(" a", " a");
    }

    @Test
    public void newLineCharacterPreserved() {
        check("hello\nthere", "hello\nthere");
    }

    @Test
    public void carriageReturnRemoved() {
        check("hello\r\nthere", "hello\nthere");
    }

    @Test
    public void whitespaceConservedAfterNewLine() {
        check("hello\n there", "hello\n there");
    }

    @Test
    public void wrapRightTrimsWhitespaceBeforeNewLine() {
        check("abc    \ncde   ", "abc\ncde   ");
    }

    @Test
    public void longWordForcesBreak() {
        check("hellothere", "hello-\nthere");
    }

    @Test
    public void longWordForcesBreakNoHyphens() {
        assertEquals("hellot\nhere",
                WordWrap.from("hellothere").maxWidth(6).insertHyphens(false).wrap());
    }

    @Test
    public void breakOnCommaDoesNotHappenWithoutSpaceAfter() {
        check("hi,there", "hi,th-\nere");
    }

    @Test
    public void noHyphenAfterDigits() {
        check("1234567890", "123456\n7890");
    }

    @Test
    public void breakOnCommasWithDigits() {
        check("1,2,3,4,5,6,7,8,9", "1,2,3,\n4,5,6,\n7,8,9");
    }

    @Test
    public void longThenShort() {
        check("hellothere\n  boo", "hello-\nthere\n  boo");
    }

    @Test
    public void longThenShortWithMoreLines() {
        check("hellothere\n  boo\n  hi", "hello-\nthere\n  boo\n  hi");
    }

    @Test
    public void endWithNewLine() {
        check("a\n", "a\n");
    }

    @Test
    public void spaceAndQuestionMark() {
        check("  ?", "  ?");
    }

    @Test
    public void dontBreakOnQuestionMark() {
        check("ab cde?", "ab\ncde?");
    }

    @Test
    public void breakOnQuote() {
        check("says 'helo'", "says\n'helo'");
    }

    @Test
    public void breakQuoteInMiddle() {
        check("why he's nasty", "why\nhe's\nnasty");
    }

    @Test
    public void shortThenLong() {
        check("hi mygoodnessme", "hi\nmygoo-\ndness-\nme");
    }

    @Test
    public void longWhitespaceThenWord() {
        check("        a", "\na");
    }

    @Test
    public void longWhitespaceLastLine() {
        check("          ", "");
    }

    @Test
    public void longWhitespaceThenNewLine() {
        check("          \n", "\n");
    }

    @Test
    public void conserveWhitespace() {
        check("  ab\n   cd\n  ef\n\nhi", "  ab\n   cd\n  ef\n\nhi");
    }

    ////////////////////////////////////////////
    // Left trim tests
    ////////////////////////////////////////////

    @Test
    public void testLeftTrimLeadingEmpty() {
        StringBuilder2 s = new StringBuilder2("");
        WordWrap.leftTrim(s);
        assertEquals("", s.toString());
    }

    @Test
    public void testLeftTrimLeadingSpaces0() {
        StringBuilder2 s = new StringBuilder2("abc");
        WordWrap.leftTrim(s);
        assertEquals("abc", s.toString());
    }

    @Test
    public void testLeftTrimLeadingSpaces1() {
        StringBuilder2 s = new StringBuilder2(" abc");
        WordWrap.leftTrim(s);
        assertEquals("abc", s.toString());
    }

    @Test
    public void testLeftTrimLeadingSpaces3() {
        StringBuilder2 s = new StringBuilder2("   abc");
        WordWrap.leftTrim(s);
        assertEquals("abc", s.toString());
    }

    ////////////////////////////////////////////
    // Right trim tests
    ////////////////////////////////////////////

    @Test
    public void testRightTrimAtEnd() {
        assertEquals("abc", WordWrap.rightTrim("abc  "));
    }

    @Test
    public void testRightTrimNoSpace() {
        assertEquals("abc", WordWrap.rightTrim("abc"));
    }

    @Test
    public void testRightTrimEmpty() {
        assertEquals("", WordWrap.rightTrim(""));
    }

    @Test
    public void testRightTrimOnlySpace() {
        assertEquals("", WordWrap.rightTrim("  "));
    }

    ////////////////////////////////////////////
    // Builder tests
    ////////////////////////////////////////////

    @Test
    public void testStringWidth() {
        String text = WordWrap.from("abc").maxWidth(4).stringWidth(s -> s.length() * 2).wrap();
        assertEquals("a-\nbc", text);
    }

    @Test
    public void testNewLineOverride() {
        String text = WordWrap.from("abc").maxWidth(2).newLine("\r\n").wrap();
        assertEquals("a-\r\nbc", text);
    }

    @Test
    public void testSetWordChars() {
        String text = WordWrap.from("abc").maxWidth(2).extraWordChars("abc").wrap();
        assertEquals("a-\nbc", text);
    }

    @Test
    public void testIncludeWordChars() {
        String text = WordWrap.from("abc").maxWidth(2).extraWordChars("")
                .includeExtraWordChars("abc").wrap();
        assertEquals("a-\nbc", text);
    }

    @Test
    public void testExcludeWordChars() {
        String text = WordWrap.from("abc").maxWidth(2).extraWordChars("abc")
                .excludeExtraWordChars("abc").wrap();
        assertEquals("a-\nbc", text);
    }

    @Test
    public void testDontBreakLongWords() {
        String s = WordWrap.from("hello jonathon").maxWidth(6).breakWords(false).wrap();
        assertEquals("hello\njonathon", s);
    }

    @Test
    public void testDontBreakLongWords2() {
        String s = WordWrap.from("hell jonathon").maxWidth(6).breakWords(false).wrap();
        assertEquals("hell\njonathon", s);
    }

    @Test
    public void testFromInputStream() {
        ByteArrayInputStream bytes = new ByteArrayInputStream(
                "hi".getBytes(StandardCharsets.UTF_8));
        assertEquals("hi", WordWrap.fromUtf8(bytes).maxWidth(6).wrap());
    }

    @Test
    public void testFromFile() throws IOException {
        Files.write(new File("target/test1.txt").toPath(), "hi".getBytes(StandardCharsets.UTF_8));
        assertEquals("hi", WordWrap.from(new File("target/test1.txt"), StandardCharsets.UTF_8)
                .maxWidth(6).wrap());
    }

    @Test(expected = IORuntimeException.class)
    public void testWriterThrows() {
        WordWrap.from("abc").wrap(new Writer() {

            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("boo");
            }

            @Override
            public void flush() throws IOException {

            }

            @Override
            public void close() throws IOException {

            }
        });
    }

    @Test(expected = IORuntimeException.class)
    public void testWrapToFileThrows() {
        WordWrap.from("abc").wrap(new File("target/doesNoExist/temp.txt"), StandardCharsets.UTF_8);
    }

    @Test(expected = IORuntimeException.class)
    public void testCloseReaderThrows() {
        WordWrap.close(new Reader() {

            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                return 0;
            }

            @Override
            public void close() throws IOException {
                throw new IOException("boo");
            }
        });
    }

    @Test(expected = IORuntimeException.class)
    public void testFromFileDoesNotExist() throws IOException {
        WordWrap.from(new File("target/doesNotExist"), StandardCharsets.UTF_8).maxWidth(6).wrap();
    }
    
    @Test
    public void testToList() {
        List<String> list = WordWrap.from("hello there how are you").maxWidth(10).wrapToList();
        assertEquals(Arrays.asList("hello", "there how", "are you"), list);
    }
    
    @Test
    public void testToListWithNewLines() {
        List<String> list = WordWrap.from("hello\n\n\n\nhow how\n").maxWidth(10).wrapToList();
        assertEquals(Arrays.asList("hello", "", "","", "how how"), list);
    }
    
    @Test
    public void testToListFinishWithNewLines() {
        List<String> list = WordWrap.from("hello\n\n\n\n").maxWidth(10).wrapToList();
        assertEquals(Arrays.asList("hello", "", "",""), list);
    }
    
    @Test(expected=IORuntimeException.class)
    public void testLineConsumerThrows() {
        WordWrap.from("hello there how are you").maxWidth(10).wrap(new LineConsumer() {

            @Override
            public void write(char[] chars, int offset, int length) throws IOException {
                throw new IOException("problem");
            }

            @Override
            public void writeNewLine() throws IOException {
                throw new IOException("problem");
            }});
    }
    
    @Test(expected=IORuntimeException.class)
    public void testLineConsumerThrowsDontCloseReader() {
        StringReader reader = new StringReader("hello there how are you");
        WordWrap.from(reader, false).maxWidth(10).wrap(new LineConsumer() {

            @Override
            public void write(char[] chars, int offset, int length) throws IOException {
                throw new IOException("problem");
            }

            @Override
            public void writeNewLine() throws IOException {
                throw new IOException("problem");
            }});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxWidthZero() {
        WordWrap.from("abc").maxWidth(0);
    }

    @Test
    public void testWrapToFile() {
        File file = new File("target/testWrap.txt");
        WordWrap.from("abc").wrap(file.getAbsolutePath(), StandardCharsets.UTF_8);
        assertTrue(file.exists());
    }
    
    @Test
    public void testDontWrapNumbers() {
        assertEquals("hello\n123", WordWrap.from("hello 123").includeDigitsAsExtraWordChars().breakWords(false).maxWidth(8).wrap());
    }

    @Test
    public void testNumbersWrapByDefault() {
        assertEquals("hello 12\n3", WordWrap.from("hello 123").breakWords(false).maxWidth(8).wrap());
    }
    
    @Test
    public void testDontWrapDecimalNumberDefaultSeparator() {
        assertEquals("hello\n12.3", WordWrap.from("hello 12.3").includeDigitsAsExtraWordChars().breakWords(false).maxWidth(8).wrapDecimalSeparator(false).wrap());
    }
    
    @Test
    public void testWrapDecimalNumber() {
        assertEquals("hello 12\n.3", WordWrap.from("hello 12.3").includeDigitsAsExtraWordChars().breakWords(false).maxWidth(8).wrapDecimalSeparator(true).wrap());
    }
    
    @Test
    public void testDontWrapDecimalNumberCommaSeparator() {
        assertEquals("hello\n12,3", WordWrap.from("hello 12,3").includeDigitsAsExtraWordChars().breakWords(false).maxWidth(8).wrapDecimalSeparator(true).wrap());
    }
    
    ////////////////////////////////////////////
    // Novel wrapping tests
    ////////////////////////////////////////////

    @Test
    public void testImportanceOfBeingEarnest() throws IOException {
        WordWrap.fromClasspathUtf8("/the-importance-of-being-earnest.txt") //
                .maxWidth(20) //
                .wrapUtf8("target/the-importance-of-being-earnest.txt");
    }

    @Test
    public void testTheBlackGang() throws IOException {
        WordWrap.fromClasspathUtf8("/the-black-gang.txt") //
                .maxWidth(20) //
                .wrapUtf8("target/the-black-gang.txt");
    }

    @Test
    public void testTreasureIsland() throws IOException {
        WordWrap.fromClasspathUtf8("/treasure-island-fragment.txt") //
                .maxWidth(80) //
                .wrapUtf8("target/treasure-island-fragment.txt");
    }
    
    @Test
    public void testIsDigits() {
    	assertFalse(WordWrap.isDigits("abc"));
    	assertFalse(WordWrap.isDigits("abc1"));
    	assertFalse(WordWrap.isDigits(" abc1"));
    	assertTrue(WordWrap.isDigits("1"));
    	assertTrue(WordWrap.isDigits("12"));
    	assertTrue(WordWrap.isDigits(" 1"));
    	assertTrue(WordWrap.isDigits(" 12"));
    	assertFalse(WordWrap.isDigits(" 12 "));
    }

    @Test
    public void testIsUtilityClass() {
        Asserts.assertIsUtilityClass(WordWrap.class);
    }
    
    private static final class Check {
        final String input;
        final String output;
        final String name;

        Check(String input, String output, String name) {
            this.input = input;
            this.output = output;
            this.name = name;
        }

    }

    private static List<Check> checks = new ArrayList<>();

    private static void check(String text, String expected) {
        String s = WordWrap.from(text).maxWidth(6).wrap();
        // System.out.println(s.replace(" ","\u2423"));
        assertEquals(expected, s);
        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
        checks.add(new Check(text, expected, name));
        int i = 0;
        try (FileWriter out = new FileWriter("src/docs/rules.md")) {
            out.append("## Wrapping rules\n");
            out.append(
                    "The rules below are generated from the unit tests in `WordWrapTest.java`. The default wrapping configuration used is a line length of 6 characters. The $ symbol is used to represent a new line character.\n\n");
            for (Check check : checks) {
                i++;
                out.append(i + ". **" + check.name + "**\n\n");
                out.append("Input:\n");
                out.append("```\n");
                out.append(prettify(check.input));
                out.append("\n```\n");
                out.append("Output:\n");
                out.append("```\n");
                out.append(prettify(check.output));
                out.append("\n```\n\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String prettify(String s) {
        return s.replace(" ", "\u2423").replace("\n", "$\n");
    }

    public static void main(String[] args) throws IOException {
        int i = 0;
        long t = 0;
        ByteArrayOutputStream b = new ByteArrayOutputStream(128 * 1024);
        byte[] bytes = Files
                .readAllBytes(new File("src/test/resources/treasure-island-fragment.txt").toPath());
        String text = new String(bytes, StandardCharsets.UTF_8);
        while (true) {
            b.reset();
            Writer out = new OutputStreamWriter(b, StandardCharsets.UTF_8);
            WordWrap.from(text) //
                    .maxWidth(80) //
                    .wrap(out);
            i++;
            if (i % 100 == 0) {
                if (i > 1000 && t == 0) {
                    t = System.currentTimeMillis();
                    i = 0;
                }
                long dt = System.currentTimeMillis() - t;
                if (dt != 0) {
                    System.out.println("avgPerSecond=" + (i * 1000 / dt));
                }
            }
        }
    }

}
