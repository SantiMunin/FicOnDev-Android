package es.udc.smunin.empresauriostic.ordermanager.model;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import es.udc.smunin.empresauriostic.ordermanager.model.callbacks.BooleanCallback;
import es.udc.smunin.empresauriostic.ordermanager.model.callbacks.ListCallback;
import es.udc.smunin.empresauriostic.ordermanager.model.objectmodels.Order;
import es.udc.smunin.empresauriostic.ordermanager.model.objectmodels.Product;
import es.udc.smunin.empresauriostic.ordermanager.model.util.ParsingUtils;
import es.udc.smunin.empresauriostic.ordermanager.model.util.PreferencesUtil;

/**
 * Provides a simple way to perform network operations. A callback (
 * {@link net.notanumber.turiskana.model.callbacks.BooleanCallback} for example)
 * must be provided in order to decide what to do with the response.
 * 
 * @author Santiago Mun�n <burning1@gmail.com>
 * 
 */
public class OperationsManager {
	private final static String URL = "http://iaguis.no-ip.org";
	private static OperationsManager instance = new OperationsManager();
	private static AsyncHttpClient client;
	private final static String TAG = "OperationsManager";
	private final static int TIMEOUT = 7000;

	private OperationsManager() {
		client = new AsyncHttpClient();
		client.setTimeout(TIMEOUT);
	}

	public static OperationsManager getInstance() {
		return instance;
	}

	public void doLogin(final Context context, final String email,
			final String password, final BooleanCallback callback) {
		PreferencesUtil.setSessionId(context, "");
		client.post(buildUrl("login", email, password),
				new AsyncHttpResponseHandler() {
					public void onSuccess(String response) {
						String sessionId = response;

						if (sessionId.length() > 0) {
							Log.i(TAG, "Login done! (" + sessionId + ")");
							PreferencesUtil.setMail(context, email);
							PreferencesUtil.setPassword(context, password);
							PreferencesUtil.setSessionId(context, sessionId);
							callback.onSuccess();

						} else {
							Log.i(TAG, "Failed login :/");
							PreferencesUtil.setMail(context, "");
							PreferencesUtil.setSessionId(context, "");
							callback.onFailure();

						}
					}

					@Override
					public void onFailure(Throwable arg0, String arg1) {
						super.onFailure(arg0, arg1);
						Log.d(TAG, arg0.getMessage() + arg1);
						callback.onFailure();

					}
				});
	}

	public void getProducts(final Context context,
			final ListCallback<Product> callback) {
		String sessionId = PreferencesUtil.getSessionId(context);
		client.get(buildUrl("listproducts", sessionId),
				new AsyncHttpResponseHandler() {
					public void onSuccess(String response) {
						// TODO
						List<Product> products = ParsingUtils
								.parseProducts(response);

						if (products != null) {
							if (products.size() > 0) {
								callback.onSuccess(products);
							} else {
								callback.onSuccessEmptyList();
							}
						} else {
							callback.onFailure();
						}
					}

					@Override
					public void onFailure(Throwable arg0, String arg1) {
						super.onFailure(arg0, arg1);
						Log.d(TAG, arg0.getMessage() + arg1);
						callback.onFailure();

					}
				});
	}

	public void getPendingOrders(final Context context,
			final ListCallback<Order> callback) {
		String sessionId = PreferencesUtil.getSessionId(context);
		client.get(buildUrl("pendingorders", sessionId),
				new AsyncHttpResponseHandler() {
					public void onSuccess(String response) {
						// TODO
						List<Order> orders = ParsingUtils.parseOrders(context,
								response, false);

						if (orders != null) {
							if (orders.size() > 0) {
								callback.onSuccess(orders);
							} else {
								callback.onSuccessEmptyList();
							}
						} else {
							callback.onFailure();
						}
					}

					@Override
					public void onFailure(Throwable arg0, String arg1) {
						super.onFailure(arg0, arg1);
						Log.d(TAG, arg0.getMessage() + arg1);
						callback.onFailure();

					}
				});

	}

	public void newOrder(final Context context, final BooleanCallback callback,
			String orderData) {
		String sessionId = PreferencesUtil.getSessionId(context);
		RequestParams params = new RequestParams();
		params.put("order", orderData);
		Log.d(TAG, "data: " + orderData);
		client.post(buildUrl("neworder", sessionId), params,
				new AsyncHttpResponseHandler() {
					public void onSuccess(String response) {
						// TODO
						boolean ok = ParsingUtils
								.parseNewOrderSubmitted(response);

						if (ok) {
							callback.onSuccess();
						} else {
							callback.onFailure();
						}
					}

					@Override
					public void onFailure(Throwable arg0, String arg1) {
						super.onFailure(arg0, arg1);
						Log.d(TAG, arg0.getMessage() + arg1);
						callback.onFailure();

					}
				});
	}

	public void getOrderStatus(final Context context, int id,
			final ListCallback<Order> callback) {
		String sessionId = PreferencesUtil.getSessionId(context);
		client.get(buildUrl("orderstatus", sessionId, String.valueOf(id)),
				new AsyncHttpResponseHandler() {

					public void onSuccess(String response) {
						// TODO
						List<Order> deliveredOrders = ParsingUtils
								.parseNewOrders(response);

						if (deliveredOrders != null) {
							if (deliveredOrders.size() > 0) {
								callback.onSuccess(deliveredOrders);
							} else {
								callback.onSuccessEmptyList();
							}
						} else {
							callback.onFailure();
						}
					}

					@Override
					public void onFailure(Throwable arg0, String arg1) {
						super.onFailure(arg0, arg1);
						callback.onFailure();
						Log.d(TAG, arg0.getMessage() + arg1);
					}
				});

	}

	public void getCompletedOrders(final Context context,
			final ListCallback<Order> callback) {
		String sessionId = PreferencesUtil.getSessionId(context);
		client.get(buildUrl("readyorders", sessionId, "1"),
				new AsyncHttpResponseHandler() {
					public void onSuccess(String response) {
						List<Order> orders = ParsingUtils.parseOrders(context,
								response, true);

						if (orders != null) {
							if (orders.size() > 0) {
								callback.onSuccess(orders);
							} else {
								callback.onSuccessEmptyList();
							}
						} else {
							callback.onFailure();
						}
					}

					@Override
					public void onFailure(Throwable arg0, String arg1) {
						super.onFailure(arg0, arg1);
						Log.d(TAG, arg0.getMessage() + arg1);
						callback.onFailure();

					}
				});

	}

	public void getAllCompletedOrders(final Context context,
			final ListCallback<Order> callback) {
		String sessionId = PreferencesUtil.getSessionId(context);
		client.get(buildUrl("readyorders", sessionId, "0"),
				new AsyncHttpResponseHandler() {
					public void onSuccess(String response) {
						// TODO
						List<Order> orders = ParsingUtils.parseOrders(context,
								response, false);

						if (orders != null) {
							if (orders.size() > 0) {
								callback.onSuccess(orders);
							} else {
								callback.onSuccessEmptyList();
							}
						} else {
							callback.onFailure();
						}
					}

					@Override
					public void onFailure(Throwable arg0, String arg1) {
						super.onFailure(arg0, arg1);
						Log.d(TAG, arg0.getMessage() + arg1);
						callback.onFailure();

					}
				});

	}

	public void getFinalizedOrders(final Context context,
			final ListCallback<Order> callback, boolean new_orders) {
		String sessionId = PreferencesUtil.getSessionId(context);
		String number = "0";
		if (new_orders) {
			number = "1";
		}
		client.get(buildUrl("pickedorders", sessionId, number),
				new AsyncHttpResponseHandler() {
					public void onSuccess(String response) {
						Log.d(TAG, "response: " + response);
						List<Order> orders = ParsingUtils.parseOrders(context,
								response, false);

						if (orders != null) {
							if (orders.size() > 0) {
								callback.onSuccess(orders);
							} else {
								callback.onSuccessEmptyList();
							}
						} else {
							callback.onFailure();
						}
					}

					@Override
					public void onFailure(Throwable arg0, String arg1) {
						Log.d(TAG, arg0.getMessage() + arg1);
						callback.onFailure();
					}
				});
	}

	private String buildUrl(String... params) {
		String result = URL;
		for (int i = 0; i < params.length; i++) {
			result += "/" + params[i];
		}
		Log.d(TAG, "url: " + result);
		return result;
	}
}
