package ro.mta.licenta.badea;

import ro.mta.licenta.badea.models.EmployeeModel;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Client {
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream out = null;
    private String AESkey = null;
    private int tip = -1;
    private static Client single_instance = null;
    private EmployeeModel currentUser;

    public Client(String address, int port) throws Exception {

        socket = new Socket(address, port);
        System.out.println("Connected");
        out = new DataOutputStream(socket.getOutputStream());
        input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

    }

    public static Client getInstance() {
        try {
            if (single_instance == null)
                single_instance = new Client("localhost", 5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return single_instance;
    }

    public static void deleteInstance() {
        single_instance = null;
    }

    public void setCurrentUser(EmployeeModel user){
        this.currentUser=user;
    }

    public void sendText(String msg) throws Exception{
        out.writeUTF(msg);
    }

    public String receiveText() throws Exception{
        String msg="";
        msg=input.readUTF();

        return msg;
    }


}
