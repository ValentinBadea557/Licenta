package ro.mta.server.dao;

import com.google.gson.*;
import org.bouncycastle.util.encoders.Hex;
import org.json.JSONObject;
import ro.mta.server.Database;
import ro.mta.server.GsonDateFormat.LocalDateDeserializer;
import ro.mta.server.GsonDateFormat.LocalDateSerializer;
import ro.mta.server.entities.*;


import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class UserDAO implements IUserDAO {
    public User utilizator;

    public UserDAO() {
        utilizator = new User();
    }

    public UserDAO(User usr) {
        utilizator = usr;
    }


    @Override
    public String addUserPlusCompany(String username, String parola, String nume_companie, String Nume, String Prenume, String Adrs, String Telefon, String mail, int admin) {
        JSONObject result = new JSONObject();
        int checkunicity = checkUsernameUnicity(username);
        if (checkunicity == 1) {
            CompanieDAO comp = new CompanieDAO();
            int addComp = comp.addCompany(nume_companie);


            if (addComp == -1) {
                //eroare
                result.put("Result Register", "Error trying to add Company!");

            } else if (addComp == 0) {
                //exista deja
                result.put("Result Register", "This company's name is already used!");

            } else {
                //adaugare cu succes
                addOnlyUser(username, parola, nume_companie, Nume, Prenume, Adrs, Telefon, mail, 1);
                result.put("Result Register", "ok");
            }
        } else {
            result.put("Result Register", "Username already used!");
        }
        return result.toString();
    }

    @Override
    public String addOnlyUser(String username, String parola, String nume_companie, String Nume, String Prenume, String Adrs, String Telefon, String mail, int admin) {

        JSONObject json = new JSONObject();

        int unicity = checkUsernameUnicity(username);
        if (unicity == 1) {
            CompanieDAO cmp = new CompanieDAO();
            int id_companie = cmp.getCompanyIDbasedOnName(nume_companie);


            Database db = new Database();
            Connection con = db.getConn();


            String hashpass = hashFunction(parola);
            String sql = "Insert into Useri(Username,Parola,ID_Companie) "
                    + "Values ('" + username + "','" + hashpass + "','" + id_companie + "')";

            try {
                Statement stmt = con.createStatement();
                stmt.execute(sql);
            } catch (Exception e) {
                json.put("Response Add User", "Error");
                e.printStackTrace();
            }
            int id = getUserIDbasedOnUsername(username);
            String sql2 = "Insert into Date_Personale(ID_User,Nume,Prenume,Adresa,Telefon,Email,Ore_Max_Munca,Ore_Munca) "
                    + "Values (" + id + ",'" + Nume + "','" + Prenume + "','" + Adrs + "','" + Telefon + "','" + mail + "'," + 8 + "," + 0 + ")";
            try {
                Statement stmt = con.createStatement();
                stmt.execute(sql2);
            } catch (Exception e) {
                json.put("Response Add User", "Error");
                e.printStackTrace();
            }

            if (admin == 1) {
                int id_user = getUserIDbasedOnUsername(username);
                addAdminbasedOnUserID(id_user);
            }
            json.put("Response Add User", "ok");
        } else {
            json.put("Response Add User", "Duplicate username");
        }

        return json.toString();

    }

    /**
     * @param username
     * @return id-ul corespunzator usernameului sau -1 daca returneaza eroare
     */
    @Override
    public int getUserIDbasedOnUsername(String username) {
        Database db = new Database();
        Connection con = db.getConn();
        int id = -1;
        String sql = "Select ID_User from Useri "
                + "where Username='" + username + "'";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                id = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            id = -1;
        }
        return id;
    }

    /**
     * @param username
     * @return 1 daca usernameul este unic, 0 daca se repeta
     */
    @Override
    public int checkUsernameUnicity(String username) {
        Database db = new Database();
        Connection con = db.getConn();

        int unicity = 1;
        String sql = "Select Username from Useri "
                + "Where Username='" + username + "'";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                unicity = 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            unicity = -1;
        }

        return unicity;
    }

    @Override
    public int addAdminbasedOnUserID(int userID) {
        Database db = new Database();
        Connection con = db.getConn();

        String sql = "Insert into Admini(ID_User) "
                + "Values (" + userID + ")";
        try {
            Statement stmt = con.createStatement();
            stmt.execute(sql);
            System.out.println("Admin adaugat cu succes");
        } catch (SQLException e) {
            System.out.println("Eroare la add admin");
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public String login(String user, String password) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        Database db = new Database();
        Connection con = db.getConn();
        String hashPassword = hashFunction(password);
        int results = 0;
        String result = null;
        User userReturned = new User();

        String sql = "Select * from Useri U " +
                "inner join Date_Personale DP " +
                "on U.ID_User=DP.ID_User " +
                "where U.Username='" + user + "' AND U.Parola='" + hashPassword + "'";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                results++;
                int idCompanie;

                userReturned.setID(rs.getInt(1));
                userReturned.setAdmin(checkIfUserIsAdmin(rs.getInt(1)));
                userReturned.setUsername(rs.getString(2));
                userReturned.setPassword(password);
                idCompanie = rs.getInt(4);
                Companie company = getCompanybasedOnID(idCompanie);
                userReturned.setCompany(company);
                userReturned.setLastname(rs.getString(7));
                userReturned.setFirstname(rs.getString(8));
                userReturned.setAddr(rs.getString(9));
                userReturned.setPhone(rs.getString(10));
                userReturned.setEmail(rs.getString(11));
                userReturned.setOre_max_munca(rs.getInt(12));
                userReturned.setOre_munca(rs.getInt(13));

                result = gson.toJson(userReturned);
            }
            if (results != 0)
                return result;
        } catch (SQLException e) {
            e.printStackTrace();
            result = "Eroare";
        }
        return "Eroare";
    }


    @Override
    public String hashFunction(String pass) {
        String sha256hex = new String();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(
                    pass.getBytes(StandardCharsets.UTF_8));
            sha256hex = new String(Hex.encode(hash));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return sha256hex;

    }

    @Override
    public int checkIfUserIsAdmin(int IDuser) {
        Database db = new Database();
        Connection con = db.getConn();
        String sql = "Select * from Admini " +
                "Where ID_User=" + IDuser;
        int result = 0;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                result = 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Companie getCompanybasedOnID(int IDcompany) {
        Database db = new Database();
        Connection con = db.getConn();
        String sql = "Select * from Companii " +
                "Where ID_Companie=" + IDcompany;

        Companie company = new Companie();

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                company.setId(rs.getInt(1));
                company.setNume(rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return company;
    }

    /**
     * @param IDuser
     * @return returneaza un String ce contine 2 vectori, vector cu ID uri proiecte si un vector cu numele acestora.
     */
    @Override
    public String getListOfActiveProjects(int IDuser) {
        Database db = new Database();
        Connection con = db.getConn();
        String sql = "Select * from Proiecte P " +
                "Inner join Proiecte_Useri PU " +
                "on P.ID_Proiect=PU.ID_Proiect " +
                "where PU.ID_USER=" + IDuser;

        JsonArray ids = new JsonArray();
        JsonArray names = new JsonArray();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                ids.add(rs.getInt(1));
                names.add(rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JSONObject obj = new JSONObject();
        obj.put("IDs", ids);
        obj.put("Names:", names);

        String jsonString = obj.toString();
        return jsonString;
    }

    /***
     *
     * @param idUser
     * @return user object based on ID
     */
    @Override
    public User getUserbasedOnID(int idUser) {
        Database db = new Database();
        Connection con = db.getConn();
        User userReturned = new User();

        String sql = "Select * from Useri U " +
                "inner join Date_Personale DP " +
                "on U.ID_User=DP.ID_User " +
                "Where U.ID_User=" + idUser;

        String result = null;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {

                userReturned.setID(rs.getInt(1));
                userReturned.setAdmin(checkIfUserIsAdmin(rs.getInt(1)));
                userReturned.setUsername(rs.getString(2));
//                int idCompanie = rs.getInt(4);
//                Companie company = getCompanybasedOnID(idCompanie);
//                userReturned.setCompany(company);
                userReturned.setLastname(rs.getString(7));
                userReturned.setFirstname(rs.getString(8));
                userReturned.setAddr(rs.getString(9));
                userReturned.setPhone(rs.getString(10));
                userReturned.setEmail(rs.getString(11));
                userReturned.setOre_max_munca(rs.getInt(12));
                userReturned.setOre_munca(rs.getInt(13));

            }
        } catch (SQLException e) {
            e.printStackTrace();
            result = "Eroare";
        }
        return userReturned;
    }

    @Override
    public String selectListOfEmployeesNotAdmins(int ID_company) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();


        Database db = new Database();
        Connection con = db.getConn();

        String sql = "Select U.ID_User,DP.Nume,DP.Prenume from Useri U " +
                "left join Admini A " +
                "on U.ID_User=A.ID_User " +
                "inner join Date_Personale DP " +
                "on U.ID_User=DP.ID_User " +
                "where A.ID_Admin is null ";

        Companie company = new Companie();
        String result = null;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                User user = new User();
                user.setID(rs.getInt(1));
                user.setLastname(rs.getString(2));
                user.setFirstname(rs.getString(3));
                company.addEmployee(user);
            }

            result = gson.toJson(company);

        } catch (SQLException e) {
            JSONObject response = new JSONObject();
            response.put("Response", "not ok");
            result = response.toString();
            e.printStackTrace();
        }

        return result;
    }

    /**
     * @param jsonProject
     * @return
     */
    @Override
    public String createNewProject(String jsonProject) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        JSONObject response = new JSONObject();

        Database db = new Database();
        Connection con = db.getConn();

        Project project = gson.fromJson(jsonProject, Project.class);


        /**Insert Project*/
        String sql = "Insert into Proiecte " +
                "Values ('" + project.getCoordonator().getID() + "','" + project.getNume() + "','" + project.getDescriere() +
                "','" + project.getStarttime() + "','" + project.getDeadline() + "',0) ;\n" +
                "Select SCOPE_IDENTITY();";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                project.setID(rs.getInt(1));
                System.out.println("ID proiect creat:" + rs.getInt(1));
            }
        } catch (SQLException e) {
            response.put("Response Create", "Error");
            e.printStackTrace();
        }

        /**Insert Project's Resources*/
        ArrayList<Resource> listaRes = project.getListaResurseCurente();
        for (int i = 0; i < listaRes.size(); i++) {
            String sqlResurseAdd = "Insert into Resurse_Proiecte " +
                    "Values (" + project.getID() + "," + listaRes.get(i).getID() + "," + listaRes.get(i).getCantitate() + ");";

            try {
                Statement stmt = con.createStatement();
                stmt.execute(sqlResurseAdd);
            } catch (Exception e) {
                response.put("Response Create", "Error");
                e.printStackTrace();
            }
        }

        /**Insert roles and permissions**/
        ArrayList<User> listaEmployees = project.getListaOameni();
        for (int i = 0; i < listaEmployees.size(); i++) {
            int id_permission = 0;
            String sql1 = "SELECT * from Permisiuni " +
                    "Where Nivel='" + listaEmployees.get(i).getPermission() + "'; ";
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql1);
                ResultSetMetaData meta = rs.getMetaData();
                while (rs.next()) {
                    id_permission = rs.getInt(1);
                }
            } catch (SQLException e) {
                response.put("Response Create", "Error");
                e.printStackTrace();
            }
            int id_role = createRoleOrGetRoleID(listaEmployees.get(i).getRole());
            System.out.println("ID role:" + id_role + " IDproject=" + project.getID() + " IDuser=" + listaEmployees.get(i).getID() + " Id permision=" + id_permission);
            String sql2 = "Insert into Proiecte_Useri " +
                    "Values (" + project.getID() + "," + listaEmployees.get(i).getID() + "," + id_role + "," + id_permission + ");";

            try {
                Statement stmt = con.createStatement();
                stmt.execute(sql2);
            } catch (Exception e) {
                response.put("Response Create", "Error");
                e.printStackTrace();
            }
        }

        /**Insert echipa*/
        ArrayList<Team> echipe = project.getListaEchipe();
        for (int i = 0; i < echipe.size(); i++) {
            String sqlTeam = "Insert into Echipe " +
                    "Values ('" + echipe.get(i).getName() + "','" + echipe.get(i).getStarttime() + "','" + echipe.get(i).getDeadline() + "" +
                    "'," + project.getID() + "); " +
                    "Select SCOPE_IDENTITY();";
            int id_team = 0;
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sqlTeam);
                ResultSetMetaData meta = rs.getMetaData();
                while (rs.next()) {
                    id_team = rs.getInt(1);
                }
            } catch (SQLException e) {
                response.put("Response Create", "Error");
                e.printStackTrace();
            }

            /**Add every employee to his team*/
            ArrayList<User> listaUseriEchipa = echipe.get(i).getListaUseri();
            System.out.println(gson.toJson(echipe.get(i)));
            for (int j = 0; j < listaUseriEchipa.size(); j++) {
                String sql2Team = "Insert into Echipe_Useri " +
                        "Values (" + id_team + "," + listaUseriEchipa.get(j).getID() + "); ";
                try {
                    Statement stmt = con.createStatement();
                    stmt.execute(sql2Team);
                } catch (Exception e) {
                    response.put("Response Create", "Error");
                    e.printStackTrace();
                }
            }
        }

        /**Insert Taskuri***/
        ArrayList<Task> listaTaskuri = project.getListaTaskuri();
        for (int i = 0; i < listaTaskuri.size(); i++) {
            String sqlNormal = null;
            if (listaTaskuri.get(i).getTaskParinte() != null) {

                sqlNormal = "Insert into Taskuri(Denumire,Periodicitate,Durata,Starttime,Deadline,ID_Proiect,ID_Task_Parinte)  " +
                        "Values ('" + listaTaskuri.get(i).getName() + "','" + listaTaskuri.get(i).getPeriodicity() + "' ," +
                        listaTaskuri.get(i).getDuration() + ",'" + listaTaskuri.get(i).getStarttime() + "','" +
                        listaTaskuri.get(i).getDeadline() + "' , " + project.getID() + "," + getIDofNormalTask(listaTaskuri.get(i).getTaskParinte()) + "); " +
                        "Select SCOPE_IDENTITY();";

            } else {
                sqlNormal = "Insert into Taskuri(Denumire,Periodicitate,Durata,Starttime,Deadline,ID_Proiect) " +
                        "Values ('" + listaTaskuri.get(i).getName() + "','" + listaTaskuri.get(i).getPeriodicity() + "' ," +
                        listaTaskuri.get(i).getDuration() + ",'" + listaTaskuri.get(i).getStarttime() + "','" +
                        listaTaskuri.get(i).getDeadline() + "' , " + project.getID() + "); " +
                        "Select SCOPE_IDENTITY();";
            }
            int id_task = 0;
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sqlNormal);
                ResultSetMetaData meta = rs.getMetaData();
                while (rs.next()) {
                    id_task = rs.getInt(1);
                }
            } catch (SQLException e) {
                response.put("Response Create", "Error");
                e.printStackTrace();
            }

            listaTaskuri.get(i).setID(id_task);

            try {
                createRealTasks(listaTaskuri.get(i));
            } catch (SQLException e) {
                e.printStackTrace();
            }


            String sqlAssignTo = "Insert into Taskuri_Useri " +
                    "Values (" + id_task + "," + listaTaskuri.get(i).getExecutant().getID() + ") ;";

            try {
                Statement stmt = con.createStatement();
                stmt.execute(sqlAssignTo);
            } catch (Exception e) {
                response.put("Response Create", "Error");
                e.printStackTrace();
            }

            ArrayList<Resource> listaResurseTask = listaTaskuri.get(i).getListaResurse();
            for (int j = 0; j < listaResurseTask.size(); j++) {
                System.out.println("ID TASK:" + listaTaskuri.get(i).getID() + " ID RES+" + listaResurseTask.get(j).getID());
                String sqlTaskResource = "Insert into Resurse_Taskuri " +
                        "Values (" + listaResurseTask.get(j).getID() + "," + listaTaskuri.get(i).getID() + "," + listaResurseTask.get(j).getCantitate() + "); ";

                try {
                    Statement stmt = con.createStatement();
                    stmt.execute(sqlTaskResource);
                } catch (Exception e) {
                    response.put("Response Create", "Error");
                    e.printStackTrace();
                }

            }

        }
        if (!response.has("Response Create")) {
            response.put("Final Response", "ok");
        } else {
            response.put("Final Response", "SQL Exception");
            deleteAllDataAboutAProject(project.getID());
        }

        System.out.println(response.toString());
        return response.toString();
    }

    @Override
    public String addNewTaskToProject(String jsonTask) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        Database db = new Database();
        Connection con = db.getConn();

        JSONObject response = new JSONObject();
        Task task = gson.fromJson(jsonTask, Task.class);

        String sqlNormal = null;
        if (task.getTaskParinte() != null) {

            sqlNormal = "Insert into Taskuri(Denumire,Periodicitate,Durata,Starttime,Deadline,ID_Proiect,ID_Task_Parinte)  " +
                    "Values ('" + task.getName() + "','" + task.getPeriodicity() + "' ," +
                    task.getDuration() + ",'" + task.getStarttime() + "','" +
                    task.getDeadline() + "' , " + task.getID_Proiect() + "," + getIDofNormalTask(task.getTaskParinte()) + "); " +
                    "Select SCOPE_IDENTITY();";

        } else {
            sqlNormal = "Insert into Taskuri(Denumire,Periodicitate,Durata,Starttime,Deadline,ID_Proiect) " +
                    "Values ('" + task.getName() + "','" + task.getPeriodicity() + "' ," +
                    task.getDuration() + ",'" + task.getStarttime() + "','" +
                    task.getDeadline() + "' , " + task.getID_Proiect() + "); " +
                    "Select SCOPE_IDENTITY();";
        }
        int id_task = 0;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlNormal);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                id_task = rs.getInt(1);
                System.out.println("ID TASK:" + id_task);
                task.setID(id_task);
            }
        } catch (SQLException e) {
            response.put("Response Create", "Error");
            e.printStackTrace();
        }

        System.out.println("**\n" + gson.toJson(task) + "\n****");
        try {
            createRealTasks(task);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Executant id:" + task.getExecutant().getID());
        String sqlAssignTo = "Insert into Taskuri_Useri " +
                "Values (" + id_task + "," + task.getExecutant().getID() + ") ;";

        try {
            Statement stmt = con.createStatement();
            stmt.execute(sqlAssignTo);
        } catch (Exception e) {
            response.put("Response Create", "Error");
            e.printStackTrace();
        }

        ArrayList<Resource> listaResurseTask = task.getListaResurse();
        for (int j = 0; j < listaResurseTask.size(); j++) {
            System.out.println("ID TASK:" + task.getID() + " ID RES+" + listaResurseTask.get(j).getID());
            String sqlTaskResource = "Insert into Resurse_Taskuri " +
                    "Values (" + listaResurseTask.get(j).getID() + "," + task.getID() + "," + listaResurseTask.get(j).getCantitate() + "); ";

            try {
                Statement stmt = con.createStatement();
                stmt.execute(sqlTaskResource);
            } catch (Exception e) {
                response.put("Response Create", "Error");
                e.printStackTrace();
            }

        }

        Schedule sch = new Schedule();
        sch.setTheSchedulingForEntireProject(task.getID_Proiect());
        if (!response.has("Response Create")) {
            response.put("Final Response", "ok");
        } else {
            response.put("Final Response", "SQL Exception");
        }

        System.out.println(response.toString());
        return response.toString();

    }


    @Override
    public int createRealTasks(Task task) throws SQLException {
        Database db = new Database();
        Connection con = db.getConn();

        int id_task = task.getID();
        String name = task.getName();
        int duration = task.getDuration();
        String periodicity = task.getPeriodicity();
        Task parent = task.getTaskParinte();
        int id_parinte;

        LocalDate dataCurenta = task.getStarttime();
        LocalDate deadline = task.getDeadline();

        while (dataCurenta.isBefore(deadline)) {
            if (parent != null) {
                id_parinte = getIDofNormalTask(parent);
                int id_parentReal = 0;

                String sqlGetID = "Select * from Taskuri_Reale " +
                        "Where ID_Task=" + id_parinte + " and Zi='" + dataCurenta + "';";

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sqlGetID);
                int results = 0;
                while (rs.next()) {
                    id_parentReal = rs.getInt(1);

                    results++;
                }
                String sql = null;


                if (results == 1) {
                    sql = "Insert into Taskuri_Reale(Nume,Durata,Zi,ID_Task,ID_Parinte,Startpoint) " +
                            "Values('" + name + "'," + duration + ",'" + dataCurenta + "'," + id_task + "," + id_parentReal + ",NULL);";
                } else {
                    sql = "Insert into Taskuri_Reale(Nume,Durata,Zi,ID_Task,Startpoint) " +
                            "Values('" + name + "'," + duration + ",'" + dataCurenta + "'," + id_task + ",NULL);";
                }
                stmt = con.createStatement();
                stmt.execute(sql);

            } else {
                String sql = "Insert into Taskuri_Reale(Nume,Durata,Zi,ID_Task,Startpoint) " +
                        "Values('" + name + "'," + duration + ",'" + dataCurenta + "'," + id_task + ",NULL);";

                Statement stmt = con.createStatement();
                stmt.execute(sql);

            }

            switch (periodicity) {
                case "No periodicity":
                    return 0;
                case "Daily":
                    dataCurenta = dataCurenta.plusDays(1);
                    break;
                case "Weekly":
                    dataCurenta = dataCurenta.plusWeeks(1);
                    break;
                case "Monthly":
                    dataCurenta = dataCurenta.plusMonths(1);
                    break;
            }

        }

        return 0;
    }


    @Override
    public int createRoleOrGetRoleID(String role) {
        Database db = new Database();
        Connection con = db.getConn();

        String sql = "Select * from Roluri " +
                "Where Denumire='" + role + "'; ";
        int id = 0;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (id != 0) {
            return id;
        } else {
            String sql1 = "Insert into Roluri " +
                    "values ('" + role + "') " +
                    "Select SCOPE_IDENTITY();";

            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                ResultSetMetaData meta = rs.getMetaData();
                while (rs.next()) {
                    id = rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return id;
        }

    }

    @Override
    public int getIDofNormalTask(Task task) {
        Database db = new Database();
        Connection con = db.getConn();

        String name = task.getName();
        String peridicity = task.getPeriodicity();
        int duration = task.getDuration();
        LocalDate start = task.getStarttime();
        LocalDate deadline = task.getDeadline();

        String sql = "Select * from Taskuri " +
                "Where Denumire='" + name + "' and " +
                "Periodicitate='" + peridicity + "' and Durata=" + duration + " and " +
                "Starttime='" + start + "' and Deadline='" + deadline + "' ;";

        int id = 0;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Returnez id:" + id);
        return id;
    }

    @Override
    public String viewProjects(int idUser) {
        Database db = new Database();
        Connection con = db.getConn();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        ArrayList<Project> listaProiecte = new ArrayList<>();

        String sql = "select distinct P.ID_Proiect,P.Denumire,P.Descriere,P.Finished from Proiecte P " +
                "inner join Proiecte_Useri PU " +
                "on P.ID_Proiect=PU.ID_Proiect " +
                "Where PU.ID_USER=" + idUser + " OR P.ID_Coordonator=" + idUser;

        String returned = null;
        JSONObject errorjson = new JSONObject();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                Project proiectTemp = new Project();
                proiectTemp.setID(rs.getInt(1));
                proiectTemp.setNume(rs.getString(2));
                proiectTemp.setDescriere(rs.getString(3));
                proiectTemp.setFinished(rs.getInt(4));
                listaProiecte.add(proiectTemp);
            }
            returned = gson.toJson(listaProiecte);
        } catch (SQLException e) {
            errorjson.put("Error", "SQL Exception");
            returned = errorjson.toString();
            e.printStackTrace();
        }

        System.out.println(returned);
        return returned;
    }

    @Override
    public String getEveryInfoAboutProject(int idProject) {
        Database db = new Database();
        Connection con = db.getConn();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        Project project = new Project();
        return null;
    }


    /**
     * Use on createProject function to delete all inserted data in case of sql exception
     */
    @Override
    public int deleteAllDataAboutAProject(int idProject) {
        Database db = new Database();
        Connection con = db.getConn();
        String sql = "delete RT from Resurse_Taskuri RT\n" +
                "inner join Taskuri T\n" +
                "ON T.ID_Task=RT.ID_Task\n" +
                "where T.ID_Proiect=" + idProject + ";\n" +
                "delete TU from Taskuri_Useri TU\n" +
                "inner join Taskuri T\n" +
                "ON T.ID_Task=TU.ID_Task\n" +
                "where T.ID_Proiect=" + idProject + ";\n" +
                "delete RP from Resurse_Proiecte RP\n" +
                "inner join Proiecte P\n" +
                "on RP.ID_Proiect=P.ID_Proiect\n" +
                "WHERE P.ID_Proiect=" + idProject + ";\n" +
                "delete PU from Proiecte_Useri PU\n" +
                "where PU.ID_Proiect=" + idProject + ";\n" +
                "delete EU from Echipe_Useri EU\n" +
                "inner join Echipe E\n" +
                "on EU.ID_Echipa=E.ID_Echipa\n" +
                "WHERE E.ID_Proiect=" + idProject + ";\n" +
                "delete from Echipe\n" +
                "where Echipe.ID_Proiect=" + idProject + ";\n" +
                "delete TR from Taskuri_Reale TR\n" +
                "inner join Taskuri T\n" +
                "on TR.ID_Task=T.ID_Task\n" +
                "where T.ID_Proiect=" + idProject + ";\n" +
                "delete from Taskuri\n" +
                "where Taskuri.ID_Proiect=" + idProject + ";\n" +
                "delete from Proiecte \n" +
                "where Proiecte.ID_Proiect=" + idProject + ";\n";

        int result = 0;
        try {
            Statement stmt = con.createStatement();
            stmt.execute(sql);
            result = 1;
        } catch (SQLException e) {
            e.printStackTrace();
            result = 0;
        }
        return result;
    }


    /***Functions used by administrators**/
    @Override
    public String AdministratorViewAllEmployees(int IDuseradmin) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        Database db = new Database();
        Connection con = db.getConn();
        String sql = "Select * from Useri U " +
                "Inner join Companii C " +
                "on U.ID_Companie=C.ID_Companie " +
                "where U.ID_User=" + IDuseradmin;

        Companie company = new Companie();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                company.setId(rs.getInt(5));
                company.setNume(rs.getString(6));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql2 = "Select * from Useri " +
                "Where ID_Companie=" + company.getId();

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql2);
            ResultSetMetaData meta = rs.getMetaData();
            int idUserCurrent;
            while (rs.next()) {
                idUserCurrent = rs.getInt(1);
                company.addEmployee(getUserbasedOnID(idUserCurrent));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String returnString = gson.toJson(company);

        return returnString;
    }


}


