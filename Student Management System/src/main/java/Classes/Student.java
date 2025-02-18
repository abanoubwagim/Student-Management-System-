package Classes;

public class Student {

    public Student() {

    }

    enum type{Male,Female};

    private int id;
    private String name;
    private String email;
    private String password;
    private String gender;
    private String birthYear;



    public Student(String name, String email, String password, String gender, String birthYear) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.birthYear = birthYear;
    }

    public Student(int id,String name, String email, String password, String gender, String birthYear) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.birthYear = birthYear;
    }

    public String getPassword() {
        return password;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }



    public String getGender() {
        return gender;
    }



    public String getBirthYear() {
        return birthYear;
    }

    public int getId(){return id;}


}
