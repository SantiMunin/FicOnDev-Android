package es.udc.smunin.empresauriostic.ordermanager.service;

import java.util.List;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import es.udc.smunin.empresauriostic.ordermanager.OverviewActivity;
import es.udc.smunin.empresauriostic.ordermanager.R;
import es.udc.smunin.empresauriostic.ordermanager.model.OperationsManager;
import es.udc.smunin.empresauriostic.ordermanager.model.callbacks.ListCallback;
import es.udc.smunin.empresauriostic.ordermanager.model.objectmodels.Order;

public class Alarm extends BroadcastReceiver implements ListCallback<Order> {

	private final static int mId = 0;
	private Context context;
	private final static int TIME = 1000 * 30;

	@Override
	public void onReceive(Context context, Intent arg1) {
		Log.e("AlarmReceiver", "onReceive");
		this.context = context;
		checkNewEvents(context);

	}

	private void checkNewEvents(final Context context) {
		OperationsManager.getInstance().getCompletedOrders(context, this);
		OperationsManager.getInstance().getFinalizedOrders(context,
				new ListCallback<Order>() {

					@Override
					public void onSuccess(List<Order> objects) {
						if (objects.size() > 0) {
							launchPickedNotification(context, null, true);
						} else {
							launchPickedNotification(context, objects.get(0),
									false);
						}

					}

					@Override
					public void onSuccessEmptyList() {
						Log.d("Service", "Empty! (picked)");

					}

					@Override
					public void onFailure() {
						Log.d("service", "FAIL!");
					}
				}, true);
	}

	@Override
	public void onSuccess(List<Order> objects) {
		if (objects.size() > 0) {
			launchNotification(context, null, true);
		} else {
			launchNotification(context, objects.get(0), false);
		}

	}

	@Override
	public void onSuccessEmptyList() {
		Log.d("Service", "Empty!");
	}

	@Override
	public void onFailure() {
		Log.d("service", "FAIL!");
	}

	private void launchPickedNotification(Context context, Order order,
			boolean a_lot) {
		Log.d("Service", "Picked orders!");
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Order picked!").setAutoCancel(true);
		if (a_lot) {
			mBuilder.setContentText("Some orders have been picked by your employees");
		} else {
			mBuilder.setContentText("Your order #" + order.getId()
					+ " has been picked by your employees.");
		}
		Intent resultIntent = new Intent(context, OverviewActivity.class);
		resultIntent.putExtra("ready", false);
		resultIntent.putExtra("picked", true);

		TaskStackBuilder stackBuilder = TaskStackBuilder.from(context);
		stackBuilder.addParentStack(OverviewActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(mId, mBuilder.getNotification());

	}

	private void launchNotification(Context context, Order order, boolean a_lot) {
		Log.d("Service", "New orders!");
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Order ready!")

				.setAutoCancel(true);
		if (a_lot) {
			mBuilder.setContentText("Some orders have been packed and are ready to be delivered.");
		} else {
			mBuilder.setContentText("Your order #" + order.getId()
					+ " has been packed and is ready to be delivered.");
		}
		Intent resultIntent = new Intent(context, OverviewActivity.class);
		resultIntent.putExtra("ready", true);
		resultIntent.putExtra("picked", false);

		TaskStackBuilder stackBuilder = TaskStackBuilder.from(context);
		stackBuilder.addParentStack(OverviewActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(mId, mBuilder.getNotification());
	}

	public void SetAlarm(Context context) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(context, Alarm.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
				TIME, pi); // Millisec * Second * Minute
	}

	public void CancelAlarm(Context context) {
		Intent intent = new Intent(context, Alarm.class);
		PendingIntent sender = PendingIntent
				.getBroadcast(context, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}

}
