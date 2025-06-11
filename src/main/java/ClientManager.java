import java.io.*;
import java.net.Socket;
import java.util.*;


public class ClientManager implements Runnable {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String name;
    public static ArrayList<ClientManager> clients = new ArrayList<>();
    InputStreamReader inSR;

    public ClientManager(Socket socket) {
        try {
            this.socket = socket;
            inSR = new InputStreamReader(socket.getInputStream());
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(inSR);
            name = bufferedReader.readLine();
            clients.add(this);
            broadcastMessage("Server: " + name + " подключился к чату.");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);

        }
    }

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClient();
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

    public void removeClient() {
        clients.remove(this);
        broadcastMessage("SERVER: " + name + " покинул чат.");
    }

    private void broadcastMessage(String massageToSend) {
        for (ClientManager client : clients) {
            try {
                if (!client.name.equals(name)) {
                    client.bufferedWriter.write(name + ": " + massageToSend);
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    @Override
    public void run() {
        String massageFromClient;


        while (socket.isConnected()) {
            try {
                massageFromClient = bufferedReader.readLine();
//                System.out.println(massageFromClient);
                broadcastMessage(massageFromClient);
////                if (!chCode.equals(-2))  broadcastMessage("печатает");
//                ch = (char) ((int) chCode);
//                if (!chCode.equals(-2) && !chCode.equals(10)) sb.append(ch);
//                else if (chCode.equals(10)) {
//                    massageFromClient = sb.toString();
//                    System.out.println("строка " + massageFromClient);
//                    broadcastMessage(massageFromClient);
//                    sb.delete(0, sb.length());
//                }


            } catch (IOException | NullPointerException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }


        }
    }
}
