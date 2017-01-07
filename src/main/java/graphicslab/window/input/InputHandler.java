package graphicslab.window.input;

import graphicslab.window.Window;

public abstract class InputHandler {
    public InputHandler(Window window) {
        if (window != null) {            
            window.addInputHandler(this);
        }
    }
    
    public InputHandler() {
        this(null);
    }
    
    public abstract void init(Window window);
    
    public abstract void input();
}
