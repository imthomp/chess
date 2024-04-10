package webSocketMessages.userCommands;

public class Resign extends UserGameCommand {
    public Resign(String authToken, Integer gameID) {
        super(authToken);
        this.gameID = gameID;
    }

    public Integer gameID;

    public Integer getGameID() {
        return gameID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Resign resign = (Resign) o;
        return gameID.equals(resign.gameID);
    }

    @Override
    public int hashCode() {
        return gameID.hashCode();
    }
}