package com.github.dadekuma.droidstomplib;

import com.github.dadekuma.droidstomplib.enums.StompCommandName;
import com.github.dadekuma.droidstomplib.enums.StompHeaderName;
import com.github.dadekuma.droidstomplib.payload.StompFrame;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StompFrameTest {
    private StompFrame stompFrame;

    @Before
    public void initStompFrame(){
        stompFrame = new StompFrame();
    }

    @Test
    public void buildTest(){
        stompFrame.addStompCommand(StompCommandName.CONNECT);
        stompFrame.addStompHeader(StompHeaderName.ID, "1");
        String result = stompFrame.build();
        String expected =   "CONNECT\n" +
                            "id:1\n" +
                            "\n" +
                            "\u0000";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void buildMultipleHeadersTest(){
        stompFrame.addStompCommand(StompCommandName.SEND);
        stompFrame.addStompHeader(StompHeaderName.ID, "10")
                  .addStompHeader(StompHeaderName.DESTINATION, "/topic/example")
                  .addStompHeader(StompHeaderName.CONTENT_TYPE, "text-plain");
        String result = stompFrame.build();
        String expected =   "SEND\n" +
                            "id:10\n" +
                            "destination:/topic/example\n" +
                            "content-type:text-plain\n" +
                            "\n" +
                            "\u0000";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void buildWithBody(){
        stompFrame.addStompCommand(StompCommandName.MESSAGE);
        stompFrame.addStompHeader(StompHeaderName.SUBSCRIPTION, "10")
                .addStompHeader(StompHeaderName.MESSAGE_ID,"004")
                .addStompHeader(StompHeaderName.DESTINATION, "/topic/example")
                .addStompHeader(StompHeaderName.CONTENT_TYPE, "text-plain")
                .addBody("Hello darkness my old friend");
        String result = stompFrame.build();
        String expected =   "MESSAGE\n" +
                            "subscription:10\n" +
                            "message-id:004\n" +
                            "destination:/topic/example\n" +
                            "content-type:text-plain\n" +
                            "\n" +
                            "Hello darkness my old friend\n" +
                            "\u0000";
        Assert.assertEquals(expected, result);
    }
}