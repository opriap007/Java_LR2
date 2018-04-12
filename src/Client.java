import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket;
        BufferedReader from_server;
        PrintWriter to_server;
        try {
            socket = new Socket("localhost", 9999);
            from_server = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            to_server = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String response;
        while (true) {
            System.out.print("Input \"get_sonnet\" command or \"bye\" to close: ");
            Scanner scanner = new Scanner(System.in);
            String msg = scanner.next();

            to_server.println(msg);
            to_server.flush();
            if (msg.equals("bye")) {
                break;
            }
            System.out.println("Wait for response...\n");
            while (!(response = from_server.readLine()).equals("~")) {
                System.out.println(response);
            }
            System.out.println();
        }

        from_server.close();
        to_server.close();
    }
}
