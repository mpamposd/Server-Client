# Request-Reply Messaging App

A distributed messaging system implementing a simple **request-reply protocol**. This project enables users to create accounts, send and receive messages, and manage their inboxes using a client-server architecture.

---

## Features

- **Account Management**: Create and view user accounts.
- **Messaging**: Send and receive messages between users.
- **Inbox Management**: View, read, and delete messages.
- **Authentication**: Secure user requests with unique tokens.

---

## Technologies Used

- **Language**: Java
- **Communication**: Socket-based protocol or Remote Method Invocation (RMI)
- **Concurrency**: Multithreading for handling multiple clients simultaneously

---

## System Architecture

### Server
- Listens for incoming client requests.
- Manages user accounts and their associated messages.
- Spawns threads to handle concurrent client requests.

### client
- Acts as the user interface for interacting with the server.
- Supports commands for creating accounts, sending messages, and managing the inbox.

### Core Components
- **Message**: Represents individual messages with attributes like sender, receiver, body, and read status.
- **Account**: Represents user accounts with details such as username, authentication token, and inbox.

---

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/messaging-app.git
   cd messaging-app
   ```

2. Compile the code:
   ```bash
   javac -d out src/*.java
   ```

3. Package into JAR files:
   ```bash
   jar -cf jars/Server.jar -C out server/
   jar -cf jars/Client.jar -C out client/
   ```

---

## Usage

### Start the Server
Run the server with the following command:
```bash
java -jar jars/Server.jar <port_number>
```
Example:
```bash
java -jar jars/Server.jar 5000
```

### Run the Client
Use the client to interact with the server:
```bash
java -jar jars/client.jar <server_ip> <port_number> <function_id> <arguments>
```
Example for creating a new account:
```bash
java -jar jars/client.jar localhost 5000 1 new_user
```

---

## Commands

| Function ID | Command Example                                             | Description                              |
|-------------|-------------------------------------------------------------|------------------------------------------|
| `1`         | `java -jarclient.jar localhost 5000 1 username`                 | Creates a new user account.          |
| `2`         | `java -jar client.jar localhost 5000 2 authToken`               | Lists all existing user accounts.    |
| `3`         | `java -jar client.jar localhost 5000 3 authToken recipient body`| Sends a message to another user.     |
| `4`         | `java -jar client.jar localhost 5000 4 authToken`               | Displays the user's inbox.           |
| `5`         | `java -jar client.jar localhost 5000 5 authToken message_id`    | Reads a specific message.            |
| `6`         | `java -jar client.jar localhost 5000 6 authToken message_id`    | Deletes a specific message.          |

---

## Directory Structure

```
messaging-app/
│
├── src/                # Source code
│   ├── Server.java
│   ├── Client.java
│
├── jars/               # Compiled JAR files
│   ├── Server.jar
│   └── client.jar
│
└── README.md           # Project documentation
```

---

## Contributing

Contributions are welcome! To contribute:
1. Fork the repository.
2. Create a feature branch.
3. Commit your changes.
4. Open a pull request.

---

## License


---

## Contact

For questions or support, please contact:
- **Name**: Dimitrios Bampos
- **Email**: mpamposd@csd.auth.gr
