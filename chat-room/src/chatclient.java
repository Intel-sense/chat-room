import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

class chatclient {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    static final int SERVER_PORT = 12345;
    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame;
    private JTextArea messageArea = new JTextArea(20, 40);
    private JTextField textField = new JTextField(40);
    private String username;
    private String password;

    public chatclient() {
        frame = new JFrame("Login");
        username = JOptionPane.showInputDialog(frame, "Enter your username:", "Username", JOptionPane.PLAIN_MESSAGE);
        password = JOptionPane.showInputDialog(frame, "Enter your password:", "Password", JOptionPane.PLAIN_MESSAGE);
        if (username == null || username.trim().isEmpty()) {
            username = "Anonymous";
        }
        if (password == null || password.trim().isEmpty()) {
            password = "password";
        }
        frame.setTitle(username + " Inside Chat Room");

        messageArea.setEditable(false);
        frame.getContentPane().add(new JScrollPane(messageArea), BorderLayout.CENTER);
        frame.getContentPane().add(textField, BorderLayout.SOUTH);
        frame.pack();

        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
            }
        });
    }

    private void run() throws IOException {
        Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Send username and password for authentication
        out.println(username);
        out.println(password);

        while (true) {
            String message = in.readLine();
            if (message == null) break;
            if (message.startsWith("Enter username:") || message.startsWith("Enter password:")) {
                // Handle login prompts separately (don't display them in the chat)
                continue;
            }
            messageArea.append(message + "\n");
        }
    }

    public static void main(String[] args) throws Exception {
        chatclient client = new chatclient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }
}

