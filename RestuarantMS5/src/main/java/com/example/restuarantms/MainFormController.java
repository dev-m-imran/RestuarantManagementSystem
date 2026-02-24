package com.example.restuarantms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.geometry.Insets;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.example.restuarantms.util.ValidationUtil;
import com.example.restuarantms.util.FormatUtil;
import com.example.restuarantms.util.AlertUtil;
import com.example.restuarantms.util.ImageUtil;

/**
 * MainFormController - main controller for the restaurant management system
 * Handles inventory, menu, and order management
 */
public class MainFormController implements Initializable {
    @FXML
    private Label username_label;
    @FXML
    private Button dashboard_button;
    @FXML
    private Button inventory_button;
    @FXML
    private Button menu_button;
    @FXML
    private Button customers_button;
    @FXML
    private Button logout_button;
    @FXML
    private Button inventory_import_button;
    @FXML
    private Button inventory_add_button;
    @FXML
    private Button inventory_update_button;
    @FXML
    private Button inventory_clear_button;
    @FXML
    private Button inventory_delete_button;
    @FXML
    private TextField inventory_productID_field;
    @FXML
    private TextField inventory_nameField;
    @FXML
    private TextField inventory_stockField;
    @FXML
    private TextField inventory_priceField;



    // ==== ComboBox ====
    @FXML
    private ComboBox<String> inventory_typeBox;

    @FXML
    private ComboBox<String> inventory_statusBox;



    // ===== IMAGE VIEW =====
    @FXML
    private ImageView inventory_image_view;


    // ===== ANCHOR PANES =====
    @FXML
    private AnchorPane inventory_anchor;

    @FXML
    private AnchorPane dashboard_anchor;

    @FXML
    private AnchorPane customers_anchor;

    @FXML
    private AnchorPane main_anchorPane;

    // ===== DASHBOARD LABELS =====
    @FXML
    private Label dashboard_customer_count;

    @FXML
    private Label dashboard_today_sale;

    @FXML
    private Label dashboard_total_sale;


    // ===== TABLE VIEW =====
    @FXML
    private TableView<ProductsData> inventory_table_view;

    @FXML
    private TableColumn<ProductsData, String> inventory_column_productID;

    @FXML
    private TableColumn<ProductsData, String> inventory_column_productName;

    @FXML
    private TableColumn<ProductsData, String> inventory_column_type;

    @FXML
    private TableColumn<ProductsData, Integer> inventory_column_stock;

    @FXML
    private TableColumn<ProductsData, Double> inventory_column_price;

    @FXML
    private TableColumn<ProductsData, String> inventory_column_status;

    @FXML
    private TableColumn<ProductsData, Date> inventory_column_date;

    // ===== CUSTOMERS TABLE VIEW =====
    @FXML
    private TableView<CustomerData> customers_table_view;

    @FXML
    private TableColumn<CustomerData, Integer> customers_column_id;

    @FXML
    private TableColumn<CustomerData, String> customers_column_total;

    @FXML
    private TableColumn<CustomerData, String> customers_column_date;

    @FXML
    private TableColumn<CustomerData, String> customers_column_employee;

    @FXML
    private TextField customers_search_field;

    @FXML
    private Button customers_search_button;

    @FXML
    private Button customers_clear_button;

    // ===== MENU TABLE VIEW =====
    @FXML
    private TableView<ProductsData> menu_table_view;

    @FXML
    private TableColumn<ProductsData, String> menu_column_productName;

    @FXML
    private TableColumn<ProductsData, Integer> menu_column_quantity;

    @FXML
    private TableColumn<ProductsData, Double> menu_column_price;



    // ===== MENU TOTAL / PAYMENT =====
    @FXML
    private Label menu_total;

    @FXML
    private TextField menu_amount_field;

    @FXML
    private Label menu_change;


    // ===== MENU BUTTONS =====
    @FXML
    private Button menu_pay_button;

    @FXML
    private Button menu_remove_button;

    @FXML
    private Button menu_receipt_button;


    // ===== MENU SCROLL / GRID =====
    @FXML
    private ScrollPane menu_scroll_bar;

    @FXML
    private GridPane menu_grid_pane;


    // ===== MENU ROOT ANCHOR =====
    @FXML
    private AnchorPane menu_form;


    public void showUserName(){

        String user = Data.username;
        user = user.substring(0, 1).toUpperCase() + user.substring(1);
        username_label.setText(user);

    }


    public void logOut() throws IOException {
        if (AlertUtil.showConfirmation("Are you sure you want to logout?")){
            logout_button.getScene().getWindow().hide();

            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            // Set application icon
            stage.getIcons().add(new Image(
                    getClass().getResourceAsStream("/Images/gdk.png")
            ));
            stage.setTitle("GDK MS");
            stage.setMinHeight(435);
            stage.setMinWidth(612);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }
    }


    private String[] listType = {"Meals", "Drinks"};
    public void showInventoryTypeList(){

        List<String> typeList = new ArrayList<>();

        for(String list:listType ){
            typeList.add(list);
        }

        ObservableList observableList = FXCollections.observableArrayList(typeList);
        inventory_typeBox.setItems(observableList);
    }


    private String[] list_Status = {"Available", "Unavailable"};
    public void showInventoryStatusList(){
        List<String> status_List= new ArrayList<>();

        for(String list:list_Status ){
            status_List.add(list);
        }

        ObservableList observableList = FXCollections.observableArrayList(status_List);
        inventory_statusBox.setItems(observableList);
    }


    private Connection connection;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private ResultSet resultSet;


    private ObservableList<ProductsData> productsDataObservable;
    public ObservableList<ProductsData> inventoryProductsTable() throws SQLException {
        productsDataObservable = FXCollections.observableArrayList();
        String sql = "SELECT * FROM products";

        connection = Database.connectDataBase();
        preparedStatement = connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            // Handle date parsing for SQLite (stores dates as strings)
            Date productDate = null;
            String dateStr = resultSet.getString("date");
            if (dateStr != null && !dateStr.isEmpty()) {
                productDate = Date.valueOf(dateStr);
            }

            ProductsData productData = new ProductsData(resultSet.getInt("id"),
                    resultSet.getString("product_id"),
                    resultSet.getString("product_name"),
                    resultSet.getString("type"),
                    resultSet.getInt("stock"),
                    resultSet.getDouble("price"),
                    resultSet.getString("status"),
                    resultSet.getString("image"),
                    productDate);

            productsDataObservable.add(productData);
        }

        return productsDataObservable;
    }


    private ObservableList<ProductsData> inventoryDataObservable;
    public void showInventoryData() throws SQLException {

        inventoryDataObservable = inventoryProductsTable();


        inventory_column_productID.setCellValueFactory(new PropertyValueFactory<>("productId"));
        inventory_column_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        inventory_column_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        inventory_column_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        inventory_column_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        inventory_column_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        inventory_column_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        inventory_table_view.setItems(inventoryDataObservable);

    }

    private Image image;
    // Behaviour for import Button
    public void inventoryImportImageButton(){

        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image File", "*png", "*jpg"));

        File file = openFile.showOpenDialog(main_anchorPane.getScene().getWindow());

        if (file != null){
            Data.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 162, 175, false, true);
            inventory_image_view.setImage(image);

        }

    }

    public void addInventoryButton() throws SQLException, IOException {

        if (inventory_productID_field.getText().isEmpty() || inventory_nameField.getText().isEmpty()
                || inventory_typeBox.getSelectionModel().getSelectedItem() == null || inventory_stockField.getText().isEmpty() ||
                inventory_priceField.getText().isEmpty() || inventory_statusBox.getSelectionModel().getSelectedItem() == null ||
                Data.path == null){

            AlertUtil.showError("Please fill all blank fields.");

        }else {
            // Check for product ID
            String check_productID = "SELECT product_id FROM products WHERE product_id = ?";
            connection = Database.connectDataBase();
            preparedStatement = connection.prepareStatement(check_productID);
            preparedStatement.setString(1, inventory_productID_field.getText().trim());
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                AlertUtil.showError(inventory_productID_field.getText().trim() + " already exists.");
            }
            else {
                String insertData = "INSERT INTO products"
                        + "(product_id, product_name, type, stock, price, status, image)"
                        + "VALUES(?,?,?,?,?,?,?)";
                preparedStatement = connection.prepareStatement(insertData);
                preparedStatement.setString(1, inventory_productID_field.getText().trim());
                preparedStatement.setString(2, inventory_nameField.getText().trim());
                preparedStatement.setString(3, (String) inventory_typeBox.getSelectionModel().getSelectedItem());
                preparedStatement.setString(4, inventory_stockField.getText().trim());
                preparedStatement.setString(5, inventory_priceField.getText().trim());
                preparedStatement.setString(6, (String) inventory_statusBox.getSelectionModel().getSelectedItem());

                // Map product name to correct image, or use selected image if available
                String path;
                if (Data.path != null && !Data.path.isEmpty()) {
                    path = ImageUtil.convertToResourcePath(Data.path);
                } else {
                    // Auto-map based on product name
                    path = Database.mapProductNameToImage(inventory_nameField.getText().trim());
                }
                preparedStatement.setString(7, path);

                preparedStatement.executeUpdate();

                AlertUtil.showSuccess("Successfully Added!!");

                showInventoryData();
                clearInventoryButton();
                displayMenuCards();
            }
        }
    }


    public void clearInventoryButton(){
        inventory_productID_field.setText("");
        inventory_nameField.setText("");
        inventory_typeBox.getSelectionModel().clearSelection();
        inventory_stockField.setText("");
        inventory_priceField.setText("");
        inventory_statusBox.getSelectionModel().clearSelection();
        Data.path = "";
        inventory_image_view.setImage(null);



    }

    public void selectInventoryData(){

        ProductsData productsData = inventory_table_view.getSelectionModel().getSelectedItem();
        int num = inventory_table_view.getSelectionModel().getSelectedIndex();

        if((num - 1)<-1) return;

        inventory_productID_field.setText(productsData.getProductId());
        inventory_nameField.setText(productsData.getProductName());
        inventory_stockField.setText(String.valueOf(productsData.getStock()));
        inventory_priceField.setText(String.valueOf(productsData.getPrice()));

        Data.id = productsData.getId();

        // Store the image path for update operations
        Data.path = productsData.getImage();

        // Load image from classpath resources using ImageUtil
        String imagePath = productsData.getImage();
        image = ImageUtil.loadImageFromResource(imagePath, 162, 175, false);
        if (image != null) {
            inventory_image_view.setImage(image);
        } else {
            inventory_image_view.setImage(null);
        }

    }

    public void updateInventoryButton() throws SQLException, IOException {

        if (inventory_productID_field.getText().isEmpty() || inventory_nameField.getText().isEmpty()
                || inventory_typeBox.getSelectionModel().getSelectedItem() == null || inventory_stockField.getText().isEmpty() ||
                inventory_priceField.getText().isEmpty() || inventory_statusBox.getSelectionModel().getSelectedItem() == null ||
                Data.path == null || Data.id == 0){

            AlertUtil.showError("Please fill all blank fields.");

        }else{
            // Map image based on product name, or use selected image if user imported a new one
            String path;
            if (Data.path != null && !Data.path.isEmpty() && !Data.path.startsWith("/Images/")) {
                // User imported a new image, convert it to resource path
                path = ImageUtil.convertToResourcePath(Data.path);
            } else {
                // Auto-map based on product name to ensure correct image matches the title
                path = Database.mapProductNameToImage(inventory_nameField.getText().trim());
            }
            String update_data = "UPDATE products SET product_id = ?, product_name = ?, type = ?, stock = ?, price = ?, status = ?, image = ? WHERE id = ?";

            if (AlertUtil.showConfirmation("Are you sure you want to Update Product ID: " + inventory_productID_field.getText() + "?")){
                connection = Database.connectDataBase();
                preparedStatement = connection.prepareStatement(update_data);
                preparedStatement.setString(1, inventory_productID_field.getText().trim());
                preparedStatement.setString(2, inventory_nameField.getText().trim());
                preparedStatement.setString(3, (String) inventory_typeBox.getSelectionModel().getSelectedItem());
                preparedStatement.setString(4, inventory_stockField.getText().trim());
                preparedStatement.setString(5, inventory_priceField.getText().trim());
                preparedStatement.setString(6, (String) inventory_statusBox.getSelectionModel().getSelectedItem());
                preparedStatement.setString(7, path);
                preparedStatement.setInt(8, Data.id);
                preparedStatement.executeUpdate();

                AlertUtil.showSuccess("Product ID: " + inventory_productID_field.getText() +  " updated Successfully!!!");

                showInventoryData();
                clearInventoryButton();
                displayMenuCards();
            } else {
                AlertUtil.showError("Cancelled");
            }
        }

    }

    public void deleteInventoryButton() throws SQLException, IOException {

        ProductsData selectedProduct =
                inventory_table_view.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            AlertUtil.showWarning("There is no product selected to delete.");
            return;
        }

        if (AlertUtil.showConfirmation("Are you sure you want to delete Product ID: " + selectedProduct.getProductId() + " ?")) {

            String deleteData = "DELETE FROM products WHERE id = ?";
            connection = Database.connectDataBase();
            preparedStatement = connection.prepareStatement(deleteData);
            preparedStatement.setInt(1, selectedProduct.getId());
            preparedStatement.executeUpdate();

            AlertUtil.showSuccess("Product deleted successfully.");

            showInventoryData();
            clearInventoryButton();
            displayMenuCards();
        } else {
            AlertUtil.showError("Cancelled");
        }
    }

    private ObservableList<ProductsData> cardData = FXCollections.observableArrayList();

    public ObservableList<ProductsData> getMenuData() throws SQLException {

        String sql = "SELECT * FROM products";

        ObservableList<ProductsData> listData = FXCollections.observableArrayList();

        connection = Database.connectDataBase();
        preparedStatement = connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();

        ProductsData productsData;

        while (resultSet.next()){
            // Handle date parsing for SQLite (stores dates as strings)
            Date productDate = null;
            String dateStr = resultSet.getString("date");
            if (dateStr != null && !dateStr.isEmpty()) {
                productDate = Date.valueOf(dateStr);
            }

            productsData = new ProductsData(resultSet.getInt("id"),
                    resultSet.getString("product_id"),
                    resultSet.getString("product_name"),
                    resultSet.getString("type"),
                    resultSet.getInt("stock"),
                    resultSet.getDouble("price"),
                    resultSet.getString("status"),
                    resultSet.getString("image"),
                    productDate);

            listData.add(productsData);
        }

        return listData;
    }

    public void displayMenuCards() throws SQLException, IOException {

        cardData.clear();
        cardData.addAll(getMenuData());

        int row = 0;
        int column = 0;

        menu_grid_pane.getRowConstraints().clear();
        menu_grid_pane.getColumnConstraints().clear();

        // spacing between cards
        menu_grid_pane.setHgap(70);
        menu_grid_pane.setVgap(20);

        for (int i = 0; i < cardData.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("menuCards.fxml"));
            AnchorPane pane = fxmlLoader.load();
            MenuCardController menuCard = fxmlLoader.getController();

            // PASS MAIN CONTROLLER HERE
            menuCard.setMainFormController(this);
            menuCard.setData(cardData.get(i));

            menu_grid_pane.add(pane, column, row);
            GridPane.setMargin(pane, new Insets(10));

            column++;
            if (column == 2) {  // 2 cards per row
                column = 0;
                row++;
            }
        }



    }

    public void formSwitch(ActionEvent event) throws SQLException, IOException {

        if (event.getSource() == dashboard_button){
            dashboard_anchor.setVisible(true);
            inventory_anchor.setVisible(false);
            menu_form.setVisible(false);
            customers_anchor.setVisible(false);
            loadDashboardData();
        } else if (event.getSource() == inventory_button) {
            inventory_anchor.setVisible(true);
            dashboard_anchor.setVisible(false);
            menu_form.setVisible(false);
            customers_anchor.setVisible(false);

            showInventoryTypeList();
            showInventoryStatusList();
            showInventoryData();
        } else if (event.getSource() == menu_button) {
            menu_form.setVisible(true);
            dashboard_anchor.setVisible(false);
            inventory_anchor.setVisible(false);
            customers_anchor.setVisible(false);
            displayMenuCards();
            showMenuTableView();
            calculateTotalPrice();
        } else if (event.getSource() == customers_button) {
            customers_anchor.setVisible(true);
            dashboard_anchor.setVisible(false);
            inventory_anchor.setVisible(false);
            menu_form.setVisible(false);
            loadCustomersData();
        }


    }

    private int idC;
    public void idCustomer() throws SQLException {

        String sql = "SELECT MAX(customer_id) as max_id FROM customers";
        connection = Database.connectDataBase();

        preparedStatement = connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();

        if (resultSet.next()){
            idC = resultSet.getInt("max_id");
        }

        String checkIdC = "SELECT MAX(customer_id) as max_id FROM receipts";
        preparedStatement = connection.prepareStatement(checkIdC);
        resultSet = preparedStatement.executeQuery();
        int checkID = 0;
        if (resultSet.next()){
            checkID = resultSet.getInt("max_id");
        }

        if(idC == 0){
            idC+=1;
        } else if (idC == checkID) {
            idC += 1;
        }

        Data.idC = idC;
    }


    public ObservableList<ProductsData> displayMenuOrder() throws SQLException {

        ObservableList<ProductsData> listData = FXCollections.observableArrayList();
        idCustomer();
        String sql = "SELECT id, product_name, quantity, price FROM customers WHERE customer_id = ? AND paid = 0";

        connection = Database.connectDataBase();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, idC);
        resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            ProductsData productsData = new ProductsData(
                    resultSet.getInt("id"),
                    resultSet.getString("product_name"),
                    resultSet.getInt("quantity"),
                    resultSet.getDouble("price")
            );

            listData.add(productsData);
        }

        return listData;
    }


    private ObservableList<ProductsData> menuDataObservable;

    public void showMenuTableView() throws SQLException {

        menuDataObservable = displayMenuOrder();

        menu_column_productName.setCellValueFactory(
                new PropertyValueFactory<>("productName")
        );
        menu_column_quantity.setCellValueFactory(
                new PropertyValueFactory<>("quantity")
        );
        menu_column_price.setCellValueFactory(
                new PropertyValueFactory<>("price")
        );

        menu_table_view.setItems(menuDataObservable);
    }
    private double totalAmount;
    public void calculateTotalPrice() {

        totalAmount = 0;

        for (ProductsData pd : menu_table_view.getItems()) {
            totalAmount += pd.getPrice();   // price is already quantity * unitPrice
        }

        menu_total.setText(String.format("£%.2f", totalAmount));
    }

    public void removeMenuItem() throws SQLException {

        ProductsData selected =
                menu_table_view.getSelectionModel().getSelectedItem();

        if (selected == null) {
            AlertUtil.showWarning("Please select an item to remove.");
            return;
        }

        System.out.println("Selected ID = " + selected.getId());

        if (AlertUtil.showConfirmation("Confirmation", "Remove " + selected.getProductName() + " from order?")) {
            String deleteSQL = "DELETE FROM customers WHERE id = ?";
            connection = Database.connectDataBase();
            preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, selected.getId());
            preparedStatement.executeUpdate();

            // refresh table + total
            showMenuTableView();
            calculateTotalPrice();
        }
    }

    public void refreshMenuTable() throws SQLException {
        showMenuTableView();
        calculateTotalPrice();
    }

    private double amount;
    private double change;
    public void setMenu_amount_field(){

        calculateTotalPrice();
        if(menu_amount_field.getText().isEmpty() || totalAmount == 0){
            AlertUtil.showError("Invalid input or Amount is field is empty.");

        }else {
            amount = Double.parseDouble(menu_amount_field.getText());
            change = 0;
            if (amount < totalAmount){
                AlertUtil.showError("Amount is less than Total.");
                change = 0;
            }else {
                change = amount-totalAmount;
                menu_change.setText(String.format("£%.2f", change));
            }
        }
    }
    private ProductsData productData;
    private String username;
    private String prodName;
    public void setDataPay(ProductsData data){
        this.productData = data;
        prodName = data.getProductName();

    }

    public void setMenuPayButton() throws SQLException, IOException {
        if (menu_table_view.getItems().isEmpty()) {
            AlertUtil.showError("Please add item first.");
            return;
        }

        calculateTotalPrice();

        if (amount == 0) {
            AlertUtil.showError("Please enter amount first.");
            return;
        }

        if (AlertUtil.showConfirmation("Are you sure you want to pay £: " + totalAmount + " ?")) {
            idCustomer(); // get current customer ID

            // Get items BEFORE clearing table
            List<ProductsData> receiptItems = new ArrayList<>(menu_table_view.getItems());

            // Insert into receipts
            String payInsert = "INSERT into receipts (customer_id, total, amount, cu_change, em_username) VALUES(?,?,?,?,?)";
            connection = Database.connectDataBase();
            preparedStatement = connection.prepareStatement(payInsert);
            preparedStatement.setInt(1, idC);
            preparedStatement.setDouble(2, totalAmount);
            preparedStatement.setDouble(3, amount);
            preparedStatement.setDouble(4, change);
            preparedStatement.setString(5, Data.username);
            preparedStatement.executeUpdate();

            // Mark items as paid in customers
            String markPaid = "UPDATE customers SET paid = 1 WHERE customer_id = ?";
            preparedStatement = connection.prepareStatement(markPaid);
            preparedStatement.setInt(1, idC);
            preparedStatement.executeUpdate();

            // Clear TableView
            menu_table_view.getItems().clear();
            menu_total.setText("£0.00");
            menu_amount_field.clear();
            menu_change.setText("£0.00");

            AlertUtil.showSuccess("Successfully Paid!");

            // Show receipt with items
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("receipt.fxml")
            );
            VBox receiptRoot = loader.load();

            ReceiptController receiptController = loader.getController();

            receiptController.setReceiptData(
                    receiptItems,
                    totalAmount,
                    amount,
                    change
            );

            // Show receipt in new window
            Stage receiptStage = new Stage();
            Scene receiptScene = new Scene(receiptRoot);
            receiptStage.setTitle("Receipt");
            receiptStage.setScene(receiptScene);
            receiptStage.show();
        } else {
            AlertUtil.showSuccess("Cancelled!");
        }
    }


    private List<ProductsData> getReceiptItemsFromDB(int customerId) throws SQLException {

        List<ProductsData> list = new ArrayList<>();

        String sql = "SELECT product_id, product_name, quantity, price FROM customers WHERE customer_id = ? AND paid = 1";

        connection = Database.connectDataBase();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, customerId);
        resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            ProductsData pd = new ProductsData(
                    0, // id not needed for receipt
                    resultSet.getString("product_name"),
                    resultSet.getInt("quantity"),
                    resultSet.getDouble("price")
            );
            list.add(pd);
        }

        return list;
    }

    public void printReceipt() throws SQLException, IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("receipt.fxml")
        );

        VBox receipt = loader.load();
        ReceiptController controller = loader.getController();

        List<ProductsData> items = getReceiptItemsFromDB(idC);

        controller.setReceiptData(
                items,
                totalAmount,
                amount,
                change
        );

        PrinterJob job = PrinterJob.createPrinterJob();

        if (job != null && job.showPrintDialog(menu_table_view.getScene().getWindow())) {
            job.printPage(receipt);
            job.endJob();
        }
    }




    /**
     * Search products by name or ID
     * @param searchText - text to search for
     */
    public void searchProducts(String searchText) throws SQLException {
        if (ValidationUtil.isEmpty(searchText)) {
            showInventoryData();
            return;
        }

        ObservableList<ProductsData> searchResults = FXCollections.observableArrayList();
        String sql = "SELECT * FROM products WHERE product_name LIKE ? OR product_id LIKE ?";

        connection = Database.connectDataBase();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, "%" + searchText + "%");
        preparedStatement.setString(2, "%" + searchText + "%");
        resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            // Handle date parsing for SQLite (stores dates as strings)
            Date productDate = null;
            String dateStr = resultSet.getString("date");
            if (dateStr != null && !dateStr.isEmpty()) {
                productDate = Date.valueOf(dateStr);
            }

            ProductsData productData = new ProductsData(
                    resultSet.getInt("id"),
                    resultSet.getString("product_id"),
                    resultSet.getString("product_name"),
                    resultSet.getString("type"),
                    resultSet.getInt("stock"),
                    resultSet.getDouble("price"),
                    resultSet.getString("status"),
                    resultSet.getString("image"),
                    productDate
            );
            searchResults.add(productData);
        }

        inventory_table_view.setItems(searchResults);
    }

    /**
     * Generate sales report
     */
    public void generateSalesReport() throws SQLException {
        String report = "=== SALES REPORT ===\n\n";

        connection = Database.connectDataBase();

        // Total sales
        String totalSalesSQL = "SELECT SUM(total) as total_sales FROM receipts";
        preparedStatement = connection.prepareStatement(totalSalesSQL);
        resultSet = preparedStatement.executeQuery();

        double totalSales = 0;
        if (resultSet.next()) {
            totalSales = resultSet.getDouble("total_sales");
        }

        // Total orders
        String totalOrdersSQL = "SELECT COUNT(*) as total_orders FROM receipts";
        preparedStatement = connection.prepareStatement(totalOrdersSQL);
        resultSet = preparedStatement.executeQuery();

        int totalOrders = 0;
        if (resultSet.next()) {
            totalOrders = resultSet.getInt("total_orders");
        }

        // Today's sales (SQLite uses date('now') instead of CURDATE())
        String todaySalesSQL = "SELECT SUM(total) as today_sales FROM receipts WHERE DATE(date) = DATE('now')";
        preparedStatement = connection.prepareStatement(todaySalesSQL);
        resultSet = preparedStatement.executeQuery();

        double todaySales = 0;
        if (resultSet.next()) {
            todaySales = resultSet.getDouble("today_sales");
        }

        report += "Total Sales: " + FormatUtil.formatCurrency(totalSales) + "\n";
        report += "Total Orders: " + totalOrders + "\n";
        report += "Today's Sales: " + FormatUtil.formatCurrency(todaySales) + "\n";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sales Report");
        alert.setHeaderText(null);
        alert.setContentText(report);
        alert.showAndWait();
    }

    /**
     * Load all customers data from receipts table
     */
    public void loadCustomersData() throws SQLException {
        ObservableList<CustomerData> customersList = FXCollections.observableArrayList();

        String sql = "SELECT customer_id, total, date, em_username FROM receipts ORDER BY date DESC";
        connection = Database.connectDataBase();
        preparedStatement = connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Integer customerId = resultSet.getInt("customer_id");
            Double total = resultSet.getDouble("total");
            String dateStr = resultSet.getString("date");
            String employeeName = resultSet.getString("em_username");

            CustomerData customerData = new CustomerData(customerId, total, dateStr, employeeName);
            customersList.add(customerData);
        }

        // Set up table columns
        customers_column_id.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customers_column_total.setCellValueFactory(new PropertyValueFactory<>("total"));
        customers_column_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        customers_column_employee.setCellValueFactory(new PropertyValueFactory<>("employeeName"));

        // Set column resize policy to prevent extra empty column
        customers_table_view.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        customers_table_view.setItems(customersList);
    }

    /**
     * Search customers by customer ID
     */
    public void searchCustomers() throws SQLException {
        String searchText = customers_search_field.getText().trim();

        if (ValidationUtil.isEmpty(searchText)) {
            loadCustomersData();
            return;
        }

        ObservableList<CustomerData> customersList = FXCollections.observableArrayList();

        String sql = "SELECT customer_id, total, date, em_username FROM receipts WHERE customer_id = ? ORDER BY date DESC";
        connection = Database.connectDataBase();
        preparedStatement = connection.prepareStatement(sql);

        try {
            int customerId = Integer.parseInt(searchText);
            preparedStatement.setInt(1, customerId);
        } catch (NumberFormatException e) {
            AlertUtil.showError("Please enter a valid customer ID number.");
            return;
        }

        resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Integer customerId = resultSet.getInt("customer_id");
            Double total = resultSet.getDouble("total");
            String dateStr = resultSet.getString("date");
            String employeeName = resultSet.getString("em_username");

            CustomerData customerData = new CustomerData(customerId, total, dateStr, employeeName);
            customersList.add(customerData);
        }

        if (customersList.isEmpty()) {
            AlertUtil.showWarning("No customer found with ID: " + searchText);
        }

        customers_table_view.setItems(customersList);
    }

    /**
     * Clear customer search and show all customers
     */
    public void clearCustomerSearch() throws SQLException {
        customers_search_field.setText("");
        loadCustomersData();
    }

    /**
     * Load dashboard data from database
     */
    public void loadDashboardData() throws SQLException {
        connection = Database.connectDataBase();

        // Get total number of customers (unique customer_ids from receipts)
        String customerCountSQL = "SELECT COUNT(DISTINCT customer_id) as customer_count FROM receipts";
        preparedStatement = connection.prepareStatement(customerCountSQL);
        resultSet = preparedStatement.executeQuery();

        int customerCount = 0;
        if (resultSet.next()) {
            customerCount = resultSet.getInt("customer_count");
        }
        dashboard_customer_count.setText(String.valueOf(customerCount));

        // Get today's sales
        String todaySalesSQL = "SELECT SUM(total) as today_sales FROM receipts WHERE DATE(date) = DATE('now')";
        preparedStatement = connection.prepareStatement(todaySalesSQL);
        resultSet = preparedStatement.executeQuery();

        double todaySales = 0;
        if (resultSet.next()) {
            Double sales = resultSet.getDouble("today_sales");
            if (sales != null) {
                todaySales = sales;
            }
        }
        dashboard_today_sale.setText(String.format("£%.2f", todaySales));

        // Get total sales
        String totalSalesSQL = "SELECT SUM(total) as total_sales FROM receipts";
        preparedStatement = connection.prepareStatement(totalSalesSQL);
        resultSet = preparedStatement.executeQuery();

        double totalSales = 0;
        if (resultSet.next()) {
            Double sales = resultSet.getDouble("total_sales");
            if (sales != null) {
                totalSales = sales;
            }
        }
        dashboard_total_sale.setText(String.format("£%.2f", totalSales));
    }

    /**
     * Generate inventory report
     */
    public void generateInventoryReport() throws SQLException {
        String report = "=== INVENTORY REPORT ===\n\n";

        connection = Database.connectDataBase();

        // Total products
        String totalProductsSQL = "SELECT COUNT(*) as total_products FROM products";
        preparedStatement = connection.prepareStatement(totalProductsSQL);
        resultSet = preparedStatement.executeQuery();

        int totalProducts = 0;
        if (resultSet.next()) {
            totalProducts = resultSet.getInt("total_products");
        }

        // Available products
        String availableSQL = "SELECT COUNT(*) as available FROM products WHERE status = 'Available'";
        preparedStatement = connection.prepareStatement(availableSQL);
        resultSet = preparedStatement.executeQuery();

        int available = 0;
        if (resultSet.next()) {
            available = resultSet.getInt("available");
        }

        // Low stock products (stock < 10)
        String lowStockSQL = "SELECT COUNT(*) as low_stock FROM products WHERE stock < 10";
        preparedStatement = connection.prepareStatement(lowStockSQL);
        resultSet = preparedStatement.executeQuery();

        int lowStock = 0;
        if (resultSet.next()) {
            lowStock = resultSet.getInt("low_stock");
        }

        report += "Total Products: " + totalProducts + "\n";
        report += "Available Products: " + available + "\n";
        report += "Low Stock Products: " + lowStock + "\n";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Inventory Report");
        alert.setHeaderText(null);
        alert.setContentText(report);
        alert.showAndWait();
    }

    private void initShowInventoryData() {
        try {
            showInventoryData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initDisplayMenuCards() {
        try {
            displayMenuCards();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void initShowMenuTableView() {
        try {
            showMenuTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initLoadDashboardData() {
        try {
            loadDashboardData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        showUserName();
        showInventoryTypeList();
        showInventoryStatusList();
        initShowInventoryData();

        initDisplayMenuCards();

        initShowMenuTableView();
        calculateTotalPrice();

        initLoadDashboardData();

    }
}