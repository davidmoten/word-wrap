package org.davidmoten.text.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class Benchmarks {

    private static final String text = createText();
    private static final ByteArrayOutputStream bytes = new ByteArrayOutputStream(128 * 1024);

    @Benchmark
    public int wrapNovel() {
        WordWrap.from(text) //
                .maxWidth(80) //
                .wrap(createWriter());
        return bytes.size();
    }

    private static Writer createWriter() {
        bytes.reset();
        return new OutputStreamWriter(bytes, StandardCharsets.UTF_8);
    }

    private static String createText() {
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(
                    new File("src/test/resources/treasure-island-fragment.txt").toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }
}