package graphicslab.audio;

import static org.lwjgl.openal.AL10.*;

import org.joml.Vector3f;

import graphicslab.Initializable;

public class Source implements Initializable {

    private int sourceId;
    private Vector3f position;
    private Vector3f velocity;
    
    private boolean looping;
    private boolean relative;

    public Source(boolean looping, boolean relative) {
    	this.position = new Vector3f();
    	this.velocity = new Vector3f();
    	
    	this.looping = looping;
    	this.relative = relative;
    }
    
    @Override
    public void init() {
        this.sourceId = alGenSources();
        if (looping) {
            alSourcei(sourceId, AL_LOOPING, AL_TRUE);
        }
        if (relative) {
            alSourcei(sourceId, AL_SOURCE_RELATIVE, AL_TRUE);
        }
    }

    public void setBuffer(int bufferId) {
        stop();
        alSourcei(sourceId, AL_BUFFER, bufferId);
    }

    public void setPosition(Vector3f position) {
    	this.position = position;
        alSource3f(sourceId, AL_POSITION, position.x, position.y, position.z);
    }
    
    public Vector3f getPosition() {
    	return position;
    }

    public void setVelocity(Vector3f velocity) {
    	this.velocity = velocity;
        alSource3f(sourceId, AL_VELOCITY, velocity.x, velocity.y, velocity.z);
    }
    
    public Vector3f getVelocity() {
    	return velocity;
    }

    public void setGain(float gain) {
        alSourcef(sourceId, AL_GAIN, gain);
    }


    public boolean isPlaying() {
        return alGetSourcei(sourceId, AL_SOURCE_STATE) == AL_PLAYING;
    }

    public void play() {
    	alSourcePlay(sourceId);
    }

    public void pause() {
        alSourcePause(sourceId);
    }

    public void stop() {
        alSourceStop(sourceId);
    }

    public int getId() {
    	return sourceId;
    }
    
    public void cleanup() {
        stop();
        alDeleteSources(sourceId);
    }
}