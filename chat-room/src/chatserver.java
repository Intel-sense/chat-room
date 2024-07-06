import java.io.*;
import java.net.*;
import java.util.*;

public class chatserver {
    private static final int PORT = 12345;
    private static Set<PrintWriter> clientWriters = new HashSet<>();
    private static Map<String, String> users = new HashMap<>();

    public static void main(String[] args) throws IOException {
        users.put("user1", "password1");
        users.put("user2", "password2");
        System.out.println("Chat server started...");
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
                new ClientHandler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println("Enter username:");
                String username = in.readLine();
                out.println("Enter password:");
                String password = in.readLine();
                if (!users.containsKey(username) || !users.get(username).equals(password)) {
                    out.println("Invalid credentials. Connection closed.");
                    return;
                }
                this.username = username;
                synchronized (clientWriters) {
                    clientWriters.add(out);
                }
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received from " + username + ": " + message);
                    synchronized (clientWriters) {
                        for (PrintWriter writer : clientWriters) {
                            writer.println(username + ": " + message);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Couldn't close socket: " + e.getMessage());
                }
                synchronized (clientWriters) {
                    clientWriters.remove(out);
                }
            }
        }
    }
}

