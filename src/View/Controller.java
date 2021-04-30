package View;

import Server.HandleXML;
import ViewModel.ViewModel;

import java.util.Observable;
import java.util.Observer;


public class Controller  implements Observer {
    public static HandleXML XML_settings;
    public static String CSVpath;


    ButtonsController buttonsController = new ButtonsController();
    JoystickController joystickController = new JoystickController();
    ListViewController listViewController = new ListViewController();
    MenuBarController menuBarController = new MenuBarController();


    @Override
    public void update(Observable o, Object arg)
    {
    }
}
