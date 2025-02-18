package controllers;

import Classes.Student;
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

public class Updating implements showAll {
    @FXML
    private TextField newSubject;

    @FXML
    private TextField lastDay;

    @FXML
    private TextField newDay;

    @FXML
    private ImageView loading;

    @FXML
    private AnchorPane update;

    @FXML
    private TextField lastSubject;

    @FXML
    private Text text;

    private Student student;


    @FXML
    void update(ActionEvent event) {
        if (student == null) {
            text.setText("Student not found. Please return to the home screen.");
            return;
        }

        if (newSubject.getText().trim().isEmpty() || newDay.getText().trim().isEmpty()){
            text.setText("New subject and day cannot be empty.");
            transaction(text,2);
            return;
        }
        if (!subjectValid()){
            text.setText("Subject or day doesn't exist.");
            transaction(text,2);

        }else {

        String query = "UPDATE subjects SET subject = ?, day = ? WHERE id = ? AND subject =? AND day=?";
        try (
                Connection connection = Connections.connection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ){

            preparedStatement.setString(1,newSubject.getText());
            preparedStatement.setString(2,newDay.getText());
            preparedStatement.setInt(3,this.student.getId());

            preparedStatement.setString(4,lastSubject.getText());
            preparedStatement.setString(5,lastDay.getText());


            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                text.setText("Subject updated successfully!");
                transaction(this.text,2);
                loading.setVisible(true);
                transaction(loading,1);

            } else {
                text.setText("No changes made. Verify your input.");
                transaction(text,2);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
}
    }
    @Override
    public void setStudent(Student student){
        this.student = student;
    }

    @FXML
    void backToShow(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontEnd/GUI/show.fxml"));
        Parent root = loader.load();

        Show showController = loader.getController();
        showController.setStudent(this.student);

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        update.getScene().getWindow().hide();
    }
    @Override
    public void initialize(){
        loading.setVisible(false);
    }
    @Override
    public boolean subjectValid(){
        String query ="SELECT subject,day FROM subjects WHERE id = ? AND subject = ? AND day = ?";

        try (
                Connection connection = Connections.connection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ){

            preparedStatement.setInt(1,this.student.getId());
            preparedStatement.setString(2, lastSubject.getText());
            preparedStatement.setString(3, lastDay.getText());

            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    @Override
    public void transaction(Text txt, int second){
        PauseTransition pt = new PauseTransition(Duration.seconds(second));
        pt.setOnFinished(e->{
            txt.setText("");
        });
        pt.play();
    }
    @Override
    public void transaction(ImageView img, int second){
        PauseTransition pt = new PauseTransition(Duration.seconds(second));
        pt.setOnFinished(e->{
            img.setVisible(false);
        });
        pt.play();
    }



}
