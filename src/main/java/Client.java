import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String name;


    public Client(Socket socket, String userName) {
        this.socket = socket;
        name = userName;

        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите своё имя: ");
        String name = scanner.nextLine();
        Socket socket = new Socket("localhost", 1300);
        Client client = new Client(socket, name);
        client.listenForMessage();
        client.sendMessage();


    }

    public void sendMessage() {

        try {
            bufferedWriter.write(name);
            bufferedWriter.newLine();
            bufferedWriter.flush();
String message;
            int read;
            Terminal terminal;
            terminal = TerminalBuilder.builder().jna(true).system(true).build();
            terminal.enterRawMode();
            NonBlockingReader reader = terminal.reader();
            while (!socket.isClosed()) {
                read = reader.read(1000);
                terminal.writer().println(read);
                terminal.writer().flush();


                bufferedWriter.write(read);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
            reader.close();
            terminal.close();
            closeEverything(socket, bufferedReader, bufferedWriter);
        } catch (IOException e) {
            e.printStackTrace();
            closeEverything(socket, bufferedReader, bufferedWriter);

        } finally {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

    }

    public void listenForMessage() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                String messageFromGroup;
                while (!socket.isClosed()) {

                    try {
                        messageFromGroup = bufferedReader.readLine();

                        System.out.println(messageFromGroup);

                    } catch (IOException e) {
                        e.printStackTrace();
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        break;
                    }
                }

            }
        }).start();
    }

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
