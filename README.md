# Berkeley CS61B_Spring2016 Project2 - Editor

This project is to build a basic **Text Editor** from scratch using *JavaFX*, which supports the following features. 

- **Cursor** 

  The current position of the cursor is marked with a flashing vertical line.

- **Text input** 

  Each time the user types a letter on the keyboard, that letter appears on the screen after the current cursor position, and the cursor advances to be after the last letter that was typed.

- **Word wrapping** 

  The editor breaks text into lines such that it fits the width of the text editor window without requiring the user to scroll horizontally. The editor breaks lines between words rather than within words. Lines should only be broken in the middle of a word when the word does not fit on its own line.

- **Newlines** 

  When the user presses the Enter or Return key, the text editor advances the cursor to the beginning of the next line.

- **Backspace** 
  
  Pressing the backspace causes the character before the current cursor position to be deleted.

- **Open and save** 

  The editor accepts a single command line argument describing the location of the file to edit. If that file        exists, the editor displays the contents of that file. Pressing shortcut+s saves the current contents of the editor to that file.

- **Arrow keys** 

  Pressing any of the four arrow keys (up, down, left, right) causes the cursor to move accordingly (e.g., the up key should move the cursor to be on the previous line at the horizontal position closest to the horizontal position of the cursor before the arrow was pressed). 

- **Mouse input** 

  When the user clicks somewhere on the screen, the cursor moves to the place in the text closest to that location.

- **Window re-sizing** 

  When the user re-sizes the window, the text is re-displayed so that it fits in the new window.

- **Vertical scrolling** 

  The text editor has a vertical scroll bar on the right side of the editor that allows the user to vertically navigate through the file. Moving the scroll bar changes the positioning of the file (but not the cursor position), and if the cursor is moved (e.g., using the arrow keys) so that it's not visible, the scroll bar and window position is updated so that the cursor is visible.

- **Undo and redo** 

  Pressing shortcut+z undoes the most recent action (either inserting a character or removing a character), and pressing shortcut+y should redo. The editor can undo up to 100 actions, but no more.

- **Changing font size** 

  Pressing shortcut+"+" increases the font size by 4 points and pressing shortcut+"-" decreases the font size by 4 points.
> *"shortcut" means the control key on Windows and Linux, and the command key on Mac.*
