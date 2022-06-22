package ro.mta.licenta.badea;

import javafx.scene.control.Alert;
import ro.mta.licenta.badea.models.EmployeeModel;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class Client {
    private DataInputStream InStream;
    private DataOutputStream OutStream;
    private static Mac sha256_HMAC;
    private static SecretKeySpec secret_key;
    private static String secret;
    private static String EncodedHeader;
    private static boolean connection;

    private String fullPathToTrustStore = ".\\lib\\truststore";
    private String fullPathToKeyStore = ".\\lib\\keystore";
    private String serverIP = "127.0.0.1";
    private String password = "valentin";
    int serverPort = 5061;

    private KeyManagerFactory kmf;
    private TrustManagerFactory tmf;
    private SSLContext sslContext;

    private static Client instance = null;
    private EmployeeModel currentUser;

    private Client() throws CertificateException, IOException, NoSuchAlgorithmException {
        this.connection=true;

        boolean status;
        status = CreateKeyTrustManagerFactory();
        if (status) {
            CreateSSLContext();
        }

        SSLSocketFactory ssf = sslContext.getSocketFactory();

        try {
            SSLSocket sslClientSocket = (SSLSocket) ssf.createSocket();
            InetSocketAddress connectAddress = new InetSocketAddress(serverIP, serverPort);
            sslClientSocket.connect(connectAddress);
            sslClientSocket.startHandshake();
            SSLSession session = sslClientSocket.getSession();
            System.out.println("Session: " + session);
            InStream = new DataInputStream(sslClientSocket.getInputStream());
            OutStream = new DataOutputStream(sslClientSocket.getOutputStream());
        } catch (IOException e) {
            this.connection=false;
          //  e.printStackTrace();
        }
    }

    private void closeConnection() throws IOException {
        InStream.close();
    }

    public static Client getInstance() {

        if (instance == null) {
            try {
                instance = new Client();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return instance;
    }

    public static void deleteInstance() {
        instance = null;
    }

    public void setCurrentUser(EmployeeModel user) {
        this.currentUser = user;
    }

    public void sendText(String msg) throws Exception {
        OutStream.writeUTF(msg);
        OutStream.flush();
    }

    public String receiveText() throws Exception {
        String recv = InStream.readUTF();
        return recv;
    }

    public EmployeeModel getCurrentUser() {
        return currentUser;
    }

    public static boolean getConnection() {
        return connection;
    }

    private boolean CreateKeyTrustManagerFactory() throws CertificateException, IOException, NoSuchAlgorithmException {
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

        } catch (KeyStoreException | FileNotFoundException | UnrecoverableKeyException e) {
            retval = false;
        }
        System.out.println("CreateKeyTrustManagerFactory returned: " + retval);
        return retval;
    }

    private boolean CreateSSLContext() {
        boolean retval = true;
        try {
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        } catch (Exception ex) {
            retval = false;
        }
        System.out.println("Create context returned: " + retval);
        return retval;
    }
}
