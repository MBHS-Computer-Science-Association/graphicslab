package graphicslab.sound;

import static org.lwjgl.openal.AL10.*;

import org.joml.Vector3f;

public class SoundSource {

    private final int sourceId;
    private Vector3f position;
    private Vector3f velocity;

    public SoundSource(boolean loop, boolean relative) {
    	this.position = new Vector3f();
    	this.velocity = new Vector3f();
    	
        this.sourceId = alGenSources();
        if (loop) {
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

    public void setProperty(int param, float value) {
        alSourcef(sourceId, param, value);
    }

    public void play() {
        alSourcePlay(sourceId);
    }

    public boolean isPlaying() {
        return alGetSourcei(sourceId, AL_SOURCE_STATE) == AL_PLAYING;
    }

    public void pause() {
        alSourcePause(sourceId);
    }

    public void stop() {
        alSourceStop(sourceId);
    }

    public void cleanup() {
        stop();
        alDeleteSources(sourceId);
    }
}