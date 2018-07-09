/*
 * Copyright (C) 2013 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.davidmoten.text.utils;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

import com.github.davidmoten.guavamini.Preconditions;

/**
 * A {@link Reader} that reads the characters in a {@link CharSequence}. Like
 * {@code StringReader}, but works with any {@link CharSequence}.
 *
 * @author Colin Decker
 */
// TODO(cgdecker): make this public? as a type, or a method in CharStreams?
final class CharSequenceReader extends Reader {

    private CharSequence seq;
    private int pos;
    private int mark;

    public CharSequenceReader(CharSequence seq) {
        this.seq = Preconditions.checkNotNull(seq);
    }

    private void checkOpen() throws IOException {
        if (seq == null) {
            throw new IOException("reader closed");
        }
    }

    private boolean hasRemaining() {
        return remaining() > 0;
    }

    private int remaining() {
        return seq.length() - pos;
    }

    @Override
    public synchronized int read(CharBuffer target) throws IOException {
        Preconditions.checkNotNull(target);
        checkOpen();
        if (!hasRemaining()) {
            return -1;
        }
        int charsToRead = Math.min(target.remaining(), remaining());
        for (int i = 0; i < charsToRead; i++) {
            target.put(seq.charAt(pos++));
        }
        return charsToRead;
    }

    @Override
    public synchronized int read() throws IOException {
        checkOpen();
        return hasRemaining() ? seq.charAt(pos++) : -1;
    }

    @Override
    public synchronized int read(char[] cbuf, int off, int len) throws IOException {
        if (len < 0 || off < 0 || off + len > cbuf.length) {
            throw new IndexOutOfBoundsException();
        }
        checkOpen();
        if (!hasRemaining()) {
            return -1;
        }
        int charsToRead = Math.min(len, remaining());
        for (int i = 0; i < charsToRead; i++) {
            cbuf[off + i] = seq.charAt(pos++);
        }
        return charsToRead;
    }

    @Override
    public synchronized long skip(long n) throws IOException {
        Preconditions.checkArgument(n >= 0, "n may not be negative");
        checkOpen();
        int charsToSkip = (int) Math.min(remaining(), n); // safe because remaining is an int
        pos += charsToSkip;
        return charsToSkip;
    }

    @Override
    public synchronized boolean ready() throws IOException {
        checkOpen();
        return true;
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public synchronized void mark(int readAheadLimit) throws IOException {
        Preconditions.checkArgument(readAheadLimit >= 0, "readAheadLimit may not be negative");
        checkOpen();
        mark = pos;
    }

    @Override
    public synchronized void reset() throws IOException {
        checkOpen();
        pos = mark;
    }

    @Override
    public synchronized void close() throws IOException {
        seq = null;
    }
}
