package ro.mta.server.entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ro.mta.server.Database;
import ro.mta.server.GsonDateFormat.LocalDateDeserializer;
import ro.mta.server.GsonDateFormat.LocalDateSerializer;
import ro.mta.server.GsonDateFormat.LocalDateTimeDeserializer;
import ro.mta.server.GsonDateFormat.LocalDateTimeSerializer;
import ro.mta.server.dao.ResourceDAO;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Schedule {
    private ArrayList<TaskReal> listaTaskuri = new ArrayList<>();
    private ArrayList<Resource> listaResurse = new ArrayList<>(); // + DURATIONS
    private HashMap<Integer, Integer> schedulingMap = new HashMap<Integer, Integer>(); //id task real / starttime unit

    public void fillWithZeroWhenResourceIsNotUsed() {
        for (int i = 0; i < listaTaskuri.size(); i++) {
            for (int j = 0; j < listaResurse.size(); j++) {
                if (!listaTaskuri.get(i).checkIfTaskUseAResource(listaResurse.get(j).getID())) {
                    listaTaskuri.get(i).addIntoHashMap(listaResurse.get(j).getID(), 0);
                }
            }
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

    }


    public void startScheduling() {
        for (int i = 0; i < listaTaskuri.size(); i++) {
            setStarttimeAndCompletionTime(listaTaskuri.get(i));
        }


        int z=1;

        while (z<12) {
            System.out.println("Iteratia nr: " + z);
            algorithm();
            recalculateCompletion();

            printStartTimesAndCompletions();
            System.out.println("\n*******FINISH ITERATIE*******");
            z++;

            System.out.println();
        }
    }

    public void recalculateCompletion() {
        for (int i = 0; i < listaTaskuri.size(); i++) {
            TaskReal task = listaTaskuri.get(i);
//            if (task.getParentID() != 0) {
//                int parentStart = 0;
//                for (int j = 0; j < listaTaskuri.size(); j++) {
//                    if (listaTaskuri.get(j).getID() == task.getParentID()) {
//                        parentStart = listaTaskuri.get(j).getCompletionTime();
//                        task.setStartTime(parentStart);
//                        task.setCompletionTime(parentStart + task.getDuration());
//                    }
//                }
//            } else {
//                task.setCompletionTime(task.getStartTime() + task.getDuration());
//            }
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


    public boolean algorithm() {


        ArrayList<Integer> listaCompletion = new ArrayList<>();
        for (int i = 0; i < listaTaskuri.size(); i++) {
            listaCompletion.add(listaTaskuri.get(i).getCompletionTime());
        }
        Collections.sort(listaCompletion);


        /** Sort on starttime*/
        Collections.sort(listaTaskuri);
        for (int z = 0; z < listaTaskuri.size(); z++) {
            System.out.print(listaTaskuri.get(z).getName() + " ");
        }
        System.out.println();

        /**Print StartTimes and Completions*/
        printStartTimesAndCompletions();


        /**list of completions times*/
        List<Integer> listWithoutDuplicates = listaCompletion.stream().distinct().collect(Collectors.toList());

        int old_value = listaTaskuri.get(0).getStartTime();
        TaskReal tobeModified = listaTaskuri.get(0);

        for (int i = 0; i < listWithoutDuplicates.size(); i++) {

            int t = listWithoutDuplicates.get(i);
            System.out.println("\nCurrent T:" + t);
            old_value = listaTaskuri.get(0).getStartTime();
            tobeModified = listaTaskuri.get(0);

            for (int k = 0; k < listaResurse.size(); k++) {
                int o = 0;
                ArrayList<TaskReal> F = new ArrayList<>();

                System.out.println("RESURSA CURENTA : " + listaResurse.get(k).getID());

                for (int j = 0; j < listaTaskuri.size(); j++) {

                   // System.out.println("Primul modificat: "+tobeModified.getName());
                    int start = listaTaskuri.get(j).getStartTime();
                    int completion = listaTaskuri.get(j).getCompletionTime();
                    int resRequest = listaTaskuri.get(j).quantityOfResourceRequest(listaResurse.get(k).getID());

                    if ((start < t) && (completion >= t) && (resRequest > 0)) {
                        System.out.println(listaTaskuri.get(j).getName() + " -> O =" + o + " + " + resRequest);
                        o += resRequest;
                        F.add(listaTaskuri.get(j));

                        /**TO BE MODIFIED*/
                        if (listaTaskuri.get(j).getStartTime() > old_value) {
                            tobeModified = listaTaskuri.get(j);
                            old_value = listaTaskuri.get(j).getStartTime();
                        } else {
                            int area = tobeModified.getCompletionTime() * tobeModified.quantityOfResourceRequest(listaResurse.get(k).getID());
                            int curentArea = listaTaskuri.get(j).getCompletionTime() * listaTaskuri.get(j).quantityOfResourceRequest(listaResurse.get(k).getID());
                            if (curentArea > area) {
                                tobeModified = listaTaskuri.get(j);
                                //  System.out.println("Primul modificat va fi:"+tobeModified.getName());
                            }
                        }
                    }

                    if (o > listaResurse.get(k).getCantitate()) {

                        System.out.println(o + " > " + listaResurse.get(k).getCantitate());
                        int currentStart = tobeModified.getStartTime();
                        currentStart++;
                        tobeModified.setStartTime(currentStart);
                        System.out.println("Modific " + tobeModified.getName() + " val noua:" + tobeModified.getStartTime());

                        return false;
                    }
                }
            }
        }
        System.out.println("CORECT****!!!!");
        return true;

    }

    public void printStartTimesAndCompletions() {
        for (int i = 0; i < listaTaskuri.size(); i++) {
            System.out.print(listaTaskuri.get(i).getName() + ":" + listaTaskuri.get(i).getStartTime() + " ");
        }
        System.out.println();
        for (int i = 0; i < listaTaskuri.size(); i++) {
            System.out.print(listaTaskuri.get(i).getName() + ":" + listaTaskuri.get(i).getCompletionTime() + " ");
        }
        System.out.println();

    }

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
