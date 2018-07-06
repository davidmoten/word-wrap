package org.davidmoten.text.utils;

final class CharSequenceConcat implements CharSequence {

    private final CharSequence a;
    private final CharSequence b;

    CharSequenceConcat(CharSequence a, CharSequence b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public int length() {
        return a.length() + b.length();
    }

    @Override
    public char charAt(int index) {
        if (index < a.length()) {
            return a.charAt(index);
        } else {
            return b.charAt(index - a.length());
        }
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new CharSequence() {

            @Override
            public int length() {
                return end - start;
            }

            @Override
            public char charAt(int index) {
                return CharSequenceConcat.this.charAt(start + index);
            }

            @Override
            public CharSequence subSequence(int start2, int end2) {
                // only support one level of substring
                StringBuilder s = new StringBuilder(end2 - start2);
                for (int i = start2; i < end2; i++) {
                    s.append(charAt(i));
                }
                return s;
            }

            @Override
            public String toString() {
                StringBuilder s = new StringBuilder();
                int len = length();
                for (int i = 0; i < len; i++) {
                    s.append(charAt(i));
                }
                return s.toString();
            }

        };
    }

    @Override
    public String toString() {
        return a.toString() + b.toString();
    }

}