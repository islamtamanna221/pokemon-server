//package org.example.websocket;
//
//import org.springframework.web.socket.*;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.util.*;
//
//public class GameWebSocketHandler
//        extends TextWebSocketHandler {
//
//    private static final Set<WebSocketSession>
//            sessions =
//            Collections.synchronizedSet(
//                    new HashSet<>()
//            );
//
//    @Override
//    public void afterConnectionEstablished(
//            WebSocketSession session
//    ) {
//
//        sessions.add(session);
//
//        System.out.println(
//                "PLAYER CONNECTED"
//        );
//    }
//
//    @Override
//    protected void handleTextMessage(
//            WebSocketSession session,
//            TextMessage message
//    ) throws Exception {
//
//        for(
//                WebSocketSession s :
//                sessions
//        ) {
//
//            if(s.isOpen()) {
//
//                s.sendMessage(
//                        message
//                );
//            }
//        }
//    }
//}
//update
//package org.example.websocket; // আপনার প্রজেক্টের আসল প্যাকেজ নাম দিন
//
//import org.example.repository.PlayerRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//import java.net.URI;
//
//@Component
//public class GameWebSocketHandler extends TextWebSocketHandler {
//
//    @Autowired
//    private PlayerRepository playerRepository; // 👈 আপনার রিপোজিটরি ইনজেক্ট করা নিশ্চিত করুন
//
//    /**
//     * 🟢 প্লেয়ার যখন LibGDX গেম থেকে সকেটে সফলভাবে কানেক্ট হবে
//     */
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        try {
//            URI uri = session.getUri();
//            if (uri != null && uri.getQuery() != null) {
//                String query = uri.getQuery(); // হ্যান্ডেল করবে: "playerId=X"
//                String[] pairs = query.split("&");
//                for (String pair : pairs) {
//                    String[] idx = pair.split("=");
//                    if (idx[0].equals("playerId") && idx.length > 1) {
//                        Long playerId = Long.parseLong(idx[1]);
//
//                        // 🎯 ডাটাবেজে স্ট্যাটাস ১ (অনলাইন) করার জন্য কুয়েরি ফায়ার করা হলো
//                        playerRepository.updateOnlineStatus(playerId, 1);
//
//                        System.out.println("⚡ [SUCCESS] Player ID " + playerId + " is now ONLINE in database!");
//
//                        // সেশনের ভেতর আইডি সেভ রাখা হলো যাতে ডিসকানেক্টের সময় কাজে লাগে
//                        session.getAttributes().put("playerId", playerId);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            System.err.println("❌ Failed to update online status on connect:");
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 🔴 প্লেয়ার যখন গেম ক্লোজ করবে বা লগআউট বাটনে ক্লিক করবে
//     */
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        try {
//            // সেশন থেকে কানেক্টেড প্লেয়ারের আইডি নেওয়া হচ্ছে
//            Long playerId = (Long) session.getAttributes().get("playerId");
//
//            if (playerId != null) {
//                // 🎯 ডাটাবেজে স্ট্যাটাস ০ (অফলাইন) করার জন্য কুয়েরি ফায়ার করা হলো
//                playerRepository.updateOnlineStatus(playerId, 0);
//                System.out.println("⚡ [SUCCESS] Player ID " + playerId + " is now OFFLINE in database!");
//            }
//        } catch (Exception e) {
//            System.err.println("❌ Failed to update online status on disconnect:");
//            e.printStackTrace();
//        }
//    }
//}
//cc
//package org.example.websocket;
//
//import org.example.repository.PlayerRepository;
//import org.json.JSONObject; // 👈 জেসন পার্স করার জন্য এটি লাগবে
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage; // 👈 নতুন যুক্ত হলো
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//import java.net.URI;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Component
//public class GameWebSocketHandler extends TextWebSocketHandler {
//
//    @Autowired
//    private PlayerRepository playerRepository;
//
//    // 📡 অনলাইন প্লেয়ারদের সকেট সেশন ট্র্যাক রাখার জন্য ম্যাপ (মেসেজ ফরওয়ার্ড করতে এটি মাস্ট লাগবে)
//    private static final Map<Long, WebSocketSession> playerSessions = new ConcurrentHashMap<>();
//
//    /**
//     * 🟢 প্লেয়ার যখন LibGDX গেম থেকে সকেটে সফলভাবে কানেক্ট হবে
//     */
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        try {
//            URI uri = session.getUri();
//            if (uri != null && uri.getQuery() != null) {
//                String query = uri.getQuery();
//                String[] pairs = query.split("&");
//                for (String pair : pairs) {
//                    String[] idx = pair.split("=");
//                    if (idx[0].equals("playerId") && idx.length > 1) {
//                        Long playerId = Long.parseLong(idx[1]);
//
//                        // ১. ডাটাবেজে স্ট্যাটাস ১ (অনলাইন) করা
//                        playerRepository.updateOnlineStatus(playerId, 1);
//                        System.out.println("⚡ [SUCCESS] Player ID " + playerId + " is now ONLINE in database!");
//
//                        // ২. 🎯 [নতুন অ্যাডিশন]: সেশন ম্যাপে প্লেয়ার আইডি এবং সেশন স্টোর করা হচ্ছে
//                        playerSessions.put(playerId, session);
//                        session.getAttributes().put("playerId", playerId);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            System.err.println("❌ Failed to update online status on connect:");
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * ⚔️ 🎯 [সবচেয়ে গুরুত্বপূর্ণ মিসিং মেথড]: ফ্রন্টএন্ড থেকে আসা মেসেজ রিসিভ এবং ফরওয়ার্ড করার জন্য
//     */
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//        System.out.println("📥 Server Received Message: " + payload);
//
//        // যদি মেসেজটি JSON ফরম্যাটে হয় (ব্যাটেল রিকোয়েস্ট, এক্সেপ্ট, বা ডিক্লাইন)
//        if (payload.startsWith("{") && payload.endsWith("}")) {
//            try {
//                JSONObject json = new JSONObject(payload);
//                String action = json.optString("action", "");
//
//                // কেস ১: ব্যাটেল রিকোয়েস্ট বা ডিক্লাইন আসলে সরাসরি রিসিভারকে ফরওয়ার্ড করো
//                if ("send_battle_request".equals(action) || "decline_battle_request".equals(action)) {
//                    Long receiverId = json.getLong("receiverId");
//                    WebSocketSession receiverSession = playerSessions.get(receiverId);
//
//                    if (receiverSession != null && receiverSession.isOpen()) {
//                        receiverSession.sendMessage(new TextMessage(payload));
//                        System.out.println("🚀 [FORWARDED] Action '" + action + "' sent to Player ID: " + receiverId);
//                    } else {
//                        System.out.println("❌ [FAILED] Player ID " + receiverId + " is not connected to socket!");
//                    }
//                }
//                // কেস ২: অপোনেন্ট যদি চ্যালেঞ্জ ACCEPT করে, তবে দুজনকে ম্যাচ স্টার্টের কমান্ড (start_battle) পাঠাও
//                else if ("accept_battle_request".equals(action)) {
//                    Long senderId = json.getLong("senderId");
//                    Long receiverId = json.getLong("receiverId");
//
//                    // নতুন অ্যাকশন তৈরি করা হলো দুজনের স্ক্রিন পরিবর্তন করার জন্য
//                    JSONObject startBattleJson = new JSONObject(payload);
//                    startBattleJson.put("action", "start_battle");
//                    TextMessage startMessage = new TextMessage(startBattleJson.toString());
//
//                    // চ্যালেঞ্জারকে পাঠানো হলো
//                    WebSocketSession senderSession = playerSessions.get(senderId);
//                    if (senderSession != null && senderSession.isOpen()) senderSession.sendMessage(startMessage);
//
//                    // রিসিভারকে (যে এক্সেপ্ট করেছে) পাঠানো হলো
//                    WebSocketSession receiverSession = playerSessions.get(receiverId);
//                    if (receiverSession != null && receiverSession.isOpen()) receiverSession.sendMessage(startMessage);
//
//                    System.out.println("⚔️ MATCH STARTED between Player " + senderId + " and Player " + receiverId);
//                }
//
//            } catch (Exception e) {
//                System.err.println("❌ Server JSON parsing error: " + e.getMessage());
//            }
//        }
//        // কেস ৩: পুরনো ব্যাটেল সিস্টেমের জন্য THROW মেকানিজম (যদি লাগে)
//        else if (payload.startsWith("THROW")) {
//            Long currentSenderId = (Long) session.getAttributes().get("playerId");
//            for (Map.Entry<Long, WebSocketSession> entry : playerSessions.entrySet()) {
//                if (!entry.getKey().equals(currentSenderId) && entry.getValue().isOpen()) {
//                    entry.getValue().sendMessage(new TextMessage(payload));
//                }
//            }
//        }
//    }
//
//    /**
//     * 🔴 প্লেয়ার যখন গেম ক্লোজ করবে বা লগআউট বাটনে ক্লিক করবে
//     */
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        try {
//            Long playerId = (Long) session.getAttributes().get("playerId");
//
//            if (playerId != null) {
//                // ১. ডাটাবেজে স্ট্যাটাস ০ (অফলাইন) করা
//                playerRepository.updateOnlineStatus(playerId, 0);
//                System.out.println("⚡ [SUCCESS] Player ID " + playerId + " is now OFFLINE in database!");
//
//                // ২. 🎯 [নতুন অ্যাডিশন]: সেশন ম্যাপ থেকে প্লেয়ারকে রিমুভ করা
//                playerSessions.remove(playerId);
//            }
//        } catch (Exception e) {
//            System.err.println("❌ Failed to update online status on disconnect:");
//            e.printStackTrace();
//        }
//    }
//}
//n
//package org.example.websocket;
//
//import org.example.repository.PlayerRepository;
//import org.example.service.BattleService; // 👈 নতুন সার্ভিস ইম্পোর্ট করা হলো
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//import java.net.URI;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Component
//public class GameWebSocketHandler extends TextWebSocketHandler {
//
//    @Autowired
//    private PlayerRepository playerRepository;
//
//    @Autowired
//    private BattleService battleService; // 👈 নতুন সার্ভিস ইনজেক্ট করা হলো
//
//    // 📡 অনলাইন প্লেয়ারদের সকেট সেশন ট্র্যাক রাখার জন্য ম্যাপ
//    private static final Map<Long, WebSocketSession> playerSessions = new ConcurrentHashMap<>();
//
//    /**
//     * 🟢 প্লেয়ার যখন LibGDX গেম থেকে সকেটে সফলভাবে কানেক্ট হবে
//     */
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        try {
//            URI uri = session.getUri();
//            if (uri != null && uri.getQuery() != null) {
//                String query = uri.getQuery();
//                String[] pairs = query.split("&");
//                for (String pair : pairs) {
//                    String[] idx = pair.split("=");
//                    if (idx[0].equals("playerId") && idx.length > 1) {
//                        Long playerId = Long.parseLong(idx[1]);
//
//                        // ১. ডাটাবেজে স্ট্যাটাস ১ (অনলাইন) করা
//                        playerRepository.updateOnlineStatus(playerId, 1);
//                        System.out.println("⚡ [SUCCESS] Player ID " + playerId + " is now ONLINE in database!");
//
//                        // ২. সেশন ম্যাপে প্লেয়ার আইডি এবং সেশন স্টোর করা হচ্ছে
//                        playerSessions.put(playerId, session);
//                        session.getAttributes().put("playerId", playerId);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            System.err.println("❌ Failed to update online status on connect:");
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * ⚔️ 🎯 [আপডেটেড মেথড]: ফ্রন্টএন্ড থেকে আসা মেসেজ রিসিভ, প্রোসেস এবং রিয়েল-টাইম ফরওয়ার্ড করার জন্য
//     */
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//        System.out.println("📥 Server Received Message: " + payload);
//
//        // যদি মেসেজটি JSON ফরম্যাটে হয়
//        if (payload.startsWith("{") && payload.endsWith("}")) {
//            try {
//                JSONObject json = new JSONObject(payload);
//                String action = json.optString("action", "");
//
//                // কেস ১: ব্যাটেল রিকোয়েস্ট বা ডিক্লাইন আসলে সরাসরি রিসিভারকে ফরওয়ার্ড করো
//                if ("send_battle_request".equals(action) || "decline_battle_request".equals(action)) {
//                    Long receiverId = json.getLong("receiverId");
//                    WebSocketSession receiverSession = playerSessions.get(receiverId);
//
//                    if (receiverSession != null && receiverSession.isOpen()) {
//                        receiverSession.sendMessage(new TextMessage(payload));
//                        System.out.println("🚀 [FORWARDED] Action '" + action + "' sent to Player ID: " + receiverId);
//                    } else {
//                        System.out.println("❌ [FAILED] Player ID " + receiverId + " is not connected to socket!");
//                    }
//                }
//                // কেস ২: অপোনেন্ট যদি চ্যালেঞ্জ ACCEPT করে, তবে দুজনকে ম্যাচ স্টার্টের কমান্ড (start_battle) পাঠাও
//                else if ("accept_battle_request".equals(action)) {
//                    Long senderId = json.getLong("senderId");
//                    Long receiverId = json.getLong("receiverId");
//
//                    JSONObject startBattleJson = new JSONObject(payload);
//                    startBattleJson.put("action", "start_battle");
//
//                    // 🎯 ডাইনামিক রুম আইডি জেনারেট করে যুক্ত করা হলো যাতে ডাটাবেজে ট্র্যাক থাকে
//                    long generatedRoomId = System.currentTimeMillis();
//                    startBattleJson.put("roomId", generatedRoomId);
//
//                    TextMessage startMessage = new TextMessage(startBattleJson.toString());
//
//                    // চ্যালেঞ্জারকে পাঠানো হলো
//                    WebSocketSession senderSession = playerSessions.get(senderId);
//                    if (senderSession != null && senderSession.isOpen()) senderSession.sendMessage(startMessage);
//
//                    // রিসিভারকে (যে এক্সেপ্ট করেছে) পাঠানো হলো
//                    WebSocketSession receiverSession = playerSessions.get(receiverId);
//                    if (receiverSession != null && receiverSession.isOpen()) receiverSession.sendMessage(startMessage);
//
//                    System.out.println("⚔️ MATCH STARTED between Player " + senderId + " and Player " + receiverId + " in Room: " + generatedRoomId);
//                }
//
//                // 🔄 কেস ৩: [নতুন অ্যাকশন]: এক প্লেয়ার পোকেমন সিলেক্ট বা লক করলে অন্য প্লেয়ারের স্ক্রিনে দেখানোর জন্য
//                else if ("lock_pokemon".equals(action)) {
//                    Long receiverId = json.getLong("receiverId"); // বিপক্ষ প্লেয়ার আইডি
//                    WebSocketSession receiverSession = playerSessions.get(receiverId);
//
//                    if (receiverSession != null && receiverSession.isOpen()) {
//                        receiverSession.sendMessage(new TextMessage(payload)); // অপোনেন্টের স্ক্রিনে পোকেমন ভিজিবল করার জন্য ডেটা পাঠানো হলো
//                        System.out.println("📢 Shared Pokemon Selection with Opponent Player ID: " + receiverId);
//                    }
//                }
//
//                // 🔄 কেস ৪: [নতুন অ্যাকশন]: প্লেয়ার যখন কোনো মুভ সিলেক্ট করে ENTER চাপবে (ATTACK করবে)
//                else if ("execute_attack".equals(action)) {
//                    Long roomId = json.getLong("roomId");
//                    Long attackerId = json.getLong("attackerId");
//                    Long defenderId = json.getLong("defenderId");
//                    Long defenderPokemonId = json.getLong("defenderPokemonId");
//                    String moveName = json.getString("moveName");
//                    int damage = json.getInt("damage");
//
//                    // ⚔️ ১. সার্ভিস রান করে ডাটাবেজের HP আপডেট করা হলো এবং নতুন HP নেওয়া হলো
//                    int updatedDefenderHp = battleService.executeAttack(roomId, attackerId, defenderId, defenderPokemonId, moveName, damage);
//
//                    // ২. দুই স্ক্রিনেই লাইভ এনিমেশন ও ড্যামেজ টেক্সট শো করার জন্য রেসপন্স জেসন রেডি করা
//                    JSONObject attackResult = new JSONObject();
//                    attackResult.put("action", "attack_result");
//                    attackResult.put("attackerId", attackerId);
//                    attackResult.put("defenderId", defenderId);
//                    attackResult.put("moveName", moveName);
//                    attackResult.put("damage", damage);
//                    attackResult.put("newHp", updatedDefenderHp);
//
//                    // 🩸 ৩. HP শূন্য হলে ম্যাচ শেষ (isGameOver = true) ঘোষণা করা এবং উইনার সেট করা
//                    if (updatedDefenderHp <= 0) {
//                        attackResult.put("isGameOver", true);
//                        attackResult.put("winnerId", attackerId);
//                    } else {
//                        attackResult.put("isGameOver", false);
//                    }
//
//                    TextMessage resultMessage = new TextMessage(attackResult.toString());
//
//                    // ৪. অ্যাটাককারী প্লেয়ারের স্ক্রিনে রেজাল্ট ব্রডকাস্ট করা হলো
//                    WebSocketSession attackerSession = playerSessions.get(attackerId);
//                    if (attackerSession != null && attackerSession.isOpen()) attackerSession.sendMessage(resultMessage);
//
//                    // ৫. ডিফেন্ডার (যে ড্যামেজ খেয়েছে) তার স্ক্রিনে রেজাল্ট ব্রডকাস্ট করা হলো
//                    WebSocketSession defenderSession = playerSessions.get(defenderId);
//                    if (defenderSession != null && defenderSession.isOpen()) defenderSession.sendMessage(resultMessage);
//                }
//
//            } catch (Exception e) {
//                System.err.println("❌ Server JSON parsing error: " + e.getMessage());
//            }
//        }
//        // কেস ৫: পুরনো ব্যাটেল সিস্টেমের জন্য THROW মেকানিজম (আপনার কোড অনুযায়ী নিরাপদ রাখা হলো)
//        else if (payload.startsWith("THROW")) {
//            Long currentSenderId = (Long) session.getAttributes().get("playerId");
//            for (Map.Entry<Long, WebSocketSession> entry : playerSessions.entrySet()) {
//                if (!entry.getKey().equals(currentSenderId) && entry.getValue().isOpen()) {
//                    entry.getValue().sendMessage(new TextMessage(payload));
//                }
//            }
//        }
//    }
//
//    /**
//     * 🔴 প্লেয়ার যখন গেম ক্লোজ করবে বা লগআউট বাটনে ক্লিক করবে
//     */
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        try {
//            Long playerId = (Long) session.getAttributes().get("playerId");
//
//            if (playerId != null) {
//                // ১. ডাটাবেজে স্ট্যাটাস ০ (অফলাইন) করা
//                playerRepository.updateOnlineStatus(playerId, 0);
//                System.out.println("⚡ [SUCCESS] Player ID " + playerId + " is now OFFLINE in database!");
//
//                // ২. সেশন ম্যাপ থেকে প্লেয়ারকে রিমুভ করা
//                playerSessions.remove(playerId);
//            }
//        } catch (Exception e) {
//            System.err.println("❌ Failed to update online status on disconnect:");
//            e.printStackTrace();
//        }
//    }
//}
//package org.example.websocket;
//
//import org.example.repository.PlayerRepository;
//import org.example.service.BattleService;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//import java.net.URI;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Component
//public class GameWebSocketHandler extends TextWebSocketHandler {
//
//    @Autowired
//    private PlayerRepository playerRepository;
//
//    @Autowired
//    private BattleService battleService;
//
//    // 📡 অনলাইন প্লেয়ারদের সকেট সেশন ট্র্যাক রাখার জন্য কনকারেন্ট ম্যাপ
//    private static final Map<Long, WebSocketSession> playerSessions = new ConcurrentHashMap<>();
//
//    /**
//     * 🟢 প্লেয়ার যখন LibGDX গেম থেকে সকেটে সফলভাবে কানেক্ট হবে
//     */
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        try {
//            URI uri = session.getUri();
//            if (uri != null && uri.getQuery() != null) {
//                String query = uri.getQuery();
//                String[] pairs = query.split("&");
//                for (String pair : pairs) {
//                    String[] idx = pair.split("=");
//                    if (idx[0].equals("playerId") && idx.length > 1) {
//                        Long playerId = Long.parseLong(idx[1]);
//
//                        // ১. ডাটাবেজে স্ট্যাটাস ১ (অনলাইন) করা
//                        playerRepository.updateOnlineStatus(playerId, 1);
//                        System.out.println("⚡ [SUCCESS] Player ID " + playerId + " is now ONLINE in database!");
//
//                        // ২. সেশন ম্যাপে প্লেয়ার আইডি এবং সেশন স্টোর করা হচ্ছে
//                        playerSessions.put(playerId, session);
//                        session.getAttributes().put("playerId", playerId);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            System.err.println("❌ Failed to update online status on connect:");
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * ⚔️ 🎯 [সম্পূর্ণ প্রোফেশনাল মেথড]: ফ্রন্টএন্ড থেকে আসা মেসেজ রিসিভ, প্রোসেস এবং রিয়েল-টাইম ফরওয়ার্ড করার জন্য
//     */
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//        System.out.println("📥 Server Received Message: " + payload);
//
//        // যদি মেসেজটি JSON ফরম্যাটে হয়
//        if (payload.startsWith("{") && payload.endsWith("}")) {
//            try {
//                JSONObject json = new JSONObject(payload);
//                String action = json.optString("action", "");
//
//                // কেস ১: ব্যাটেল রিকোয়েস্ট বা ডিক্লাইন আসলে সরাসরি রিসিভারকে ফরওয়ার্ড করো
//                if ("send_battle_request".equals(action) || "decline_battle_request".equals(action)) {
//                    Long receiverId = json.getLong("receiverId");
//                    WebSocketSession receiverSession = playerSessions.get(receiverId);
//
//                    if (receiverSession != null && receiverSession.isOpen()) {
//                        receiverSession.sendMessage(new TextMessage(payload));
//                        System.out.println("🚀 [FORWARDED] Action '" + action + "' sent to Player ID: " + receiverId);
//                    } else {
//                        System.out.println("❌ [FAILED] Player ID " + receiverId + " is not connected to socket!");
//                    }
//                }
//                // কেস ২: অপোনেন্ট যদি চ্যালেঞ্জ ACCEPT করে, তবে ডাটাবেজে রুম ক্রিয়েট করে দুজনকে ম্যাচ স্টার্টের কমান্ড পাঠাও
//                else if ("accept_battle_request".equals(action)) {
//                    Long senderId = json.getLong("senderId");
//                    Long receiverId = json.getLong("receiverId");
//
//                    JSONObject startBattleJson = new JSONObject(payload);
//                    startBattleJson.put("action", "start_battle");
//
//                    // 🎯 [সংশোধন]: র‍্যান্ডম টাইমের বদলে সার্ভিস কল করে ডাটাবেজের আসল অটো-ইনক্রিমেন্ট রুম আইডি বসানো হলো
//                    long dbRoomId = battleService.startNewBattleRoom(senderId, receiverId);
//                    startBattleJson.put("roomId", dbRoomId);
//
//                    TextMessage startMessage = new TextMessage(startBattleJson.toString());
//
//                    // চ্যালেঞ্জারকে (ম্যাচ মেকার) পাঠানো হলো
//                    WebSocketSession senderSession = playerSessions.get(senderId);
//                    if (senderSession != null && senderSession.isOpen()) senderSession.sendMessage(startMessage);
//
//                    // রিসিভারকে (যে এক্সেপ্ট করেছে) পাঠানো হলো
//                    WebSocketSession receiverSession = playerSessions.get(receiverId);
//                    if (receiverSession != null && receiverSession.isOpen()) receiverSession.sendMessage(startMessage);
//
//                    System.out.println("⚔️ MATCH STARTED between Player " + senderId + " and Player " + receiverId + " inside Official DB Room ID: " + dbRoomId);
//                }
//
//                // 🔄 কেস ৩: এক প্লেয়ার পোকেমন সিলেক্ট বা লক করলে অন্য প্লেয়ারের স্ক্রিনে দেখানোর জন্য
//                else if ("lock_pokemon".equals(action)) {
//                    Long receiverId = json.getLong("receiverId");
//                    WebSocketSession receiverSession = playerSessions.get(receiverId);
//
//                    if (receiverSession != null && receiverSession.isOpen()) {
//                        receiverSession.sendMessage(new TextMessage(payload));
//                        System.out.println("📢 Shared Pokemon Selection with Opponent Player ID: " + receiverId);
//                    }
//                }
//
//                // 🔄 কেস ৪: প্লেয়ার যখন গেমের স্ক্রিনে SPACE চেপে কোনো মুভ বা অ্যাটাক এক্সিকিউট করবে
//                else if ("execute_attack".equals(action)) {
//                    Long roomId = json.getLong("roomId");
//                    Long attackerId = json.getLong("attackerId");
//                    Long defenderId = json.getLong("defenderId");
//                    Long defenderPokemonId = json.getLong("defenderPokemonId");
//                    String moveName = json.getString("moveName");
//                    int damage = json.getInt("damage");
//
//                    // ⚔️ ১. সার্ভিস রান করে ডাটাবেজের HP আপডেট করা হলো এবং লাইভ অবশিষ্ট HP নেওয়া হলো
//                    int updatedDefenderHp = battleService.executeAttack(roomId, attackerId, defenderId, defenderPokemonId, moveName, damage);
//
//                    // ২. দুই স্ক্রিনেই লাইভ অ্যানিমেশন ও ড্যামেজ টেক্সট শো করার জন্য রেসপন্স জেসন রেডি করা
//                    JSONObject attackResult = new JSONObject();
//                    attackResult.put("action", "attack_result");
//                    attackResult.put("attackerId", attackerId);
//                    attackResult.put("defenderId", defenderId);
//                    attackResult.put("moveName", moveName);
//                    attackResult.put("damage", damage);
//                    attackResult.put("newHp", updatedDefenderHp);
//
//                    // 🩸 ৩. HP শূন্য বা তার কম হলে ম্যাচ শেষ ঘোষণা করা এবং উইনার সেট করা
//                    if (updatedDefenderHp <= 0) {
//                        attackResult.put("isGameOver", true);
//                        attackResult.put("winnerId", attackerId);
//                    } else {
//                        attackResult.put("isGameOver", false);
//                    }
//
//                    TextMessage resultMessage = new TextMessage(attackResult.toString());
//
//                    // ৪. অ্যাটাককারী প্লেয়ারের স্ক্রিনে লাইভ রেজাল্ট পাঠানো হলো
//                    WebSocketSession attackerSession = playerSessions.get(attackerId);
//                    if (attackerSession != null && attackerSession.isOpen()) attackerSession.sendMessage(resultMessage);
//
//                    // ৫. ডিফেন্ডার প্লেয়ারের স্ক্রিনে লাইভ ড্যামেজ বা অ্যানিমেশন ট্রিগার করার জন্য পাঠানো হলো
//                    WebSocketSession defenderSession = playerSessions.get(defenderId);
//                    if (defenderSession != null && defenderSession.isOpen()) defenderSession.sendMessage(resultMessage);
//                }
//
//            } catch (Exception e) {
//                System.err.println("❌ Server JSON processing error: " + e.getMessage());
//                e.printStackTrace();
//            }
//        }
//        // কেস ৫: পুরানো কোডের থ্রো মেকানিজম স্ট্র্রিং ব্যাকআপ
//        else if (payload.startsWith("THROW")) {
//            Long currentSenderId = (Long) session.getAttributes().get("playerId");
//            for (Map.Entry<Long, WebSocketSession> entry : playerSessions.entrySet()) {
//                if (!entry.getKey().equals(currentSenderId) && entry.getValue().isOpen()) {
//                    entry.getValue().sendMessage(new TextMessage(payload));
//                }
//            }
//        }
//    }
//
//    /**
//     * 🔴 প্লেয়ার যখন গেম ক্লোজ করবে বা লগআউট বাটনে ক্লিক করবে
//     */
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        try {
//            Long playerId = (Long) session.getAttributes().get("playerId");
//
//            if (playerId != null) {
//                // ১. ডাটাবেজে স্ট্যাটাস ০ (অফলাইন) করা
//                playerRepository.updateOnlineStatus(playerId, 0);
//                System.out.println("⚡ [SUCCESS] Player ID " + playerId + " is now OFFLINE in database!");
//
//                // ২. সেশন ম্যাপ থেকে প্লেয়ারকে রিমুভ করা
//                playerSessions.remove(playerId);
//            }
//        } catch (Exception e) {
//            System.err.println("❌ Failed to update online status on disconnect:");
//            e.printStackTrace();
//        }
//    }
//}
package org.example.websocket;

import org.example.repository.PlayerRepository;
import org.example.service.BattleService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private BattleService battleService;

    // 📡 অনলাইন প্লেয়ারদের সকেট সেশন ট্র্যাক রাখার জন্য কনকারেন্ট ম্যাপ
    private static final Map<Long, WebSocketSession> playerSessions = new ConcurrentHashMap<>();

    /**
     * 🟢 প্লেয়ার যখন LibGDX গেম থেকে সকেটে সফলভাবে কানেক্ট হবে
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        try {
            URI uri = session.getUri();
            if (uri != null && uri.getQuery() != null) {
                String query = uri.getQuery();
                String[] pairs = query.split("&");
                for (String pair : pairs) {
                    String[] idx = pair.split("=");
                    if (idx[0].equals("playerId") && idx.length > 1) {
                        Long playerId = Long.parseLong(idx[1]);

                        // ১. ডাটাবেজে স্ট্যাটাস ১ (অনলাইন) করা
                        playerRepository.updateOnlineStatus(playerId, 1);
                        System.out.println("⚡ [SUCCESS] Player ID " + playerId + " is now ONLINE in database!");

                        // ২. সেশন ম্যাপে প্লেয়ার আইডি এবং সেশন স্টোর করা হচ্ছে
                        playerSessions.put(playerId, session);
                        session.getAttributes().put("playerId", playerId);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to update online status on connect:");
            e.printStackTrace();
        }
    }

    /**
     * ⚔️ 🎯 [সম্পূর্ণ প্রোফেশনাল মেথড]: ফ্রন্টএন্ড থেকে আসা মেসেজ রিসিভ, প্রোসেস এবং রিয়েল-টাইম ফরওয়ার্ড করার জন্য
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("📥 Server Received Message: " + payload);

        // যদি মেসেজটি JSON ফরম্যাটে হয়
        if (payload.startsWith("{") && payload.endsWith("}")) {
            try {
                JSONObject json = new JSONObject(payload);
                String action = json.optString("action", "");

                // কেস ১: ব্যাটেল রিকোয়েস্ট বা ডিক্লাইন আসলে সরাসরি রিসিভারকে ফরওয়ার্ড করো
                if ("send_battle_request".equals(action) || "decline_battle_request".equals(action)) {
                    Long receiverId = json.getLong("receiverId");
                    WebSocketSession receiverSession = playerSessions.get(receiverId);

                    if (receiverSession != null && receiverSession.isOpen()) {
                        receiverSession.sendMessage(new TextMessage(payload));
                        System.out.println("🚀 [FORWARDED] Action '" + action + "' sent to Player ID: " + receiverId);
                    } else {
                        System.out.println("❌ [FAILED] Player ID " + receiverId + " is not connected to socket!");
                    }
                }
                // কেস ২: অপোনেন্ট যদি চ্যালেঞ্জ ACCEPT করে, তবে ডাটাবেজে রুম ক্রিয়েট করে দুজনকে ম্যাচ স্টার্টের কমান্ড পাঠাও
                else if ("accept_battle_request".equals(action)) {
                    Long senderId = json.getLong("senderId");
                    Long receiverId = json.getLong("receiverId");

                    JSONObject startBattleJson = new JSONObject(payload);
                    startBattleJson.put("action", "start_battle");

                    // 🎯 [সংশোধন]: র‍্যান্ডম টাইমের বদলে সার্ভিস কল করে ডাটাবেজের আসল অটো-ইনক্রিমেন্ট রুম আইডি বসানো হলো
                    long dbRoomId = battleService.startNewBattleRoom(senderId, receiverId);
                    startBattleJson.put("roomId", dbRoomId);

                    TextMessage startMessage = new TextMessage(startBattleJson.toString());

                    // চ্যালেঞ্জারকে (ম্যাচ মেকার) পাঠানো হলো
                    WebSocketSession senderSession = playerSessions.get(senderId);
                    if (senderSession != null && senderSession.isOpen()) senderSession.sendMessage(startMessage);

                    // রিসিভারকে (যে এক্সেপ্ট করেছে) পাঠানো হলো
                    WebSocketSession receiverSession = playerSessions.get(receiverId);
                    if (receiverSession != null && receiverSession.isOpen()) receiverSession.sendMessage(startMessage);

                    System.out.println("⚔️ MATCH STARTED between Player " + senderId + " and Player " + receiverId + " inside Official DB Room ID: " + dbRoomId);
                }

                // 🔄 কেস ৩: এক প্লেয়ার পোকেমন সিলেক্ট বা লক করলে অন্য প্লেয়ারের স্ক্রিনে দেখানোর জন্য
                else if ("lock_pokemon".equals(action)) {
                    Long receiverId = json.getLong("receiverId");
                    WebSocketSession receiverSession = playerSessions.get(receiverId);

                    if (receiverSession != null && receiverSession.isOpen()) {
                        receiverSession.sendMessage(new TextMessage(payload));
                        System.out.println("📢 Shared Pokemon Selection with Opponent Player ID: " + receiverId);
                    }
                }

                // 🔄 কেস ৪: প্লেয়ার যখন গেমের স্ক্রিনে SPACE চেপে কোনো মুভ বা অ্যাটাক এক্সিকিউট করবে
                else if ("execute_attack".equals(action)) {
                    // 🛠️ [ফিক্সড]: জাভা ফ্রন্টএন্ডের পাঠানো স্নেক_কেস (snake_case) কি-গুলোর সাথে ১০০% ম্যাচ করা হলো
                    Long roomId = json.getLong("room_id");
                    Long attackerId = json.getLong("attacker_id");
                    Long defenderId = json.getLong("defender_id");
                    Long defenderPokemonId = json.optLong("defenderPokemonId", 1L); // সেফটি ডিফল্ট ভ্যালু ১
                    String moveName = json.getString("move_name");
                    int damage = json.getInt("damage_dealt");

                    System.out.println("⚔️ Attacking DB Sync Process Initiated for Room: " + roomId);

                    // ⚔️ ১. সার্ভিস রান করে ডাটাবেজের HP আপডেট করা হলো এবং লাইভ অবশিষ্ট HP নেওয়া হলো
                    int updatedDefenderHp = battleService.executeAttack(roomId, attackerId, defenderId, defenderPokemonId, moveName, damage);

                    // ২. ফ্রন্টএন্ডের `BattleManager.java` এর রিসিভার আর্কিটেকচারের সাথে মিলিয়ে রেসপন্স জেসন রেডি করা হচ্ছে
                    JSONObject attackResult = new JSONObject();
                    attackResult.put("action", "attack_result");
                    attackResult.put("attacker_id", attackerId);
                    attackResult.put("defender_id", defenderId);
                    attackResult.put("move_name", moveName);
                    attackResult.put("damage_dealt", damage);
                    attackResult.put("newHp", updatedDefenderHp);

                    // 🩸 ৩. HP শূন্য বা তার কম হলে ম্যাচ শেষ ঘোষণা করা এবং উইনার সেট করা
                    if (updatedDefenderHp <= 0) {
                        attackResult.put("isGameOver", true);
                        attackResult.put("winnerId", attackerId);
                    } else {
                        attackResult.put("isGameOver", false);
                    }

                    TextMessage resultMessage = new TextMessage(attackResult.toString());

                    // ৪. অ্যাটাককারী প্লেয়ারের স্ক্রিনে লাইভ রেজাল্ট পাঠানো হলো
                    WebSocketSession attackerSession = playerSessions.get(attackerId);
                    if (attackerSession != null && attackerSession.isOpen()) {
                        attackerSession.sendMessage(resultMessage);
                    }

                    // ৫. ডিফেন্ডার প্লেয়ারের স্ক্রিনে লাইভ ড্যামেজ বা অ্যানিমেশন ট্রিগার করার জন্য পাঠানো হলো
                    WebSocketSession defenderSession = playerSessions.get(defenderId);
                    if (defenderSession != null && defenderSession.isOpen()) {
                        defenderSession.sendMessage(resultMessage);
                    }

                    System.out.println("🚀 [BROADCAST SUCCESS] Attack details dispatched back to both clients.");
                }

            } catch (Exception e) {
                System.err.println("❌ Server JSON processing error: " + e.getMessage());
                e.printStackTrace();
            }
        }
        // কেস ৫: পুরানো কোডের থ্রো মেকানিজম স্ট্র্রিং ব্যাকআপ
        else if (payload.startsWith("THROW")) {
            Long currentSenderId = (Long) session.getAttributes().get("playerId");
            for (Map.Entry<Long, WebSocketSession> entry : playerSessions.entrySet()) {
                if (!entry.getKey().equals(currentSenderId) && entry.getValue().isOpen()) {
                    entry.getValue().sendMessage(new TextMessage(payload));
                }
            }
        }
    }

    /**
     * 🔴 প্লেয়ার যখন গেম ক্লোজ করবে বা লগআউট বাটনে ক্লিক করবে
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        try {
            Long playerId = (Long) session.getAttributes().get("playerId");

            if (playerId != null) {
                // ১. ডাটাবেজে স্ট্যাটাস ০ (অফলাইন) করা
                playerRepository.updateOnlineStatus(playerId, 0);
                System.out.println("⚡ [SUCCESS] Player ID " + playerId + " is now OFFLINE in database!");

                // ২. সেশন ম্যাপ থেকে প্লেয়ারকে রিমুভ করা
                playerSessions.remove(playerId);
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to update online status on disconnect:");
            e.printStackTrace();
        }
    }
}