package ro.mta.licenta.badea.temporalUse;

import java.util.ArrayList;

public class SelectedWorkersIDs {
    public static ArrayList<WorkerModel> listaIDs=new ArrayList<>();

    public void addWorker(WorkerModel work){
        this.listaIDs.add(work);
    }

    public void clearList(){
        this.listaIDs.clear();
    }

    public void printList(){
        for(int i=0;i<this.listaIDs.size();i++){
            System.out.print(listaIDs.get(i)+" ");
        }
    }
}
