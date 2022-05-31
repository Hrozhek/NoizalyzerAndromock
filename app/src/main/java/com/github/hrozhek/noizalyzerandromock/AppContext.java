package com.github.hrozhek.noizalyzerandromock;

import java.util.UUID;

public class AppContext {

    private static final AppContext INSTANCE = new AppContext();

    private String server;
    private int port;
    private String endpoint;

    private String ws;

    private ConnectionClient connectionClient;

    private Timeout readTimeout;
    private Timeout writeTimeout;

    private UUID id;

    public static AppContext getAppCon() {
        return INSTANCE;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getWs() {
        return ws;
    }

    public void setWs(String ws) {
        this.ws = ws;
    }

    public ConnectionClient getConnectionClient() {
        return connectionClient;
    }

    public void setConnectionClient(ConnectionClient connectionClient) {
        this.connectionClient = connectionClient;
    }

    public Timeout getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Timeout readTimeout) {
        this.readTimeout = readTimeout;
    }

    public Timeout getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(Timeout writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    private AppContext() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
