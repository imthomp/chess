package webSocketMessages.userCommands;

public class Leave extends UserGameCommand {
    public Leave(String authToken, Integer gameID) {
        super(authToken);
        this.commandType = CommandType.LEAVE;
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
        Leave leave = (Leave) o;
        return gameID.equals(leave.gameID);
    }

    @Override
    public int hashCode() {
        return gameID.hashCode();
    }
}