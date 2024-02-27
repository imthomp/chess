package result;

import model.GameData;

import java.util.HashSet;

public record ListGamesResult(HashSet<GameData> games, String message) {}
