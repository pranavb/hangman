package com.hulu.recruitment.gallows.pbolar.hangman_solver.logic;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hulu.recruitment.gallows.pbolar.hangman_solver.entity.GameStatus;

import static java.util.Arrays.asList;

public class GameController {

    //	private static final String WORD_REGEX = "\\w+";
    //	private Pattern wordPattern = Pattern.compile("\\w+");
    private static final String SPLIT_REGEX = "[\\s-:\"]";
    private static final Character WILDCARD = '_';

    private final int mapSize;

    private GameStatus game;
    private List<String> words;
    private Set<Character> guessedLetters;
    private Map<Integer, List<Character>> freqMap;

    public GameController(GameStatus game) {
        this.game = game;
        this.freqMap = FrequencyMap.getFreqMap();
        this.mapSize = freqMap.size();
        this.guessedLetters = new HashSet<>();
        this.words = getArrangedWords(game.getState());
    }

    public void updateGameStatus(GameStatus currentStatus) {
        this.game = currentStatus;
        this.words = getArrangedWords(currentStatus.getState());
    }

    /**
     * Parse the blanks from the state returned
     *
     * @param rawState string returned by the API
     * @return ordered list of words to be processed
     */
    private List<String> getArrangedWords(String rawState) {
        List<String> wordList = Arrays.asList(rawState.split(SPLIT_REGEX));

        // CONFIG 1: Sort by lowest remaining blanks
//        wordList.sort((String s1, String s2) -> numOfWildCards(s1) - numOfWildCards(s2));

        // CONFIG 2: Sort by shortest overall word length
        wordList.sort(Comparator.comparingInt(String::length));

        return wordList;
    }

    /**
     * @param s String on which to perform wildcard counting
     * @return int num of '_' found
     */
    private int numOfWildCards(String s) {
        return ((Long) s.chars().filter(c -> WILDCARD.equals((char) c)).count()).intValue();
    }

    /**
     * @param words words of the current hangman phrase
     * @return index of word to be tested next
     */
    private int getWordToGuessFor(List<String> words) {
        int i = 0;
        for (String word : words) {
            if (word.contains(WILDCARD.toString())) break;
            i++;
        }
        return i;
    }

    /**
     * Get the next character to test for
     *
     * @return the 'guessed' Character to be tested
     */
    public Character nextGuess() {
        String wordToGuess = words.get(getWordToGuessFor(words));

        // The length of the word is used to determine the most likely hit from the frequency map
        // If it's a giant word (greater than our frequency map supports), set word length to largest in our map
        int wordLength = (wordToGuess.length() < mapSize) ? wordToGuess.length() : mapSize - 1;

        // Get the most likely character from the frequency map for this word
        // If for the current word's length, we've exhausted all options, choose the largest word and try again
        Character guessChar = getCharToGuess(wordLength);
        while (guessedLetters.contains(guessChar)) {
            freqMap.get(wordLength).remove(0);
            guessChar = getCharToGuess(wordLength);
        }

        // Record the usage of this guess
        guessedLetters.add(guessChar);

        return guessChar;
    }

    /**
     * @param wordLength length of the currently selected word
     * @return Character to be guessed
     */
    private Character getCharToGuess(int wordLength) {
        return (freqMap.get(wordLength).size() != 0) ? freqMap.get(wordLength).get(0) : getLikelyAlphabet();
    }

    /**
     * List based on data from Wikipedia (https://en.wikipedia.org/wiki/Letter_frequency)
     *
     * @return most likely alphabet that hasn't been guessed yet
     */
    private Character getLikelyAlphabet() {
        Character[] alphaFreq = {'t', 'a', 'o', 'i', 'n', 's', 'h', 'r', 'd', 'l', 'c', 'u',
                'm', 'w', 'f', 'g', 'y', 'p', 'b', 'v', 'k', 'j', 'x', 'q', 'z'};
        Character chosen = null;
        for (Character alphabet : alphaFreq) {
            if (!guessedLetters.contains(alphabet)) {
                chosen = alphabet;
                break;
            }
        }
        return chosen;
    }

}
