package org.davidmoten.text.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CharSequenceConcatTest {
    
    @Test
    public void testConcat() {
        CharSequenceConcat s = new CharSequenceConcat("ab","cd");
        assertEquals("abcd", s.toString());
        assertEquals('a', s.charAt(0));
        assertEquals('d', s.charAt(3));
        assertEquals(4, s.length());
        assertEquals("bc", s.subSequence(1, 3).toString());
        assertEquals("c", s.subSequence(1, 3).subSequence(1, 2).toString());
    }

}
