package ro.mta.licenta.badea.temporalUse;

import ro.mta.licenta.badea.models.EmployeeModel;
import ro.mta.licenta.badea.models.ResourceModel;

import java.util.ArrayList;

public class SelectedWorkersIDs {
    public static ArrayList<WorkerModel> listaIDs=new ArrayList<>();
    public static ArrayList<WorkerModel> finalList=new ArrayList<>();
    public static ArrayList<ResourceModel> listaResurse=new ArrayList<>();
    public static ArrayList<EmployeeModel> listaEmployees=new ArrayList<>();
    public static ArrayList<ResourceModel> listaResProiect=new ArrayList<>();

    public void printResourceList(){
        for(int i=0;i<listaResurse.size();i++){
            System.out.println(listaResurse.get(i).getDenumire()+" -> "+listaResurse.get(i).getCantitate());
        }
    }

    public void printEmployees(){
        System.out.println("********************\n");
        for(int i=0;i<listaEmployees.size();i++){
            System.out.println("ID: "+listaEmployees.get(i).getID());
            System.out.println("First: "+listaEmployees.get(i).getFirstname());
            System.out.println("Last: "+listaEmployees.get(i).getLastname());
            System.out.println("Role: "+listaEmployees.get(i).getRole());
            System.out.println("Lvl: "+listaEmployees.get(i).getPermision());

        }
    }



    public static ArrayList<ResourceModel> getListaResurseProiect() {
        return listaResProiect;
    }

    public ArrayList<EmployeeModel> getListaEmployees() {
        return listaEmployees;
    }

    public void addEmployee(EmployeeModel emp){ this.listaEmployees.add(emp);}

    public int getSizeOfEmployeesList(){return this.listaEmployees.size();}

    public void addResource(ResourceModel res){
        this.listaResurse.add(res);
    }

    public void addWorker(WorkerModel work){
        this.finalList.add(work);
        this.listaIDs.add(work);
    }

    public int getSizeOfWorkersList(){return listaIDs.size();}

    public void deleteFromList(WorkerModel worker){
        finalList.remove(worker);
    }

    public WorkerModel getWorkerbasedOnIndex(int index){
        return listaIDs.get(index);
    }

    public ResourceModel getResourceBasedOnIndex(int index){return listaResurse.get(index);}

    public int returnSizeOfResourceList(){return listaResurse.size();}

    public void clearList(){
        this.listaIDs.clear();
    }

    public void clearResourceList(){
        listaResurse.clear();

    }

    public void printListaResurse(){
        System.out.println("******");
        for(int i=0;i<listaResProiect.size();i++){
            System.out.println(listaResProiect.get(i).getDenumire() +" "+listaResProiect.get(i).getCantitate());
        }
        System.out.println("******");

    }


}
