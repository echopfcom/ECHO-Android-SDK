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

package com.echopf.members;

import android.os.Parcel;
import android.os.Parcelable;

import com.echopf.*;

import org.json.JSONObject;


/**
 * {@.en An ECHOPushNotificationObject is a push notification object.}
 * {@.ja プッシュ通知オブジェクト。}
 */
public class ECHOPushNotificationObject extends ECHODistributedObject<ECHOPushNotificationObject>
								implements  Fetchable<ECHOPushNotificationObject>, 
											Pushable<ECHOPushNotificationObject>, 
											Deletable<ECHOPushNotificationObject> {

	/* Begin constructors */

	/**
	 * {@.en Constructs a new ECHOPushNotificationObject.}
	 * {@.ja 新しいプッシュ通知としてオブジェクトを生成します。}
	 * 
	 * @param instanceId
	 * 		{@.en the reference ID of a member instance}
	 * 		{@.ja メンバーインスタンスID}
	 */
	public ECHOPushNotificationObject(String instanceId) {
		super(instanceId, "push_notification");
	}
	
	
	/**
	 * {@.en Constructs a new ECHOPushNotificationObject based on an existing one on the remote server.}
	 * {@.ja 既存のプッシュ通知としてオブジェクトを生成します。}
	 * 
	 * @param instanceId
	 * 		{@.en the reference ID of a member instance}
	 * 		{@.ja　メンバーインスタンスID}
	 * @param refid
	 * 		{@.en the reference ID of the existing one}
	 * 		{@.ja 既存プッシュ通知のID}
	 */
	public ECHOPushNotificationObject(String instanceId, String refid) {
		super(instanceId, "push_notification", refid);
	}

	
	/**
	 * Constructs a new ECHOPushNotificationObject based on an existing one on the remote server.
	 * @param instanceId the reference ID of a member instance
	 * @param refid the reference ID of the existing one
	 * @param source a source JSONObject to copy
	 */
	public ECHOPushNotificationObject(String instanceId, String refid, JSONObject source) {
		this(instanceId, refid);
		copyData(source);
	}

	/* End constructors */

	
	/*
	 * Implement a Fetchable
	 * @see com.echopf.Fetchable#fetch()
	 */
	public ECHOPushNotificationObject fetch() throws ECHOException {
		doFetch(true, null);
		return this;
	}

	
	/*
	 * Implement a Fetchable
	 * @see com.echopf.Fetchable#fetchInBackground()
	 */
	public void fetchInBackground(FetchCallback<ECHOPushNotificationObject> callback) {
		try {
			doFetch(false, callback);
		} catch (ECHOException e) {
			throw new InternalError();
		}
	}
	

	/*
	 * Implement a Pushable
	 * @see com.echopf.Pushable#push()
	 */
	public ECHOPushNotificationObject push() throws ECHOException {
		doPush(true, null);
		return this;
	}
	

	/*
	 * Implement a Pushable
	 * @see com.echopf.Pushable#pushInBackground()
	 */
	public void pushInBackground(PushCallback<ECHOPushNotificationObject> callback) {
		try {
			doPush(false, callback);
		} catch (ECHOException e) {
			throw new InternalError();
		}
	}
	
	
	/*
	 * Implement a Deletebale
	 * @see com.echopf.Deletebale#delete()
	 */
	public ECHOPushNotificationObject delete() throws ECHOException {
		doDelete(true, null);
		return this;
	}
	
	
	/*
	 * Implement a Deletebale
	 * @see com.echopf.Deletebale#deleteInBackground()
	 */
	public void deleteInBackground(DeleteCallback<ECHOPushNotificationObject> callback) {
		try {
			doDelete(false, callback);
		} catch (ECHOException e) {
			throw new InternalError();
		}
	}


	/* Begin Parcel methods */
	
	public static final Parcelable.Creator<ECHOPushNotificationObject> CREATOR = new Parcelable.Creator<ECHOPushNotificationObject>() {  
        public ECHOPushNotificationObject createFromParcel(Parcel in) {  
            return new ECHOPushNotificationObject(in);
        }
        
        public ECHOPushNotificationObject[] newArray(int size) {  
            return new ECHOPushNotificationObject[size];  
        }  
    };
	
    public ECHOPushNotificationObject(Parcel in) {
    	super(in);
     }
     
 	/* End Parcel methods */
}
