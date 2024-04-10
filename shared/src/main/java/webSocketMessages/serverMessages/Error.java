package webSocketMessages.serverMessages;

public class Error extends ServerMessage {
    public Error(String errorMessage) {
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
    }

    public String errorMessage;

    public String getMessage() {
        return errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Error error = (Error) o;
        return errorMessage.equals(error.errorMessage);
    }

    @Override
    public int hashCode() {
        return errorMessage.hashCode();
    }
}
