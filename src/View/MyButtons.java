package View;

import View.Controller;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class MyButtons extends Pane
{
    ChoiceBox playSpeedDropDown;
    Label timer;
    Button play;
    Button pause;
    Button stop;

    public List<Node> set()
    {
        List <Node> ret = new ArrayList<>();

        play = new Button("Play");
        play.setLayoutX(25);
        play.setLayoutY(470);
        play.setPrefSize(74,42);
        ret.add(play);

        pause = new Button("Pause");
        pause.setLayoutX(105);
        pause.setLayoutY(470);
        pause.setPrefSize(74,42);
        ret.add(pause);

        stop = new Button("Stop");
        stop.setLayoutX(185);
        stop.setLayoutY(470);
        stop.setPrefSize(74,42);
        ret.add(stop);

        Button left = new Button("<<<");
        left.setLayoutX(265);
        left.setLayoutY(470);
        left.setPrefSize(74,42);
        ret.add(left);

        Button right = new Button(">>>");
        right.setLayoutX(345);
        right.setLayoutY(470);
        right.setPrefSize(74,42);
        ret.add(right);

        Button minus15 = new Button("-15s");
        minus15.setLayoutX(425);
        minus15.setLayoutY(470);
        minus15.setPrefSize(74,42);
        ret.add(minus15);

        Button plus15 = new Button("+15s");
        plus15.setLayoutX(505);
        plus15.setLayoutY(470);
        plus15.setPrefSize(74,42);
        ret.add(plus15);

        Label playSpeed = new Label("Play Speed");
        playSpeed.setFont(new Font(20));
        playSpeed.setLayoutX(588);
        playSpeed.setLayoutY(470);
        playSpeed.setPrefSize(105,36);
        ret.add(playSpeed);

        playSpeedDropDown = new ChoiceBox();
        playSpeedDropDown.getItems().addAll("x0.5", "x1.0", "x1.5", "x2.0");
        playSpeedDropDown.setLayoutX(695);
        playSpeedDropDown.setLayoutY(470);
        playSpeedDropDown.setPrefSize(67,42);
        ret.add(playSpeedDropDown);

        Slider slider = new Slider();
        slider.setLayoutX(25);
        slider.setLayoutY(520);
        slider.setPrefSize(560,42);
        ret.add(slider);

        timer = new Label();
        timer.setFont(new Font(15));
        timer.setLayoutY(525);
        timer.setLayoutX(610);
        ret.add(timer);

        return ret;

    }
}
