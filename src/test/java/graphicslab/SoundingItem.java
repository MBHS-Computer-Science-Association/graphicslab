package graphicslab;

import org.joml.Vector3f;

import graphicslab.audio.Source;

public class SoundingItem extends Item {

	private final Source sound;
	private final Vector3f velocity;
	
	public SoundingItem(Mesh mesh) {
		super(mesh);
		velocity = new Vector3f();
		sound = new Source(true, false);
	}
	
	public void update() {
		super.getPosition().add(velocity);
		sound.setPosition(super.getPosition());
	}
	
	public Source getSoundSource() {
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
