package org.davidmoten.text.utils;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.junit.Test;

import com.github.davidmoten.junit.Asserts;

public class WordWrapTest {

    @Test
    public void testIsUtilityClass() {
        Asserts.assertIsUtilityClass(WordWrap.class);
    }

    @Test
    public void testTrimLeadingSpaces0() {
        StringBuilder2 s = new StringBuilder2("abc");
        WordWrap.leftTrim(s);
        assertEquals("abc", s.toString());
    }

    @Test
    public void testTrimLeadingSpaces1() {
        StringBuilder2 s = new StringBuilder2(" abc");
        WordWrap.leftTrim(s);
        assertEquals("abc", s.toString());
    }

    @Test
    public void testRightTrim() {
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

    @Test
    public void testTrimLeadingSpaces3() {
        StringBuilder2 s = new StringBuilder2("   abc");
        WordWrap.leftTrim(s);
        assertEquals("abc", s.toString());
    }

    @Test
    public void testLongLineSplitsOnWhiteSpace() {
        checkWrap("hello there", "hello\nthere");
    }

    @Test
    public void testLongLineTwoSpaces() {
        checkWrap("hello  there", "hello\nthere");
    }

    @Test
    public void testLongLineALotOfWhiteSpace() {
        checkWrap("hello          there", "hello\nthere");
    }

    @Test
    public void testPrecedingWhitespace() {
        checkWrap("  he", "  he");
    }

    @Test
    public void testPrecedingWhitespaceLongWord() {
        checkWrap("  helloyou", "  hel-\nloyou");
    }

    @Test
    public void testWhitespacePreservedAfterNewLine() {
        checkWrap("hello\n  the", "hello\n  the");
    }

    @Test
    public void testShortLineNoWhitespace() {
        checkWrap("hello", "hello");
    }

    @Test
    public void testShortLineHasWhitespace() {
        checkWrap("hi bo", "hi bo");
    }

    @Test
    public void testEmpty() {
        checkWrap("", "");
    }

    @Test
    public void testOneLetter() {
        checkWrap("a", "a");
    }

    @Test
    public void testSpaceThenOneLetter() {
        checkWrap(" a", " a");
    }

    @Test
    public void testNewLine() {
        checkWrap("hello\nthere", "hello\nthere");
    }

    @Test
    public void testCarriageReturnNewLine() {
        checkWrap("hello\r\nthere", "hello\nthere");
    }

    @Test
    public void testWhitespaceConservedAfterNewLine() {
        checkWrap("hello\n there", "hello\n there");
    }

    @Test
    public void testLongWordForcesBreak() {
        checkWrap("hellothere", "hello-\nthere");
    }

    @Test
    public void testLongWordForcesBreakNoHyphens() {
        assertEquals("hellot\nhere", WordWrap.from("hellothere").maxWidth(6).insertHyphens(false).wrap());
    }

    @Test
    public void breakOnComma() {
        checkWrap("hi,there", "hi,th-\nere");
    }

    @Test
    public void noHyphenAfterDigits() {
        checkWrap("1234567890", "123456\n7890");
    }

    @Test
    public void breakOnCommas() {
        checkWrap("1,2,3,4,5,6,7,8,9", "1,2,3,\n4,5,6,\n7,8,9");
    }

    @Test
    public void longThenShort() {
        checkWrap("hellothere\n  boo", "hello-\nthere\n  boo");
    }

    @Test
    public void longThenShortWithMoreLines() {
        checkWrap("hellothere\n  boo\n  hi", "hello-\nthere\n  boo\n  hi");
    }

    @Test
    public void testEndWithNewLine() {
        checkWrap("a\n", "a\n");
    }

    @Test
    public void spaceAndQuestionMark() {
        checkWrap("  ?", "  ?");
    }

    @Test
    public void dontBreakOnQuestionMark() {
        checkWrap("ab cde?", "ab\ncde?");
    }

    @Test
    public void testBreakOnQuote() {
        checkWrap("says 'helo'", "says\n'helo'");
    }

    @Test
    public void testBreakQuoteInMiddle() {
        checkWrap("why he's nasty", "why\nhe's\nnasty");
    }

    @Test
    public void testBuilder() {
        WordWrap.from("hello there").maxWidth(6);
    }

    @Test
    public void testShortThenLong() {
        checkWrap("hi mygoodnessme", "hi\nmygoo-\ndness-\nme");
    }

    @Test
    public void testLongWhitespaceThenWord() {
        checkWrap("        a", "\na");
    }

    @Test
    public void testLongWhitespaceLastLine() {
        checkWrap("          ", "");
    }

    @Test
    public void testLongWhitespaceThenNewLine() {
        checkWrap("          \n", "\n");
    }

    @Test
    public void testConserveWhitespace() {
        checkWrap("  ab\n   cd\n  ef\n\nhi", "  ab\n   cd\n  ef\n\nhi");
    }
    
    @Test
    public void testStringWidth() {
        String text = WordWrap.from("abc").maxWidth(4).stringWidth(s -> s.length() * 2).wrap();
        assertEquals("a-\nbc",text);
    }

    @Test
    public void testNewLineOverride() {
        String text = WordWrap.from("abc").maxWidth(2).newLine("\r\n").wrap();
        assertEquals("a-\r\nbc",text);
    }
    
    @Test
    public void testSetWordChars() {
        String text = WordWrap.from("abc").maxWidth(2).wordChars("abc").wrap();
        assertEquals("a-\nbc",text);
    }
    
    @Test
    public void testIncludeWordChars() {
        String text = WordWrap.from("abc").maxWidth(2).wordChars("").includeWordChars("abc").wrap();
        assertEquals("a-\nbc",text);
    }
    
    @Test
    public void testExcludeWordChars() {
        String text = WordWrap.from("abc").maxWidth(2).wordChars("abc").excludeWordChars("abc").wrap();
        assertEquals("a-\nbc",text);
    }
    
    private void checkWrap(String text, String expected) {
        String s = WordWrap.from(text).maxWidth(6).wrap();
        System.out.println(s);
        assertEquals(expected, s);
    }

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
    public void testFromInputStream() {
        ByteArrayInputStream bytes = new ByteArrayInputStream("hi".getBytes(StandardCharsets.UTF_8));
        assertEquals("hi", WordWrap.fromUtf8(bytes).maxWidth(6).wrap());
    }
    

    public static void main(String[] args) throws IOException {
        int i = 0;
        long t = 0;
        ByteArrayOutputStream b = new ByteArrayOutputStream(128*1024);
        byte[] bytes = Files.readAllBytes(new File("src/test/resources/treasure-island-fragment.txt").toPath());
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
