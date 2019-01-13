package editor;

import java.util.Stack;

public class Redo_Undo {
    private class Action {
        boolean isType;
        String s;

        Action (boolean isType, String s) {
            this.isType = isType;
            this.s = s;
        }
    }

    private Stack<Action> undos;
    private Stack<Action> redos;

    private TextNode textNode;

    public Redo_Undo(TextNode textNode) {
        undos = new Stack<>();
        redos = new Stack<>();
        this.textNode = textNode;
    }

    public void resetRedo() {
        redos = new Stack<>();
    }
    public void addChar(String s) {
        if (undos.size() >= 100) undos.remove(0);
        undos.add(new Action(true, s));
    }

    public void deleteChar(String s) {
        if (undos.size() >= 100) undos.remove(0);
        undos.add(new Action(false, s));
    }
    public void redo() {
        if (redos.isEmpty()) return;
        Action action = redos.pop();
        undos.add(new Action(!action.isType, action.s));
        if (action.isType) textNode.deleteChar();
        else textNode.addChar(action.s);
    }

    public void undo() {
        if (undos.isEmpty()) return;
        Action action = undos.pop();
        redos.add(new Action(!action.isType, action.s));
        if (action.isType) textNode.deleteChar();
        else textNode.addChar(action.s);
    }
}
