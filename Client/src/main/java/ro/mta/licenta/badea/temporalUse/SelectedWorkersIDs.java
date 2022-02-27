package ro.mta.licenta.badea.temporalUse;

import ro.mta.licenta.badea.models.EmployeeModel;
import ro.mta.licenta.badea.models.ResourceModel;

import java.util.ArrayList;

public class SelectedWorkersIDs {
    public static ArrayList<WorkerModel> listaIDs=new ArrayList<>();
    public static ArrayList<WorkerModel> finalList=new ArrayList<>();
    public static ArrayList<ResourceModel> listaResurse=new ArrayList<>();
    public static ArrayList<EmployeeModel> listaEmployees=new ArrayList<>();

    public void printResourceList(){
        for(int i=0;i<listaResurse.size();i++){
            System.out.println(listaResurse.get(i).getDenumire()+" -> "+listaResurse.get(i).getCantitate());
        }
    }

    public void printEmployees(){
        System.out.println("********************\n");
        for(int i=0;i<listaEmployees.size();i++){
            System.out.println(listaEmployees.get(i).getID());
        }
    }

    public void addEmployee(EmployeeModel emp){ this.listaEmployees.add(emp);}

    public void addResource(ResourceModel res){
        this.listaResurse.add(res);
    }

    public void addWorker(WorkerModel work){
        this.finalList.add(work);
        this.listaIDs.add(work);
    }

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

    public void clearResourceList(){listaResurse.clear();}

    public void printList(){
        for(int i=0;i<this.listaIDs.size();i++){
            System.out.print(listaIDs.get(i).getFullName()+"***|***\n");
        }
    }
}
