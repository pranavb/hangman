package com.hulu.recruitment.gallows.pbolar.hangman_solver.entity;

/**
 * Entity representing data from Hulu's API
 */
public class GameStatus {

    public static final String ALIVE_STATE = "ALIVE";

    public enum STATUS {
        ALIVE, FREE, DEAD
    }

    private String status, token, remaining_guesses, state;

    public GameStatus(String status, String token, String remaining_guesses, String state) {
        super();
        this.status = status;
        this.token = token;
        this.remaining_guesses = remaining_guesses;
        this.state = state;
    }

    public GameStatus(String status) {
        this.status = status;
        this.state = "";
    }

    public boolean hasEnded() {
        return !STATUS.ALIVE.toString().equals(status);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRemaining_guesses() {
        return remaining_guesses;
    }

    public void setRemaining_guesses(String remaining_guesses) {
        this.remaining_guesses = remaining_guesses;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "GameStatus [status=" + status + ", token=" + token + ", remaining_guesses=" + remaining_guesses
                + ", state=" + state + "]";
    }

}
