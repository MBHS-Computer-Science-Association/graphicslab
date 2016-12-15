package graphicslab.audio;

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

/**
 * Represents an OpenAL audio device context. It manages {@link SoundBuffer}, {@link Source}, and the {@link Listener} as objects.
 * It handles initialization and termination of the OpenAL system. All of the sound buffer resources are shared in an OpenAL context.
 */
public class AudioContext {

    private long device;
    private long context;

    private Listener listener;

    private final List<SoundBuffer> soundBufferList;

    private final Map<String, Source> soundSourceMap;

    public AudioContext() {
        soundBufferList = new ArrayList<>();
        soundSourceMap = new HashMap<>();
        listener = new Listener();
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

    public void addSoundSource(String name, Source soundSource) {
        this.soundSourceMap.put(name, soundSource);
    }

    public void addSoundBuffer(SoundBuffer soundBuffer) {
        this.soundBufferList.add(soundBuffer);
    }

    public Listener getListener() {
        return this.listener;
    }

    public void setListener(Listener listener) {
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
        
        for (Source soundSource : soundSourceMap.values()) {
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