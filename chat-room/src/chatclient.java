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
    private JFrame frame; // Remove initialization
    private JTextArea messageArea = new JTextArea(20, 40);
    private JTextField textField = new JTextField(40);
    private String username;

    public chatclient() {
        // Prompt for username
        username = JOptionPane.showInputDialog(frame, "Enter your username:", "Username", JOptionPane.PLAIN_MESSAGE);
        if (username == null || username.trim().isEmpty()) {
            username = "Anonymous";
        }

        frame = new JFrame(username + " Inside Chat Room"); // Set JFrame title to username
        messageArea.setEditable(false);
        frame.getContentPane().add(new JScrollPane(messageArea), BorderLayout.CENTER);
        frame.getContentPane().add(textField, BorderLayout.SOUTH);
        frame.pack();

        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                out.println(username + ": " + textField.getText());
                textField.setText("");
            }
        });
    }

    private void run() throws IOException {
        Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        while (true) {
            String message = in.readLine();
            if (message == null) break;
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








