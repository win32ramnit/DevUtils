package org.manish.utilix.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Provides small IO helpers for reading streams and copying data.
 */
public final class IOUtils {
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    private IOUtils() {
    }

    /**
     * Reads all bytes from an input stream without closing it.
     *
     * @param input the source stream
     * @return a byte array containing the full stream contents
     * @throws NullPointerException if {@code input} is {@code null}
     * @throws IOException if reading from the stream fails
     */
    public static byte[] toByteArray(InputStream input) throws IOException {
        Objects.requireNonNull(input, "input");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    /**
     * Reads an input stream into a string without closing the stream.
     *
     * @param input the source stream
     * @param charset the charset to use; defaults to UTF-8 when {@code null}
     * @return the decoded string value
     * @throws NullPointerException if {@code input} is {@code null}
     * @throws IOException if reading from the stream fails
     */
    public static String toString(InputStream input, Charset charset) throws IOException {
        Charset useCharset = charset == null ? StandardCharsets.UTF_8 : charset;
        return new String(toByteArray(input), useCharset);
    }

    /**
     * Copies all bytes from an input stream to an output stream without closing either stream.
     *
     * @param input the source stream
     * @param output the destination stream
     * @return the number of bytes copied
     * @throws NullPointerException if {@code input} or {@code output} is {@code null}
     * @throws IOException if reading or writing fails
     */
    public static long copy(InputStream input, OutputStream output) throws IOException {
        Objects.requireNonNull(input, "input");
        Objects.requireNonNull(output, "output");
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long total = 0L;
        int read;
        while ((read = input.read(buffer)) != -1) {
            output.write(buffer, 0, read);
            total += read;
        }
        return total;
    }
}
