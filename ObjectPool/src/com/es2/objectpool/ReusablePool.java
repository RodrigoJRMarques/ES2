package com.es2.objectpool;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReusablePool {
    private static final int DEFAULT_POOL_SIZE = 10;
    private static final String DEFAULT_ENDPOINT = "http://example.com";

    private static final ReusablePool INSTANCE = new ReusablePool();

    private final List<HttpURLConnection> available;
    private final List<HttpURLConnection> inUse;
    private int maxPoolSize;

    private ReusablePool() {
        this.available = new ArrayList<>();
        this.inUse = new ArrayList<>();
        this.maxPoolSize = DEFAULT_POOL_SIZE;
    }

    public static ReusablePool getInstance() {
        return INSTANCE;
    }

    public synchronized HttpURLConnection acquire() throws IOException, PoolExhaustedException {
        if (!available.isEmpty()) {
            HttpURLConnection connection = available.remove(available.size() - 1);
            inUse.add(connection);
            return connection;
        }

        if (inUse.size() >= maxPoolSize) {
            throw new PoolExhaustedException();
        }

        URL endpoint = new URL(DEFAULT_ENDPOINT);
        HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();
        inUse.add(connection);
        return connection;
    }

    public synchronized void release(HttpURLConnection conn) throws ObjectNotFoundException {
        if (conn == null || !inUse.remove(conn)) {
            throw new ObjectNotFoundException();
        }
        available.add(conn);
    }

    public synchronized void resetPool() {
        disconnectAll(inUse);
        disconnectAll(available);
        inUse.clear();
        available.clear();
    }

    public synchronized void setMaxPoolSize(int size) {
        if (size > 0) {
            this.maxPoolSize = size;
            trimAvailableConnections();
        }
    }

    private void trimAvailableConnections() {
        while ((inUse.size() + available.size()) > maxPoolSize && !available.isEmpty()) {
            HttpURLConnection connection = available.remove(available.size() - 1);
            connection.disconnect();
        }
    }

    private void disconnectAll(List<HttpURLConnection> connections) {
        for (HttpURLConnection connection : connections) {
            connection.disconnect();
        }
    }
}