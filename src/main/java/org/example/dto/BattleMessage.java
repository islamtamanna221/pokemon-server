package org.example.dto;

public class BattleMessage {
    private String action; // send_battle_request, incoming_battle_request, accept_battle_request, start_battle, decline_battle_request
    private Long senderId;
    private String senderName;
    private Long receiverId;
    private String receiverName;

    // Default Constructor
    public BattleMessage() {}

    // Getters and Setters
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
}