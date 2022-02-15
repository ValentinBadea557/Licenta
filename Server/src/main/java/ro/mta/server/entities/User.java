package ro.mta.server.entities;

public class User {
    private String username;
    private String password;
    private Companie company;
    private String firstname;
    private String lastname;
    private String addr;
    private String phone;
    private String email;
    private int ore_max_munca;
    private int ore_munca;
    private int admin;

    public User(){};

    public int getAdmin() {
        return admin;
    }

    public Companie getCompany() {
        return company;
    }

    public int getOre_max_munca() {
        return ore_max_munca;
    }

    public int getOre_munca() {
        return ore_munca;
    }

    public String getAddr() {
        return addr;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getUsername() {
        return username;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public void setCompany(Companie company) {
        this.company = company;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setOre_max_munca(int ore_max_munca) {
        this.ore_max_munca = ore_max_munca;
    }

    public void setOre_munca(int ore_munca) {
        this.ore_munca = ore_munca;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }
}
