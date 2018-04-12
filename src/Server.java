import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Server {
    public static void main(String args[]) {
        String filename = "sonnets.txt";
        ArrayList<String> sonnets = new ArrayList<>();
        try {
            ServerSocket listen = new ServerSocket(9999);
            while (true) {
                System.out.println("Connection waiting ...");
                Socket socket = listen.accept();
                System.out.println("Client connected!\n");

                BufferedReader from_client = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter to_client = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    System.out.println("Wait for a message..");
                    String msg = from_client.readLine();
                    if (msg.equals("bye")) {
                        to_client.close();
                        from_client.close();
                        socket.close();
                        System.out.println("Client disconnected!\n");
                        break;
                    }
                    if (msg.equals("get_sonnet")) {
                        File inputFile = new File(filename);
                        if (!inputFile.exists()) {
                            to_client.println("Unable to open file: " + filename);
                            break;
                        }

                        System.out.println("Reading from file " + filename);
                        BufferedReader input = new BufferedReader(new FileReader(inputFile));
                        String line, sonnet = "";
                        while ((line = input.readLine()) != null) {
                            sonnet += line + "\n";
                            if (line.equals("~")) {
                                sonnets.add(sonnet);
                                sonnet = "";
                            }
                        }
                        input.close();
                        String random_sonnet = sonnets.get((new Random()).nextInt(sonnets.size()));
                        System.out.println("Sending random sonnet..\n");
                        to_client.println(random_sonnet);
                    } else {
                        System.out.println("Unknown command: " + msg + "\n");
                        to_client.println("Unknown command: " + msg);
                        to_client.println("~");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}
