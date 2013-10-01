package ch.unige.tpgcrowd.net.listener;

public interface TPGObjectListener<T> {
	
	public void onSuccess(T results);
	
	public void onFailure();
}
