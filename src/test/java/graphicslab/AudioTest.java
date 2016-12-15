package graphicslab;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import org.lwjgl.glfw.GLFW;
import static org.lwjgl.openal.AL10.*;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;

import graphicslab.audio.AudioContext;
import graphicslab.audio.SoundBuffer;
import graphicslab.window.Window;
import graphicslab.window.input.KeyboardInput;

public class AudioTest {
	static int xPosition = -10000;
	static int xVelocity = 0;
	static boolean isMoving = false;

	public static void main(String[] args) {
		
		AudioContext manager = new AudioContext();
		try {
			manager.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		SoundBuffer buffer = null;
		try {
			buffer = new SoundBuffer("src/test/rimsky.ogg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int sourceId = alGenSources();
		alSourcei(sourceId, AL_BUFFER, buffer.getId());
		
		alDistanceModel(AL11.AL_LINEAR_DISTANCE);
		
		Window window = new Window(50, 50, "Audio Test");
		
		KeyboardInput keyboard = new KeyboardInput();
		
		window.setInitializeRoutine((w) -> {
			keyboard.init(w);
		});
		
		xPosition = -10000;
		xVelocity = 0;
		isMoving = false;
		
		window.setInputRoutine((w) -> {
			if (keyboard.isKeyPressed(GLFW.GLFW_KEY_E)) {
				alSourcePlay(sourceId);
			}
			
			if (keyboard.isKeyPressed(GLFW.GLFW_KEY_R)) {
				isMoving = true;
				xVelocity += 1;
				alListener3f(AL_VELOCITY, xVelocity, 0, 0);
			}

			if (keyboard.isKeyPressed(GLFW.GLFW_KEY_F)) {
				isMoving = true;
				xVelocity -= 1;
				alListener3f(AL_VELOCITY, xVelocity, 0, 0);
			}
			
			if (!keyboard.isKeyPressed(GLFW.GLFW_KEY_T)) {
				float[] array = new float[3];
				alGetListenerfv(AL_POSITION, array);
				System.out.print(Arrays.toString(array) + "\t");
				alGetListenerfv(AL_VELOCITY, array);
				System.out.println(Arrays.toString(array));
			}
		});
		
		window.setStateRoutine((w) -> {
			if (isMoving) {
				xPosition += xVelocity;
				alListener3f(AL_POSITION, xPosition, 0, 0);
			}
		});
		
		
		
		window.showWindow();
		
		window.startLoop();
		window.destroyWindow();
		
		manager.cleanup();
	}
}
