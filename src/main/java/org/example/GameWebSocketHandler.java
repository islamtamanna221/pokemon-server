
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

    // Connected players
    private static final Map<Long, WebSocketSession> playerSessions =
            new ConcurrentHashMap<>();

    // Player connection
    @Override
    public void afterConnectionEstablished(WebSocketSession session)
            throws Exception {

        try {
            URI uri = session.getUri();

            if (uri == null || uri.getQuery() == null) {
                session.close(CloseStatus.BAD_DATA);
                return;
            }

            Long playerId = null;

            String[] pairs = uri.getQuery().split("&");

            for (String pair : pairs) {
                String[] idx = pair.split("=", 2);

                if (idx.length == 2 && idx[0].equals("playerId")) {
                    playerId = Long.parseLong(idx[1]);
                    break;
                }
            }

            if (playerId == null) {
                session.close(CloseStatus.BAD_DATA);
                return;
            }

            // Remove previous session for this player safely
            WebSocketSession oldSession = playerSessions.put(playerId, session);

            session.getAttributes().put("playerId", playerId);

            if (oldSession != null && oldSession.isOpen()
                    && !oldSession.getId().equals(session.getId())) {
                oldSession.close(CloseStatus.NORMAL);
            }

            playerRepository.updateOnlineStatus(playerId, 1);

            System.out.println(
                    "Player connected: " + playerId
            );

            session.sendMessage(
                    new TextMessage("CONNECTED:" + playerId)
            );

        } catch (Exception e) {
            System.err.println("Connection error: " + e.getMessage());
            e.printStackTrace();
            if (session.isOpen()) {
                session.close(CloseStatus.SERVER_ERROR);
            }
        }
    }

    // Send message to one player
    private void sendToPlayer(Long playerId, String payload)
            throws Exception {

        WebSocketSession receiver = playerSessions.get(playerId);

        if (receiver != null && receiver.isOpen()) {
            synchronized (receiver) {
                receiver.sendMessage(new TextMessage(payload));
            }

            System.out.println(
                    "Message forwarded to Player: " + playerId
            );
        } else {
            System.out.println(
                    "Player not connected: " + playerId
            );
        }
    }

    // Send message to opponent
    private void sendToOpponent(Long senderId, String payload)
            throws Exception {

        for (Map.Entry<Long, WebSocketSession> entry
                : playerSessions.entrySet()) {

            Long receiverId = entry.getKey();
            WebSocketSession receiver = entry.getValue();

            if (!receiverId.equals(senderId) && receiver.isOpen()) {
                synchronized (receiver) {
                    receiver.sendMessage(new TextMessage(payload));
                }
            }
        }
    }

    // Receive and process messages
    @Override
    protected void handleTextMessage(
            WebSocketSession session,
            TextMessage message) throws Exception {

        String payload = message.getPayload();

        Long currentPlayerId =
                (Long) session.getAttributes().get("playerId");

        System.out.println(
                "Server received message from Player "
                        + currentPlayerId + ": " + payload
        );

        if (currentPlayerId == null) {
            return;
        }

        // JSON messages
        if (payload.startsWith("{") && payload.endsWith("}")) {

            try {
                JSONObject json = new JSONObject(payload);
                String action = json.optString("action", "");

                // Battle request / decline
                if ("send_battle_request".equals(action)
                        || "decline_battle_request".equals(action)) {

                    Long receiverId = json.getLong("receiverId");

                    sendToPlayer(receiverId, payload);
                }

                // Accept battle request
                else if ("accept_battle_request".equals(action)) {

                    Long senderId = json.getLong("senderId");
                    Long receiverId = json.getLong("receiverId");

                    if (!senderId.equals(currentPlayerId)
                            && !receiverId.equals(currentPlayerId)) {
                        System.err.println("Invalid battle request participants");
                        return;
                    }

                    long dbRoomId =
                            battleService.startNewBattleRoom(
                                    senderId, receiverId
                            );

                    json.put("action", "start_battle");
                    json.put("roomId", dbRoomId);

                    String startPayload = json.toString();

                    sendToPlayer(senderId, startPayload);
                    sendToPlayer(receiverId, startPayload);

                    System.out.println(
                            "Battle started. Room ID: " + dbRoomId
                    );
                }

                // Pokemon selection
                else if ("lock_pokemon".equals(action)) {

                    Long receiverId = json.getLong("receiverId");

                    sendToPlayer(receiverId, payload);
                }

                // Execute attack
                else if ("execute_attack".equals(action)) {

                    Long roomId = json.getLong("room_id");
                    Long attackerId = json.getLong("attacker_id");
                    Long defenderId = json.getLong("defender_id");

                    if (!attackerId.equals(currentPlayerId)) {
                        System.err.println("Invalid attacker ID");
                        return;
                    }

                    Long defenderPokemonId =
                            json.optLong("defenderPokemonId", 1L);

                    String moveName = json.getString("move_name");
                    int damage = json.getInt("damage_dealt");

                    if (damage < 0) {
                        System.err.println("Invalid damage value");
                        return;
                    }

                    int updatedDefenderHp =
                            battleService.executeAttack(
                                    roomId,
                                    attackerId,
                                    defenderId,
                                    defenderPokemonId,
                                    moveName,
                                    damage
                            );

                    JSONObject attackResult = new JSONObject();

                    attackResult.put("action", "attack_result");
                    attackResult.put("attacker_id", attackerId);
                    attackResult.put("defender_id", defenderId);
                    attackResult.put("move_name", moveName);
                    attackResult.put("damage_dealt", damage);
                    attackResult.put("newHp", updatedDefenderHp);

                    if (updatedDefenderHp <= 0) {
                        attackResult.put("isGameOver", true);
                        attackResult.put("winnerId", attackerId);
                    } else {
                        attackResult.put("isGameOver", false);
                    }

                    String resultPayload = attackResult.toString();

                    sendToPlayer(attackerId, resultPayload);
                    sendToPlayer(defenderId, resultPayload);

                    System.out.println(
                            "Attack result sent to both players."
                    );
                }

                // Generic JSON action: forward to opponent
                else {
                    sendToOpponent(currentPlayerId, payload);
                }

            } catch (Exception e) {
                System.err.println(
                        "JSON processing error: " + e.getMessage()
                );
                e.printStackTrace();
            }

        } else {

            // Ordinary text messages and THROW messages
            sendToOpponent(currentPlayerId, payload);

            System.out.println(
                    "Text message broadcast from Player "
                            + currentPlayerId
            );
        }
    }

    // Player disconnect
    @Override
    public void afterConnectionClosed(
            WebSocketSession session,
            CloseStatus status) throws Exception {

        Long playerId =
                (Long) session.getAttributes().get("playerId");

        if (playerId != null) {

            // Remove only if this is still the active session
            playerSessions.remove(playerId, session);

            if (!playerSessions.containsKey(playerId)) {
                playerRepository.updateOnlineStatus(playerId, 0);
            }

            System.out.println(
                    "Player disconnected: " + playerId
            );
        }
    }
}
