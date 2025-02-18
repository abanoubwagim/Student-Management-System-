package controllers;

import Classes.Student;
import Classes.Subjects;
import connection.Connections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import showAllAttributes.showAll;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Show implements showAll {

    @FXML
    private ListView<String> subjectsList;

    @FXML
    private AnchorPane show;

    private Subjects subjects;
    private Student student;

    @FXML
    void doneToHome(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontEnd/GUI/home.fxml"));
        Parent root = loader.load();

        Home homeController = loader.getController();
        homeController.setStudent(this.student);
        homeController.setSubjects(this.subjects);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

        show.getScene().getWindow().hide();
    }

    @FXML
    void updateToUpdating(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontEnd/GUI/updating.fxml"));
        Parent root = loader.load();

        Updating updatingController = loader.getController();
        updatingController.setStudent(this.student);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

        show.getScene().getWindow().hide();
    }

    @FXML
    void deleteToDelete(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontEnd/GUI/delete.fxml"));
        Parent root = loader.load();

        delete deleteController = loader.getController();
        deleteController.setStudent(this.student);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

        show.getScene().getWindow().hide();
    }

    @Override
    public void setStudent(Student student){
        this.student = student;
        printAllSubjects();
    }

    private void printAllSubjects(){

        List<String> subjectFromDb = new ArrayList<>();

        String query="SELECT subject,day FROM subjects WHERE id = ?";
        try (
                Connection connection = Connections.connection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ){
            preparedStatement.setInt(1,this.student.getId());

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                String sub = rs.getString("subject");
                String day = rs.getString("day");
                subjectFromDb.add("Subject: "+sub+" Day: "+day);
            }

            if (subjectFromDb.isEmpty()){
                subjectsList.getItems().add("No Subject available.");
            }else {
                subjectsList.getItems().addAll(subjectFromDb);
            }

        }catch (Exception e ){
            System.out.println(e.getMessage());
        }
    }





}
