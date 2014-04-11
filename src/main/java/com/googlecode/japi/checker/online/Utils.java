package com.googlecode.japi.checker.online;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public final class Utils {
    private Utils() {
    }
    
    public static String readAsString(InputStream is) throws IOException {
        StringWriter writer = new StringWriter();
        int c;
        while ((c = is.read()) != -1) {
            writer.write(c);
        }
        return writer.toString();
    }
}
