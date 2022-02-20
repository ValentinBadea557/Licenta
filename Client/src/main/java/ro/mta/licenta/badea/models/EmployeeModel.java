package ro.mta.licenta.badea.models;

public class EmployeeModel {
    private int ID;
    private String username;
    private String password;
    private String FirstName;
    private String LastName;
    private String phone;
    private String address;
    private String mail;
    private int admin;

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
        return address;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getMail() {
        return mail;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
