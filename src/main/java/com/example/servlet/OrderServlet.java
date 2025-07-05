package com.example.servlet;

import com.example.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class OrderServlet extends HttpServlet {

    private final Map<Integer, Order> orders = new ConcurrentHashMap<>();
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Order order = mapper.readValue(req.getInputStream(), Order.class);
        if (orders.containsKey(order.getId())) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write("Order with id " + order.getId() + " already exists");
            return;
        }
        orders.put(order.getId(), order);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setContentType("application/json");
        mapper.writeValue(resp.getOutputStream(), order);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Missing id parameter");
            return;
        }
        int id = Integer.parseInt(idParam);
        Order order = orders.get(id);
        if (order == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("Order not found");
            return;
        }
        resp.setContentType("application/json");
        mapper.writeValue(resp.getOutputStream(), order);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Order updatedOrder = mapper.readValue(req.getInputStream(), Order.class);
        if (!orders.containsKey(updatedOrder.getId())) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("Order not found");
            return;
        }
        orders.put(updatedOrder.getId(), updatedOrder);
        resp.setContentType("application/json");
        mapper.writeValue(resp.getOutputStream(), updatedOrder);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Missing id parameter");
            return;
        }
        int id = Integer.parseInt(idParam);
        Order removed = orders.remove(id);
        if (removed == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("Order not found");
            return;
        }
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}

