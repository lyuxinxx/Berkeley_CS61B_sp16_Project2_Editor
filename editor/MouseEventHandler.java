package editor;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;


public class MouseEventHandler implements EventHandler<MouseEvent> {

    private TextNode textNode;
    private MyCursor cursor;

    public MouseEventHandler(TextNode textNode, MyCursor cursor) {
        this.textNode = textNode;
        this.cursor = cursor;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        double mousePressedX = mouseEvent.getX();
        double mousePressedY = mouseEvent.getY();
        textNode.render();
        textNode.setCurrNode(mousePressedX, mousePressedY + SBar.Yoffset);
        cursor.render();
    }
}
