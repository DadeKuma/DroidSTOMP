package com.github.dadekuma.droidstomplib.enums;

public enum StompCommandName {
    CONNECT("CONNECT"),
    CONNECTED("CONNECTED"),
    SEND("SEND"),
    SUBSCRIBE("SUBSCRIBE"),
    UNSUBSCRIBE("UNSUBSCRIBE"),
    BEGIN("BEGIN"),
    COMMIT("COMMIT"),
    ABORT("ABORT"),
    ACK("ACK"),
    NACK("NACK"),
    DISCONNECT("DISCONNECT"),
    MESSAGE("MESSAGE"),
    RECEIPT("RECEIPT"),
    ERROR("ERROR")
    ;

    String command;
    StompCommandName(String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return command;
    }

    public static StompCommandName getEnum(String stompCommandName) {
        for (StompCommandName h : StompCommandName.values()) {
            if (h.toString().equals(stompCommandName)) return h;
        }
        throw new IllegalArgumentException("StompCommandName not found.");
    }
}
