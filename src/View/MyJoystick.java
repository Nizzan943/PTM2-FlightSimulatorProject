package View;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

import java.util.ArrayList;
import java.util.List;

public class MyJoystick extends Pane {

    Slider rudder;
    Slider throttle;

    Circle innerCircle;

    public List<Node> set() {
        List<Node> ret = new ArrayList<>();

        rudder = new Slider();
        rudder.setLayoutX(620);
        rudder.setLayoutY(174);
        rudder.setPrefSize(160, 14);
        ret.add(rudder);

        throttle = new Slider();
        throttle.setLayoutX(620);
        throttle.setLayoutY(33);
        throttle.setPrefSize(14, 139);
        throttle.setOrientation(Orientation.VERTICAL);
        ret.add(throttle);


        Circle outerCircle = new Circle();
        outerCircle.setRadius(65);
        outerCircle.setLayoutX(710);
        outerCircle.setLayoutY(103);
        outerCircle.setFill(Paint.valueOf("e8e8e8"));
        outerCircle.setStroke(Paint.valueOf("BLACK"));
        outerCircle.setStrokeType(StrokeType.INSIDE);
        ret.add(outerCircle);

        innerCircle = new Circle();
        innerCircle.setRadius(35);
        innerCircle.setLayoutX(710);
        innerCircle.setLayoutY(103);
        innerCircle.setFill(Paint.valueOf("afb4b9"));
        innerCircle.setStroke(Paint.valueOf("BLACK"));
        innerCircle.setStrokeType(StrokeType.INSIDE);
        ret.add(innerCircle);

        return ret;
    }
}
