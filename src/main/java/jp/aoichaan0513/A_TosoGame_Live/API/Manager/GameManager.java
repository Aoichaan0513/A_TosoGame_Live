package jp.aoichaan0513.A_TosoGame_Live.API.Manager;

public class GameManager {

    private static GameState game = GameState.NONE;

    public static boolean isGame(GameState game) {
        return GameManager.game == game;
    }

    public static boolean isGame() {
        return game == GameState.READY || game == GameState.GAME;
    }

    public static void setGameState(GameState game) {
        GameManager.game = game;
    }

    public static GameState getGameState() {
        return game;
    }

    public enum GameState {
        READY,
        GAME,
        END,
        NONE
    }
}
