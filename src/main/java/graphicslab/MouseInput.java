package graphicslab;

import org.joml.Vector2d;
import org.joml.Vector2f;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorEnterCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

public class MouseInput {

    private final Vector2d previousPos;

    private final Vector2d currentPos;

    private final Vector2f displVec;

    private boolean inWindow = false;

    private boolean leftButtonPressed = false;

    private boolean rightButtonPressed = false;

    private GLFWCursorPosCallbackI cursorPosCallback;

    private GLFWCursorEnterCallbackI cursorEnterCallback;

    private GLFWMouseButtonCallbackI mouseButtonCallback;

    public MouseInput() {
        previousPos = new Vector2d(-1, -1);
        currentPos = new Vector2d(0, 0);
        displVec = new Vector2f();
    }

    public void init(Window window) {
        glfwSetCursorPosCallback(window.getWindowHandle(), (cursorPosCallback = (windowpointer, xpos, ypos) -> {
            currentPos.x = xpos;
            currentPos.y = ypos;
    	}));
        
        glfwSetCursorEnterCallback(window.getWindowHandle(), (cursorEnterCallback = (windowpointer, entered) -> {
        	inWindow = entered;
        }));
        
        glfwSetMouseButtonCallback(window.getWindowHandle(), (mouseButtonCallback = (windowpointer, button, action, mods) -> {
        	leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
        	rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
        }));
    }

    public Vector2f getDisplVec() {
        return displVec;
    }

    public void input(Window window) {
        displVec.x = 0;
        displVec.y = 0;
        if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
            double deltax = currentPos.x - previousPos.x;
            double deltay = currentPos.y - previousPos.y;
            boolean rotateX = deltax != 0;
            boolean rotateY = deltay != 0;
            if (rotateX) {
                displVec.y = (float) deltax;
            }
            if (rotateY) {
                displVec.x = (float) deltay;
            }
        }
        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
    }

    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }
}