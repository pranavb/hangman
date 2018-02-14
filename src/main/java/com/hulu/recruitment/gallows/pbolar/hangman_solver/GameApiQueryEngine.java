package com.hulu.recruitment.gallows.pbolar.hangman_solver;

import java.io.IOException;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.hulu.recruitment.gallows.pbolar.hangman_solver.entity.GameStatus;

/**
 * Handles all the queries to Hulu's test API
 */
public class GameApiQueryEngine {

    private static final String NEW_GAME_REQUEST_URL =
            "http://gallows.hulu.com/play?code=pbolar@andrew.cmu.edu";
    private static String GAME_GUESS_URL =
            "http://gallows.hulu.com/play?code=pbolar@andrew.cmu.edu&token=%s&guess=%s";

    private Gson gsonEngine;
    private CloseableHttpClient httpclient;

    public GameApiQueryEngine() {
        gsonEngine = new Gson();
        httpclient = HttpClients.createDefault();
    }

    public GameStatus getNewGame() {
        return gsonEngine.fromJson(fireRequest(NEW_GAME_REQUEST_URL), GameStatus.class);
    }

    public GameStatus makeGuess(String token, Character guess) {
        if (guess == null) return new GameStatus("");
        String guessRequest = String.format(GAME_GUESS_URL, token, guess);
        return gsonEngine.fromJson(fireRequest(guessRequest), GameStatus.class);
    }

    private String fireRequest(String getRequest) {
        CloseableHttpResponse response1 = null;
        String resp = "";
        try {
            response1 = httpclient.execute(new HttpGet(getRequest));
            HttpEntity entity1 = response1.getEntity();

            try (Scanner s = new Scanner(entity1.getContent())) {
                while (s.hasNextLine()) resp += s.nextLine();
            }
            EntityUtils.consume(entity1);
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            try {
                if (response1 != null) response1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resp;
    }

}
