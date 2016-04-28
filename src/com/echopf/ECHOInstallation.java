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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;


/**
 * {@.en An ECHOInstallation is a device installation object for push notification.}
 * {@.ja プッシュ通知受信端末オブジェクト。}
 */
public class ECHOInstallation {

	protected String deviceToken = null;
	protected String deviceType = "android";
	protected final Object lock = new Object();
	
	private String GCM_SENDER_ID_KEY = "com.echopf.push.gcm_sender_id";
	protected final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	
	/* Begin constructors */

	/**
	 * {@.en Constructs a new ECHOInstallation.}
	 * {@.ja 新しい端末としてオブジェクトを生成します。}
	 */
	public ECHOInstallation() {
		super();
	}

	/**
	 * Constructs a new ECHOInstallation based on an existing one on the remote server.
	 * @param source a source JSONObject to copy
	 */
	public ECHOInstallation(JSONObject source) {
		this();
		copyData(source);
	}
	
	/* End constructors */

	
	/**
	 * {@.en Gets registration ID from the GCM server by synchronous communication.}
	 * {@.ja 同期通信によるregistrationIDの取得。}
	 * 
	 * @throws ECHOException 
	 */
	public ECHOInstallation getRegistrationId() throws ECHOException {
		doGetRegistrationId(true, null);
		return this;
	}

	
	/**
	 * {@.en Gets registration ID from the GCM server in a background thread.
	 * 			A callback is done in the main (UI) thread after the getting.}
	 * {@.ja 非同期通信によるregistrationIDの取得。取得完了後に指定したコールバックをメインスレッドで実行します。}
	 * 
	 * @param callback
	 */
	public void getRegistrationIdInBackground(InstallationCallback callback) {
		try {
			doGetRegistrationId(false, callback);
		} catch (ECHOException e) {
			throw new InternalError();
		}
	}

	
	/**
	 * Converts this object into an acceptable JSONObject for the API.
	 * 
	 * @return the formatted JSONObject
	 */
	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		
		try {
			if(deviceType instanceof String) obj.put("device_type", deviceType);
			if(deviceToken instanceof String) obj.put("device_token", deviceToken);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		
		return obj;
	}

	
	/**
	 * Copies data from a JSONObject.
	 *
	 * @param source the source JSONObject
	 */
	protected void copyData(JSONObject source) {
		String deviceType = source.optString("device_type");
		if(!(deviceType instanceof String)) return; // skip
		
		String deviceToken = source.optString("device_token");
		if(!(deviceToken instanceof String)) return; // skip

		this.deviceType = deviceType;
		this.deviceToken = deviceToken;
	}

	
	/**
	 * Does Get registration id from the GCM server in a background thread.
	 * 
	 * @param sync if set TRUE, then the main (UI) thread is waited for complete the fetching in a background thread. 
	 * 				 (a synchronous communication)
	 * @param callback invoked after the getting is completed
	 * @throws ECHOException 
	 */
	protected void doGetRegistrationId(final boolean sync, final InstallationCallback callback) throws ECHOException {
		if(!checkPlayServices(ECHO.context)) return;
		
		// Get senderId from AndroidManifest.xml
		String senderId = null;
		try {
			ApplicationInfo appInfo = ECHO.context.getPackageManager().getApplicationInfo(ECHO.context.getPackageName(), PackageManager.GET_META_DATA);
			senderId = appInfo.metaData.getString(GCM_SENDER_ID_KEY);
			senderId = (senderId.startsWith("id:")) ? senderId.substring(3):null;
		} catch (NameNotFoundException ignored) {
			// skip
		}
		if(senderId == null) throw new RuntimeException("`" + GCM_SENDER_ID_KEY + "` is not specified in `AndroidManifest.xml`.");
		
		// Get ready a background thread
		final Handler handler = new Handler();
		final String fSenderId = senderId;
		
	    ExecutorService executor = Executors.newSingleThreadExecutor();
	    Callable<Object> communictor = new Callable<Object>() {

	    	@Override
	    	public Object call() throws ECHOException {

    			ECHOException exception = null;

				InstanceID instanceID = InstanceID.getInstance(ECHO.context);
		        String token = null;
		        
				try {
					token = instanceID.getToken(fSenderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE);
    	    		synchronized (lock) {
    	    			deviceToken = token;
    	    		}
				} catch (Exception e) {
					exception = new ECHOException(e);
				}
			
    			if(sync == false) {
    				
					// Execute a callback method in the main (UI) thread.
					final ECHOException fException = exception;
					final String fToken = token;
					
					handler.post(new Runnable() {
						@Override
						public void run() {
							ECHOInstallation.this.deviceToken = fToken;
							callback.done(fException);
						}
					});
    			
    			}else{
    				
    				if(exception != null) throw exception;
    			
    			}
				
	    		return null;
	    	}
	    };
	    
	    Future<Object> future = executor.submit(communictor);

	    if(sync) {
		    try {
		    	future.get();
		    } catch (InterruptedException e) {	
		    	Thread.currentThread().interrupt(); // ignore/reset
		    } catch (ExecutionException e) {
		    	Throwable e2 = e.getCause();
		    	
		    	if (e2 instanceof ECHOException) {
		    		throw (ECHOException) e2;
		    	}
		    	
		    	throw new RuntimeException(e2);
		    }
	    }
	}
	
	
	/**
     * Checks whether Google-Play-Services is available.
     *
     * @param context
     * @return bool
     */
	protected boolean checkPlayServices(Context context) {
    	if(context == null) throw new IllegalArgumentException("The SDK is not initialized. Please call `ECHO.initialize()`.");
    	
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int resultCode = googleAPI.isGooglePlayServicesAvailable(context);
        // int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        
        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(resultCode)) {
            // if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
            	googleAPI.getErrorDialog((Activity) context, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
                // GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) context, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                throw new RuntimeException("This device is not supported.");
            }
            return false;
        }
        return true;
    }
}
