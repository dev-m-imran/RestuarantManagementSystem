package com.example.restuarantms;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * ReceiptController - handles receipt display and printing
 */
public class ReceiptController {

    @FXML
    private VBox receiptPane;

    @FXML
    private VBox itemsBox;

    @FXML
    private Label dateLabel;

    @FXML
    private Label totalLabel;

    @FXML
    private Label amountLabel;

    @FXML
    private Label changeLabel;

    public void setReceiptData(List<ProductsData> items,
                               double total,
                               double amount,
                               double change) {

        dateLabel.setText(
                "Date: " + LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        );

        itemsBox.getChildren().clear();

        for (ProductsData item : items) {
            String line = String.format(
                    "%-15s x%-2d %6.2f",
                    item.getProductName(),
                    item.getQuantity(),
                    item.getPrice()
            );

            Label label = new Label(line);
            label.setStyle("-fx-font-family: monospace; -fx-font-size:12;");
            itemsBox.getChildren().add(label);
        }

        totalLabel.setText(String.format("TOTAL   : £%.2f", total));
        amountLabel.setText(String.format("CASH    : £%.2f", amount));
        changeLabel.setText(String.format("CHANGE  : £%.2f", change));
    }

    public VBox getReceiptPane() {
        return receiptPane;
    }
}
