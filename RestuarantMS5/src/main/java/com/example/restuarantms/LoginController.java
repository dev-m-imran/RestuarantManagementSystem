package com.example.restuarantms;

import com.example.restuarantms.util.AlertUtil;
import com.example.restuarantms.util.ValidationUtil;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * LoginController - handles user login and registration
 */
public class LoginController implements UserAccount {

    @FXML
    private AnchorPane login_page;

    //sideform
    @FXML
    private AnchorPane ms_page_left;

    @FXML
    private AnchorPane register_page;

    @FXML
    private TextField login_username;

    @FXML
    private PasswordField login_password;

    @FXML
    private Button login_button;

    @FXML
    private Hyperlink login_forgot_password;

    @FXML
    private Button login_imran_button;

    @FXML
    private Button ms_create_account_btn;

    @FXML
    private TextField register_username;

    @FXML
    private PasswordField register_password;

    @FXML
    private ComboBox<String> register_questions;

    @FXML
    private TextField register_answers;

    @FXML
    private Button signup_button;


    @FXML
    private Button ms_already_account_btn;

    @FXML
    private AnchorPane forgot_password_anchor;

    @FXML
    private AnchorPane update_password_anchor;

    @FXML
    private TextField forgot_username;

    @FXML
    private ComboBox<String> forgot_questions_box;

    @FXML
    private TextField forgot_answer;

    @FXML
    private Button forgot_proceedBtn;

    @FXML
    private Button forgot_backBtn;

    @FXML
    private PasswordField update_forgot_password;

    @FXML
    private PasswordField update_forgot_password_confirm;

    @FXML
    private Button update_passwordBtn;




    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;





    public void switchAnchorPane(ActionEvent event){

        TranslateTransition slide = new TranslateTransition();

        if (event.getSource() == ms_create_account_btn){
            slide.setNode(ms_page_left);
            slide.setToX(300);
            slide.setDuration(Duration.seconds(.5));

            slide.setOnFinished((ActionEvent e)->{
                ms_already_account_btn.setVisible(true);
                ms_create_account_btn.setVisible(false);



            });
            slide.play();
        } else if (event.getSource()==ms_already_account_btn) {
            slide.setNode(ms_page_left);
            slide.setToX(0);
            slide.setDuration(Duration.seconds(.5));

            slide.setOnFinished((ActionEvent e)->{
                ms_already_account_btn.setVisible(false);
                ms_create_account_btn.setVisible(true);

            });
            slide.play();
        }


    }

    // security questions ComboBox for registration form.
    private String[] questionsArray = {
            "What is your favourite Color?",
            "What is your Mother name?",
            "What is your favourite place?"};


    public void registerQuestionsList(){
        List<String> listQuestions = new ArrayList<>();

        for(String q: questionsArray){
            listQuestions.add(q);
        }

        ObservableList observableList = FXCollections.observableArrayList(listQuestions);
        register_questions.setItems(observableList);
    }

    /**
     Handles new user (employee) registration:
     Validates input fields
     Checks if username already exists
     Validates password length
     Inserts new record into employees table
     Switches back to login form on success
     */
    public void registerBTN() throws SQLException {
        if (ValidationUtil.isEmpty(register_username.getText())
                || ValidationUtil.isEmpty(register_password.getText())
                || register_questions.getSelectionModel().getSelectedItem() == null
                || ValidationUtil.isEmpty(register_answers.getText())){

            AlertUtil.showError("Please fill all blank fields.");

        }
        else {
            // Insert new employee
            String registerData = "INSERT INTO employees(username, password, question, answer) "
                    + "VALUES(?,?,?,?)";
            connection = Database.connectDataBase();

            // Check If the User already exists
            String checkUsername = "SELECT username FROM employees WHERE username = ?";
            preparedStatement = connection.prepareStatement(checkUsername);
            preparedStatement.setString(1, register_username.getText().trim());
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                AlertUtil.showError(register_username.getText().trim() + " is taken. Please choose another username.");
                // check if the password is more than 8 character or not
            }else if(!ValidationUtil.isValidPassword(register_password.getText())){
                AlertUtil.showError("Password must be 8 character long");
            }else{
                preparedStatement = connection.prepareStatement(registerData);
                preparedStatement.setString(1, register_username.getText().trim());
                preparedStatement.setString(2, register_password.getText());
                preparedStatement.setString(3, (String)register_questions.getSelectionModel().getSelectedItem());
                preparedStatement.setString(4, register_answers.getText());

                preparedStatement.executeUpdate();

                AlertUtil.showSuccess("Success!! Account is registered.");

                // Clear registration fields
                register_username.setText("");
                register_password.setText("");
                register_questions.getSelectionModel().clearSelection();
                register_answers.setText("");

                // Slide back to login form
                TranslateTransition slide = new TranslateTransition();
                slide.setNode(ms_page_left);
                slide.setToX(0);
                slide.setDuration(Duration.seconds(.5));

                slide.setOnFinished((ActionEvent e)->{
                    ms_already_account_btn.setVisible(false);
                    ms_create_account_btn.setVisible(true);

                });
                slide.play();
            }
        }
    }

    /**
     Handles user login:
     Validates credentials against database
     On success: opens main application window with icon
     Hides login window
     */
    public void loginButton() throws SQLException, IOException {

        if (ValidationUtil.isEmpty(login_username.getText()) || ValidationUtil.isEmpty(login_password.getText())){
            AlertUtil.showError("Incorrect Username/ Password");
        }
        else{
            String selectData = "SELECT username, password FROM employees WHERE username = ? and password = ?";

            connection = Database.connectDataBase();
            preparedStatement = connection.prepareStatement(selectData);
            preparedStatement.setString(1, login_username.getText());
            preparedStatement.setString(2, login_password.getText());

            resultSet = preparedStatement.executeQuery();


            // If successful it will proceed to dashboard
            if (resultSet.next()){

                Data.username = login_username.getText();

                AlertUtil.showSuccess("User logged in Successfully!!");
                // Open main application window
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("mainForm.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(fxmlLoader.load());
                // Set application icon
                stage.getIcons().add(new Image(
                        getClass().getResourceAsStream("/Images/gdk.png")
                ));
                stage.setTitle("GDK MS");
                stage.setResizable(false);
                stage.setScene(scene);
                stage.show();

                // Clear login fields
                login_username.setText("");
                login_password.setText("");

                // Close login window
                login_button.getScene().getWindow().hide();
            }
            // If not it'll show error message
            else{
                AlertUtil.showError("Incorrect Username/ Password");
            }

        }



    }


    //  Shows the first step of forgot password (username + question)

    public void switchForm(){
        forgot_password_anchor.setVisible(true);
        login_page.setVisible(false);
        update_password_anchor.setVisible(false);
    }


     // Switch back to login page from forgot password

    public void switchFormBack(){
        login_page.setVisible(true);
        forgot_password_anchor.setVisible(false);
        update_password_anchor.setVisible(false);
        // Clear and reset forgot password fields
        forgot_username.setText("");
        forgot_questions_box.getSelectionModel().clearSelection();
        forgot_questions_box.setDisable(false);
        forgot_questions_box.getItems().clear();
        // Reload questions list
        List<String> listQuestions = new ArrayList<>();
        for(String q: questionsArray){
            listQuestions.add(q);
        }
        ObservableList observableList = FXCollections.observableArrayList(listQuestions);
        forgot_questions_box.setItems(observableList);
        forgot_answer.setText("");
        // Clear update password fields
        update_forgot_password.setText("");
        update_forgot_password_confirm.setText("");
    }

    /**
     * Check if username exists and load security question
     */
    public void checkForgotUsername() throws SQLException {
        if (ValidationUtil.isEmpty(forgot_username.getText())) {
            AlertUtil.showError("Please enter username.");
            return;
        }

        String checkUser = "SELECT question FROM employees WHERE username = ?";
        connection = Database.connectDataBase();
        preparedStatement = connection.prepareStatement(checkUser);
        preparedStatement.setString(1, forgot_username.getText().trim());
        resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            String question = resultSet.getString("question");
            // Set the question in the combo box
            forgot_questions_box.getItems().clear();
            forgot_questions_box.getItems().add(question);
            forgot_questions_box.getSelectionModel().select(question);
            forgot_questions_box.setDisable(true); // Disable so user can't change it
            AlertUtil.showSuccess("User found! Please answer the security question.");
        } else {
            AlertUtil.showError("Username not found!");
            forgot_questions_box.getItems().clear();
            forgot_questions_box.setDisable(false);
        }
    }

    /**
     *Verify security answer and proceed to password update
     *If correct → show new password fields
     */
    public void verifyForgotPassword() throws SQLException {
        if (ValidationUtil.isEmpty(forgot_username.getText())) {
            AlertUtil.showError("Please enter username first.");
            return;
        }

        if (forgot_questions_box.getSelectionModel().getSelectedItem() == null) {
            AlertUtil.showError("Please check username to load security question.");
            return;
        }

        if (ValidationUtil.isEmpty(forgot_answer.getText())) {
            AlertUtil.showError("Please enter the answer to security question.");
            return;
        }

        // Verify username, question, and answer match
        String verifySQL = "SELECT username FROM employees WHERE username = ? AND question = ? AND answer = ?";
        connection = Database.connectDataBase();
        preparedStatement = connection.prepareStatement(verifySQL);
        preparedStatement.setString(1, forgot_username.getText().trim());
        preparedStatement.setString(2, forgot_questions_box.getSelectionModel().getSelectedItem());
        preparedStatement.setString(3, forgot_answer.getText().trim());
        resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            // Verification successful - show update password screen
            forgot_password_anchor.setVisible(false);
            update_password_anchor.setVisible(true);
            // Clear update password fields
            update_forgot_password.setText("");
            update_forgot_password_confirm.setText("");
        } else {
            AlertUtil.showError("Incorrect answer to security question!");
        }
    }

    /**
     * Update password after verification
     * Validates new password → updates database → returns to login
     */
    public void updatePassword() throws SQLException {
        if (ValidationUtil.isEmpty(update_forgot_password.getText()) ||
                ValidationUtil.isEmpty(update_forgot_password_confirm.getText())) {
            AlertUtil.showError("Please fill all password fields.");
            return;
        }

        if (!update_forgot_password.getText().equals(update_forgot_password_confirm.getText())) {
            AlertUtil.showError("Passwords do not match!");
            return;
        }

        if (!ValidationUtil.isValidPassword(update_forgot_password.getText())) {
            AlertUtil.showError("Password must be at least 8 characters long.");
            return;
        }

        // Update password
        String updateSQL = "UPDATE employees SET password = ? WHERE username = ?";
        connection = Database.connectDataBase();
        preparedStatement = connection.prepareStatement(updateSQL);
        preparedStatement.setString(1, update_forgot_password.getText());
        preparedStatement.setString(2, forgot_username.getText().trim());
        preparedStatement.executeUpdate();

        AlertUtil.showSuccess("Password updated successfully!");

        // Return to login page
        switchFormBack();
    }


    // Implementation of UserAccount interface
    @Override
    public String getUsername() {
        return login_username.getText();
    }

    @Override
    public String getPassword() {
        return login_password.getText();
    }

    @Override
    public boolean validateLogin(String username, String password) throws SQLException {
        if (ValidationUtil.isEmpty(username) || ValidationUtil.isEmpty(password)) {
            return false;
        }

        String selectData = "SELECT username, password FROM employees WHERE username = ? and password = ?";
        connection = Database.connectDataBase();

        preparedStatement = connection.prepareStatement(selectData);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        resultSet = preparedStatement.executeQuery();

        return resultSet.next();
    }

    /**
     * Auto-fill login fields with Imran's credentials
     */
    public void fillImranLogin() {
        login_username.setText("imran");
        login_password.setText("123456789");
    }

    public void initialize(){
        registerQuestionsList();
        // Initialize forgot password questions list
        List<String> listQuestions = new ArrayList<>();
        for(String q: questionsArray){
            listQuestions.add(q);
        }
        ObservableList observableList = FXCollections.observableArrayList(listQuestions);
        forgot_questions_box.setItems(observableList);
    }
}