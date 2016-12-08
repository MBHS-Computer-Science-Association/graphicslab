package graphicslab;

public interface Loadable {
	public void load();
	
	public boolean isLoaded();
	
	class LoadException extends RuntimeException {
		private static final long serialVersionUID = 4137420494978793572L;
		
		public LoadException(String message) {
			super(message);
		}
	}
}
