

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class ClientOrig {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String name;


    public ClientOrig(Socket socket, String userName) {
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
        ClientOrig client = new ClientOrig(socket, name);
        client.listenForMessage();
        client.sendMessage();


    }

    public void sendMessage() {

        try {
            bufferedWriter.write(name);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            Scanner scanner = new Scanner(System.in);

            String message;
            while (!socket.isClosed()) {
                while (scanner.nextLine().isBlank()) {
                    bufferedWriter.write( "печатает");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
                message = scanner.nextLine();
                if (message.equals("exit")) break;
                bufferedWriter.write( message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
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
