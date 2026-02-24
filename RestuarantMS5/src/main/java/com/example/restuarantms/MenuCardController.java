package com.example.restuarantms;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import com.example.restuarantms.util.AlertUtil;
import com.example.restuarantms.util.ImageUtil;

/**
 * MenuCardController - handles individual menu card display and interactions
 */
public class MenuCardController implements Initializable {


    // ===== MENU CARD ROOT =====
    @FXML
    private AnchorPane menu_card;


    // ===== PRODUCT INFO =====
    @FXML
    private Label product_name;

    @FXML
    private Label product_price;


    // ===== PRODUCT IMAGE =====
    @FXML
    private ImageView product_image_view;


    // ===== PRODUCT CONTROLS =====
    @FXML
    private Spinner<Integer> product_spinner;

    @FXML
    private Button product_add_button;


    private ProductsData productsData;
    private Image image;

    private String prodID;
    private String type;
    private String prodImage;

    private double pr;
    public void setData(ProductsData data) {
        this.productsData = data;

        prodID = data.getProductId();

        prodImage = data.getImage();


        product_name.setText(data.getProductName());
        product_price.setText(String.format("$%.2f", data.getPrice()));

        // Load image from database path using classpath resources
        String path = data.getImage();
        if (path != null && !path.isEmpty()) {
            // Use ImageUtil to load from classpath resources
            Image image = ImageUtil.loadImageFromResource(path, 193, 150, false);
            if (image != null) {
                product_image_view.setImage(image);
            }
        }

        pr = data.getPrice();

    }

    private MainFormController mainFormController;

    public void setMainFormController(MainFormController controller) {
        this.mainFormController = controller;
    }


    private SpinnerValueFactory<Integer> spinner;

    public void spinnerQuantity(){
        spinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        product_spinner.setValueFactory(spinner);
    }



    private Connection connection;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private ResultSet resultSet;
    private int quantity;
    private double totalPrice;
    public void addQuantityButton() throws SQLException {


        mainFormController.idCustomer();

        quantity =product_spinner.getValue();
        String check= "";
        String checkAvailable = "SELECT status FROM products WHERE product_id = ?";
        connection = Database.connectDataBase();

        preparedStatement = connection.prepareStatement(checkAvailable);
        preparedStatement.setString(1, prodID);
        resultSet = preparedStatement.executeQuery();

        if (resultSet.next()){
            check = resultSet.getString("status");
        }

        if (!check.equals("Available") || quantity == 0){
            AlertUtil.showError("Item is not available or quantity is 0");
            return;
        }else{
            int checkStock = 0;
            String quantityCheck = "SELECT stock FROM products WHERE product_id = ?";
            preparedStatement = connection.prepareStatement(quantityCheck);
            preparedStatement.setString(1, prodID);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                checkStock = resultSet.getInt("stock");
            }
            int updatedStock = checkStock - quantity;
            if (checkStock<quantity){
                AlertUtil.showError("Product is Out of Stock");
                return;
            }
            if (checkStock == 0) {
                prodImage = prodImage.replace("\\", "\\\\");

                String stockUpdate =
                        "UPDATE products SET "
                                + "product_name = ?, "
                                + "type = ?, "
                                + "stock = ?, "
                                + "price = ?, "
                                + "status = ?, "
                                + "image = ? "
                                + "WHERE product_id = ?";

                preparedStatement = connection.prepareStatement(stockUpdate);
                preparedStatement.setString(1, product_name.getText());
                preparedStatement.setString(2, productsData.getType());
                preparedStatement.setInt(3, updatedStock);
                preparedStatement.setDouble(4, pr);
                preparedStatement.setString(5, "Unavailable");
                preparedStatement.setString(6, prodImage); // image PATH, not Image object
                preparedStatement.setString(7, prodID);

                preparedStatement.executeUpdate();
            } else{
                String insertData = "INSERT INTO customers " +
                        "(customer_id, product_id, product_name, quantity, price, em_username) "
                        + "VALUES(?,?,?,?,?,?)";
                preparedStatement = connection.prepareStatement(insertData);
                preparedStatement.setString(1, String.valueOf(Data.idC));
                preparedStatement.setString(2, String.valueOf(prodID));
                preparedStatement.setString(3, product_name.getText());
                preparedStatement.setString(4, String.valueOf(quantity));
                totalPrice = quantity*pr;

                preparedStatement.setString(5, String.valueOf(totalPrice));
                preparedStatement.setString(6, Data.username);

                preparedStatement.executeUpdate();
                mainFormController.refreshMenuTable();

                prodImage = prodImage.replace("\\", "\\\\");

                String stockUpdate =
                        "UPDATE products SET "
                                + "product_name = ?, "
                                + "type = ?, "
                                + "stock = ?, "
                                + "price = ?, "
                                + "status = ? "
                                + "WHERE product_id = ?";

                preparedStatement = connection.prepareStatement(stockUpdate);
                preparedStatement.setString(1, product_name.getText());
                preparedStatement.setString(2, productsData.getType());
                preparedStatement.setInt(3, updatedStock);
                preparedStatement.setDouble(4, pr);
                preparedStatement.setString(5, check);

                preparedStatement.setString(6, prodID);

                preparedStatement.executeUpdate();

                AlertUtil.showSuccess("Successfully Added!!!");

            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        spinnerQuantity();
    }
}