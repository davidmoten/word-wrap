package org.davidmoten.text.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StringBuilder2Test {

    @Test
    public void test() {
        StringBuilder2 b = new StringBuilder2("abc");
        assertEquals("abc", b.toString());
    }

    @Test
    public void testSetLength() {
        StringBuilder2 b = new StringBuilder2("abc");
        b.setLength(2);
        assertEquals("ab", b.toString());
    }

    @Test
    public void testDelete() {
        StringBuilder2 b = new StringBuilder2("abc");
        b.delete(1, 2);
        assertEquals("ac", b.toString());
    }

    @Test
    public void testAppendChar() {
        StringBuilder2 b = new StringBuilder2("abc");
        b.append('d');
        assertEquals("abcd", b.toString());
    }

    @Test
    public void testAppendStringBuilder2() {
        StringBuilder2 b = new StringBuilder2("abc");
        StringBuilder2 c = new StringBuilder2("def");
        b.append(c);
        assertEquals("abcdef", b.toString());
    }

    @Test
    public void testBlank() {
        StringBuilder2 a = new StringBuilder2();
        a.append('a');
        assertEquals(1, a.length());
        assertEquals("a", a.toString());
    }

    @Test
    public void testCharAt() {
        StringBuilder2 b = new StringBuilder2("abc");
        assertEquals('a', b.charAt(0));
        assertEquals('b', b.charAt(1));
        assertEquals('c', b.charAt(2));
    }

    @Test
    public void testSubsequence() {
        StringBuilder2 b = new StringBuilder2("abcdef");
        assertEquals("abc", b.subSequence(0, 3).toString());
    }

    @Test
    public void testSubsequence2() {
        StringBuilder2 b = new StringBuilder2("abcdef");
        assertEquals("bcd", b.subSequence(1, 4).toString());
    }

    @Test
    public void testSubsequence3() {
        StringBuilder2 b = new StringBuilder2("abcdef");
        assertEquals("abcdef", b.subSequence(0, 6).toString());
    }

    @Test
    public void testSubstring() {
        StringBuilder2 b = new StringBuilder2("abcdef");
        assertEquals("cd", b.substring(2, 4).toString());
    }

    @Test
    public void testRightTrimEmpty() {
        assertEquals(0, new StringBuilder2("").rightTrim().length());
    }

    @Test
    public void testRightTrimNoSpace() {
        assertEquals(3, new StringBuilder2("abc").rightTrim().length());
    }

    @Test
    public void testRightTrimSpacesOnly() {
        assertEquals(0, new StringBuilder2("   ").rightTrim().length());
    }

    @Test
    public void testRightTrimSpacesAfterLetters() {
        assertEquals(3, new StringBuilder2("abc   ").rightTrim().length());
    }
}
