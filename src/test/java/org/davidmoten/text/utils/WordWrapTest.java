package org.davidmoten.text.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.junit.Test;

import com.github.davidmoten.junit.Asserts;

public class WordWrapTest {

    ////////////////////////////////////////////
    // Word wrap tests
    ////////////////////////////////////////////

    @Test
    public void testLongLineSplitsOnWhiteSpace() {
        check("hello there", "hello\nthere");
    }

    @Test
    public void testLongLineTwoSpaces() {
        check("hello  there", "hello\nthere");
    }

    @Test
    public void testLongLineALotOfWhiteSpace() {
        check("hello          there", "hello\nthere");
    }

    @Test
    public void testPrecedingWhitespaceConserved() {
        check("  he", "  he");
    }

    @Test
    public void testPrecedingWhitespaceLongWord() {
        check("  helloyou", "  hel-\nloyou");
    }

    @Test
    public void testWhitespacePreservedAfterNewLine() {
        check("hello\n  the", "hello\n  the");
    }

    @Test
    public void testShortLineNoWhitespace() {
        check("hello", "hello");
    }

    @Test
    public void testShortLineHasWhitespace() {
        check("hi bo", "hi bo");
    }

    @Test
    public void testEmpty() {
        check("", "");
    }

    @Test
    public void testOneLetter() {
        check("a", "a");
    }

    @Test
    public void testSpaceThenOneLetter() {
        check(" a", " a");
    }

    @Test
    public void testNewLine() {
        check("hello\nthere", "hello\nthere");
    }

    @Test
    public void testCarriageRemoved() {
        check("hello\r\nthere", "hello\nthere");
    }

    @Test
    public void testWhitespaceConservedAfterNewLine() {
        check("hello\n there", "hello\n there");
    }

    @Test
    public void testWrapRightTrimsWhitespaceBeforeNewLine() {
        check("abc    \ncde   ", "abc\ncde   ");
    }

    @Test
    public void testLongWordForcesBreak() {
        check("hellothere", "hello-\nthere");
    }

    @Test
    public void testLongWordForcesBreakNoHyphens() {
        assertEquals("hellot\nhere",
                WordWrap.from("hellothere").maxWidth(6).insertHyphens(false).wrap());
    }

    @Test
    public void breakOnComma() {
        check("hi,there", "hi,th-\nere");
    }

    @Test
    public void noHyphenAfterDigits() {
        check("1234567890", "123456\n7890");
    }

    @Test
    public void breakOnCommas() {
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
    public void testEndWithNewLine() {
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
    public void testBreakOnQuote() {
        check("says 'helo'", "says\n'helo'");
    }

    @Test
    public void testBreakQuoteInMiddle() {
        check("why he's nasty", "why\nhe's\nnasty");
    }

    @Test
    public void testBuilder() {
        WordWrap.from("hello there").maxWidth(6);
    }

    @Test
    public void testShortThenLong() {
        check("hi mygoodnessme", "hi\nmygoo-\ndness-\nme");
    }

    @Test
    public void testLongWhitespaceThenWord() {
        check("        a", "\na");
    }

    @Test
    public void testLongWhitespaceLastLine() {
        check("          ", "");
    }

    @Test
    public void testLongWhitespaceThenNewLine() {
        check("          \n", "\n");
    }

    @Test
    public void testConserveWhitespace() {
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
    public void testIsUtilityClass() {
        Asserts.assertIsUtilityClass(WordWrap.class);
    }

    private static void check(String text, String expected) {
        String s = WordWrap.from(text).maxWidth(6).wrap();
//        System.out.println(s);
        assertEquals(expected, s);
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
