////package org.example.repository;
////
////import org.example.entity.PlayerPokemon;
////import org.springframework.data.jpa.repository.JpaRepository;
////
////public interface PlayerPokemonRepository
////        extends JpaRepository<PlayerPokemon, Long> {
////
////    long countByPlayerId(
////            Long playerId
////    );
////}
////n
//package org.example.repository;
//
//import org.example.entity.PlayerPokemon;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.transaction.annotation.Transactional;
//import java.util.Optional;
//
//public interface PlayerPokemonRepository extends JpaRepository<PlayerPokemon, Long> {
//
//    long countByPlayerId(Long playerId);
//
//    // 🎯 প্লেয়ার আইডি এবং পোকেমন আইডি দিয়ে রো খুঁজে বের করার জন্য
//    Optional<PlayerPokemon> findByPlayerIdAndPokemonId(Long playerId, Long pokemonId);
//
//    // 🎯 লাইভ ড্যামেজ খাওয়ার পর ডাটাবেজে পোকেমনের HP আপডেট করার জন্য
//    @Transactional
//    @Modifying
//    @Query("UPDATE PlayerPokemon p SET p.currentHp = :hp WHERE p.playerId = :playerId AND p.pokemonId = :pokemonId")
//    void updatePokemonHp(@Param("playerId") Long playerId, @Param("pokemonId") Long pokemonId, @Param("hp") int hp);
//}
package org.example.repository;

import org.example.entity.PlayerPokemon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository; // 👈 এটি যুক্ত করা হয়েছে
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Repository // 👈 স্প্রিং বুটকে রিপোজিটরি চেনানোর জন্য এই অ্যানোটেশনটি মাস্ট লাগবে
public interface PlayerPokemonRepository extends JpaRepository<PlayerPokemon, Long> {

    long countByPlayerId(Long playerId);

    // 🎯 প্লেয়ার আইডি এবং পোকেমন আইডি দিয়ে রো খুঁজে বের করার জন্য
    Optional<PlayerPokemon> findByPlayerIdAndPokemonId(Long playerId, Long pokemonId);

    // 🎯 লাইভ ড্যামেজ খাওয়ার পর ডাটাবেজে পোকেমনের HP আপডেট করার জন্য
    @Transactional
    @Modifying
    @Query("UPDATE PlayerPokemon p SET p.currentHp = :hp WHERE p.playerId = :playerId AND p.pokemonId = :pokemonId")
    void updatePokemonHp(@Param("playerId") Long playerId, @Param("pokemonId") Long pokemonId, @Param("hp") int hp);
}