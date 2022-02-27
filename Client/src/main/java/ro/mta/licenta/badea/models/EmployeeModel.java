package ro.mta.licenta.badea.models;

public class EmployeeModel {
    private int ID;
    private String username;
    private String password;
    private CompanyModel company;
    private String firstname;
    private String lastname;
    private String phone;
    private String addr;
    private String email;
    private int ore_max_munca;
    private int ore_munca;
    private int admin;
    private String role;
    private String permission;

    public EmployeeModel(){}

    public EmployeeModel(int id, String username, String pass, String firstName, String lastName, String phone, String addr, String mail) {
        this.ID = id;
        this.username = username;
        this.password = pass;
        this.firstname = firstName;
        this.lastname = lastName;
        this.phone = phone;
        this.addr = addr;
        this.email = mail;
    }

    public EmployeeModel(int id, String username, String firstName, String lastName,String addr, String phone,  String mail , int oremax,int ore_munca,int admin) {
        this.ID = id;
        this.username = username;
        this.firstname = firstName;
        this.lastname = lastName;
        this.phone = phone;
        this.addr = addr;
        this.email = mail;
        this.ore_max_munca=oremax;
        this.ore_munca=ore_munca;
        this.admin=admin;
    }

    public EmployeeModel(String firstName,String lastName){
        this.firstname=firstName;
        this.lastname=lastName;
    }

    public String getFullName(){
        String fullname=this.firstname+" "+this.lastname;
        return fullname;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setOre_munca(int ore_munca) {
        this.ore_munca = ore_munca;
    }

    public void setOre_max_munca(int ore_max_munca) {
        this.ore_max_munca = ore_max_munca;
    }

    public int getOre_munca() {
        return ore_munca;
    }

    public int getOre_max_munca() {
        return ore_max_munca;
    }

    public String getPermision() {
        return permission;
    }

    public String getRole() {
        return role;
    }

    public void setPermision(String permision) {
        this.permission = permision;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public int getAdmin() {
        return admin;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return addr;
    }


    public String getMail() {
        return email;
    }

    public void setAddress(String addr) {
        this.addr = addr;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setMail(String mail) {
        this.email = mail;
    }
}
