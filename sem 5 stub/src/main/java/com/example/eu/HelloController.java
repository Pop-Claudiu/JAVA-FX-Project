package com.example.eu;

import Domain.Car;
import Domain.Inchiriere;
import Service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HelloController {
    private Service service;

    public HelloController() {
    }

    public HelloController(Service service) {
        this.service = service;
    }
    public Button carButton;
    public VBox mainView;
    public Button rentalButton;
    public ListView listView;
    public GridPane gridPane;
    public Button carID, manufacturer, startDate, model, endDate;
    public TextField textField4, textField1, textField2, textField3;

    public void initialize() {
        carID.setVisible(false);
        startDate.setVisible(false);
        endDate.setVisible(false);
        textField4.setVisible(false);
    }

    public void listViewMouseClicked() {
        Object obj = listView.getSelectionModel().getSelectedItem();
        if(obj instanceof Car){
            Car car = (Car) obj;
            textField1.setText(Integer.toString(car.getID()));
            textField2.setText(car.getMarca());
            textField3.setText(car.getModel());
        } else if (obj instanceof Inchiriere) {
            Inchiriere inchiriere = (Inchiriere) obj;
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            textField1.setText(Integer.toString(inchiriere.getID()));
            textField2.setText(Integer.toString(inchiriere.getCar().getID()));
            textField3.setText(formatter.format(inchiriere.getStartDate()));
            textField4.setText(formatter.format(inchiriere.getEndDate()));
        }
    }
    public void carButtonClicked(MouseEvent mouseEvent) {
        ObservableList<Car> list = FXCollections.observableList(service.getRepoCar());
        listView.setItems(list);
        model.setVisible(true);
        manufacturer.setVisible(true);
        carID.setVisible(false);
        startDate.setVisible(false);
        endDate.setVisible(false);
        textField4.setVisible(false);
        textField1.clear();
        textField2.clear();
        textField3.clear();

    }

    public void rentalButtonClicked(MouseEvent mouseEvent) {
        ObservableList<Inchiriere> list = FXCollections.observableList(service.getRepoInchiriere());
        listView.setItems(list);
        model.setVisible(false);
        manufacturer.setVisible(false);
        carID.setVisible(true);
        startDate.setVisible(true);
        endDate.setVisible(true);
        textField4.setVisible(true);
        textField1.clear();
        textField2.clear();
        textField3.clear();
        textField4.clear();
    }

    public void addButtonClicked(){
        try {
            if(textField4.isVisible()){
                //Inseamna ca suntem pe inchiriere
                service.addInchiriere(textField1.getText(), textField2.getText(), textField3.getText(), textField4.getText());
                ObservableList<Inchiriere> list = FXCollections.observableList(service.getRepoInchiriere());
                listView.setItems(list);
            }
            else {
                service.addCar(textField1.getText(), textField2.getText(), textField3.getText());
                ObservableList<Car> list = FXCollections.observableList(service.getRepoCar());
                listView.setItems(list);
            }

        } catch (Exception e) {
            Alert errorPopUp = new Alert(Alert.AlertType.ERROR);
            errorPopUp.setTitle("Error");
            errorPopUp.setContentText(e.getMessage());
            errorPopUp.show();
        }
    }
    public void updateButtonClicked(){
        try {
            if(textField4.isVisible()){
                service.updateInchiriere(textField1.getText(), textField2.getText(), textField3.getText(), textField4.getText());
                ObservableList<Inchiriere> list = FXCollections.observableList(service.getRepoInchiriere());
                listView.setItems(list);
            }
            else {
                service.updateCar(textField1.getText(), textField2.getText(), textField3.getText());
                ObservableList<Car> list = FXCollections.observableList(service.getRepoCar());
                listView.setItems(list);
            }

        } catch (Exception e) {
            Alert errorPopUp = new Alert(Alert.AlertType.ERROR);
            errorPopUp.setTitle("Error");
            errorPopUp.setContentText(e.getMessage());
            errorPopUp.show();
        }
    }
    public void deleteButtonClicked(){
        try {
            if(textField4.isVisible()){
                service.deleteInchiriere(textField1.getText());
                ObservableList<Inchiriere> list = FXCollections.observableList(service.getRepoInchiriere());
                listView.setItems(list);
            }
            else {
                service.deleteCar(textField1.getText());
                ObservableList<Car> list = FXCollections.observableList(service.getRepoCar());
                listView.setItems(list);
            }

        } catch (Exception e) {
            Alert errorPopUp = new Alert(Alert.AlertType.ERROR);
            errorPopUp.setTitle("Error");
            errorPopUp.setContentText(e.getMessage());
            errorPopUp.show();
        }
    }

    public void mostRentedCars() {
        var result = service.getMostRentedCars();
        List<String> auxiliary = new ArrayList<>();
        for(var it: result.entrySet()) {
            String aux = it.getKey().toString() + " rented: " + it.getValue();
            auxiliary.add(aux);
        }
        listView.setItems(FXCollections.observableList(auxiliary));
    }
    public void rentalsMonth() {
        var result = service.getEachMonthNumberOfRentals();
        List<String> auxiliary = new ArrayList<>();
        for(var it: result.entrySet()) {
            String aux = it.getKey() + " no rentals: " + it.getValue();
            auxiliary.add(aux);
        }
        listView.setItems(FXCollections.observableList(auxiliary));
    }

    public void carsLonges() {
        var result = service.getCarWithLongestRentalPeriod();
        List<String> auxiliary = new ArrayList<>();
        for(var it: result.entrySet()) {
            String aux = it.getKey().toString() + ", days rented: " + it.getValue();
            auxiliary.add(aux);
        }
        listView.setItems(FXCollections.observableList(auxiliary));
    }
}