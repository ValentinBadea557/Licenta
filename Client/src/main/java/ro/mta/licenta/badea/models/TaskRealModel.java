package ro.mta.licenta.badea.models;

import java.time.LocalDate;
import java.util.HashMap;

public class TaskRealModel {

    private int ID;
    private String name;
    private int duration;
    private LocalDate day;
    private TaskModel originTask;
    private int parentID;
    private int startTime;
    private int completionTime;
    private HashMap<Integer, Integer> resourceUsage = new HashMap<Integer, Integer>(); //IDres / Quantity


    public int getQuantityOfResourceRequest(int idResource) {
        return resourceUsage.get(idResource);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public TaskModel getOriginTask() {
        return originTask;
    }

    public void setOriginTask(TaskModel originTask) {
        this.originTask = originTask;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public void addIntoHashMap(int idRes, int quantity) {
        this.resourceUsage.put(idRes, quantity);
    }

    public String printResourceUsage(){
        String retunedString = null;
        for (Integer key: resourceUsage.keySet()) {
            retunedString+="\nkey : " + key+" value : " + resourceUsage.get(key)+"\n";
        }
         return retunedString;
    }

    @Override
    public String toString() {
        return "TaskRealModel{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                ", day=" + day +
                ", originTask=" + originTask +
                ", parentID=" + parentID +
                ", startTime=" + startTime +
                ", completionTime=" + completionTime +
                "\n"+printResourceUsage()+
                '}';

    }

    public boolean checkIfTaskUseAResource(int idRes) {
        if (resourceUsage.containsKey(idRes)) {
            return true;
        } else {
            return false;
        }
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }
}
