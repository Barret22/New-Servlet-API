package com.example.servlet;

import jakarta.servlet.ServletInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DelegatingServletInputStream extends ServletInputStream {
    private final InputStream sourceStream;

    public DelegatingServletInputStream(InputStream sourceStream) {
        this.sourceStream = sourceStream;
    }

    @Override
    public int read() throws IOException {
        return sourceStream.read();
    }

    @Override
    public boolean isFinished() {
        try {
            return sourceStream.available() == 0;
        } catch (IOException e) {
            return true;
        }
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(jakarta.servlet.ReadListener readListener) {
    }
}
