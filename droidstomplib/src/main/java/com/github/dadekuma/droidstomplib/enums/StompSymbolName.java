package com.github.dadekuma.droidstomplib.enums;

public enum StompSymbolName {
    SEPARATOR(":"),
    NEWLINE("\n"),
    TERMINATOR("\u0000");

    private String symbol;
    StompSymbolName(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
