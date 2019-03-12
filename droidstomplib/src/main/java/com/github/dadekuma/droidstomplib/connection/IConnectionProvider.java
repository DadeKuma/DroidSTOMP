package com.github.dadekuma.droidstomplib.connection;

import com.github.dadekuma.droidstomplib.payload.HttpHeader;

import java.util.Collection;

public interface IConnectionProvider {
    void connect(String stompEndpoint, Collection<HttpHeader> headers);
    void send(String payload);
    void setStompCallback(IStompCallback stompCallback);
    boolean isConnected();
    boolean isConnecting();
}
