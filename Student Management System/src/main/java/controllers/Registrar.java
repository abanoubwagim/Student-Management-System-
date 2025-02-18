package controllers;

import Classes.Student;
import connection.Connections;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
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

public class Registrar implements showAll {

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private PasswordField password;

    @FXML
    private TextField name;

    @FXML
    private TextField birth;

    @FXML
    private TextField email;

    @FXML
    private Text text;

    @FXML
    private ImageView loading;

    @FXML
    private AnchorPane reg;

    @Override
    public void initialize(){
        loading.setVisible(false);
        comboBox.getItems().addAll("Male", "Female");
    }


    @FXML
    void signIn(ActionEvent event) throws IOException {
        reg.getScene().getWindow().hide();

            Parent root = FXMLLoader.load(getClass().getResource("/FrontEnd/GUI/signIn.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();


    }

    @FXML
    void signUP(ActionEvent event) {
        registrar();
        PauseTransition pt = new PauseTransition();
        pt.setDuration(Duration.seconds(1));
        pt.setOnFinished(
                ev -> loading.setVisible(false)
        );
        pt.play();
    }

    public void registrar(){
        if (userExists() == 1) {
            text.setText("The user already Exists");
            transaction(text,2);
        }else {
            String name = this.name.getText();
            String email =this.email.getText();
            String password = this.password.getText();
            String gender = this.comboBox.getSelectionModel().getSelectedItem();
            String birth = this.birth.getText();

            Student student = new Student(name,email,password,gender,birth);

            String query = "INSERT INTO student (name,email,password,gender,birthYear) VALUES (?,?,?,?,?)";
            try (
                    Connection connection = Connections.connection();
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
            ) {

                preparedStatement.setString(1, student.getName());
                preparedStatement.setString(2, student.getEmail());
                preparedStatement.setString(3, student.getPassword());
                preparedStatement.setString(4, student.getGender());
                preparedStatement.setString(5, student.getBirthYear());

                int execute = preparedStatement.executeUpdate();
                text.setText("Registrar Successfully");
                transaction(text,2);
                if (execute == 1) {
                    loading.setVisible(true);
                    transaction(loading,1);
                } else {
                    text.setText("Failed to register");
                    transaction(text,2);
                }
            } catch (Exception e) {
                    text.setText("Please fill all fields.");
                    transaction(text,2);
            }
        }
    }
    @Override
    public int userExists(){
        String query = "SELECT * FROM student WHERE email =?";
        try(
                Connection connection = Connections.connection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ){
            preparedStatement.setString(1,email.getText());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
            return 1;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return 0;
    }

    @Override
    public void transaction(Text text, int second){
        PauseTransition pt = new PauseTransition(Duration.seconds(2));
        pt.setOnFinished(ev->
                text.setText(""));
        pt.play();
    }

    }


