package editor;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.ScrollBar;

public class SBar {
    static double Yoffset = 0;

    private ScrollBar scrollBar;
    private TextNode textNode;
    private MyCursor cursor;
    private Group textRoot;

    public SBar(Group root, Group textRoot, TextNode textNode, MyCursor cursor) {
        this.textNode = textNode;
        this.cursor = cursor;
        this.textRoot = textRoot;

        double max = textNode.getNumOfLines() * textNode.getTextHeight();
        if (max <= Editor.WINDOW_HEIGHT)
            max = Editor.WINDOW_HEIGHT;

        scrollBar = new ScrollBar();
        scrollBar.setOrientation(Orientation.VERTICAL);
        scrollBar.setCursor(Cursor.DEFAULT);
        scrollBar.setLayoutX(Editor.USABLE_WIDTH);
        scrollBar.setPrefHeight(Editor.WINDOW_HEIGHT);
        scrollBar.setMin(Editor.WINDOW_HEIGHT);
        scrollBar.setMax(max);
        scrollBar.setValue(Editor.WINDOW_HEIGHT);

        root.getChildren().add(scrollBar);

        scrollBar.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldValue,
                    Number newValue) {
                Yoffset = newValue.doubleValue() - Editor.WINDOW_HEIGHT;
                textRoot.setLayoutY(-Yoffset);
            }
        });
    }

    // update when resizing the window
    public void resize() {
        scrollBar.setLayoutX(Editor.USABLE_WIDTH);
        scrollBar.setPrefHeight(Editor.WINDOW_HEIGHT);

        double newMin = Editor.WINDOW_HEIGHT;
        double newMax = textNode.getNumOfLines() * textNode.getTextHeight();
        if (newMax <= Editor.WINDOW_HEIGHT)
            newMax = Editor.WINDOW_HEIGHT;

        /*
        double newValue = (scrollBar.getMax() == scrollBar.getMin())?
                newMin :
                (scrollBar.getValue()-scrollBar.getMin())/(scrollBar.getMax()-scrollBar.getMin
                     ()) * (newMax-newMin) + newMin;
        */

        scrollBar.setMin(newMin);
        scrollBar.setMax(newMax);

        /*
        if (newValue == scrollBar.getValue()) {
            Yoffset = newValue - editor.Editor.WINDOW_HEIGHT;
            textRoot.setLayoutY(-Yoffset);
        }
        scrollBar.setValue(newValue);  */

        if (newMin == newMax) scrollBar.setValue(newMin);
        double newValue = Yoffset + Editor.WINDOW_HEIGHT;
        scrollBar.setValue(newValue);
    }

    // update to display the cursor
    public void update() {
        double value = scrollBar.getValue();
        if (cursor.getY() < Yoffset)
            value = cursor.getY() + Editor.WINDOW_HEIGHT;
        else if (cursor.getY() + textNode.getTextHeight() > Yoffset + Editor.WINDOW_HEIGHT)
            value = cursor.getY() + textNode.getTextHeight();

        scrollBar.setValue(value);

    }
}
