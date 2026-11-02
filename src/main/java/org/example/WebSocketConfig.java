//package org.example.websocket;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.socket.config.annotation.*;
//
//@Configuration
//@EnableWebSocket
//public class WebSocketConfig
//        implements WebSocketConfigurer {
//
//    @Override
//    public void registerWebSocketHandlers(
//            WebSocketHandlerRegistry registry
//    ) {
//
//        registry.addHandler(
//
//                        new GameWebSocketHandler(),
//
//                        "/game"
//
//                )
//
//                .setAllowedOrigins("*");
//    }
//}
//update
//package org.example.websocket;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.socket.config.annotation.*;
//
//@Configuration
//@EnableWebSocket
//public class WebSocketConfig implements WebSocketConfigurer {
//
//    // 🚀 [মেইন ফিক্স]: স্প্রিং বুটের তৈরি করা হ্যান্ডলার Bean এখানে নিয়ে আসা হচ্ছে
//    private final GameWebSocketHandler gameWebSocketHandler;
//
//    // কনস্ট্রাক্টর ইনজেকশন (এর ফলে স্প্রিং বুট নিজে হ্যান্ডলারটিকে রেপোজিটরি সহ এখানে পাস করবে)
//    public WebSocketConfig(GameWebSocketHandler gameWebSocketHandler) {
//        this.gameWebSocketHandler = gameWebSocketHandler;
//    }
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        // 🎯 [ফিক্স]: 'new' কেটে দিয়ে ওপরে তৈরি করা 'gameWebSocketHandler' ভ্যারিয়েবলটি বসানো হলো
//        registry.addHandler(
//                gameWebSocketHandler,
//                "/game"
//        ).setAllowedOrigins("*");
//    }
//}
//check
package org.example.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final GameWebSocketHandler gameWebSocketHandler;

    // কনস্ট্রাক্টর ইনজেকশন এর মাধ্যমে হ্যান্ডলারটিকে পাস করা হচ্ছে
    public WebSocketConfig(GameWebSocketHandler gameWebSocketHandler) {
        this.gameWebSocketHandler = gameWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // ক্লায়েন্টরা "ws://localhost:8080/game" ইউআরএল-এ কানেক্ট করতে পারবে
        registry.addHandler(gameWebSocketHandler, "/game").setAllowedOrigins("*");
    }
}