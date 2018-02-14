package com.hulu.recruitment.gallows.pbolar.hangman_solver;

import com.hulu.recruitment.gallows.pbolar.hangman_solver.entity.GameStatus;
import com.hulu.recruitment.gallows.pbolar.hangman_solver.logic.GameController;

public class App {
    public static void main(String[] args) {

        // Init dependencies
        GameApiQueryEngine queryEngine = new GameApiQueryEngine();

        // Running the program infinitely until termination is called by the user
        while (true) {

            // Create a new game
            GameStatus game = queryEngine.getNewGame();
            GameController controller = new GameController(game);

            do {
                System.out.println(game);

                // Make a guess
                Character guess = controller.nextGuess();
                game = queryEngine.makeGuess(game.getToken(), guess);

                // Update the game after every turn
                controller.updateGameStatus(game);

            } while (!game.hasEnded());
            System.out.println(GameStatus.STATUS.FREE.toString().equals(game.getState()) ? "Win!" : "Lose =/");
            System.out.println("___________________\n");
        }

    }
}
