//package org.example.service;
//
//import org.example.entity.BattleTurn;
//import org.example.entity.PlayerPokemon;
//import org.example.repository.BattleTurnRepository;
//import org.example.repository.PlayerPokemonRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import java.util.Optional;
//
//@Service
//public class BattleService {
//
//    @Autowired
//    private PlayerPokemonRepository playerPokemonRepository;
//
//    @Autowired
//    private BattleTurnRepository battleTurnRepository;
//
//    @Transactional
//    public synchronized int executeAttack(Long roomId, Long attackerId, Long defenderId, Long defenderPokemonId, String moveName, int damage) {
//
//        // ১. ডিফেন্ডারের পোকেমন ডাটাবেজ থেকে খুঁজে বের করা
//        Optional<PlayerPokemon> targetPokemonOpt = playerPokemonRepository.findByPlayerIdAndPokemonId(defenderId, defenderPokemonId);
//
//        int currentHp = 100; // ডিফল্ট বা সেফটি ফলব্যাক
//        if (targetPokemonOpt.isPresent()) {
//            currentHp = targetPokemonOpt.get().getCurrentHp();
//        }
//
//        // ২. ড্যামেজ মাইনাস করে নতুন HP ক্যালকুলেট করা
//        int newHp = currentHp - damage;
//        if (newHp < 0) newHp = 0;
//
//        // ৩. ডাটাবেজে পোকেমনের নতুন HP আপডেট করা
//        playerPokemonRepository.updatePokemonHp(defenderId, defenderPokemonId, newHp);
//
//        // ৪. battle_turns টেবিলে এই টার্নের মুভ এবং ড্যামেজ হিস্ট্রি সেভ করা
//        BattleTurn turn = new BattleTurn();
//        turn.setRoomId(roomId);
//        turn.setAttackerPlayerId(attackerId);
//        turn.setDefenderPlayerId(defenderId);
//        turn.setMoveName(moveName);
//        turn.setDamage(damage);
//        battleTurnRepository.save(turn);
//
//        System.out.println("⚔️ [BATTLE LOG] Room " + roomId + ": Player " + attackerId + " hits Player " + defenderId +
//                " with '" + moveName + "' for " + damage + " damage. Target New HP: " + newHp);
//
//        return newHp; // ফ্রন্টএন্ড দুই স্ক্রিনে লাইভ দেখানোর জন্য নতুন HP রিটার্ন করা হলো
//    }
//}
//
//package org.example.service;
//
//import org.example.entity.BattleRoom;
//import org.example.entity.BattleTurn;
//import org.example.entity.Player;
//import org.example.entity.PlayerPokemon;
//import org.example.repository.BattleRoomRepository;
//import org.example.repository.BattleTurnRepository;
//import org.example.repository.PlayerRepository;
//import org.example.repository.PlayerPokemonRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import java.util.Optional;
//
//@Service
//public class BattleService {
//
//    @Autowired
//    private PlayerPokemonRepository playerPokemonRepository;
//
//    @Autowired
//    private BattleTurnRepository battleTurnRepository;
//
//    @Autowired
//    private BattleRoomRepository battleRoomRepository;
//
//    @Autowired
//    private PlayerRepository playerRepository;
//
//    /**
//     * 🤝 ১. অপোনেন্ট চ্যালেঞ্জ ACCEPT করলে ডাটাবেজে অফিশিয়াল রুম ক্রিয়েট করার মেথড
//     */
//    @Transactional
//    public Long startNewBattleRoom(Long p1Id, Long p2Id) {
//        Player p1 = playerRepository.findById(p1Id)
//                .orElseThrow(() -> new RuntimeException("Player 1 not found"));
//        Player p2 = playerRepository.findById(p2Id)
//                .orElseThrow(() -> new RuntimeException("Player 2 not found"));
//
//        BattleRoom room = new BattleRoom();
//        room.setPlayer1(p1);
//        room.setPlayer2(p2);
//        room.setStatus("PLAYING");
//
//        BattleRoom savedRoom = battleRoomRepository.save(room);
//        return savedRoom.getId();
//    }
//
//    /**
//     * ⚔️ ২. অ্যাটাক মেথড (setRoomId এরর সম্পূর্ণ ফিক্সড এবং অবজেক্ট ও উইনার লজিক সহ)
//     */
//    @Transactional
//    public synchronized int executeAttack(Long roomId, Long attackerId, Long defenderId, Long defenderPokemonId, String moveName, int damage) {
//
//        // ১. ডিফেন্ডারের পোকেমন ডাটাবেজ থেকে খুঁজে বের করা
//        Optional<PlayerPokemon> targetPokemonOpt = playerPokemonRepository.findByPlayerIdAndPokemonId(defenderId, defenderPokemonId);
//
//        int currentHp = 100; // ডিফল্ট বা সেফটি ফলব্যাক
//        if (targetPokemonOpt.isPresent()) {
//            currentHp = targetPokemonOpt.get().getCurrentHp();
//        }
//
//        // ২. ড্যামেজ মাইনাস করে নতুন HP ক্যালকুলেট করা
//        int newHp = currentHp - damage;
//        if (newHp < 0) newHp = 0;
//
//        // ৩. ডাটাবেজে পোকেমনের নতুন HP আপডেট করা
//        playerPokemonRepository.updatePokemonHp(defenderId, defenderPokemonId, newHp);
//
//        // ৪. 🛠️ [FIXED]: battle_turns টেবিলে ডাটাবেজ রিলেশনশিপ অনুযায়ী অবজেক্ট সেট করে চাল সেভ করা
//        BattleTurn turn = new BattleTurn();
//
//        // খালি অবজেক্টে আইডি সেট করে পাস করা (যাতে setRoomId এরর আর না আসে)
//        BattleRoom roomRef = new BattleRoom();
//        roomRef.setId(roomId);
//        turn.setRoom(roomRef);
//
//        Player attackerRef = new Player();
//        attackerRef.setId(attackerId);
//        turn.setAttacker(attackerRef);
//
//        Player defenderRef = new Player();
//        defenderRef.setId(defenderId);
//        turn.setDefender(defenderRef);
//
//        turn.setMoveName(moveName);
//        turn.setDamage(damage);
//        battleTurnRepository.save(turn); // সফলভাবে টার্ন সেভ হলো
//
//        System.out.println("⚔️ [BATTLE LOG] Room " + roomId + ": Player " + attackerId + " hits Player " + defenderId +
//                " with '" + moveName + "' for " + damage + " damage. Target New HP: " + newHp);
//
//        // 🏆 ৫. পোকেমন ফেইন্ট বা ডেড হলে (HP <= 0) ম্যাচ শেষ করা এবং উইন-লস আপডেট করা
//        if (newHp <= 0) {
//            Optional<BattleRoom> roomOpt = battleRoomRepository.findById(roomId);
//            if (roomOpt.isPresent()) {
//                BattleRoom room = roomOpt.get();
//                Player winner = playerRepository.findById(attackerId).orElse(null);
//                Player loser = playerRepository.findById(defenderId).orElse(null);
//
//                if (winner != null && loser != null) {
//                    room.setStatus("FINISHED");
//                    room.setWinner(winner);
//                    battleRoomRepository.save(room);
//
//                    // প্লেয়ার প্রোফাইলে স্কোরবোর্ড আপডেট (উইন্স এবং লসেস ফিল্ড অনুযায়ী)
//                    winner.setWins(winner.getWins() + 1);
//                    loser.setLosses(loser.getLosses() + 1);
//                    playerRepository.save(winner);
//                    playerRepository.save(loser);
//
//                    System.out.println("🏆 [MATCH FINISHED] Room " + roomId + " Winner: " + winner.getUsername());
//                }
//            }
//        }
//
//        return newHp;
//    }
package org.example.service;

import org.example.entity.BattleRoom;
import org.example.entity.BattleTurn;
import org.example.entity.Player;
import org.example.entity.PlayerPokemon;
import org.example.repository.BattleRoomRepository;
import org.example.repository.BattleTurnRepository;
import org.example.repository.PlayerRepository;
import org.example.repository.PlayerPokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class BattleService {

    @Autowired
    private PlayerPokemonRepository playerPokemonRepository;

    @Autowired
    private BattleTurnRepository battleTurnRepository;

    @Autowired
    private BattleRoomRepository battleRoomRepository;

    @Autowired
    private PlayerRepository playerRepository;

    /**
     * 🤝 ১. অপোনেন্ট চ্যালেঞ্জ ACCEPT করলে ডাটাবেজে অফিশিয়াল রুম ক্রিয়েট করার মেথড
     */
    @Transactional
    public Long startNewBattleRoom(Long p1Id, Long p2Id) {
        Player p1 = playerRepository.findById(p1Id)
                .orElseThrow(() -> new RuntimeException("Player 1 not found"));
        Player p2 = playerRepository.findById(p2Id)
                .orElseThrow(() -> new RuntimeException("Player 2 not found"));

        BattleRoom room = new BattleRoom();
        room.setPlayer1(p1);
        room.setPlayer2(p2);
        room.setStatus("PLAYING");

        BattleRoom savedRoom = battleRoomRepository.save(room);
        return savedRoom.getId();
    }

    /**
     * ⚔️ ২. অ্যাটাক মেথড (Foreign Key Constraint Crash থেকে সম্পূর্ণ সুরক্ষিত)
     */
    @Transactional
    public synchronized int executeAttack(Long roomId, Long attackerId, Long defenderId, Long defenderPokemonId, String moveName, int damage) {

        // ১. ডিফেন্ডারের পোকেমন ডাটাবেজ থেকে খুঁজে বের করা
        Optional<PlayerPokemon> targetPokemonOpt = playerPokemonRepository.findByPlayerIdAndPokemonId(defenderId, defenderPokemonId);

        int currentHp = 100; // ডিফল্ট বা সেফটি ফলব্যাক
        if (targetPokemonOpt.isPresent()) {
            currentHp = targetPokemonOpt.get().getCurrentHp();
        }

        // ২. ড্যামেজ মাইনাস করে নতুন HP ক্যালকুলেট করা
        int newHp = currentHp - damage;
        if (newHp < 0) newHp = 0;

        // ③. ডাটাবেজে পোকেমনের নতুন HP আপডেট করা
        if (targetPokemonOpt.isPresent()) {
            playerPokemonRepository.updatePokemonHp(defenderId, defenderPokemonId, newHp);
        }

        // 🛠️ ৪. [SAFETY GUARD ADDED]: রুম আইডি ডাটাবেজে না থাকলে ইনসার্ট এড়ানো (ক্র্যাশ ফিক্স)
        if (roomId == null || roomId == 0L || !battleRoomRepository.existsById(roomId)) {
            System.err.println("⚠️ [DROP SAVE] Invalid Room ID (" + roomId + "). Skipping battle_turns persistence to prevent DB Crash.");
        } else {
            // ৪. battle_turns টেবিলে ডাটাবেজ রিলেশনশিপ অনুযায়ী অবজেক্ট সেট করে চাল সেভ করা
            BattleTurn turn = new BattleTurn();

            // খালি অবজেক্টে আইডি সেট করে পাস করা (যাতে setRoomId এরর আর না আসে)
            BattleRoom roomRef = new BattleRoom();
            roomRef.setId(roomId);
            turn.setRoom(roomRef);

            Player attackerRef = new Player();
            attackerRef.setId(attackerId);
            turn.setAttacker(attackerRef);

            Player defenderRef = new Player();
            defenderRef.setId(defenderId);
            turn.setDefender(defenderRef);

            turn.setMoveName(moveName);
            turn.setDamage(damage);

            // 💾 JpaRepository-এর মেইন সেভ পয়েন্ট, প্রতিটি চাল এখানে হিস্ট্রি টেবিলে রাইট হবে!
            battleTurnRepository.save(turn);
            System.out.println("✅ [DB SAVE] Turn history recorded successfully in battle_turns table!");
        }

        System.out.println("⚔️ [BATTLE LOG] Room " + roomId + ": Player " + attackerId + " hits Player " + defenderId +
                " with '" + moveName + "' for " + damage + " damage. Target New HP: " + newHp);

        // 🏆 ৫. পোকেমন ফেইন্ট বা ডেড হলে (HP <= 0) ম্যাচ শেষ করা এবং উইন-লস আপডেট করা
        // (রুম আইডি ডাটাবেজে থাকলেই কেবল উইনার হিস্ট্রি প্রসেস হবে)
        if (newHp <= 0 && roomId != null && roomId != 0L && battleRoomRepository.existsById(roomId)) {
            Optional<BattleRoom> roomOpt = battleRoomRepository.findById(roomId);
            if (roomOpt.isPresent()) {
                BattleRoom room = roomOpt.get();
                Player winner = playerRepository.findById(attackerId).orElse(null);
                Player loser = playerRepository.findById(defenderId).orElse(null);

                if (winner != null && loser != null) {
                    room.setStatus("FINISHED");
                    room.setWinner(winner);
                    battleRoomRepository.save(room);

                    // 🛠️ [FIXED]: wins এবং losses সরাসরি primitive int হওয়ায় সরাসরি ডাটা প্লাস হবে
                    winner.setWins(winner.getWins() + 1);
                    loser.setLosses(loser.getLosses() + 1);

                    playerRepository.save(winner);
                    playerRepository.save(loser);

                    System.out.println("🏆 [MATCH FINISHED] Room " + roomId + " Winner: " + winner.getUsername());
                }
            }
        }

        return newHp;
    }
}