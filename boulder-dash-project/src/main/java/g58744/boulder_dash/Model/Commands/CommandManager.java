package g58744.boulder_dash.Model.Commands;

import java.util.Stack;

public class CommandManager {

    private final Stack<Command> undoStack;
    private final Stack<Command> redoStack;

    public CommandManager() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    /**
     * Executes the given command and memorises it.
     * @param cmd the command to execute
     */
    public void doCommand(Command cmd) {
        cmd.execute();
        redoStack.push(cmd);
        undoStack.clear();
    }

    /**
     * undoes recent move(s)
     */
    public void undo() {
        if(!redoStack.empty()) {
            Command cmd = redoStack.pop();
            cmd.unexecute();
            undoStack.push(cmd);
        }
    }

    /**
     * redoes changes that have been undone
     */
    public void redo(){
        if(!undoStack.empty()) {
            Command cmd = undoStack.pop();
            cmd.execute();
            redoStack.push(cmd);
        }
    }
}
