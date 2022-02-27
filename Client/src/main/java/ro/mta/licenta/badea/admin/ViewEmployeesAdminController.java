package ro.mta.licenta.badea.admin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import ro.mta.licenta.badea.models.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ViewEmployeesAdminController implements Initializable {

    @FXML
    private TableColumn<EmployeeModel, String> addressColumn;

    @FXML
    private TableColumn<EmployeeModel, String> firstNameColumn;

    @FXML
    private TableColumn<EmployeeModel, String> lastNameColumn;

    @FXML
    private TableColumn<EmployeeModel, String> mailColumn;

    @FXML
    private TableColumn<EmployeeModel, String> phoneColumn;

    @FXML
    private TableColumn<EmployeeModel, String> usernameColumn;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<EmployeeModel> tableEmployeesView;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String test="{\n" +
                "  \"id\": 1,\n" +
                "  \"nume\": \"Tesla Inc.\",\n" +
                "  \"listaEmployees\": [\n" +
                "    {\n" +
                "      \"ID\": 9,\n" +
                "      \"username\": \"valentin.badea\",\n" +
                "      \"firstname\": \"Valentin\",\n" +
                "      \"lastname\": \"Badea\",\n" +
                "      \"addr\": \"Bucuresti\",\n" +
                "      \"phone\": \"112\",\n" +
                "      \"email\": \"badea@mta.ro\",\n" +
                "      \"ore_max_munca\": 8,\n" +
                "      \"ore_munca\": 0,\n" +
                "      \"admin\": 0\n" +
                "    },\n" +
                "    {\n" +
                "      \"ID\": 11,\n" +
                "      \"username\": \"matei.mihai\",\n" +
                "      \"firstname\": \"Mihai\",\n" +
                "      \"lastname\": \"Matei\",\n" +
                "      \"addr\": \"Brasov\",\n" +
                "      \"phone\": \"1111\",\n" +
                "      \"email\": \"matei@yaho.com\",\n" +
                "      \"ore_max_munca\": 8,\n" +
                "      \"ore_munca\": 0,\n" +
                "      \"admin\": 1\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        CompanyModel company= new CompanyModel();

        company=gson.fromJson(test, CompanyModel.class);

        company.printDetails();
        ArrayList<EmployeeModel> listaAuxEmployees=new ArrayList<>();
        listaAuxEmployees=company.getListaPersonal();
        for(int i=0;i<listaAuxEmployees.size();i++){
            employeesList.add(listaAuxEmployees.get(i));
            System.out.println(employeesList.get(i).getMail());
        }


        /**Set table**/

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        mailColumn.setCellValueFactory(new PropertyValueFactory<>("mail"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        tableEmployeesView.setItems(employeesList);


        /**Set search engine**/
        FilteredList<EmployeeModel> filteredData = new FilteredList<>(employeesList, b -> true);

        searchField.textProperty().addListener((observable,oldValue,newValue)->{
            filteredData.setPredicate(projectModel ->{

                if(newValue.isEmpty() || newValue.isBlank() || newValue==null){
                    return true;
                }
                String searchKeyword=newValue.toLowerCase();
                if(projectModel.getFirstname().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                }else if(projectModel.getLastname().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                }
                else if(projectModel.getUsername().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                }else if(projectModel.getMail().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                }else if(projectModel.getPhone().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                }else if(projectModel.getAddress().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                }
                else
                    return false; //no match
            });
        });

        SortedList<EmployeeModel> sortedData=new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableEmployeesView.comparatorProperty());

        tableEmployeesView.setItems(sortedData);
    }

    private ObservableList<EmployeeModel> employeesList = FXCollections.observableArrayList();
}
