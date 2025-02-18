package controllers;

import Classes.Student;
import Classes.Subjects;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import showAllAttributes.showAll;

import java.io.IOException;

public class Home implements showAll {


    @FXML
    private Text gender;

    @FXML
    private Text birthYear;

    @FXML
    private Text name;

    @FXML
    private Text id;

    @FXML
    private AnchorPane home;

    private Student student;

    private Subjects subjects;



    @FXML
    void addItem(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontEnd/GUI/adding.fxml"));
        Parent root = loader.load();
        Adding addingController = loader.getController();
        addingController.setStudent(this.student);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();


        home.getScene().getWindow().hide();

    }

    @FXML
    void showItems(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontEnd/GUI/show.fxml"));
        Parent root = loader.load();

        Show showController = loader.getController();
        showController.setStudent(this.student);


        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

        home.getScene().getWindow().hide();

    }

    @FXML
    void signOut(ActionEvent event) throws IOException {
        home.getScene().getWindow().hide();

        Parent root = FXMLLoader.load(getClass().getResource("/FrontEnd/GUI/signIn.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    @Override
    public void setStudent(Student student) {
        this.student = student;

        id.setText(String.valueOf(student.getId()));
        name.setText(student.getName());
        gender.setText(student.getGender());
        birthYear.setText(student.getBirthYear());

    }

    public void setSubjects(Subjects subjects){
        this.subjects = subjects;
    }







}
