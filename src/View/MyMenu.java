package View;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;

public class MyMenu extends Pane {

    Menu m;
    MenuItem m1;
    MenuBar mb;

    public MenuBar set()
    {
        m = new Menu("Settings");

        m1 = new MenuItem("Load XML file");

        m.getItems().add(m1);

       mb = new MenuBar();

        mb.getMenus().add(m);
        mb.setPrefHeight(25);
        mb.setPrefWidth(797);
        return mb;
    }
}
