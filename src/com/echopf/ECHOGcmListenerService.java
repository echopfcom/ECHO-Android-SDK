/*******
 Copyright 2015 NeuroBASE,Inc. All Rights Reserved.
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 **********/

package com.echopf;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;


/**
 *  A ECHOGcmListenerService is GCM push notification receiver class. 
 */
public class ECHOGcmListenerService extends GcmListenerService {
	
	private String NOTIFY_ONLY_LATEST_KEY = "com.echopf.push.notify_only_latest";
	private String NOTIFICATION_ICON_KEY = "com.echopf.push.notification_icon";
	private String PUSH_OPEN_ACTIVITY_KEY = "com.echopf.push.open_activity";
	
	@Override
	public void onMessageReceived(String from, Bundle data) {
		if (!data.containsKey("message") && !data.containsKey("title")) return;

    	// get ApplicationInfo
        ApplicationInfo appInfo = null;
        try {
			appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			throw new RuntimeException(e);
		}
        
        // make notification id
        int id = (appInfo.metaData.getBoolean(NOTIFY_ONLY_LATEST_KEY, false)) ? 0:(int)System.currentTimeMillis();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder builder = getNotificationBuilder(data);
        notificationManager.notify(id, builder.build());
	}
	
	
	/**
	 * Sets notification layouts.
	 * @param data 
	 * @return NotificationCompat.Builder
	 */
    public NotificationCompat.Builder getNotificationBuilder(Bundle data) {
    	NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this).setAutoCancel(true);
    	
    	// get ApplicationInfo
        ApplicationInfo appInfo = null; String appName = null;
        try {
			appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
	        appName = getPackageManager().getApplicationLabel(getPackageManager().getApplicationInfo(getPackageName(), 0)).toString();
		} catch (NameNotFoundException e) {
			throw new RuntimeException(e);
		}
        
        // set title
        String title = (data.getString("title") != null) ? data.getString("title"):appName;
        notificationBuilder.setContentTitle(title);
        
        // set message
        if (data.getString("message") != null) 
        	notificationBuilder.setContentText(data.getString("message"));

        // set icon
        int manifestIco = appInfo.metaData.getInt(NOTIFICATION_ICON_KEY);
        int icon = (manifestIco != 0) ? manifestIco:appInfo.icon;
        notificationBuilder.setSmallIcon(icon);

        // set sound
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder.setSound(defaultSoundUri);
        
        // set content intent
        String activity = appInfo.metaData.getString(PUSH_OPEN_ACTIVITY_KEY);
        if(activity != null) {
        	
			try {
				Intent intent = new Intent(this, Class.forName(activity))
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
						.setComponent(new ComponentName(appInfo.packageName, activity))
						.putExtras(data);
	            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	            notificationBuilder.setContentIntent(pendingIntent);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
            
        }
        
        return notificationBuilder;
    }
}
