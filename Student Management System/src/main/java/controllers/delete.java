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

public class delete implements showAll {

    @FXML
    private TextField subject;

    @FXML
    private Text text;

    @FXML
    private ImageView loading;

    @FXML
    private AnchorPane delete;

    @FXML
    private TextField day;

    private Student student;

    @FXML
    void doneToShow(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontEnd/GUI/show.fxml"));
        Parent root = loader.load();

        Show showController = loader.getController();
        showController.setStudent(this.student);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

        delete.getScene().getWindow().hide();
    }

    @FXML
    void deleteFromDb(ActionEvent event) {
        if (subject.getText().isEmpty() || day.getText().isEmpty()){
            text.setText("fill all fields.");
            transaction(text,2);
        }
        if (!subjectValid()){
                text.setText("doesn't exist");
                transaction(text,2);
        }else {
            String query ="DELETE FROM subjects WHERE id = ? AND subject = ? AND day = ?";
            try(
                    Connection connection = Connections.connection();
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
            ) {
                preparedStatement.setInt(1, this.student.getId());
                preparedStatement.setString(2, subject.getText());
                preparedStatement.setString(3, day.getText());

                int rowsDeleted = preparedStatement.executeUpdate();

                if (rowsDeleted > 0) {
                    text.setText("Subject deleted successfully!");
                    transaction(text,2);
                } else {
                    text.setText("No matching record found.");
                }

                loading.setVisible(true);
                transaction(loading, 1);
        }catch (Exception e ){
                System.out.println(e.getMessage());
            }
        }
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
    public void transaction(Text text , int second){
        PauseTransition pt = new PauseTransition(Duration.seconds(second));
        pt.setOnFinished(e->{
            text.setText("");
        });
        pt.play();
    }
    @Override
    public void transaction(ImageView load , int second){
        PauseTransition pt = new PauseTransition(Duration.seconds(second));
        pt.setOnFinished(e->{
            load.setVisible(false);
        });
        pt.play();
    }
    @Override
    public boolean subjectValid(){
        String query ="SELECT subject,day FROM subjects WHERE id = ? AND subject = ? AND day = ?";

        try (
                Connection connection = Connections.connection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ){

            preparedStatement.setInt(1,this.student.getId());
            preparedStatement.setString(2, subject.getText());
            preparedStatement.setString(3, day.getText());

            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

}
