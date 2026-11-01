
//c
//package org.example.service;
//
//import org.example.dto.LoginRequest;
//import org.example.dto.LoginResponse;
//import org.example.dto.RegisterRequest;
//import org.example.entity.PlayerPokemon;
//import org.example.repository.PlayerPokemonRepository;
//import org.example.entity.Player;
//
//import org.example.repository.PlayerRepository;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.example.dto.OnlinePlayerResponse;
//@Service
//public class AuthService {
//
//    @Autowired
//    private PlayerRepository playerRepository;
//    @Autowired
//    private PlayerPokemonRepository playerPokemonRepository;
//
//    // =========================
//    // REGISTER
//    // =========================
//
//    public String register(
//            RegisterRequest request
//    ) {
//
//        if (
//                playerRepository.findByUsername(
//                        request.getUsername()
//                ) != null
//        ) {
//
//            return "USERNAME_EXISTS";
//        }
//
//        if (
//                playerRepository.findByEmail(
//                        request.getEmail()
//                ) != null
//        ) {
//
//            return "EMAIL_EXISTS";
//        }
//
//        Player player = new Player();
//
//        player.setUsername(
//                request.getUsername()
//        );
//
//        player.setEmail(
//                request.getEmail()
//        );
//
//        player.setPassword(
//                request.getPassword()
//        );
//
//        playerRepository.save(
//                player
//        );
//
//        return "REGISTER_SUCCESS";
//    }
//
//    // =========================
//    // LOGIN
//    // =========================
//
//    public Object login(
//            LoginRequest request
//    ) {
//
//        Player player = null;
//
//        if (
//                request.getEmail() != null
//                        &&
//                        !request.getEmail().isEmpty()
//        ) {
//
//            player =
//                    playerRepository.findByEmail(
//                            request.getEmail()
//                    );
//        }
//
//        if (player == null) {
//
//            return "USER_NOT_FOUND";
//        }
//
//        if (
//                !player.getPassword().equals(
//                        request.getPassword()
//                )
//        ) {
//
//            return "WRONG_PASSWORD";
//        }
//
//        return new LoginResponse(
//
//                player.getId(),
//
//                player.getUsername(),
//
//                player.getPokeballs()
//        );
//    }
//
//    // =========================
//    // USE POKEBALL
//    // =========================
//
//    public String usePokeball(
//            Long playerId
//    ) {
//
//        Player player =
//                playerRepository
//                        .findById(playerId)
//                        .orElse(null);
//
//        if(player == null) {
//
//            return "PLAYER_NOT_FOUND";
//        }
//
//        if(player.getPokeballs() > 0) {
//
//            player.setPokeballs(
//
//                    player.getPokeballs() - 1
//            );
//
//            playerRepository.save(
//                    player
//            );
//        }
//
//        return "UPDATED";
//    }
//    //
//     //onlin palyer
//    public List<OnlinePlayerResponse>
//    getPlayersWithTwoPokemon() {
//
//        List<OnlinePlayerResponse> list =
//                new ArrayList<>();
//
//        List<Player> players =
//                playerRepository.findAll();
//
//        for(Player player : players) {
//
//            long total =
//
//                    playerPokemonRepository
//                            .countByPlayerId(
//                                    player.getId()
//                            );
//
//            if(total >= 2) {
//
//                list.add(
//
//                        new OnlinePlayerResponse(
//
//                                player.getId(),
//
//                                player.getUsername(),
//
//                                (int) total
//                        )
//                );
//            }
//        }
//
//        return list;
//    }
//    // =========================
//// COLLECT POKEBALL
//// =========================
//
//    public String collectPokeball(
//            Long playerId
//    ) {
//
//        Player player =
//                playerRepository
//                        .findById(playerId)
//                        .orElse(null);
//
//        if(player == null) {
//
//            return "PLAYER_NOT_FOUND";
//        }
//
//        if(player.getPokeballs() < 5) {
//
//            player.setPokeballs(
//
//                    player.getPokeballs() + 1
//            );
//
//            playerRepository.save(
//                    player
//            );
//        }
//
//        return "UPDATED";
//    }
//    public String savePokemon(
//
//            Long playerId,
//
//            String pokemonName
//
//    ) {
//
//        Long pokemonId = 4L;
//
//        if(pokemonName.equalsIgnoreCase("charmander")) {
//
//            pokemonId = 1L;
//        }
//
//        else if(pokemonName.equalsIgnoreCase("greninja")) {
//
//            pokemonId = 2L;
//        }
//
//        else if(pokemonName.equalsIgnoreCase("bulbasaur")) {
//
//            pokemonId = 3L;
//        }
//
//        else if(pokemonName.equalsIgnoreCase("pikachu")) {
//
//            pokemonId = 4L;
//        }
//
//        PlayerPokemon playerPokemon =
//                new PlayerPokemon();
//
//        playerPokemon.setPlayerId(
//                playerId
//        );
//
//        playerPokemon.setPokemonId(
//                pokemonId
//        );
//
//        playerPokemon.setCurrentHp(
//                100
//        );
//
//        playerPokemonRepository.save(
//                playerPokemon
//        );
//
//        return "POKEMON_SAVED";
//    }
//}
//check
package org.example.service;

import org.example.dto.LoginRequest;
import org.example.dto.LoginResponse;
import org.example.dto.RegisterRequest;
import org.example.entity.PlayerPokemon;
import org.example.repository.PlayerPokemonRepository;
import org.example.entity.Player;
import org.example.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import org.example.dto.OnlinePlayerResponse;

@Service
public class AuthService {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PlayerPokemonRepository playerPokemonRepository;

    // =========================
    // REGISTER
    // =========================
    public String register(RegisterRequest request) {
        if (playerRepository.findByUsername(request.getUsername()) != null) {
            return "USERNAME_EXISTS";
        }

        if (playerRepository.findByEmail(request.getEmail()) != null) {
            return "EMAIL_EXISTS";
        }

        Player player = new Player();
        player.setUsername(request.getUsername());
        player.setEmail(request.getEmail());
        player.setPassword(request.getPassword());

        playerRepository.save(player);
        return "REGISTER_SUCCESS";
    }

    // =========================
    // LOGIN
    // =========================
    public Object login(LoginRequest request) {
        Player player = null;

        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            player = playerRepository.findByEmail(request.getEmail());
        }

        if (player == null) {
            return "USER_NOT_FOUND";
        }

        if (!player.getPassword().equals(request.getPassword())) {
            return "WRONG_PASSWORD";
        }

        return new LoginResponse(
                player.getId(),
                player.getUsername(),
                player.getPokeballs()
        );
    }

    // =========================
    // USE POKEBALL
    // =========================
    public String usePokeball(Long playerId) {
        Player player = playerRepository.findById(playerId).orElse(null);

        if (player == null) {
            return "PLAYER_NOT_FOUND";
        }

        if (player.getPokeballs() > 0) {
            player.setPokeballs(player.getPokeballs() - 1);
            playerRepository.save(player);
        }

        return "UPDATED";
    }

    // ======================================================================
    // 🌐 ONLINE PLAYERS: ডাটাবেজের অনলাইন স্ট্যাটাস ১ এবং পোকেমন ২+ চেক লজিক
    // ======================================================================
    public List<OnlinePlayerResponse> getPlayersWithTwoPokemon() {
        List<OnlinePlayerResponse> list = new ArrayList<>();
        List<Player> players = playerRepository.findAll();

        for (Player player : players) {
            long total = playerPokemonRepository.countByPlayerId(player.getId());

            // 🎯 [মেইন কন্ডিশন ফিক্স]: প্লেয়ার অনলাইন (status == 1) এবং পোকেমন ২ বা তার বেশি হতে হবে
            if (player.getOnlineStatus() == 1 && total >= 2) {
                list.add(
                        new OnlinePlayerResponse(
                                player.getId(),
                                player.getUsername(),
                                (int) total,
                                player.getOnlineStatus() // 👈 DTO-তে অনলাইন স্ট্যাটাস পাস করা হলো
                        )
                );
            }
        }

        return list;
    }

    // =========================
    // COLLECT POKEBALL
    // =========================
    public String collectPokeball(Long playerId) {
        Player player = playerRepository.findById(playerId).orElse(null);

        if (player == null) {
            return "PLAYER_NOT_FOUND";
        }

        if (player.getPokeballs() < 5) {
            player.setPokeballs(player.getPokeballs() + 1);
            playerRepository.save(player);
        }

        return "UPDATED";
    }

    // =========================
    // SAVE POKEMON
    // =========================
    public String savePokemon(Long playerId, String pokemonName) {
        Long pokemonId = 4L;

        if (pokemonName.equalsIgnoreCase("charmander")) {
            pokemonId = 1L;
        } else if (pokemonName.equalsIgnoreCase("greninja")) {
            pokemonId = 2L;
        } else if (pokemonName.equalsIgnoreCase("bulbasaur")) {
            pokemonId = 3L;
        } else if (pokemonName.equalsIgnoreCase("pikachu")) {
            pokemonId = 4L;
        }

        PlayerPokemon playerPokemon = new PlayerPokemon();
        playerPokemon.setPlayerId(playerId);
        playerPokemon.setPokemonId(pokemonId);
        playerPokemon.setCurrentHp(100);

        playerPokemonRepository.save(playerPokemon);
        return "POKEMON_SAVED";
    }
}