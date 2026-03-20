package org.manish.utilix;

import org.manish.utilix.numbers.NumberUtils;
import org.manish.utilix.text.StringUtils;
import org.manish.utilix.time.DateTimeUtils;

/**
 * Simple entry point that demonstrates a few representative DevUtils operations.
 */
public final class Main {
    private Main() {
    }

    /**
     * Runs a small console demonstration of the library.
     *
     * @param args command-line arguments; not used
     */
    public static void main(String[] args) {
        System.out.println("DevUtils sample");
        System.out.println("slug = " + StringUtils.toKebabCase("Hello World"));
        System.out.println("rounded = " + NumberUtils.round(10.345, 2));
        System.out.println("timestamp = " + DateTimeUtils.formatInstant(DateTimeUtils.nowUtc()));
    }
}
