package com.github.hrozhek.noizalyzerandromock;

import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ConnectionClient {

    private final String address;
    private final int port;
    private final String endpoint;
    private final OkHttpClient client = new OkHttpClient();

    public ConnectionClient(String address, int port, String endpoint) {
        this.address =  "192.168.1.131";//todo
        this.port = port;
        this.endpoint = endpoint;
    }

    public UUID initConnection() {
        Request request = new Request.Builder()
                .url(formatRequest("controller"))
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);//todo
        }
        System.out.println("Controller registered! " + response + " body: " + response.body());
        String uuid;
        try {
            uuid = response.body().string();
            uuid = uuid.substring(uuid.indexOf("\"") + 1, uuid.length() - 1);
            System.out.println("uuid: " + uuid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return UUID.fromString(uuid);
    }

    private String formatRequest(String path) {
        return String.format("http://%s:%d/%s/%s", address, port, endpoint, path);
    }

    public String getWsLink(UUID id) {
        String path = String.format("controller/%s/file", id);
        Request request = new Request.Builder()
                .url(formatRequest(path))
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);//todo
        }
        System.out.println("Controller registered! " + response + " body: " + response.body());
        String ws;
        try {
            ws = response.body().string();
            System.out.println("ws: " + ws);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ws;
    }
}
