package ro.mta.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bouncycastle.util.encoders.Hex;
import org.json.JSONObject;
import ro.mta.server.GsonDateFormat.LocalDateDeserializer;
import ro.mta.server.GsonDateFormat.LocalDateSerializer;
import ro.mta.server.dao.*;
import ro.mta.server.entities.Project;
import ro.mta.server.entities.Schedule;
import ro.mta.server.entities.Task;
import ro.mta.server.entities.User;
import ro.mta.server.handlers.HandleUser;
import ro.mta.server.handlers.HandlerAdmin;

import javax.net.ssl.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Date;

public class Main {
    private static SSLContext sslContext;
    private static final String fullPathToTrustStore = ".\\lib\\truststore";
    private static final String fullPathToKeyStore = ".\\lib\\keystore";
    private final String serverIP = "127.0.0.1";
     private static final String password = "valentin";
    //private static final String password = "abcd1234";
    private static KeyManagerFactory kmf;
    private static TrustManagerFactory tmf;

    public static void main(String[] args) throws CertificateException, IOException, NoSuchAlgorithmException {


        boolean status;
        status = CreateKeyTrustManagerFactory();
        if (status) {
            CreateSSLContext();
        }

        try {
            SSLServerSocketFactory ssf = sslContext.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(5061);
            // ServerSocket ss = new ServerSocket(5000);

            // client request
            while (true) {
                Socket s = null;
                try {
                    // socket object to receive incoming client requests
                    s = serverSocket.accept();
                    System.out.println("Socket:" + s);

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

    }


    private static boolean CreateKeyTrustManagerFactory() throws CertificateException, IOException, NoSuchAlgorithmException {
        boolean retval = true;
        char[] passPhrase = password.toCharArray();
        try {

            KeyStore ksKeys = KeyStore.getInstance("PKCS12");
            ksKeys.load(new FileInputStream(fullPathToKeyStore), passPhrase);
            KeyStore ksTrust = KeyStore.getInstance("PKCS12");
            ksTrust.load(new FileInputStream(fullPathToTrustStore), passPhrase);

            kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ksKeys, passPhrase);
            tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ksKeys);

        } catch (Exception ex) {
            retval = false;
        }
        System.out.println("CreateKeyTrustManagerFactory returned: " + retval);
        return retval;
    }

    private static boolean CreateSSLContext() {
        boolean retval = true;
        try {
            sslContext = SSLContext.getInstance("TLSv1.3");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        } catch (Exception ex) {
            retval = false;
        }
        System.out.println("CreateSSLContext returned: " + retval);
        return retval;
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
            System.out.println("Am trimis:" + msg.length());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        System.out.println("Am intrat in run\n");
        System.out.println("Socket in run:" + this.s);
        System.out.println("input:" + this.dis);
        System.out.println("output:" + this.dos);


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
            sendMessage(solvedMessage);


            if (jsonreceived.get("Type").toString().equals("Login")) {
                JSONObject json = new JSONObject(solvedMessage);
                if (!json.has("Response Login")) {
                    admin = json.getInt("admin");
                    if (admin == 1) {
                        while (true) {
                            HandlerAdmin hndlAdmin = new HandlerAdmin();
                            String receivedAdmin = receiveMessage();

                            System.out.println("Am primit :" + receivedAdmin);
                            /**Check if user closed the application*/
                            JSONObject adminJson = new JSONObject(receivedAdmin);
                            if (adminJson.get("Type").toString().equals("Exit")) {
                                System.out.println("Clientul a inchis aplicatia");
                                continueReceiving = false;
                                break;
                            }
                            if (adminJson.get("Type").toString().equals("Logout")) {
                                System.out.println("Logout");
                                JSONObject logOutResponse = new JSONObject();
                                logOutResponse.put("Logout Response", "ok");
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
                            String receiveUser = receiveMessage();
                            JSONObject userJSON = new JSONObject(receiveUser);
                            if (userJSON.get("Type").toString().equals("Exit")) {
                                System.out.println("Clientul a inchis aplicatia");
                                continueReceiving = false;
                                break;
                            }
                            if (userJSON.get("Type").toString().equals("Logout")) {
                                System.out.println("Logout");
                                JSONObject logOutResponse = new JSONObject();
                                logOutResponse.put("Logout Response", "ok");
                                sendMessage(logOutResponse.toString());
                                break;
                            }

                            hndl.setMessageReceived(receiveUser);
                            hndl.analyzeMessage();
                            solvedMessage = hndl.getMessageToSend();
                            sendMessage(solvedMessage);


                            System.out.println("Am trimis");
                        }
                    }
                }

            }


        }
        closeConnection(); /*adaugat recent**/
        System.out.println("Socketul " + s + " a fost inchis!\n");
    }

}