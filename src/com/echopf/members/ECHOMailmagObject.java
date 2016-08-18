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
 * {@.en An ECHOMailmagObject is a e-mail magazine object.}
 * {@.ja メールマガジンオブジェクト。}
 */
public class ECHOMailmagObject extends ECHODistributedObject<ECHOMailmagObject>
								implements  Fetchable<ECHOMailmagObject>, 
											Pushable<ECHOMailmagObject>, 
											Deletable<ECHOMailmagObject> {

	/* Begin constructors */

	/**
	 * {@.en Constructs a new ECHOMailmagObject.}
	 * {@.ja 新しいメールマガジンとしてオブジェクトを生成します。}
	 * 
	 * @param instanceId
	 * 		{@.en the reference ID of a member instance}
	 * 		{@.ja メンバーインスタンスID}
	 */
	public ECHOMailmagObject(String instanceId) {
		super(instanceId, "mailmag");
	}
	
	
	/**
	 * {@.en Constructs a new ECHOMailmagObject based on an existing one on the remote server.}
	 * {@.ja 既存のメールマガジンとしてオブジェクトを生成します。}
	 * 
	 * @param instanceId
	 * 		{@.en the reference ID of a member instance}
	 * 		{@.ja　メンバーインスタンスID}
	 * @param refid
	 * 		{@.en the reference ID of the existing one}
	 * 		{@.ja 既存メールマガジンのID}
	 */
	public ECHOMailmagObject(String instanceId, String refid) {
		super(instanceId, "mailmag", refid);
	}

	
	/**
	 * Constructs a new ECHOMailmagObject based on an existing one on the remote server.
	 * @param instanceId the reference ID of a member instance
	 * @param refid the reference ID of the existing one
	 * @param source a source JSONObject to copy
	 */
	public ECHOMailmagObject(String instanceId, String refid, JSONObject source) {
		this(instanceId, refid);
		copyData(source);
	}

	/* End constructors */

	
	/*
	 * Implement a Fetchable
	 * @see com.echopf.Fetchable#fetch()
	 */
	public ECHOMailmagObject fetch() throws ECHOException {
		doFetch(true, null);
		return this;
	}

	
	/*
	 * Implement a Fetchable
	 * @see com.echopf.Fetchable#fetchInBackground()
	 */
	public void fetchInBackground(FetchCallback<ECHOMailmagObject> callback) {
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
	public ECHOMailmagObject push() throws ECHOException {
		doPush(true, null);
		return this;
	}
	

	/*
	 * Implement a Pushable
	 * @see com.echopf.Pushable#pushInBackground()
	 */
	public void pushInBackground(PushCallback<ECHOMailmagObject> callback) {
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
	public ECHOMailmagObject delete() throws ECHOException {
		doDelete(true, null);
		return this;
	}
	
	
	/*
	 * Implement a Deletebale
	 * @see com.echopf.Deletebale#deleteInBackground()
	 */
	public void deleteInBackground(DeleteCallback<ECHOMailmagObject> callback) {
		try {
			doDelete(false, callback);
		} catch (ECHOException e) {
			throw new InternalError();
		}
	}


	/* Begin Parcel methods */
	
	public static final Parcelable.Creator<ECHOMailmagObject> CREATOR = new Parcelable.Creator<ECHOMailmagObject>() {  
        public ECHOMailmagObject createFromParcel(Parcel in) {  
            return new ECHOMailmagObject(in);
        }
        
        public ECHOMailmagObject[] newArray(int size) {  
            return new ECHOMailmagObject[size];  
        }  
    };
	
    public ECHOMailmagObject(Parcel in) {
    	super(in);
     }
     
 	/* End Parcel methods */
}
