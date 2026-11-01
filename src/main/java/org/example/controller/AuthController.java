package org.example.controller;

import org.example.dto.LoginRequest;
import org.example.dto.RegisterRequest;
import org.example.dto.PokeballRequest;
import org.example.dto.PokemonCatchRequest;
import java.util.List;
import org.example.dto.OnlinePlayerResponse;
import org.example.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AuthService authService;

    // =========================
    // REGISTER
    // =========================

    @PostMapping("/register")
    public String register(

            @RequestBody
            RegisterRequest request

    ) {

        return authService.register(
                request
        );
    }

    // =========================
    // LOGIN
    // =========================

    @PostMapping("/login")
    public Object login(

            @RequestBody
            LoginRequest request

    ) {

        return authService.login(
                request
        );
    }

    // =========================
    // USE POKEBALL
    // =========================

    @PostMapping("/use-pokeball")
    public String usePokeball(

            @RequestBody
            PokeballRequest request

    ) {

        return authService.usePokeball(

                request.getPlayerId()
        );
    }

    // =========================
    // COLLECT POKEBALL
    // =========================

    @PostMapping("/collect-pokeball")
    public String collectPokeball(

            @RequestBody
            PokeballRequest request

    ) {

        return authService.collectPokeball(

                request.getPlayerId()
        );
    }
//
@GetMapping("/online-players")
public List<OnlinePlayerResponse>
onlinePlayers() {

    return authService
            .getPlayersWithTwoPokemon();
}
    // =========================
    // SAVE POKEMON
    // =========================
    //

    @PostMapping("/save-pokemon")
    public String savePokemon(

            @RequestBody
            PokemonCatchRequest request

    ) {

        return authService.savePokemon(

                request.getPlayerId(),

                request.getPokemonName()
        );
    }
}