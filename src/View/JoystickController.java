package View;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import java.util.Observable;

public class JoystickController extends Observable
{

    @FXML
    Circle Joystick;
    double defaultY;
    double defaultX;
    boolean mousePushed;

    public JoystickController() {
        this.mousePushed = false;


    }

   public void MouseDown(MouseEvent mouseEvent)
   {
       if(!mousePushed) {
           mousePushed = true;

           defaultX = Joystick.getCenterX();

           defaultY = Joystick.getCenterY();
       }

   }

    public void MouseUp(MouseEvent mouseEvent)
    {
        if(mousePushed)
        {
            mousePushed = false;

            Joystick.setCenterX(defaultX);

            Joystick.setCenterY(defaultY);

        }
    }


    public void MouseMove(MouseEvent mouseEvent)
    {
        if(mousePushed)
        {

            Joystick.setCenterY(mouseEvent.getY());

            Joystick.setCenterX(mouseEvent.getX());

            Joystick.getBoundsInParent();



        }

    }


}
