package ro.mta.licenta.badea.temporalUse;

import java.util.ArrayList;

public class SelectedWorkersIDs {
    public static ArrayList<WorkerModel> listaIDs=new ArrayList<>();
    public static ArrayList<WorkerModel> finalList=new ArrayList<>();

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
    public void clearList(){
        this.listaIDs.clear();
    }

    public void printList(){
        for(int i=0;i<this.listaIDs.size();i++){
            System.out.print(listaIDs.get(i).getFullName()+"***|***\n");
        }
    }
}
