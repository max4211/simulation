package visualization;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class GridPaneController {

    @FXML
    private void mouseEntered(MouseEvent e) {
        Node source = (Node)e.getSource();
        Integer col = GridPane.getColumnIndex(source);
        Integer row = GridPane.getRowIndex(source);
        System.out.printf("Mouse entered cell [%d, %d]", col.intValue(), row.intValue());
    }
}
