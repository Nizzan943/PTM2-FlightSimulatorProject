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
    Slider aileron;
    Slider elevator;

    public List<Node> set() {
        List<Node> ret = new ArrayList<>();

        aileron = new Slider();
        aileron.setLayoutX(620);
        aileron.setLayoutY(174);
        aileron.setPrefSize(160, 14);
        ret.add(aileron);

        elevator = new Slider();
        elevator.setLayoutX(620);
        elevator.setLayoutY(33);
        elevator.setPrefSize(14, 139);
        elevator.setOrientation(Orientation.VERTICAL);
        ret.add(elevator);


        Circle outerCircle = new Circle();
        outerCircle.setRadius(65);
        outerCircle.setLayoutX(710);
        outerCircle.setLayoutY(103);
        outerCircle.setFill(Paint.valueOf("e8e8e8"));
        outerCircle.setStroke(Paint.valueOf("BLACK"));
        outerCircle.setStrokeType(StrokeType.INSIDE);
        ret.add(outerCircle);

        Circle innerCircle = new Circle();
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
