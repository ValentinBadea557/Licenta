package ro.mta.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bouncycastle.util.encoders.Hex;
import org.json.JSONObject;
import ro.mta.server.dao.CompanieDAO;
import ro.mta.server.dao.ResourceDAO;
import ro.mta.server.dao.TaskGeneralDAO;
import ro.mta.server.dao.UserDAO;
import ro.mta.server.entities.User;
import ro.mta.server.handlers.HandleUser;
import ro.mta.server.handlers.HandlerAdmin;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        try {

            ServerSocket ss = new ServerSocket(5000);

            // client request
            while (true) {
                Socket s = null;
                try {
                    // socket object to receive incoming client requests
                    s = ss.accept();
                    System.out.println("A new client is connected : " + s);
                    // obtaining input and out streams
                    DataInputStream dis = new DataInputStream(s.getInputStream());
                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                    System.out.println("Assigning new thread for this client");

                    // create a new thread object
                    Thread t = new ClientHandler(s, dis, dos);

                    // Invoking the start() method
                    t.start();


                } catch (Exception e) {
                    s.close();
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        /////////

    }

}

class ClientHandler extends Thread {
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;

    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    void closeConnection() {
        try {
            s.close();
            dis.close();
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String receiveMessage() {
        try {
            String msg = "";
            msg = dis.readUTF();

            return msg;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Eroare receive";
    }

    void sendMessage(String msg) {
        try {
            dos.writeUTF(msg);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        System.out.println("Am intrat in run\n");
        boolean logged = false;
        boolean continueReceiving = true;

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();


        int admin = 0;

        while (continueReceiving) {
            HandleUser hndl = new HandleUser();
            String received = receiveMessage();

            JSONObject jsonreceived = new JSONObject(received);
            if (jsonreceived.get("Type").toString().equals("Exit")) {
                System.out.println("Clientul a inchis aplicatia");
                break;
            }

            hndl.setMessageReceived(received);
            hndl.analyzeMessage();
            String solvedMessage = hndl.getMessageToSend();
            System.out.println(solvedMessage);
            sendMessage(solvedMessage);

            if (jsonreceived.get("Type").toString().equals("Login")) {
                JSONObject json = new JSONObject(solvedMessage);
                if (!json.has("Response Login")) {
                    admin = json.getInt("admin");
                    if (admin == 1) {
                        while (true) {
                            HandlerAdmin hndlAdmin = new HandlerAdmin();
                            String receivedAdmin = receiveMessage();

                            System.out.println("Am primit :"+receivedAdmin);
                            /**Check if user closed the application*/
                            JSONObject adminJson = new JSONObject(receivedAdmin);
                            if (adminJson.get("Type").toString().equals("Exit")) {
                                System.out.println("Clientul a inchis aplicatia");
                                continueReceiving = false;
                                break;
                            }
                            if(adminJson.get("Type").toString().equals("Logout")){
                                System.out.println("Logout");
                                JSONObject logOutResponse=new JSONObject();
                                logOutResponse.put("Logout Response","ok");
                                sendMessage(logOutResponse.toString());
                                break;
                            }

                            hndlAdmin.setMessageReceived(receivedAdmin);
                            hndlAdmin.analyzeMessage();
                            solvedMessage = hndlAdmin.getMessageToSend();
                            sendMessage(solvedMessage);

                        }
                    } else {
                        while (true) {
                            String receiveUser=receiveMessage();
                            JSONObject userJSON=new JSONObject(receiveUser);
                            if(userJSON.get("Type").toString().equals("Logout")){
                                System.out.println("Logout");
                                JSONObject logOutResponse=new JSONObject();
                                logOutResponse.put("Logout Response","ok");
                                sendMessage(logOutResponse.toString());
                                break;
                            }
                        }
                    }
                }

            }


        }


    }
}