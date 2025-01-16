import java.io.*;
import java.net.*;

public class client {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java client <server_ip> <server_port> <fn_id> [<args>...]");
            return;
        }

        String serverIp = args[0];        int serverPort = Integer.parseInt(args[1]);
        String fnId = args[2];

        try (Socket socket = new Socket(serverIp, serverPort);
             OutputStream output = socket.getOutputStream();
             InputStream input = socket.getInputStream();
             PrintWriter writer = new PrintWriter(output, true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {

            StringBuilder request = new StringBuilder(fnId);
            for (int i = 3; i < args.length; i++) {
                request.append(" ").append(args[i]);
            }

            writer.println(request);

            String response;
            while ((response = reader.readLine()) != null) {
                System.out.println(response);
            }
        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
