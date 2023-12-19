package com.example.eu;

import Domain.*;
import Repository.*;
import Service.*;
import UI.UI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    protected IEntityFactory<Car> carFactory;
    protected IEntityFactory<Inchiriere> inchiriereFactory;
    @Override
    public void start(Stage stage) throws IOException {
        try{
            Settings settings = Settings.getInstance();
            carFactory = new CarFactory();
            inchiriereFactory = new InchiriereFactory();
            IRepository<Car> repoCar;
            IRepository<Inchiriere> repoInchiriere;
            //Class repoType = Class.forName(settings.getRepository());
            //TODO replace with refelction
            switch (settings.getRepository()) {
                case "binary":
                    repoCar = new BinaryFileRepository<Car>(settings.getCar());
                    repoInchiriere = new BinaryFileRepository<Inchiriere>(settings.getInchiriere());
                    break;
                case "text":
                    repoCar = new TextFileRepository<>(settings.getCar(), carFactory);
                    repoInchiriere = new TextFileRepository<>(settings.getInchiriere(), inchiriereFactory);
                    break;
                case "DB":
                    repoCar = new DBCarRepository("jdbc:sqlite:DataBaseGUI.db");
                    repoInchiriere = new DBInchiriereRepository("jdbc:sqlite:DataBaseGUI.db");
                    break;
                default:
                    repoCar = new Repository<Car>();
                    repoInchiriere = new Repository<Inchiriere>();
            }
            Service service = new Service(repoCar, repoInchiriere);
//            for (int i = 1; i <= 100 ; i++) {
//                repoCar.deleteEntity(i);
//                repoInchiriere.deleteEntity(i);
//            }

            if(Objects.equals(settings.getRunConfig(), "terminal")){
                UI ui = new UI(service);
                ui.runMenu();
            }
            else if(Objects.equals(settings.getRunConfig(), "UI")){
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
                fxmlLoader.setController(new HelloController(service));
                Scene scene = new Scene(fxmlLoader.load(), 600, 600);
                stage.setScene(scene);
                stage.show();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}