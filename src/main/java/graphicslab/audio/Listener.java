package graphicslab.audio;

import org.joml.Vector3f;

import graphicslab.Initializable;

import static org.lwjgl.openal.AL10.*;

/**
 * The listener is an object representation of the OpenAL listener. For each context, there is only one listener that can receive data.
 * The pitches of sound sources are affected by velocity and position of both the sources and the listener.
 *
 */
public class Listener implements Initializable {

	private final Vector3f position;
	private final Vector3f velocity;
	
    public Listener() {
        this(new Vector3f(0, 0, 0));
    }

    public Listener(Vector3f position) {
    	this.position = position;
    	this.velocity = new Vector3f();
    }
    
    @Override
    public void init() {    	
    	alListener3f(AL_POSITION, position.x, position.y, position.z);
    	alListener3f(AL_VELOCITY, 0, 0, 0);
    }

    public void setVelocity(Vector3f velocity) {
    	this.velocity.set(velocity);
        alListener3f(AL_VELOCITY, velocity.x, velocity.y, velocity.z);
    }

    public void setPosition(Vector3f position) {
        alListener3f(AL_POSITION, position.x, position.y, position.z);
    }

    public void setOrientation(Vector3f at, Vector3f up) {
        float[] data = new float[6];
        data[0] = at.x;
        data[1] = at.y;
        data[2] = at.z;
        data[3] = up.x;
        data[4] = up.y;
        data[5] = up.z;
        alListenerfv(AL_ORIENTATION, data);
    }
}