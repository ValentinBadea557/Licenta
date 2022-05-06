package ro.mta.server.entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ro.mta.server.Database;
import ro.mta.server.GsonDateFormat.LocalDateDeserializer;
import ro.mta.server.GsonDateFormat.LocalDateSerializer;
import ro.mta.server.GsonDateFormat.LocalDateTimeDeserializer;
import ro.mta.server.GsonDateFormat.LocalDateTimeSerializer;
import ro.mta.server.dao.ResourceDAO;
import ro.mta.server.dao.UserDAO;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Schedule {
    private ArrayList<TaskReal> listaTaskuri = new ArrayList<>();
    private ArrayList<User> listaUseri = new ArrayList<>();
    private ArrayList<Resource> listaResurse = new ArrayList<>(); // + DURATIONS
    private HashMap<Integer, Integer> schedulingMap = new HashMap<Integer, Integer>(); //id task real / starttime unit
    private ArrayList<LocalDate> listaDateCalendaristice = new ArrayList<>();
    private ArrayList<TaskReal> listaTaskuriImposibleToSchedule = new ArrayList<>();

    int found = 0;


    public void setTheSchedulingForEntireProject(int idProject) {

        getAllDates(idProject);


//        for(Resource r:listaResurse){
//            System.out.println(r.getDenumire()+" cantitate:"+r.getCantitate());
//        }

        int i = 1;
        for (LocalDate dataCalendaristica : listaDateCalendaristice) {
            i++;
            System.out.println("Data curenta:" + dataCalendaristica + " Size rs:" + listaResurse.size());
            //  printDetailsAboutRes();

            getListOfResources(5013);
            getListOfRealTasks(5013, dataCalendaristica);
            listaTaskuri.get(0).printResourceUsage();
            getListOfPeople(5013, dataCalendaristica);
            translateUsersIntoResources();
            fillWithZeroWhenResourceIsNotUsed();
            // printDetailsAboutRes();



            if (startScheduling()) {
                for (TaskReal task : listaTaskuri) {
                    System.out.println(task.getID() + " " + task.getName() + " S:" + task.getStartTime() + " D:" + task.getCompletionTime());
                    setStartPointsForTask(task.getID(), task.getStartTime());
                }
            } else {
                for (TaskReal task : listaTaskuri) {
                    System.out.println(task.getID() + " " + task.getName() + " S:" + task.getStartTime() + " D:" + task.getCompletionTime());
                    setStartPointsForTask(task.getID(), task.getStartTime());
                }
                System.out.println("Lista pentru care nu se poate realiza ordonarea:");
                for (TaskReal task : listaTaskuri) {
                    if (task.getCompletionTime() > 24) {
                        listaTaskuriImposibleToSchedule.add(task);
                        setNullStartPointForTask(task.getID());
                        System.out.println(task.getName());
                    }
                }
            }


//            for(Resource r:listaResurse){
//                found=0;
//                calculatePositions(r);
//            }


            listaTaskuri.clear();
            listaUseri.clear();
            listaResurse.clear();
            listaTaskuriImposibleToSchedule.clear();

        }

        //  calculatePositions(listaResurse.get(0));


    }

    public void getAllDates(int idProject) {
        listaDateCalendaristice.clear();
        Database db = new Database();
        Connection con = db.getConn();

        String sql = "select distinct TR.Zi from Taskuri_Reale TR";

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();

            while (rs.next()) {
                LocalDate aux;
                aux = rs.getDate(1).toLocalDate();
                listaDateCalendaristice.add(aux);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void printDates() {
        for (LocalDate d : listaDateCalendaristice) {
            System.out.println(d);
        }
    }


    /*** daca resursa nu e utilizata, adauga 0 la cantitatea utilizata*/
    public void fillWithZeroWhenResourceIsNotUsed() {
        for (int i = 0; i < listaTaskuri.size(); i++) {
            for (int j = 0; j < listaResurse.size(); j++) {
                if (!listaTaskuri.get(i).checkIfTaskUseAResource(listaResurse.get(j).getID())) {
                    listaTaskuri.get(i).addIntoHashMap(listaResurse.get(j).getID(), 0);
                }
            }
        }
    }

    public void translateUsersIntoResources() {
        for (TaskReal task : listaTaskuri) {
            int idUser = getExecutantOfRealTask(task.getID());
            idUser = idUser * (-1);
            task.addIntoHashMap(idUser, 1);
        }
        for (User usr : listaUseri) {
            int id = usr.getID();
            id = id * (-1);
            Resource rs = new Resource();
            rs.setID(id);
            rs.setDenumire(usr.getFirstname() + " " + usr.getLastname());
            rs.setCantitate(1);
            listaResurse.add(rs);
        }
    }

    public void printUseri() {
        for (User usr : listaUseri) {
            System.out.println(usr.getFirstname() + " " + usr.getLastname());
        }
    }

    public void printDetailsAboutRes() {
        for (TaskReal task : listaTaskuri) {
            System.out.println(task.getID() + " " + task.getName() + " Size rs array:" + listaResurse.size());
            for (Resource rs : listaResurse) {
                System.out.println("\t " + rs.getDenumire() + " :" + task.getQuantityOfResourceRequest(rs.getID()));
            }
        }
    }

    public boolean startScheduling() {
        for (int i = 0; i < listaTaskuri.size(); i++) {
            setStarttimeAndCompletionTime(listaTaskuri.get(i));
        }

//        listaTaskuri.get(0).changeResUsage(2, 7);
//        System.out.println(listaTaskuri.get(0).quantityOfResourceRequest(listaResurse.get(0).getID()));

        boolean isSolved = false;
        boolean continueAlgorithm = false;
        while (!continueAlgorithm) {
            continueAlgorithm = algorithm();
            recalculateCompletion();
            int currentMakespan = getMakespan();
            if (currentMakespan > 24) {
                isSolved = false;
                System.out.println(currentMakespan);
                System.out.println("Nu exista solutie!");
                break;
            }
            isSolved = true;
        }
        System.out.println("Is solved? : " + isSolved);
        //   printStartTimesAndCompletions();
        //   System.out.println();
        return isSolved;
    }

    public int getMakespan() {
        int max = listaTaskuri.get(0).getCompletionTime();
        for (int i = 0; i < listaTaskuri.size(); i++) {
            if (listaTaskuri.get(i).getCompletionTime() > max) {
                max = listaTaskuri.get(i).getCompletionTime();
            }
        }

        return max;
    }

    public void recalculateCompletion() {
        for (int i = 0; i < listaTaskuri.size(); i++) {
            TaskReal task = listaTaskuri.get(i);

            if (task.getParentID() != 0) {
                for (int j = 0; j < listaTaskuri.size(); j++) {
                    if (listaTaskuri.get(j).getID() == task.getParentID()) {
                        if (task.getStartTime() < listaTaskuri.get(j).getCompletionTime()) {
                            task.setStartTime(listaTaskuri.get(j).getCompletionTime());
                        }
                    }
                }
            }
            task.setCompletionTime(task.getStartTime() + task.getDuration());
        }
    }

    public void calculatePositions(Resource res) {
        System.out.println("Resource: " + res.getDenumire() + " Quantity:" + res.getCantitate() + " Makespan:" + getMakespan());

        String[][] positionMat = new String[res.getCantitate()][];

        for (int i = 0; i < res.getCantitate(); i++) {
            positionMat[i] = new String[getMakespan()];
        }

        // System.out.println("Incepe pozitionarea!");

        for (int i = 0; i < res.getCantitate(); i++) {
            //   System.out.println();
            for (int j = 0; j < getMakespan(); j++) {
                positionMat[i][j] = "0";
                //   System.out.print(positionMat[i][j] + " ");
            }
        }


        String m[][] = new String[res.getCantitate()][getMakespan()];
        for (int i = 0; i < res.getCantitate(); i++) {
            for (int j = 0; j < getMakespan(); j++) {
                m[i][j] = "0";
            }
        }


        RecursiveFunction(0, 0, m, 0, res);

        //  System.out.println("\nFinalizare RESURSA\n");
    }

    public int getNoTimeslot(int timeslot) {
        int no = 0;
        for (int i = 0; i < listaTaskuri.size(); i++) {
            TaskReal task = listaTaskuri.get(i);
            if (task.getStartTime() == timeslot)
                no++;
        }

        return no;
    }

    public TaskReal getTaskNoTimeslot(int timeslot, int placedTimeslot) {
        int no = 0;
        for (int i = 0; i < listaTaskuri.size(); i++) {
            TaskReal task = listaTaskuri.get(i);
            if (task.getStartTime() == timeslot) {
                if (no == placedTimeslot)
                    return task;
                else
                    no++;
            }
        }

        return null;
    }


    //placed time splot = index task
    public void RecursiveFunction(int timeslot, int placedTimeslot, String[][] m, int noTaskPut, Resource res) {
        if (found == 1)
            return;
        if (noTaskPut == listaTaskuri.size()) {
            //   System.out.println("Am pus :" + noTaskPut);
            //   System.out.println("FINISH");
            printMatrix(m, res.getCantitate(), getMakespan());

            found = 1;
            return;
        }
        if (timeslot > getMakespan())
            return;
        if (getNoTimeslot(timeslot) == 0)
            RecursiveFunction(timeslot + 1, 0, m, noTaskPut, res);
        else if (placedTimeslot == getNoTimeslot(timeslot))
            RecursiveFunction(timeslot + 1, 0, m, noTaskPut, res);
        else {
            TaskReal task = getTaskNoTimeslot(timeslot, placedTimeslot);

            int nrResurse = task.getQuantityOfResourceRequest(res.getID());

            for (int rand = 0; rand < (res.getCantitate() - nrResurse + 1); rand++) {
                int start = task.getStartTime();
                int durata = task.getDuration();

                // System.out.println("Testez pentru :" + task.getName());
                boolean result = checkSpace(rand, start, nrResurse, durata, m);
                if (result) {
                    for (int linie = rand; linie < rand + nrResurse; linie++) {
                        for (int coloana = start; coloana < start + durata; coloana++) {
                            m[linie][coloana] = task.getName();
                        }
                    }
                    // printMatrix(m,res.getCantitate(),getMakespan());

                    RecursiveFunction(timeslot, placedTimeslot + 1, m, noTaskPut + 1, res);
//                    if(found == 1)
//                        return;
                    for (int linie = rand; linie < rand + nrResurse; linie++) {
                        for (int coloana = start; coloana < start + durata; coloana++) {
                            m[linie][coloana] = "0";
                        }
                    }
                }
            }
        }
    }

//    public void func(int myJ) {
//        for (int i = 0; i < listaTaskuri.size(); i++) {
//            TaskReal task = listaTaskuri.get(i);
//            int nrResurse = task.quantityOfResourceRequest(2);
//
//            for (int rand = myJ; rand < (7 - nrResurse + 1); rand++) {
//                int start = task.getStartTime();
//                int durata = task.getDuration();
//
//                System.out.println("Testez pentru :" + task.getName());
//                boolean res = checkSpace(rand, start, nrResurse, durata, m);
//                if (res) {
//                    for (int linie = rand; linie < rand + nrResurse; linie++) {
//                        for (int coloana = start; coloana < start + durata; coloana++) {
//                            positionMatrixR1[linie][coloana] = task.getName();
//                        }
//                    }
//                    break;
//                }
//                if (res == false && rand == (7 - nrResurse)) {
//                    System.out.println("NU SE POATE");
//                    // deleteFromMatrix(listaTaskuri.get(i-1));
//                    // printMatrix();
//                    // i-=2;
//                }
//
//            }
//
////            printMatrix();
//            System.out.println();
//
//
//        }
//    }

    public void printMatrix(String[][] m, int linii, int coloana) {
        System.out.println();

        for (int i = 0; i < linii; i++) {
            for (int j = 0; j < coloana; j++) {
                String p = "00";
                if (!m[i][j].equals("0"))
                    p = m[i][j];
                System.out.print(p + " ");
            }
            System.out.println();
        }
    }

    public boolean checkSpace(int linie, int coloana, int nrRes, int durata, String m[][]) {
        boolean ok = true;
        for (int i = linie; i < linie + nrRes; i++) {
            for (int j = coloana; j < coloana + durata; j++) {
//                System.out.println("[" + i + "]" + "[" + j + "]");
                if (!m[i][j].equals("0")) {
                    ok = false;
                }
            }
        }
        return ok;
    }

    public int getNumberOfTasksPerResource(Resource res) {
        int no = 0;
        for (int i = 0; i < listaTaskuri.size(); i++) {
            if (listaTaskuri.get(i).getQuantityOfResourceRequest(res.getID()) > 0) {
                no++;
            }
        }
        return no;
    }

    public boolean algorithm() {


        ArrayList<Integer> listaCompletion = new ArrayList<>();
        for (int i = 0; i < listaTaskuri.size(); i++) {
            listaCompletion.add(listaTaskuri.get(i).getCompletionTime());
        }
        Collections.sort(listaCompletion);


        /** Sort on starttime*/
        Collections.sort(listaTaskuri);
//        for (int z = 0; z < listaTaskuri.size(); z++) {
//            System.out.print(listaTaskuri.get(z).getName() + " ");
//        }
//        System.out.println();

        /**Print StartTimes and Completions*/
        //    printStartTimesAndCompletions();


        /**list of completions times*/
        List<Integer> listWithoutDuplicates = listaCompletion.stream().distinct().collect(Collectors.toList());

        int old_value = listaTaskuri.get(0).getStartTime();
        TaskReal tobeModified = listaTaskuri.get(0);

        for (int i = 0; i < listWithoutDuplicates.size(); i++) {

            int t = listWithoutDuplicates.get(i);
            // System.out.println("\nCurrent T:" + t);
            old_value = listaTaskuri.get(0).getStartTime();
            tobeModified = listaTaskuri.get(0);

            for (int k = 0; k < listaResurse.size(); k++) {
                int o = 0;
                ArrayList<TaskReal> F = new ArrayList<>();

                //    System.out.println("RESURSA CURENTA : " + listaResurse.get(k).getID());

                for (int j = 0; j < listaTaskuri.size(); j++) {

                    // System.out.println("Primul modificat: "+tobeModified.getName());
                    int start = listaTaskuri.get(j).getStartTime();
                    int completion = listaTaskuri.get(j).getCompletionTime();
                    int resRequest = listaTaskuri.get(j).getQuantityOfResourceRequest(listaResurse.get(k).getID());

                    if ((start < t) && (completion >= t) && (resRequest > 0)) {
                        //        System.out.println(listaTaskuri.get(j).getName() + " -> O =" + o + " + " + resRequest);
                        o += resRequest;
                        F.add(listaTaskuri.get(j));

                        /**TO BE MODIFIED*/
                        if (listaTaskuri.get(j).getStartTime() > old_value) {
                            tobeModified = listaTaskuri.get(j);
                            old_value = listaTaskuri.get(j).getStartTime();
                        } else {
                            int area = tobeModified.getCompletionTime() * tobeModified.getQuantityOfResourceRequest(listaResurse.get(k).getID());
                            int curentArea = listaTaskuri.get(j).getCompletionTime() * listaTaskuri.get(j).getQuantityOfResourceRequest(listaResurse.get(k).getID());
                            if (curentArea > area) {
                                tobeModified = listaTaskuri.get(j);

                            }
                        }
                    }

                    if (o > listaResurse.get(k).getCantitate()) {

                        //   System.out.println(o + " > " + listaResurse.get(k).getCantitate());
                        int currentStart = tobeModified.getStartTime();
                        currentStart++;
                        tobeModified.setStartTime(currentStart);
                        //   System.out.println("Modific " + tobeModified.getName() + " val noua:" + tobeModified.getStartTime());

                        return false;
                    }
                }
            }
        }
        return true;

    }

    public void printStartTimesAndCompletions() {
        System.out.print("Starttimes: ");
        for (int i = 0; i < listaTaskuri.size(); i++) {
            System.out.print(listaTaskuri.get(i).getName() + ":" + listaTaskuri.get(i).getStartTime() + " ");
        }
        System.out.print("\nCompletions: ");
        for (int i = 0; i < listaTaskuri.size(); i++) {
            System.out.print(listaTaskuri.get(i).getName() + ":" + listaTaskuri.get(i).getCompletionTime() + " ");
        }
        System.out.println();

    }

    /**
     * Seteaza starttime si completion time in functie de task-ul parinte
     */
    public void setStarttimeAndCompletionTime(TaskReal task) {
        for (int i = 0; i < listaTaskuri.size(); i++) {
            if (listaTaskuri.get(i).getParentID() != 0) {
                int parentCompletion = 0;
                for (int k = 0; k < listaTaskuri.size(); k++) {
                    if (listaTaskuri.get(k).getID() == listaTaskuri.get(i).getParentID()) {
                        parentCompletion = listaTaskuri.get(k).getCompletionTime();
                        break;
                    }
                }
                int duration = listaTaskuri.get(i).getDuration();
                listaTaskuri.get(i).setStartTime(parentCompletion);
                listaTaskuri.get(i).setCompletionTime(parentCompletion + duration);
            } else {
                listaTaskuri.get(i).setStartTime(0);
                int start = listaTaskuri.get(i).getStartTime();
                int duration = listaTaskuri.get(i).getDuration();

                listaTaskuri.get(i).setCompletionTime(start + duration);
            }
        }

    }

    public void setNullStartPointForTask(int idTaskReal){
        Database db = new Database();
        Connection con = db.getConn();

        String sql = "update Taskuri_Reale " +
                "Set Startpoint=NULL " +
                "Where Taskuri_Reale.ID=" + idTaskReal;

        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setStartPointsForTask(int idTaskReal, int startPoint) {
        Database db = new Database();
        Connection con = db.getConn();

        String sql = "update Taskuri_Reale " +
                "Set Startpoint=" + startPoint + " " +
                "Where Taskuri_Reale.ID=" + idTaskReal;

        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getExecutantOfRealTask(int idTaskReal) {
        Database db = new Database();
        Connection con = db.getConn();

        String sql = "select TU.ID_User from Taskuri_Reale TR\n" +
                "inner join Taskuri T\n" +
                "on TR.ID_Task=T.ID_Task\n" +
                "inner join Taskuri_Useri TU\n" +
                "on TU.ID_Task=T.ID_Task\n" +
                "where TR.ID=" + idTaskReal;

        int idUser = 0;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();

            while (rs.next()) {
                idUser = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idUser;
    }

    public void getListOfRealTasks(int idProject, LocalDate day) {
        if (this.listaTaskuri != null)
            this.listaTaskuri.clear();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        Database db = new Database();
        Connection con = db.getConn();

        String sql = "select * from Taskuri_Reale TR\n" +
                "inner join Taskuri T\n" +
                "on TR.ID_Task=T.ID_Task\n" +
                "where TR.Zi='" + day + "' and T.ID_Proiect=" + idProject;

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();

            ArrayList listaLocalTasks = new ArrayList<>();
            while (rs.next()) {
                int idTask = rs.getInt(1);
                TaskReal task = new TaskReal();

                listaLocalTasks.add(task.setResourcesFromDB(idTask));
            }


            this.listaTaskuri = listaLocalTasks;

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void getListOfPeople(int idProject, LocalDate data) {
        listaUseri.clear();
        Database db = new Database();
        Connection con = db.getConn();

        String sql = "select distinct PU.ID_USER from Taskuri_Reale TR\n" +
                "inner join Taskuri T\n" +
                "on TR.ID_Task=T.ID_Task\n" +
                "inner join Proiecte P\n" +
                "on T.ID_Proiect=P.ID_Proiect\n" +
                "inner join Proiecte_Useri PU\n" +
                "on P.ID_Proiect=Pu.ID_Proiect\n" +
                "where Zi='" + data + "' and P.ID_Proiect=" + idProject;

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();

            while (rs.next()) {
                ResourceDAO res = new ResourceDAO();
                UserDAO usrD = new UserDAO();
                User usr = new User();
                int idUser = rs.getInt(1);

                usr = usrD.getUserbasedOnID(idUser);

                this.listaUseri.add(usr);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void getListOfResources(int idProject) {
        this.listaResurse.clear();

        Database db = new Database();
        Connection con = db.getConn();

        String sql = "select RP.ID_Resursa,RP.Cantitate from Proiecte P\n" +
                "inner join Resurse_Proiecte RP\n" +
                "on P.ID_Proiect=RP.ID_Proiect\n" +
                "where P.ID_Proiect=" + idProject;

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();

            while (rs.next()) {
                ResourceDAO res = new ResourceDAO();
                Resource resource = new Resource();
                int idRes = rs.getInt(1);
                resource = res.getFullResourceBasedOnID(idRes);
                resource.setCantitate(rs.getInt(2));

                this.listaResurse.add(resource);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
