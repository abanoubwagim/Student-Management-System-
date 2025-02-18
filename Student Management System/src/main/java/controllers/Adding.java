package controllers;

import Classes.Student;
import Classes.Subjects;
import connection.Connections;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import showAllAttributes.showAll;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Adding implements showAll {

    @FXML
    private TextField subject;

    @FXML
    private Text text;

    @FXML
    private ImageView loading;

    @FXML
    private TextField day;

    @FXML
    private AnchorPane adding;

    private Student student;
    private Subjects subjects;



    @FXML
    void AddInDataBase(ActionEvent event) {

        if (subject.getText().isEmpty() || day.getText().isEmpty()){
            text.setText("subject and day cannot be empty.");
            transaction(text,1);
            return;
        }
        if (!checkText()){
            text.setText("Adding before.");
            transaction(text,1);
        }else {
            String query="INSERT INTO subjects(id,subject,day) VALUES (?,?,?)";
            try(
                    Connection connection = Connections.connection();
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
            ){

                String sub = subject.getText();
                String da = day.getText();

                preparedStatement.setInt(1,student.getId());
                preparedStatement.setString(2,sub);
                preparedStatement.setString(3,da);

                preparedStatement.executeUpdate();

                Subjects subjects = new Subjects(sub,da,this.student);
                subjects.addSubject(sub,da,this.student);

                text.setText("Adding Successfully.");
                loading.setVisible(true);
                transaction(loading,1);
                transaction(text,2);


            } catch (SQLException e) {
                System.out.println(e.getMessage());
                text.setText("Failed to add subject. Please try again.");
            }
        }

    }

    @FXML
    void backToHome(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontEnd/GUI/home.fxml"));
        Parent root = loader.load();

        Home homeController = loader.getController();
        homeController.setStudent(this.student);
        homeController.setSubjects(this.subjects);


        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

        adding.getScene().getWindow().hide();
    }

    @Override
    public void initialize(){
        loading.setVisible(false);
    }
    @Override
    public void setStudent(Student student) {
        this.student = student;
    }
    @Override
    public void transaction(Text text, int second){
        PauseTransition pt = new PauseTransition(Duration.seconds(second));
        pt.setOnFinished(e->{
            text.setText("");
        });
        pt.play();
    }
    @Override
    public void transaction(ImageView load, int second){
        PauseTransition pt = new PauseTransition(Duration.seconds(second));
        pt.setOnFinished(e->{
            load.setVisible(false);
        });
        pt.play();
    }

    public boolean checkText(){
        String query="SELECT subject,day FROM subjects WHERE id = ?";
        try(
                Connection connection = Connections.connection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ){
            preparedStatement.setInt(1,student.getId());

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                String sub = rs.getString("subject");
                String d = rs.getString("day");

                if (sub.equals(this.subject.getText()) && d.equals(this.day.getText())){
                    return false;
                }
            }

            return true;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
