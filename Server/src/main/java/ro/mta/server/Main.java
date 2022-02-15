package ro.mta.server;

import org.bouncycastle.util.encoders.Hex;
import org.json.JSONObject;
import ro.mta.server.dao.CompanieDAO;
import ro.mta.server.dao.ResourceDAO;
import ro.mta.server.dao.UserDAO;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args){

        ResourceDAO rs=new ResourceDAO();
      rs.getResourcebasedOnID(1);
      rs.getResourceIDbasedOnName("laptop");

//        usr.addUserPlusCompany("alex","1234","Microsoft INC","Alex","Marius","Constanta","4342","alex@gmail.com",1);
    }
}
