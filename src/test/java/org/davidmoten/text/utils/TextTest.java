package org.davidmoten.text.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TextTest {
    
    @Test
    public void testTrimLeadingSpaces() {
        StringBuilder s = new StringBuilder(" abc");
        Text.trimLeadingSpaces(s);
        assertEquals("abc", s.toString());
    }

    @Test
    public void testLongLineSplitsOnWhiteSpace() {
        checkWrap("hello there", "hello\nthere");
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
        checkWrap("hellothere", "hellot\nhere");
    }

    private void checkWrap(String text, String expected) {
        String s = Text.wordWrap(text, 6);
        System.out.println(s);
        assertEquals(expected, s);
    }

}
