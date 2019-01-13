import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class MyCursor {

    private Rectangle cursor;
    private TextNode textNode;

    public MyCursor(TextNode textNode, Group root) {
        cursor = new Rectangle(1,0);
        this.textNode = textNode;
        root.getChildren().add(cursor);
        makeRectangleColorChange();

        // init
        Text temp = new Text("");
        temp.setFont((Font.font(Editor.fontName, Editor.fontSize)));
        double textHeight = temp.getLayoutBounds().getHeight();
        cursor.setHeight(textHeight);
        cursor.setX(Editor.STARTING_TEXT_POSITION_X);
        cursor.setY(Editor.STARTING_TEXT_POSITION_Y);
    }

    public void print() {
        System.out.println((int)cursor.getX() + ", " + (int)cursor.getY());
    }

    public double getY() {
        return cursor.getY();
    }

    public void render() {
        Text temp = new Text("");
        temp.setFont((Font.font(Editor.fontName, Editor.fontSize)));
        double textHeight = temp.getLayoutBounds().getHeight();
        cursor.setHeight(textHeight);

        Text currText = textNode.getCurrText();
        double textWidth = currText.getLayoutBounds().getWidth();
        cursor.setX(currText.getX() + textWidth);
        cursor.setY(currText.getY());
    }

    private class RectangleBlinkEventHandler implements EventHandler<ActionEvent> {
        private int currentColorIndex = 0;
        private Color[] cursorColors = {Color.WHITE, Color.BLACK};

        RectangleBlinkEventHandler() {
            // Set the color to be the first color in the list.
            changeColor();
        }

        private void changeColor() {
            cursor.setFill(cursorColors[currentColorIndex]);
            currentColorIndex = (currentColorIndex + 1) % 2;
        }

        @Override
        public void handle(ActionEvent event) {
            changeColor();
        }
    }

    /** Makes the text bounding box change color periodically. */
    public void makeRectangleColorChange() {
        // Create a Timeline that will call the "handle" function of RectangleBlinkEventHandler
        // every 1 second.
        final Timeline timeline = new Timeline();
        // The rectangle should continue blinking forever.
        timeline.setCycleCount(Timeline.INDEFINITE);
        RectangleBlinkEventHandler cursorChange = new RectangleBlinkEventHandler();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.5), cursorChange);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

}
