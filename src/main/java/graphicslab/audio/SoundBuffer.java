package graphicslab.audio;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;
import static org.lwjgl.stb.STBVorbis.*;
import org.lwjgl.stb.STBVorbisInfo;

import graphicslab.Loadable;
import graphicslab.util.Utils;

public class SoundBuffer implements Loadable {
	private int bufferId;
	
	private ShortBuffer buffer;
	private int format;
	private int frequency;
	
	private boolean isLoaded;
	
	public SoundBuffer(String file) throws IOException {
		isLoaded = false;
		
		try (STBVorbisInfo info = STBVorbisInfo.malloc()){
			buffer = readVorbis(file, 32 * 1024, info);
			format = info.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16;
			frequency = info.sample_rate();
		}
	}
	
	private ShortBuffer readVorbis(String resource, int bufferSize, STBVorbisInfo info) throws IOException {
		ByteBuffer vorbis;
		vorbis = Utils.ioResourceToByteBuffer(resource, bufferSize);

		IntBuffer error = BufferUtils.createIntBuffer(1);
		long decoder = stb_vorbis_open_memory(vorbis, error, null);
		if (decoder == NULL) {
			throw new RuntimeException("Failed to open Ogg Vorbis file. Error: " + error.get(0));
		}

		stb_vorbis_get_info(decoder, info);

		int channels = info.channels();

		int lengthSamples = stb_vorbis_stream_length_in_samples(decoder);

		ShortBuffer pcm = BufferUtils.createShortBuffer(lengthSamples);

		pcm.limit(stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm) * channels);
		stb_vorbis_close(decoder);

		return pcm;
	}

	@Override
	public void load() {
		this.bufferId = alGenBuffers();
		alBufferData(bufferId, format, buffer, frequency);
		buffer = null;
		isLoaded = true;
	}
	
	@Override
	public boolean isLoaded() {
		return isLoaded;
	}
	
    public int getId() {
    	if (!isLoaded()) {
    		load();
    	}
    	
        return bufferId;
    }

    public void cleanup() {
        alDeleteBuffers(bufferId);
    }
}
