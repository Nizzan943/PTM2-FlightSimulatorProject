package View;

import Model.Model;
import ViewModel.ViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Model model = new Model();
        ViewModel viewModel = new ViewModel(model);
        model.addObserver(viewModel);
        Parent root = FXMLLoader.load(getClass().getResource("App.fxml"));
        MenuBarController menuBarController = new MenuBarController();
        menuBarController.setViewModel(viewModel);
        primaryStage.setTitle("Flight Simulator");
        primaryStage.setScene(new Scene(root, 797  , 571));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
