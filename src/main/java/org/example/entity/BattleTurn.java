//package org.example.entity;
//
//import jakarta.persistence.*;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "battle_turns")
//public class BattleTurn {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "room_id")
//    private Long roomId;
//
//    @Column(name = "attacker_player_id")
//    private Long attackerPlayerId;
//
//    @Column(name = "defender_player_id")
//    private Long defenderPlayerId;
//
//    @Column(name = "move_name", length = 50)
//    private String moveName;
//
//    @Column(name = "damage")
//    private int damage;
//
//    @Column(name = "created_at")
//    private LocalDateTime createdAt = LocalDateTime.now();
//
//    // Getters and Setters
//    public Long getId() { return id; }
//    public Long getRoomId() { return roomId; }
//    public void setRoomId(Long roomId) { this.roomId = roomId; }
//    public Long getAttackerPlayerId() { return attackerPlayerId; }
//    public void setAttackerPlayerId(Long attackerPlayerId) { this.attackerPlayerId = attackerPlayerId; }
//    public Long getDefenderPlayerId() { return defenderPlayerId; }
//    public void setDefenderPlayerId(Long defenderPlayerId) { this.defenderPlayerId = defenderPlayerId; }
//    public String getMoveName() { return moveName; }
//    public void setMoveName(String moveName) { this.moveName = moveName; }
//    public int getDamage() { return damage; }
//    public void setDamage(int damage) { this.damage = damage; }
//}
package org.example.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "battle_turns")
public class BattleTurn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private BattleRoom room;

    @ManyToOne
    @JoinColumn(name = "attacker_player_id", nullable = false)
    private Player attacker;

    @ManyToOne
    @JoinColumn(name = "defender_player_id", nullable = false)
    private Player defender;

    @Column(name = "move_name", length = 50)
    private String moveName;

    private Integer damage;

    // 🛠️ [SAFETY FIX]: insertable = true করে জাভা থেকেই অটো-টাইমস্ট্যাম্প সেট করার ব্যবস্থা করা হলো
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BattleRoom getRoom() { return room; }
    public void setRoom(BattleRoom room) { this.room = room; }

    public Player getAttacker() { return attacker; }
    public void setAttacker(Player attacker) { this.attacker = attacker; }

    public Player getDefender() { return defender; }
    public void setDefender(Player defender) { this.defender = defender; }

    public String getMoveName() { return moveName; }
    public void setMoveName(String moveName) { this.moveName = moveName; }

    public Integer getDamage() { return damage; }
    public void setDamage(Integer damage) { this.damage = damage; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    // 🛠️ [ADDED]: সেটার মেথড যোগ করা হলো সেফটির জন্য
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}