package ch.unige.tpgcrowd.net.listener;

import ch.unige.tpgcrowd.model.ITPGModelEntity;

public interface TPGObjectListener<T extends ITPGModelEntity> {
	
	public void onSuccess(final T results);
	
	public void onFailure();
}
