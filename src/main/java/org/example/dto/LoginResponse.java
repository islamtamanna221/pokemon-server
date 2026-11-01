package org.example.dto;

public class LoginResponse {

    private Long id;

    private String username;

    private int pokeballs;

    public LoginResponse() {
    }

    public LoginResponse(
            Long id,
            String username,
            int pokeballs
    ) {

        this.id = id;
        this.username = username;
        this.pokeballs = pokeballs;
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

    public void setUsername(
            String username
    ) {
        this.username = username;
    }

    public int getPokeballs() {
        return pokeballs;
    }

    public void setPokeballs(
            int pokeballs
    ) {
        this.pokeballs = pokeballs;
    }
}