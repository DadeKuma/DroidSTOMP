# DroidSTOMP
[![](https://jitpack.io/v/DadeKuma/DroidSTOMP.svg)](https://jitpack.io/#DadeKuma/DroidSTOMP)
[![Build Status](https://travis-ci.com/DadeKuma/DroidSTOMP.svg?branch=master)](https://travis-ci.com/DadeKuma/DroidSTOMP)

An Android library that implements the messaging protocol [STOMP](https://stomp.github.io/) that can be used over websockets.

<b>Here's a short demo of the library used to build a simple chat!</b>
<p align="center">
  <img width="460" height="300" src="https://media.giphy.com/media/YVBxHgqj27rw8T0WGk/giphy.gif">
</p>

## How to install

### Gradle
Add jitpack repository and DroidSTOMP dependency to your build.gradle
```
repositories {
    ...
    maven { url "https://jitpack.io" }
}
dependencies {
    ...
    implementation 'com.github.DadeKuma:DroidSTOMP:0.4.1'
}
```
<b>Done!</b>

## How to use
#### 1. Add internet permission to your Android Manifest.
```
<uses-permission android:name="android.permission.INTERNET"/>
```
#### 2. Create a new StompClient and set a callback. 
```Java
stompClient = new StompClient();
stompClient.setStompCallback(new StompCallback() {
            @Override
            public void onMessageReceived(String topic, Long subscriptionId, String message) {
                //in this example we just log the message received...
                Log.i("StompExample", "message recevied: " + message + " on topic: " + topic)
            }
        });
```
#### 3. Connect to your server websocket endpoint. After you connect you can subscribe to server topics!
```Java
String serverEndpoint = "ws://10.0.2.2:8080/your_server_endpoint";
String topic = "/app/example";
stompClient.connect(serverEndpoint);
stompClient.subscribe(topic);
```
#### 4. If you want to send a message you can do it in this way.
```Java
String topic = "/app/example";
String message = "hello world!";
stompClient.send(topic, message);
```
If you need more examples you can check the [sample application.](https://github.com/DadeKuma/DroidSTOMP/blob/master/app/src/main/java/com/github/dadekuma/droidstomp/MainActivity.java)

### IMPORTANT NOTE:

Since Android 9.0 (API level 28) cleartext support is disabled by default. So your app will not send cleartext http requests, if you don't enable it. 

If you don't know how to solve this problem I suggest to look at [this](https://stackoverflow.com/a/50834600/8128154) post on StackOverflow.


### Backend
If you are using Spring Boot as your backend your classes should look like this:
#### WebSocketConfig.java
```Java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/your_server_endpoint");
    }
}
```

#### HelloWorldController.java
```Java
@RestController
public class HelloWorldController {

    @MessageMapping("/example")
    @SendTo("/topic/exampleTopic")
    public String processMessageFromClient(String message){
        return message;
    }
}
```
