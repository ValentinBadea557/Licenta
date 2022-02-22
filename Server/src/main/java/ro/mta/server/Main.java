package ro.mta.server;

import org.bouncycastle.util.encoders.Hex;
import org.json.JSONObject;
import ro.mta.server.dao.CompanieDAO;
import ro.mta.server.dao.ResourceDAO;
import ro.mta.server.dao.TaskGeneralDAO;
import ro.mta.server.dao.UserDAO;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args){
        TaskGeneralDAO general=new TaskGeneralDAO();
        LocalDateTime s=LocalDateTime.parse("2022-02-22T02:00:10");
        LocalDateTime d=LocalDateTime.parse("2022-03-09T02:00:15");


        general.getIDBasedOnName("proiect first", s,d);



//        usr.addUserPlusCompany("alex","1234","Microsoft INC","Alex","Marius","Constanta","4342","alex@gmail.com",1);
    }
}
