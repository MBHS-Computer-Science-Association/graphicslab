package graphicslab;

import org.joml.Vector3f;

import graphicslab.sound.SoundSource;

public class SoundingItem extends Item {

	private final SoundSource sound;
	private final Vector3f velocity;
	
	public SoundingItem(Mesh mesh) {
		super(mesh);
		velocity = new Vector3f();
		sound = new SoundSource(true, false);
	}
	
	public void update() {
		super.getPosition().add(velocity);
		sound.setPosition(super.getPosition());
	}
	
	public SoundSource getSoundSource() {
		return sound;
	}
	
	public void setVelocity(Vector3f velocity) {
		this.velocity.set(velocity);
		sound.setVelocity(velocity);
	}
	
	public Vector3f getVelocity() {
		return velocity;
	}
	
	
}
