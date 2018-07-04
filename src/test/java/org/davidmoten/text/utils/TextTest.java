package org.davidmoten.text.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TextTest {

    @Test
    public void testTrimLeadingSpaces0() {
        StringBuilder s = new StringBuilder("abc");
        Text.leftTrim(s);
        assertEquals("abc", s.toString());
    }

    @Test
    public void testTrimLeadingSpaces1() {
        StringBuilder s = new StringBuilder(" abc");
        Text.leftTrim(s);
        assertEquals("abc", s.toString());
    }

    @Test
    public void testRightTrim() {
        assertEquals("abc", Text.rightTrim("abc  "));
    }
    
    @Test
    public void testRightTrimNoSpace() {
        assertEquals("abc", Text.rightTrim("abc"));
    }
    
    @Test
    public void testRightTrimEmpty() {
        assertEquals("", Text.rightTrim(""));
    }
    
    @Test
    public void testRightTrimOnlySpace() {
        assertEquals("", Text.rightTrim("  "));
    }

    @Test
    public void testTrimLeadingSpaces3() {
        StringBuilder s = new StringBuilder("   abc");
        Text.leftTrim(s);
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
        checkWrap("  helloyou", "  hell\noyou");
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
        checkWrap("hellothere", "hellot\nhere");
    }
    
    @Test
    public void breakOnComma() {
        checkWrap("hi,there", "hi,\nthere");
    }
    
    @Test
    public void breakOnCommas() {
        checkWrap("1,2,3,4,5,6,7,8,9","1,2,3,\n4,5,6,\n7,8,9");
    }
    
    @Test
    public void longThenShort() {
        checkWrap("hellothere\n  boo", "hellot\nhere\n  boo");
    }
    
    @Test
    public void longThenShortWithMoreLines() {
        checkWrap("hellothere\n  boo\n  hi", "hellot\nhere\n  boo\n  hi");
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
    public void testBreakOnQuote() {
        checkWrap("says 'helo'", "says\n'helo'");
    }
    
    @Test
    public void testBreakQuoteInMiddle() {
        checkWrap("why he's nasty" , "why\nhe's\nnasty");
    }
    
    private void checkWrap(String text, String expected) {
        String s = Text.wordWrap(text, 6);
        System.out.println(s);
        assertEquals(expected, s);
    }

}
