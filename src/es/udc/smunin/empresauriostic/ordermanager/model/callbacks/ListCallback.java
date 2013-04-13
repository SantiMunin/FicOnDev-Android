package es.udc.smunin.empresauriostic.ordermanager.model.callbacks;

import java.util.List;

public interface ListCallback<T> {
	
	public void onSuccess(List<T> objects);
	
	public void onSuccessEmptyList();
	
	public void onFailure();

}
