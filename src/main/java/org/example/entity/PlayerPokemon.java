package org.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "player_pokemon")
public class PlayerPokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "player_id")
    private Long playerId;

    @Column(name = "pokemon_id")
    private Long pokemonId;

    @Column(name = "current_hp")
    private int currentHp;

    public Long getId() {
        return id;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getPokemonId() {
        return pokemonId;
    }

    public void setPokemonId(Long pokemonId) {
        this.pokemonId = pokemonId;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = currentHp;
    }
}