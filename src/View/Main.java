package View;

import Model.Model;
import ViewModel.ViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override

    public void start(Stage primaryStage) throws Exception{

        Model model = new Model();
        ViewModel viewModel = new ViewModel(model);
        model.addObserver(viewModel);
        FXMLLoader fxml = new FXMLLoader();
        fxml.setLocation(getClass().getResource("App.fxml"));
        Parent root = fxml.load();
        Controller mwc = fxml.getController(); // View
        mwc.setViewModel(viewModel);
        viewModel.addObserver(mwc);
        primaryStage.setTitle("Flight Simulator");
        primaryStage.setScene(new Scene(root, 797  , 571));
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}
