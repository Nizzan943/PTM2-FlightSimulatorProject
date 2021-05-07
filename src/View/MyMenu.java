package View;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;

public class MyMenu extends Pane {

    Menu menu;
    MenuItem loadXML;
    MenuItem loadAlgorithm;
    MenuBar menuBar;

    public MenuBar set() {
        menu = new Menu("Settings");

        loadXML = new MenuItem("Load XML file");

        loadAlgorithm = new MenuItem("Load Algorithm class");

        menu.getItems().addAll(loadXML,loadAlgorithm);

        menuBar = new MenuBar();

        menuBar.getMenus().add(menu);

        menuBar.setPrefHeight(25);

        menuBar.setPrefWidth(797);

        return menuBar;
    }
}
