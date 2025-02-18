package controllers;

import Classes.Student;
import connection.Connections;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import java.sql.SQLException;

public class SignIn implements showAll {
    @FXML
    private PasswordField password;

    @FXML
    private Text text;

    @FXML
    private ImageView loading;

    @FXML
    private TextField email;

    @FXML
    private AnchorPane sign;


    @FXML
    void signUp(ActionEvent event) throws IOException {
        sign.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("/FrontEnd/GUI/registrar.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void logIn(ActionEvent event) throws IOException {
        if (fieldEmpty() == 0){
            text.setText("Fill all fields.");
            transaction(text,1);
            return;
        }

        if (userExists() == 1) {

            String check = checkPassword(email.getText());
            String pass = password.getText();


            if (check == null) {
                text.setText("Invalid Password");
                transaction(text,1);
                return;
            }

            if (check.equals(pass)) {

                text.setText("Login Successfully");
                loading.setVisible(true);
                transaction(text,2);
                transaction(loading,2);
                transition(sign,2);
            } else {
                text.setText("Incorrect Password");
                transaction(text,1);
            }
        } else {
            text.setText("User doesn't exist.");
            transaction(text,1);
        }
    }

    @Override
    public void initialize(){
        loading.setVisible(false);
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
        PauseTransition pt = new PauseTransition(Duration.seconds(second));
        pt.setOnFinished(ev->
                text.setText(""));
        pt.play();
    }
    @Override
    public void transaction(ImageView load, int second){
        PauseTransition pt = new PauseTransition(Duration.seconds(second));
        pt.setOnFinished(ev->
                loading.setVisible(false));
        pt.play();
    }

    public void transition(AnchorPane pane, int second) throws IOException {

        Student student = getStudentByEmail(email.getText());

        PauseTransition pt = new PauseTransition(Duration.seconds(second));
        pt.setOnFinished(ev -> {
            try {
                pane.getScene().getWindow().hide();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontEnd/GUI/home.fxml"));
                Parent root = loader.load();
                Home homeController = loader.getController();
                homeController.setStudent(student);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));

                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        pt.play();
    }

    private Student getStudentByEmail(String email) {

        String query = "SELECT * FROM student WHERE email = ?";
        try(
                Connection connection = Connections.connection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                )
        {
            preparedStatement.setString(1,email);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()){
                return new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("gender"),
                        rs.getString("birthYear")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public String checkPassword(String email){

        String query = "SELECT password FROM student WHERE email = ?";
        try (
                Connection connection = Connections.connection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ){
                preparedStatement.setString(1,email);

                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    return rs.getString("password");
                }
                else {
                    text.setText("Invalid password");
                    return null;
                }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public int fieldEmpty(){
        if (email.getText().isEmpty() || password.getText().isEmpty()){
            return 0;
        }
        return 1;
    }


}
