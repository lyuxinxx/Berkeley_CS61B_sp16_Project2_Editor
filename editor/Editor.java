package editor;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.util.List;


public class Editor extends Application {
    static int WINDOW_WIDTH = 400;
    static int WINDOW_HEIGHT = 300;
    static double USABLE_WIDTH = WINDOW_WIDTH - new ScrollBar().getLayoutBounds().getWidth();
    static String fontName = "Verdana";
    static int fontSize = 12;
    static int STARTING_TEXT_POSITION_X = 5;
    static int STARTING_TEXT_POSITION_Y = 0;
    static int MAX_LINE = 10000;
    static String filename;

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Group textRoot = new Group();
        root.getChildren().add(textRoot);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT, Color.WHITE);
        scene.setCursor(Cursor.TEXT);
        TextNode textNode = new TextNode(textRoot);
        MyCursor cursor = new MyCursor(textNode, textRoot);
        Redo_Undo redo_undo = new Redo_Undo(textNode);

        // read in
        List<String> arguments = getParameters().getRaw();
        if (arguments.isEmpty()) {
            System.out.println("Filename required");
            System.exit(1);
        }

        filename = arguments.get(0);
        try {
            File inputFile = new File(filename);
            FileReader reader = new FileReader(inputFile);
            BufferedReader bufferedReader = new BufferedReader(reader);

            int intRead = -1;
            // Keep reading from the file input read() returns -1, which means the end of the file
            // was reached.
            while ((intRead = bufferedReader.read()) != -1) {
                char charRead = (char) intRead;
                textNode.addChar(Character.toString(charRead));
            }
            textNode.reset();
            textNode.render();
            bufferedReader.close();
        } catch (FileNotFoundException fileNotFoundException) {
        } catch (IOException ioException) {
            System.out.println("Unable to open file " + filename);
        }
        SBar sBar = new SBar(root, textRoot, textNode, cursor);
        EventHandler<KeyEvent> keyEventHandler = new KeyEventHandler(textNode, cursor, sBar, redo_undo);
        scene.setOnKeyTyped(keyEventHandler);
        scene.setOnKeyPressed(keyEventHandler);
        scene.setOnMouseClicked(new MouseEventHandler(textNode, cursor));

        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldScreenWidth,
                    Number newScreenWidth) {
                WINDOW_WIDTH = newScreenWidth.intValue();
                USABLE_WIDTH = WINDOW_WIDTH - new ScrollBar().getLayoutBounds().getWidth();
                textNode.render();
                cursor.render();
                sBar.resize();
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldScreenHeight,
                    Number newScreenHeight) {
                WINDOW_HEIGHT = newScreenHeight.intValue();
                textNode.render();
                cursor.render();
                sBar.resize();
            }
        });


        primaryStage.setTitle(filename);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}