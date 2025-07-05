package com.example.servlet;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OrderServletTest {

    private OrderServlet orderServlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private ByteArrayOutputStream responseStream;

    @BeforeEach
    void setUp() {
        orderServlet = new OrderServlet();
        responseStream = new ByteArrayOutputStream();
    }

    @Test
    void testDoPost_CreatesOrder() throws Exception {
        String json = """
            {
              "id": 1,
              "date": "2025-07-04T12:00:00",
              "cost": 100.0,
              "products": [
                {"id": 1, "name": "Product A", "cost": 50.0},
                {"id": 2, "name": "Product B", "cost": 50.0}
              ]
            }
        """;

        InputStream input = new ByteArrayInputStream(json.getBytes());
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                try {
                    return input.available() == 0;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener listener) {}

            @Override
            public int read() throws IOException {
                return input.read();
            }
        };

        ServletOutputStream servletOutputStream = new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(jakarta.servlet.WriteListener listener) {}

            @Override
            public void write(int b) throws IOException {
                responseStream.write(b);
            }
        };

        when(request.getInputStream()).thenReturn(servletInputStream);
        when(response.getOutputStream()).thenReturn(servletOutputStream);

        orderServlet.doPost(request, response);

        String result = responseStream.toString();
        assertTrue(result.contains("\"id\":1"));
        assertTrue(result.contains("\"name\":\"Product A\""));
        assertTrue(result.contains("\"cost\":100.0"));
    }
}
