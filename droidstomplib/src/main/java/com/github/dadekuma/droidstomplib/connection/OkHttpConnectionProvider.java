package com.github.dadekuma.droidstomplib.connection;

import com.github.dadekuma.droidstomplib.payload.HttpHeader;

import java.util.Collection;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;


public class OkHttpConnectionProvider implements IConnectionProvider {
    private OkHttpClient okHttpClient;
    private WebSocket webSocket;
    private IStompCallback stompCallback;

    public OkHttpConnectionProvider() {
        this(new OkHttpClient());
    }

    public OkHttpConnectionProvider(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }


    @Override
    public void connect(String stompEndpoint, Collection<HttpHeader> headers) {
        Request.Builder requestBuilder = new Request.Builder().url(stompEndpoint);
        //add all http headers that you need to the request.
        for (HttpHeader h : headers) {
            requestBuilder.addHeader(h.getName(), h.getValue());
        }
        Request request = requestBuilder.build();

        webSocket = okHttpClient.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                if(stompCallback == null) return;
                stompCallback.onConnectionOpened();
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                if(stompCallback == null) return;
                stompCallback.onConnectionClosed(code, reason);
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                if(stompCallback == null) return;
                stompCallback.onResponseReceived(text);
            }
        });
    }

    @Override
    public void send(String payload) {
        webSocket.send(payload);
    }

    @Override
    public void setStompCallback(IStompCallback stompCallback) {
        this.stompCallback = stompCallback;
    }

    @Override
    public boolean isConnected() {
        return stompCallback.isConnected();
    }

    @Override
    public boolean isConnecting() {
        return stompCallback.isConnecting();
    }
}
