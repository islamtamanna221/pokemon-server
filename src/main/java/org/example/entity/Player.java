////package org.example.entity;
////
////import jakarta.persistence.*;
////
////@Entity
////@Table(name = "players")
////public class Player {
////
////    @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
////    private Long id;
////
////    private String username;
////
////    private String email;
////
////    private String password;
////
////    private int pokeballs = 5;
////
////    public Player() {
////    }
////
////    public Long getId() {
////        return id;
////    }
////
////    public String getUsername() {
////        return username;
////    }
////
////    public String getEmail() {
////        return email;
////    }
////
////    public String getPassword() {
////        return password;
////    }
////
////    public int getPokeballs() {
////        return pokeballs;
////    }
////
////    public void setUsername(String username) {
////        this.username = username;
////    }
////
////    public void setEmail(String email) {
////        this.email = email;
////    }
////
////    public void setPassword(String password) {
////        this.password = password;
////    }
////
////    public void setPokeballs(int pokeballs) {
////        this.pokeballs = pokeballs;
////    }
////}
////check
//package org.example.entity;
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "players")
//public class Player {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String username;
//
//    private String email;
//
//    private String password;
//
//    private int pokeballs = 5;
//
//    // 🎯 [নতুন সংযোজন]: ফ্রন্টএন্ড এবং আপনার রিপোজিটরির updateOnlineStatus-এর সাথে ম্যাচ করার জন্য
//    @Column(name = "online_status")
//    private int onlineStatus = 0; // ডিফল্ট ০ (অফলাইন) থাকবে, সকেটে ঢুকলে ১ হবে
//
//    // 🎯 [নতুন সংযোজন]: প্লেয়ারের কাছে কয়টি পোকেমন আছে তা ট্র্যাক করার জন্য
//    private int pokemons = 0; // ডিফল্ট ০ থাকবে, পোকেমন ক্যাচ করলে এটি আপডেট হবে
//
//    public Player() {
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public int getPokeballs() {
//        return pokeballs;
//    }
//
//    // 🎯 [নতুন গেটার]: onlineStatus এর জন্য
//    public int getOnlineStatus() {
//        return onlineStatus;
//    }
//
//    // 🎯 [নতুন গেটার]: pokemons এর জন্য
//    public int getPokemons() {
//        return pokemons;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public void setPokeballs(int pokeballs) {
//        this.pokeballs = pokeballs;
//    }
//
//    // 🎯 [নতুন সেটার]: onlineStatus এর জন্য
//    public void setOnlineStatus(int onlineStatus) {
//        this.onlineStatus = onlineStatus;
//    }
//
//    // 🎯 [নতুন সেটার]: pokemons এর জন্য
//    public void setPokemons(int pokemons) {
//        this.pokemons = pokemons;
//    }
//}
//check for battle
package org.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String password;

    private int pokeballs = 5;

    // 🎯 [নতুন সংযোজন]: ফ্রন্টএন্ড এবং আপনার রিপোজিটরির updateOnlineStatus-এর সাথে ম্যাচ করার জন্য
    @Column(name = "online_status")
    private int onlineStatus = 0; // ডিফল্ট ০ (অফলাইন) থাকবে, সকেটে ঢুকলে ১ হবে

    // 🎯 [নতুন সংযোজন]: প্লেয়ারের কাছে কয়টি পোকেমন আছে তা ট্র্যাক করার জন্য
    private int pokemons = 0; // ডিফল্ট ০ থাকবে, পোকেমন ক্যাচ করলে এটি আপডেট হবে

    // ⚔️ 🎯 [নতুন সংযোজন]: ব্যাটল সার্ভিসের উইন-লস ট্র্যাক করার জন্য (যা মিসিং ছিল)
    private int wins = 0;     // প্লেয়ারের মোট জয়ের সংখ্যা
    private int losses = 0;   // প্লেয়ারের মোট হারের সংখ্যা

    public Player() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getPokeballs() {
        return pokeballs;
    }

    // 🎯 [নতুন গেটার]: onlineStatus এর জন্য
    public int getOnlineStatus() {
        return onlineStatus;
    }

    // 🎯 [নতুন গেটার]: pokemons এর জন্য
    public int getPokemons() {
        return pokemons;
    }

    // ⚔️ [নতুন গেটার]: wins এর জন্য
    public int getWins() {
        return wins;
    }

    // ⚔️ [নতুন গেটার]: losses এর জন্য
    public int getLosses() {
        return losses;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPokeballs(int pokeballs) {
        this.pokeballs = pokeballs;
    }

    // 🎯 [নতুন সেটার]: onlineStatus এর জন্য
    public void setOnlineStatus(int onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    // 🎯 [নতুন সেটার]: pokemons এর জন্য
    public void setPokemons(int pokemons) {
        this.pokemons = pokemons;
    }

    // ⚔️ [নতুন সেটার]: wins এর জন্য
    public void setWins(int wins) {
        this.wins = wins;
    }

    // ⚔️ [নতুন সেটার]: losses এর জন্য
    public void setLosses(int losses) {
        this.losses = losses;
    }
}