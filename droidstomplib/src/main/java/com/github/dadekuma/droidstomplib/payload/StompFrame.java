package com.github.dadekuma.droidstomplib.payload;

import android.support.annotation.NonNull;

import com.github.dadekuma.droidstomplib.enums.StompCommandName;
import com.github.dadekuma.droidstomplib.enums.StompHeaderName;
import com.github.dadekuma.droidstomplib.enums.StompSymbolName;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class StompFrame {
    private Map<StompHeaderName, String> stompHeaders;
    private StompCommandName stompCommand;
    private String stompBody;

    public StompFrame() {
        stompHeaders = new LinkedHashMap<>();
    }

    public StompFrame addStompCommand(@NonNull StompCommandName command){
        this.stompCommand = command;
        return this;
    }

    public StompFrame addStompHeader(@NonNull StompHeaderName header, @NonNull String value){
        stompHeaders.put(header, value);
        return this;
    }

    public StompFrame addBody(Serializable body){
        stompBody = body.toString();
        return this;
    }

    public String build(){
        String newline = StompSymbolName.NEWLINE.toString();
        String separator = StompSymbolName.SEPARATOR.toString();
        String terminator = StompSymbolName.TERMINATOR.toString();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(stompCommand).append(newline);

        for(StompHeaderName headerName : stompHeaders.keySet()){
            String name = headerName.toString();
            String value = stompHeaders.get(headerName);
            stringBuilder.append(name).append(separator).append(value).append(newline);
        }
        stringBuilder.append(newline);
        if(stompBody != null){
            stringBuilder.append(stompBody).append(newline);
        }
        stringBuilder.append(terminator);
        return stringBuilder.toString();
    }

    public Map<StompHeaderName, String> getStompHeaders() {
        return stompHeaders;
    }

    public StompCommandName getStompCommand() {
        return stompCommand;
    }

    public String getStompBody() {
        return stompBody;
    }
}
