package graphicslab;

import graphicslab.window.Window;

public class DemoTestOne {
	public static void main(String[] args) {
		Window window = new Window(500, 500, "demo");
		
		window.setStateRoutine((win) -> {
			System.out.println("hi");
		});
		
		window.showWindow();
		window.startLoop();
	}
}
