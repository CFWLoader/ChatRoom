package evan.server.utils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by cfwloader on 3/17/15.
 */
public class ChatServer {

    private ServerSocket serverSocket;

    private static Set<ClientService> clients;

    /*
    public static void main(String[] args) {

        ChatServer chatServer = new ChatServer();

        chatServer.clients = new ConcurrentSkipListSet<ClientService>();

        Socket socket;

        ClientService clientService;

        Thread clientServiceThread;

        while (true) {
            try {
                socket = chatServer.serverSocket.accept();
                System.out.println("A client joint." + socket.getPort());
                clientService = new ClientService(socket);
                chatServer.clients.add(clientService);
                clientServiceThread = new Thread(clientService);
                clientServiceThread.start();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("A client failed to connect.");
            }
        }
    }
    */

    public ChatServer() {
        try {
            serverSocket = new ServerSocket(4991);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public Set<ClientService> getClients() {
        return clients;
    }

    public void setClients(Set<ClientService> clients) {
        ChatServer.clients = clients;
    }

    public void broadcast(String msg){
        for(ClientService clientService : clients){
            try {
                clientService.getOutputStream().writeUTF(msg);
                clientService.getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            /*
            clientService.getPrintWriter().println(clientService.clientSocket.getPort() + ":  " + msg);
            clientService.getPrintWriter().flush();
            */
        }
    }

    /*
    private static class ClientService implements Runnable,Comparable<ClientService> {

        private Socket clientSocket;

        private DataInputStream inputStream;

        private DataOutputStream outputStream;

        ClientService(Socket socket) {
            clientSocket = socket;
            try {
                inputStream = new DataInputStream(clientSocket.getInputStream());
                outputStream = new DataOutputStream(clientSocket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }/* finally {

                clientSocket = null;
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(-2);
                }
            }
        }

        @Override
        public void run() {
            String words;
            while (true) {
                try {
                    words = inputStream.readUTF();
                    if(words == null)break;
                    System.out.println(words);
                    ChatServer.broadcast(words);
                } catch (EOFException eof){
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }/* finally {
                    try {
                        inputStream.close();
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.exit(-3);
                    }
                }
            }
            try {
                inputStream.close();
                inputStream.close();

                ChatServer.clients.remove(this);
                System.out.println("Client: " + clientSocket.getPort() + " exited.");

                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public DataOutputStream getOutputStream() {
            return outputStream;
        }

        @Override
        public int compareTo(ClientService o) {
            if(o == null)return -1;
            if(this == o)return 0;
            else return 1;
        }
    }
    */

    public int validateUser(ClientService clientService){

        String rawStr = null;

        try {
             rawStr = clientService.getInputStream().readUTF();
        } catch (IOException e) {
            //e.printStackTrace();
            return -1;
        }

        String[] loginInfo = rawStr.split("-");

        String username = loginInfo[2];
        String password = loginInfo[3];

        /*
        Here should be some data confirmation of user.
        But I don't implement it because the database configuration
        is hard to synchronize in other computer.
         */

        try {

            clientService.getOutputStream().writeUTF("ack");

            clientService.getOutputStream().flush();

            //clientService.getOutputStream().flush();

            /*
            int tickTock = 0;
            while(tickTock++ < 20){

                clientService.getOutputStream().writeUTF("ack");

                clientService.getOutputStream().flush();

                System.out.println("Trying " + tickTock);
            }
            */

            //System.out.println(System.currentTimeMillis());

            //System.out.println("Ack sent.");

            clientService.setUsername(username);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
