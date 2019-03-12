package com.github.dadekuma.droidstomplib.enums;

public enum StompHeaderName {
    ACCEPT_VERSION("accept-version"),
    CONTENT_LENGTH("content-length"),
    CONTENT_TYPE("content-type"),
    ID("id"),
    ACK("ack"),
    HOST("host"),
    RECEIPT("receipt"),
    DESTINATION("destination"),
    SERVER("server"),
    SESSION("session"),
    SUBSCRIPTION("subscription"),
    MESSAGE_ID("message-id"),
    RECEIPT_ID("receipt-id"),
    HEART_BEAT("heart-beat"),
    MESSAGE("message"),
    VERSION("version"),
    ;

    private String header;
    StompHeaderName(String header) {
        this.header = header;
    }

    @Override
    public String toString() {
        return header;
    }

    public static StompHeaderName getEnum(String stompHeaderName) {
        for (StompHeaderName h : StompHeaderName.values()) {
            if (h.toString().equals(stompHeaderName)) return h;
        }
        throw new IllegalArgumentException("StompHeaderName not found.");
    }
}
