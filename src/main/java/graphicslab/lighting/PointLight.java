package graphicslab.lighting;

import org.joml.Vector3f;

public class PointLight {
	private Vector3f color;
	private Vector3f position;
	private float intensity;
	private Attenuation att;
	
	public PointLight(PointLight parent) {
		this.color = new Vector3f(parent.color);
		this.position = new Vector3f(parent.position);
		this.intensity = parent.intensity;
		this.att = parent.att;
	}
	

	public PointLight(Vector3f color, Vector3f position, float intensity) {
		this(color, position, intensity, null);
	}
	
	public PointLight(Vector3f color, Vector3f position, float intensity, Attenuation att) {
		this.color = color;
		this.position = position;
		this.intensity = intensity;
		this.att = att;
	}
	
	public Vector3f getColor() {
		return color;
	}
	public Vector3f getPosition() {
		return position;
	}
	public float getIntensity() {
		return intensity;
	}
	
	public void setAttenuation(Attenuation att) {
		this.att = att;
	}
	
	public Attenuation getAttenuation() {
		return att;
	}
	
	public class Attenuation {
		private float constant;
		private float linear;
		private float exponent;

		public Attenuation(float constant, float linear, float exponent) {
			this.constant = constant;
			this.linear = linear;
			this.exponent = exponent;
		}
		public float getConstant() {
			return constant;
		}
		public float getLinear() {
			return linear;
		}
		public float getExponent() {
			return exponent;
		}
	}
}
