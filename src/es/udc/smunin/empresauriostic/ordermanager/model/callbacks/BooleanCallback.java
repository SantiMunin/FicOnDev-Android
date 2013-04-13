package es.udc.smunin.empresauriostic.ordermanager.model.callbacks;

/**
 * Provides a callback with two different behaviors (if the operation succeeded
 * or not).
 * 
 * @author Santiago Mun’n <burning1@gmail.com>
 * 
 */
public interface BooleanCallback {
	/**
	 * Operation to perform if the operation succeeds.
	 */
	public void onSuccess();

	/**
	 * Operation to perform if the operation fails.
	 */
	public void onFailure();
}
