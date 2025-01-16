// Server.java
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int port = 5000;
    private static Map<String, Account> accounts = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Server started");

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (InputStream input = socket.getInputStream();
                 OutputStream output = socket.getOutputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                 PrintWriter writer = new PrintWriter(output, true)) {

                String request = reader.readLine();
                System.out.println("Request received: " + request);

                String response = handleRequest(request);
                writer.println(response);

            } catch (IOException e) {
                System.out.println("Client handler error: " + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private String handleRequest(String request) {
            // Split the input string into 4 parts (max)
            String[] parts = request.split(" ", 4);

            // Check if enough parts are provided for the requested function
            if (parts.length < 4 && "3".equals(parts[0])) {
                return "Error: Invalid number of arguments for sending a message.";
            }

            String fnId = parts[0];

            switch (fnId) {
                case "1": // Create account
                    return parts.length > 1 ? createAccount(parts[1]) : "Error: Username missing.";
                case "2": //Show The Accounts
                    return showAccounts();
                case "3": // Send message
                    return sendMessage(parts[1], parts[2], parts[3]);
                case "4": // Show inbox
                    return parts.length > 1 ? showInbox(parts[1]) : "Error: Auth token missing.";
                case "5": // Read message
                    return parts.length > 2 ? readMessage(parts[1], parts[2]) : "Error: Missing arguments.";
                case "6": // Delete message
                    return parts.length > 2 ? deleteMessage(parts[1], parts[2]) : "Error: Missing arguments.";
                default:
                    return "Invalid function ID";
            }
        }

        //CREATING THE ACCOUNT
        private String createAccount(String username) {
            if (accounts.containsKey(username)) {
                return "User already exists";
            }

            if (!username.matches("[a-zA-Z0-9_]+")) {
                return "Invalid Username";
            }

            int authToken = new Random().nextInt(10000) + 1;
            accounts.put(username, new Account(username, authToken));
            return String.valueOf(authToken);
        }

        //SHOWING THE ACCOUNTS THAT EXISTS
        private String showAccounts() {
            StringBuilder response = new StringBuilder();
            int index = 1;
            for (String username : accounts.keySet()) {
                response.append(index++).append(". ").append(username).append("\n");
            }
            return response.toString().trim();
        }

        //CLASS FOR USERS TO SEND MESSAGES
        private String sendMessage(String authToken, String recipient, String messageBody) {
            if (authToken == null || recipient == null || messageBody == null) {
                return "Missing required fields";
            }

            Account sender = getAccountByAuthToken(authToken);
            if (sender == null) {
                return "Invalid Auth Token";
            }

            Account receiver = accounts.get(recipient);
            if (receiver == null) {
                return "Username does not exist";
            }

            Message message = new Message(sender.username, recipient, messageBody);
            receiver.messageBox.add(message);
            return "OK";
        }


        //SHOWING INBOX OF USERS
        private String showInbox(String authToken) {
            Account account = getAccountByAuthToken(authToken);
            if (account == null) {
                return "Invalid Auth Token";
            }

            StringBuilder response = new StringBuilder();
            int index = 1;
            for (Message message : account.messageBox) {
                response.append(index++)
                        .append(" from:").append(message.sender)
                        .append(message.isRead ? "" : "*")
                        .append("\n");
            }
            return response.toString().trim();
        }

        //READ MESSAGES CLASS
        private String readMessage(String authToken, String messageId) {
            Account account = getAccountByAuthToken(authToken);
            if (account == null) {
                return "Invalid Auth Token";
            }

            try {
                int id = Integer.parseInt(messageId);
                if (id <= 0 || id > account.messageBox.size()) {
                    return "Message ID does not exist";
                }

                Message message = account.messageBox.get(id - 1);
                message.isRead = true;
                return "(" + message.sender + ") " + message.body;
            } catch (NumberFormatException e) {
                return "Invalid Message ID";
            }
        }

        //Class that DELETES messages
        private String deleteMessage(String authToken, String messageId) {
            Account account = getAccountByAuthToken(authToken);
            if (account == null) {
                return "Invalid Auth Token";
            }

            try {
                int id = Integer.parseInt(messageId);
                if (id <= 0 || id > account.messageBox.size()) {
                    return "Message does not exist";
                }

                account.messageBox.remove(id - 1);
                return "OK";
            } catch (NumberFormatException e) {
                return "Invalid Message ID";
            }
        }

        //Class to get the Auth Token of a user
        private Account getAccountByAuthToken(String authToken) {
            try {
                int token = Integer.parseInt(authToken);
                for (Account account : accounts.values()) {
                    if (account.authToken == token) {
                        return account;
                    }
                }
            } catch (NumberFormatException e) {
            }
            return null;
        }
    }

    //Class for the Accounts (QUESTION B. ACCOUNT)
    static class Account {
        String username;
        int authToken;
        List<Message> messageBox;

        public Account(String username, int authToken) {
            this.username = username;
            this.authToken = authToken;
            this.messageBox = new ArrayList<>();
        }
    }

    //Class for the Messages (QUESTION A. MESSAGE)
    static class Message {
        boolean isRead;
        String sender;
        String receiver;
        String body;

        public Message(String sender, String receiver, String body) {
            this.isRead = false;
            this.sender = sender;
            this.receiver = receiver;
            this.body = body;
        }
    }
}
