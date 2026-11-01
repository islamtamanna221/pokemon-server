//package org.example.dto;
//
//public class OnlinePlayerResponse {
//
//    private Long playerId;
//    private String username;
//    private int pokemonCount;
//
//    public OnlinePlayerResponse(
//            Long playerId,
//            String username,
//            int pokemonCount
//    ) {
//
//        this.playerId = playerId;
//        this.username = username;
//        this.pokemonCount = pokemonCount;
//    }
//
//    public Long getPlayerId() {
//        return playerId;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public int getPokemonCount() {
//        return pokemonCount;
//    }
//}
//chek
package org.example.dto;

public class OnlinePlayerResponse {

    private Long playerId;
    private String username;
    private int pokemonCount;
    private int onlineStatus; // 👈 ডাটাবেজের অনলাইন স্ট্যাটাস পাস করার জন্য নতুন ফিল্ড

    // 💡 আপনার আগের কনস্ট্রাক্টরটি আপডেট করা হলো
    public OnlinePlayerResponse(
            Long playerId,
            String username,
            int pokemonCount,
            int onlineStatus // 👈 কনস্ট্রাক্টরে স্ট্যাটাস যুক্ত করা হলো
    ) {
        this.playerId = playerId;
        this.username = username;
        this.pokemonCount = pokemonCount;
        this.onlineStatus = onlineStatus;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public String getUsername() {
        return username;
    }

    public int getPokemonCount() {
        return pokemonCount;
    }

    // 🎯 ফ্রন্টএন্ডের জন্য নতুন গেটার
    public int getOnlineStatus() {
        return onlineStatus;
    }
}