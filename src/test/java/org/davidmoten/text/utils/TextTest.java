package org.davidmoten.text.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TextTest {

    @Test
    public void test() {
        String s = Text.wordWrap("hello there", 6);
        System.out.println(s);
        assertEquals("hello\nthere", s);
    }

}
