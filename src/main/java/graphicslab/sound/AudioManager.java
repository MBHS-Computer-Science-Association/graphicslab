package graphicslab.sound;

import java.nio.IntBuffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;

import graphicslab.Camera;
import graphicslab.Transformation;

import static org.lwjgl.system.MemoryUtil.NULL;

import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.AL10.*;


public class AudioManager {

    private long device;
    private long context;

    private SoundListener listener;

    private final List<SoundBuffer> soundBufferList;

    private final Map<String, SoundSource> soundSourceMap;

    public AudioManager() {
        soundBufferList = new ArrayList<>();
        soundSourceMap = new HashMap<>();
        listener = new SoundListener();
    }

    public void init() throws Exception {
        this.device = alcOpenDevice((ByteBuffer) null);
        if (device == NULL) {
            throw new IllegalStateException("Failed to open the default OpenAL device.");
        }
        this.context = alcCreateContext(device, (IntBuffer) null);
        if (context == NULL) {
            throw new IllegalStateException("Failed to create OpenAL context.");
        }
        alcMakeContextCurrent(context);

        ALCCapabilities deviceCaps = ALC.createCapabilities(device);
        AL.createCapabilities(deviceCaps);
    }

    public void addSoundSource(String name, SoundSource soundSource) {
        this.soundSourceMap.put(name, soundSource);
    }

    public SoundSource getSoundSource(String name) {
        return this.soundSourceMap.get(name);
    }

    public void playSoundSource(String name) {
        SoundSource soundSource = this.soundSourceMap.get(name);
        if (soundSource != null && !soundSource.isPlaying()) {
            soundSource.play();
        }
    }

    public void removeSoundSource(String name) {
        this.soundSourceMap.remove(name);
    }

    public void addSoundBuffer(SoundBuffer soundBuffer) {
        this.soundBufferList.add(soundBuffer);
    }

    public SoundListener getListener() {
        return this.listener;
    }

    public void setListener(SoundListener listener) {
        this.listener = listener;
    }

    public void updateListenerPosition(Camera camera) {
        Matrix4f cameraMatrix = new Transformation().getViewMatrix(camera);
        listener.setPosition(camera.getPosition());
        Vector3f at = new Vector3f();
        cameraMatrix.positiveZ(at).negate();
        Vector3f up = new Vector3f();
        cameraMatrix.positiveY(up);
        listener.setOrientation(at, up);
    }

    public void cleanup() {
        for (SoundBuffer soundBuffer : soundBufferList) {
            soundBuffer.cleanup();
        }
        soundBufferList.clear();
        
        for (SoundSource soundSource : soundSourceMap.values()) {
            soundSource.cleanup();
        }
        soundSourceMap.clear();
        
        if (context != NULL) {
            alcDestroyContext(context);
        }
        if (device != NULL) {
            alcCloseDevice(device);
        }
    }
}