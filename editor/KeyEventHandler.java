package editor;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyEventHandler implements EventHandler<KeyEvent> {

    private TextNode textNode;
    private MyCursor cursor;
    private SBar scrollBar;
    private Redo_Undo redo_undo;

    KeyEventHandler(TextNode textNode, MyCursor cursor, SBar sBar, Redo_Undo redo_undo) {
        this.textNode = textNode;
        this.cursor = cursor;
        scrollBar = sBar;
        this.redo_undo = redo_undo;
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        if (keyEvent.isShortcutDown()) {
            if (keyEvent.getCode() == KeyCode.EQUALS) {
                Editor.fontSize += 5;
            } else if (keyEvent.getCode() == KeyCode.MINUS) {
                Editor.fontSize = Math.max(0, Editor.fontSize - 5);
            } else if (keyEvent.getCode() == KeyCode.P) {
                cursor.print();
            } else if (keyEvent.getCode() == KeyCode.S) {
                textNode.writeTo(Editor.filename);
            } else if (keyEvent.getCode() == KeyCode.Z) {
                redo_undo.undo();
            } else if (keyEvent.getCode() == KeyCode.Y) {
                redo_undo.redo();
            }
        }
        else if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
            if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.RIGHT){
                textNode.moveByKey(keyEvent.getCode());
            }
        }
        else if (keyEvent.getEventType() == KeyEvent.KEY_TYPED) {
            String characterTyped = keyEvent.getCharacter();
            if (characterTyped.length() > 0 && characterTyped.charAt(0) != 8) {
                // Ignore control keys, which have non-zero length, as well as the backspace
                // key, which is represented as a character of value = 8 on Windows.
                textNode.addChar(characterTyped);
                redo_undo.addChar(characterTyped);
                keyEvent.consume();
            } else if (characterTyped.length() > 0 && characterTyped.charAt(0) == 8) {
                redo_undo.deleteChar(textNode.deleteChar());
                keyEvent.consume();
            }
            redo_undo.resetRedo();
        }
        textNode.render();
        cursor.render();
        scrollBar.update();
    }
}
