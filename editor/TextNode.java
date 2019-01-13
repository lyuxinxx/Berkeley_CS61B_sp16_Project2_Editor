package editor;

import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.FileWriter;
import java.io.IOException;

public class TextNode {

    private Group root;
    private Node currNode;
    private Node sentinel;
    private Node[] lines;
    private long textHeight;
    private int numOfLines;
    private class Node {
        Node prev;
        Node next;
        Text text;

        Node(Text text, Node prev, Node next) {
            this.text = text;
            this.prev = prev;
            this.next = next;
        }
    }

    public TextNode(Group root) {
        sentinel = new Node(new Text(""), null,null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        currNode = sentinel;
        this.root = root;
    }

    /** add a Node after the currNode */
    public void addChar(String s) {
        Text newText = new Text();
        Node newNode = new Node(newText, currNode, currNode.next);
        currNode.next.prev = newNode;
        currNode.next = newNode;
        newText.setText(s);
        newText.setTextOrigin(VPos.TOP);
        currNode = newNode;
        root.getChildren().add(newText);
    }

    private Node addCharBefore(String s, Node x) {
        Text newText = new Text();
        Node newNode = new Node(newText, x.prev.prev, x);
        x.prev.next = newNode;
        x.prev = newNode;
        newText.setText(s);
        newText.setTextOrigin(VPos.TOP);
        root.getChildren().add(newText);

        return newNode;
    }

    /** delete currNode */
    public String deleteChar() {
        if (currNode == sentinel) return "";
        if (currNode.text.getText().equals("")) {
            root.getChildren().remove(currNode.text);
            currNode.prev.next = currNode.next;
            currNode.next.prev = currNode.prev;
            currNode = currNode.prev;
        }
        String s = currNode.text.getText();
        root.getChildren().remove(currNode.text);
        currNode.prev.next = currNode.next;
        currNode.next.prev = currNode.prev;
        currNode = currNode.prev;
        return s;
    }

    /** draw the text to the screen*/
    public void render() {
        Text temp = new Text("");
        temp.setFont((Font.font(Editor.fontName, Editor.fontSize)));
        textHeight = Math.round(temp.getLayoutBounds().getHeight());
        //if (textHeight)
        lines = new Node[Editor.MAX_LINE];
        lines[0] = sentinel;
        numOfLines = 1;
        int x = Editor.STARTING_TEXT_POSITION_X;
        int y = Editor.STARTING_TEXT_POSITION_Y;

        Node p = sentinel;
        p.text.setFont(Font.font(Editor.fontName, Editor.fontSize));
        p.text.setX(x);
        p.text.setY(y);
        Text prevText = p.text;
        p = p.next;
        Node lastSpace = null;

        while (p != sentinel) {
            p.text.setFont(Font.font(Editor.fontName, Editor.fontSize));
            if (p.text.getText().equals(" ")) {
                lastSpace = p;
            }
            if (isNewLine(p.text)) { // handles newLine
                x = Editor.STARTING_TEXT_POSITION_X;
                y += textHeight;
                lines[numOfLines++] = p;
            }
            else {
                x += Math.round(prevText.getLayoutBounds().getWidth());
                if (x + p.text.getLayoutBounds().getWidth() > Editor.USABLE_WIDTH - Editor.STARTING_TEXT_POSITION_X) {
                    // multiple space at the end of one line
                    if (p.text.getText().equals(" ")) {
                        x -= Math.round(p.text.getLayoutBounds().getWidth());
                    }
                    // when a word exceeds one line
                    else if (lastSpace == null || lastSpace.text.getY() < y) {
                        x = Editor.STARTING_TEXT_POSITION_X;
                        y += textHeight;
                        if (!p.text.getText().equals("")) p = addCharBefore("", p);
                        lines[numOfLines++] = p;
                    } else {
                        x = Editor.STARTING_TEXT_POSITION_X;
                        y += textHeight;
                        p = lastSpace.next;
                        if (!p.text.getText().equals("")) p = addCharBefore("", p);
                        lines[numOfLines++] = p;
                    }
                }
            }
            p.text.setX(x);
            p.text.setY(y);
            prevText = p.text;
            p = p.next;
        }
    }

    private boolean isNewLine(Text t) {
        return t.getText().equals("\r") || t.getText().equals("\r" +
                "\n") || t.getText().equals("\n");
    }

    /** move the currNode */
    public void moveByKey(KeyCode code) {
        if (code == KeyCode.UP) {
            double X = currNode.text.getX() + currNode.text.getLayoutBounds().getWidth();
            double Y = currNode.text.getY();
            Node tempNode = currNode;
            while (tempNode != sentinel) {
                tempNode = tempNode.prev;
                if (tempNode.text.getY() < Y && Math.abs(tempNode.text.getX()+tempNode.text.getLayoutBounds().getWidth() - X) <= Math.abs(tempNode.prev.text.getX()+tempNode.prev.text.getLayoutBounds().getWidth() - X)) {
                    currNode = tempNode;
                    break;
                }
            }
        }
        if (code == KeyCode.DOWN) {
            double X = currNode.text.getX() + currNode.text.getLayoutBounds().getWidth();
            double Y = currNode.text.getY();
            Node tempNode = currNode;
            while (tempNode.next != sentinel) {
                if (tempNode.text.getY() > Y  && Math.abs(tempNode.text.getX()+tempNode.text.getLayoutBounds().getWidth() - X) <= Math.abs(tempNode.next.text.getX()+tempNode.next.text.getLayoutBounds().getWidth() - X)) {
                    currNode = tempNode;
                    break;
                }
                tempNode = tempNode.next;
            }
        }
        if (code == KeyCode.LEFT) {
            if (currNode != sentinel) currNode = currNode.prev;
            if (currNode != sentinel && currNode.text.getText().equals("") && currNode.prev.text.getText().equals(""))
                currNode = currNode.prev;
        }
        if (code == KeyCode.RIGHT) {
            if (currNode.next != sentinel) currNode = currNode.next;
            if (currNode.next != sentinel && currNode.text.getText().equals("") && currNode.next.text.getText().equals(""))
                currNode = currNode.next;
        }
    }

    public int getNumOfLines() {
        return numOfLines;
    }

    public long getTextHeight() {
        return textHeight;
    }

    public void reset() {
        currNode = sentinel;
    }

    public void writeTo(String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            Node tempNode = sentinel.next;
            while (tempNode != sentinel) {
                writer.write(tempNode.text.getText());
                tempNode = tempNode.next;
            }
            writer.close();
            System.out.println("Successfully write to file " + filename);
        } catch (IOException e) {
            System.out.println("Unable to write to file " + filename);
        }
    }

    public Text getCurrText() {
        return currNode.text;
    }

    public void setCurrNode(double x, double y) {
        int numOfLine = (int)(y/textHeight);
        Node line = lines[numOfLine];
        if (line == null) {
            currNode = sentinel.prev;
            return;
        } else {
            while (true) {
                if (line.next == null || line.next.text.getY() > line.text.getY()) {
                    currNode = line;
                    return;
                }
                if (Math.abs(line.text.getX() + line.text.getLayoutBounds().getWidth() - x) <= Math.abs(line.next.text.getX() + line.next.text.getLayoutBounds().getWidth() - x)) {
                    currNode = line;
                    return;
                }
                line = line.next;
            }
        }
    }
}
