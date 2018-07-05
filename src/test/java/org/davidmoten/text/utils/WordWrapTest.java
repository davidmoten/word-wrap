package org.davidmoten.text.utils;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.github.davidmoten.junit.Asserts;

public class WordWrapTest {
    
    @Test
    public void testIsUtilityClass() {
        Asserts.assertIsUtilityClass(WordWrap.class);
    }

    @Test
    public void testTrimLeadingSpaces0() {
        StringBuilder s = new StringBuilder("abc");
        WordWrap.leftTrim(s);
        assertEquals("abc", s.toString());
    }

    @Test
    public void testTrimLeadingSpaces1() {
        StringBuilder s = new StringBuilder(" abc");
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
        StringBuilder s = new StringBuilder("   abc");
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
        checkWrap("1234567890","123456\n7890");
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

    private void checkWrap(String text, String expected) {
        String s = WordWrap.from(text).maxWidth(6).wrap();
        System.out.println(s);
        assertEquals(expected, s);
    }

    @Test
    public void testImportanceOfBeingEarnest() throws IOException {
        WordWrap.fromClasspathUtf8("/the-importance-of-being-earnest.txt") //
                .maxWidth(20) //
                .wrapUtf8("target/book.txt");
    }

    @Test
    public void testTheBlackGang() throws IOException {
        WordWrap.fromClasspathUtf8("/the-black-gang.txt") //
                .maxWidth(20) //
                .wrapUtf8("target/book2.txt");
    }

}
