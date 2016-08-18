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

package com.echopf.contents.blogs;

import java.text.ParseException;

import android.os.Parcel;
import android.os.Parcelable;

import com.echopf.Deletable;
import com.echopf.DeleteCallback;
import com.echopf.ECHODate;
import com.echopf.ECHOException;
import com.echopf.FetchCallback;
import com.echopf.Fetchable;
import com.echopf.PushCallback;
import com.echopf.Pushable;
import com.echopf.contents.*;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * {@.en An ECHOEntryObject is a particular entry object.}
 * {@.ja 記事オブジェクト。}
 */
public class ECHOEntryObject extends ECHOContentsObject<ECHOEntryObject>
								implements  Fetchable<ECHOEntryObject>, 
											Pushable<ECHOEntryObject>, 
											Deletable<ECHOEntryObject> {

	/* Begin constructors */
		
	/**
	 * {@.en Constructs a new ECHOEntryObject.}
	 * {@.ja 新しい記事としてオブジェクトを生成します。}
	 * 
	 * @param instanceId
	 * 		{@.en the reference ID of the instance to which this object belongs}
	 * 		{@.ja 新しい記事を所属させるブログインスタンスのID}
	 */
	public ECHOEntryObject(String instanceId) {
		super(instanceId, "entry");
	}
	

	/**
	 * {@.en Constructs a new ECHOEntryObject based on an existing one on the ECHO server.}
	 * {@.ja 既存の記事としてオブジェクトを生成します。}
	 * 
	 * @param instanceId
	 * 		{@.en the reference ID of the instance to which this object has belonged}
	 * 		{@.ja 既存記事が所属するブログインスタンスのID}
	 * @param refid
	 * 		{@.en the reference ID of the existing one}
	 * 		{@.ja 既存記事のID}
	 */
	public ECHOEntryObject(String instanceId, String refid) {
		super(instanceId, "entry", refid);
	}

	
	/**
	 * Constructs a new ECHOEntryObject based on an existing one on the remote server.
	 * @param instanceId the reference ID of the instance to which this object has belonged
	 * @param refid the reference ID of the existing one
	 * @param data : a source JSONObject to copy
	 */
	public ECHOEntryObject(String instanceId, String refid, JSONObject data) {
		super(instanceId, "entry", refid, data);
	}
	
	/* End constructors */
	
	
	/*
	 * Implement a Fetchable
	 * @see com.echopf.Fetchable#fetch()
	 */
	public ECHOEntryObject fetch() throws ECHOException {
		doFetch(true, null);
		return this;
	}
	
	
	/*
	 * Implement a Fetchable
	 * @see com.echopf.Fetchable#fetchInBackground()
	 */
	public void fetchInBackground(FetchCallback<ECHOEntryObject> callback) {
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
	public ECHOEntryObject push() throws ECHOException {
		doPush(true, null);
		return this;
	}
	

	/*
	 * Implement a Pushable
	 * @see com.echopf.Pushable#pushInBackground()
	 */
	public void pushInBackground(PushCallback<ECHOEntryObject> callback) {
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
	public ECHOEntryObject delete() throws ECHOException {
		doDelete(true, null);
		return this;
	}
	
	
	/*
	 * Implement a Deletebale
	 * @see com.echopf.Deletebale#deleteInBackground()
	 */
	public void deleteInBackground(DeleteCallback<ECHOEntryObject> callback) {
		try {
			doDelete(false, callback);
		} catch (ECHOException e) {
			throw new InternalError();
		}
	}


	@Override
	protected JSONObject buildRequestContents() {
		JSONObject obj = super.buildRequestContents();
		
		try {
			
			// published
			Object published = obj.opt("published");
			if(published instanceof ECHODate) {
				obj.put("published", ((ECHODate)published).toStringForECHO());
			}
			
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		return obj;
	}


	@Override
	protected void copyData(JSONObject source) {
		if(source == null) throw new IllegalArgumentException("Argument `source` must not be null.");

		try {
			
			// published
			String published = source.optString("published");
			if(published != null) {
				try {
					source.put("published", new ECHODate(published));
				} catch (ParseException ignored) {
					// skip
				}
			}
			
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		// super
		super.copyData(source);
	}


	/* Begin Parcel methods */
	
	public static final Parcelable.Creator<ECHOEntryObject> CREATOR = new Parcelable.Creator<ECHOEntryObject>() {  
        public ECHOEntryObject createFromParcel(Parcel in) {  
            return new ECHOEntryObject(in);
        }
        
        public ECHOEntryObject[] newArray(int size) {  
            return new ECHOEntryObject[size];  
        }  
    };
	
    public ECHOEntryObject(Parcel in) {
    	super(in);
     }
     
 	/* End Parcel methods */
}
