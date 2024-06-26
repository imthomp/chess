package client;

import client.websocket.NotificationHandler;
import webSocketMessages.serverMessages.*;
import webSocketMessages.serverMessages.Error;

import java.util.Scanner;

public class Repl implements NotificationHandler{
    private final ChessClient client;

    public Repl (String serverUrl) {
        client = new ChessClient(serverUrl, this);
    }

    public void run() {
        System.out.println("Welcome to 240 chess. Type Help to get started.");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public void notify(ServerMessage message) {
        if (message.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
            Notification notification = (Notification) message;
            System.out.println(notification.getMessage());
        }
        if (message.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
            Error error = (Error) message;
            System.out.println(error.getMessage());

        }
        printPrompt();
    }

    private void printPrompt() {
        System.out.print("\n>>> ");
    }

}