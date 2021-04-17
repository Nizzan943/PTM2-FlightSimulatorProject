package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Controller test = new Controller();
        test.test1();
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hey Yuv!");
        primaryStage.setScene(new Scene(root, 600  , 400));
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);



    }
}
