package View;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class MyClocksPannel extends Pane {
    Label altimeter;
    Label airspeed;
    Label direction;
    Label pitch;
    Label roll;
    Label yaw;

    public List<Node> set() {
        List<Node> ret = new ArrayList<>();

        altimeter = new Label("altimeter:");
        altimeter.setFont(new Font(15));
        altimeter.setLayoutX(620);
        altimeter.setLayoutY(210);
        ret.add(altimeter);

        airspeed = new Label("airspeed:");
        airspeed.setFont(new Font(15));
        airspeed.setLayoutX(620);
        airspeed.setLayoutY(250);
        ret.add(airspeed);

        direction = new Label("direction:");
        direction.setFont(new Font(15));
        direction.setLayoutX(620);
        direction.setLayoutY(290);
        ret.add(direction);

        pitch = new Label("pitch:");
        pitch.setFont(new Font(15));
        pitch.setLayoutX(620);
        pitch.setLayoutY(330);
        ret.add(pitch);

        roll = new Label("roll:");
        roll.setFont(new Font(15));
        roll.setLayoutX(620);
        roll.setLayoutY(370);
        ret.add(roll);

        yaw = new Label("yaw:");
        yaw.setFont(new Font(15));
        yaw.setLayoutX(620);
        yaw.setLayoutY(410);
        ret.add(yaw);

        return ret;
    }
}
