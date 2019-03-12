package com.github.dadekuma.droidstomplib.payload;

import android.util.Log;

import com.github.dadekuma.droidstomplib.enums.StompCommandName;
import com.github.dadekuma.droidstomplib.enums.StompHeaderName;
import com.github.dadekuma.droidstomplib.enums.StompSymbolName;

public class StompParser {
    public static StompFrame parse(String message) {
        StompFrame stompFrame = new StompFrame();
        String[] messageRows = message.split(StompSymbolName.NEWLINE.toString());
        try {
            StompCommandName stompCommandName = StompCommandName.getEnum(messageRows[0]);
            stompFrame.addStompCommand(stompCommandName);
        }
        catch (IllegalArgumentException e){
            Log.e("STOMP_ILLEGAL_COMMAND", e.getMessage());
        }
        catch (RuntimeException e){
            e.fillInStackTrace();
            Log.e("STOMP_PARSER_EX", e.getMessage());
        }

        int bodyPosition = 1;
        for (int i = 1; i < messageRows.length; ++i) {
            if (messageRows[i].equals("")) {
                bodyPosition = i;
                break;
            }
            try {
                String[] header = messageRows[i].split(StompSymbolName.SEPARATOR.toString());
                StompHeaderName headerName = StompHeaderName.getEnum(header[0]);
                String headerValue = header[1];
                stompFrame.addStompHeader(headerName, headerValue);
            }
            catch (IllegalArgumentException e){
                Log.e("STOMP_ILLEGAL_HEADER", e.getMessage());
            }
            catch (RuntimeException e){
                e.fillInStackTrace();
                Log.e("STOMP_PARSER_EX", e.getMessage());
            }
        }
        String stompBody = parseBody(bodyPosition, messageRows);
        stompFrame.addBody(stompBody);
        return stompFrame;
    }

    private static String parseBody(int startPosition, String[] messageRows){
        StringBuilder stompBody = new StringBuilder();
        for (int i = startPosition; i < messageRows.length; i++) {
            if(messageRows[i].equals(StompSymbolName.TERMINATOR.toString())){
                break;
            }
            stompBody.append(messageRows[i]);
        }
        return stompBody.toString();
    }
}