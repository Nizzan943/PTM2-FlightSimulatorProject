package View;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class MyListView extends Pane
{
    ListView listView;
    Button open;

    public List<Node> set()
    {
        List <Node> ret = new ArrayList<>();

        listView = new ListView();
        listView.setPrefSize(163, 362);
        listView.setLayoutY(43);
        listView.setLayoutX(25);
        ret.add(listView);

        open = new Button("Open");
        open.setLayoutX(25);
        open.setLayoutY(410);
        open.setPrefSize(163,42);
        ret.add(open);

        return ret;
    }
}
