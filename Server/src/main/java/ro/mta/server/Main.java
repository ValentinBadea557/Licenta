package ro.mta.server;

import org.bouncycastle.util.encoders.Hex;
import org.json.JSONObject;
import ro.mta.server.dao.CompanieDAO;
import ro.mta.server.dao.ResourceDAO;
import ro.mta.server.dao.TaskGeneralDAO;
import ro.mta.server.dao.UserDAO;
import ro.mta.server.handlers.HandleUser;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args){
        HandleUser handler=new HandleUser();
        handler.analyzeMessage("AddTaskGeneral");





//        usr.addUserPlusCompany("alex","1234","Microsoft INC","Alex","Marius","Constanta","4342","alex@gmail.com",1);
    }
}
