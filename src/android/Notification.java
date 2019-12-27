package com.hutchind.cordova.plugins.launcher;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Build;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import android.support.v4.app.NotificationManagerCompat;

import android.app.Notification;
import android.view.View;

public class Notification extends CordovaPlugin {
	public static final String TAG = "Launcher Plugin";
	public static final String ACTION_CAN_LAUNCH = "canLaunch";
	public static final String ACTION_LAUNCH = "launch";
	public static final int LAUNCH_REQUEST = 0;

	private NotificationManagerCompat notificationManager;

	public static final String CHANNEL_1_ID = "channel1";
	public static final String CHANNEL_2_ID = "channel2";

	private CallbackContext callback;

	private abstract class LauncherRunnable implements Runnable {
		public CallbackContext callbackContext;

		LauncherRunnable(CallbackContext cb) {
			this.callbackContext = cb;
		}
	}

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		callback = callbackContext;
		if (ACTION_LAUNCH.equals(action)) {
			return launch(args);
		}
		return false;
	}

	public void sendOnChannel1() {
		notificationManager = NotificationManagerCompat.from(this.cordova.getActivity().getApplicationContext());

		Resources activityRes = cordova.getActivity().getResources();
		int backResId = activityRes.getIdentifier("ic_action_previous_item", "drawable",
				cordova.getActivity().getPackageName());
		Drawable backIcon = activityRes.getDrawable(backResId);

		Notification notification = new NotificationCompat.Builder(this.cordova.getActivity().getApplicationContext(),
				CHANNEL_1_ID).setSmallIcon(backResId).setContentTitle("Titulo").setContentText("Mensagem")
						.setPriority(NotificationCompat.PRIORITY_HIGH).setCategory(NotificationCompat.CATEGORY_MESSAGE)
						.build();

		notificationManager.notify(1, notification);
	}

	private void createNotificationChannels() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID, "Channel 1",
					NotificationManager.IMPORTANCE_HIGH);
			channel1.setDescription("This is Channel 1");

			NotificationChannel channel2 = new NotificationChannel(CHANNEL_2_ID, "Channel 2",
					NotificationManager.IMPORTANCE_LOW);
			channel2.setDescription("This is Channel 2");

			NotificationManager manager = this.cordova.getActivity().getApplicationContext()
					.getSystemService(NotificationManager.class);
			manager.createNotificationChannel(channel1);
			manager.createNotificationChannel(channel2);
		}
	}

	private boolean launch(JSONArray args) throws JSONException {
		final JSONObject options = args.getJSONObject(0);
		System.out.println(options.toString());
		System.out.println("Criando notificacao local");
		createNotificationChannels();
		sendOnChannel1();
		System.out.println("Disparando notificacao");
		return true;
	}
}