//package org.example.repository;
//
//import org.example.entity.Player;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//public interface PlayerRepository
//        extends JpaRepository<Player, Long> {
//
//    Player findByUsername(
//            String username
//    );
//
//    Player findByEmail(
//            String email
//    );
//}
//updated
package org.example.repository;

import org.example.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Player findByUsername(String username);

    Player findByEmail(String email);

    // 🎯 [মেইন ফিক্স]: clearAutomatically এবং flushAutomatically ট্রু করা হলো
    // এর ফলে ডাটাবেজে রাইট হওয়ার সাথে সাথে Hibernate মেমোরি ক্লিয়ার করে নতুন ডাটা রিড করবে
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE players SET online_status = :status WHERE id = :id", nativeQuery = true)
    void updateOnlineStatus(@Param("id") Long id, @Param("status") int status);
}