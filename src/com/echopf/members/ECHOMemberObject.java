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

import com.echopf.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * {@.en An ECHOMemberObject is a particular member object.}
 * {@.ja メンバーオブジェクト。}
 */
public class ECHOMemberObject extends ECHODataObject
								implements  Fetchable<ECHOMemberObject>, 
											Pushable<ECHOMemberObject>, 
											Deletable<ECHOMemberObject> {

	/* Begin constructors */

	/**
	 * {@.en Constructs a new ECHOMemberObject.}
	 * {@.ja 新しいメンバーをオブジェクトとして生成します。}
	 * 
	 * @param instanceId
	 * 		{@.en the reference ID of the instance to which this object has belonged}
	 * 		{@.ja 新しいメンバーを所属させるメンバーインスタンスのID}
	 */
	public ECHOMemberObject(String instanceId) {
		super(instanceId, "member");
	}
	
	
	/**
	 * {@.en Constructs a new ECHOMemberObject based on an existing one on the ECHO server.}
	 * {@.ja 既存のメンバーをオブジェクトとして生成します。}
	 * 
	 * @param instanceId
	 * 		{@.en the reference ID of the instance to which this object has belonged}
	 * 		{@.ja 既存メンバーが所属するメンバーインスタンスのID}
	 * @param refid
	 * 		{@.en the reference ID of the existing one}
	 * 		{@.ja 既存メンバーのID}
	 */
	public ECHOMemberObject(String instanceId, String refid) {
		super(instanceId, "member", refid);
	}

	
	/**
	 * Constructs a new ECHOMemberObject based on an existing one on the ECHO server.
	 * @param instanceId the reference ID of the instance to which this object has belonged
	 * @param refid the reference ID of the existing one
	 * @param data a copying member object by JSONObject
	 */
	public ECHOMemberObject(String instanceId, String refid, JSONObject data) throws ECHOException {
		super(instanceId, "member", refid, data);
	}

	/* End constructors */

	
	/*
	 * Implement a Fetchable
	 * @see com.echopf.Fetchable#fetch()
	 */
	public ECHOMemberObject fetch() throws ECHOException {
		doFetch(true, null);
		return this;
	}

	
	/*
	 * Implement a Fetchable
	 * @see com.echopf.Fetchable#fetchInBackground()
	 */
	public void fetchInBackground(FetchCallback<ECHOMemberObject> callback) {
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
	public ECHOMemberObject push() throws ECHOException {
		doPush(true, null);
		return this;
	}
	

	/*
	 * Implement a Pushable
	 * @see com.echopf.Pushable#pushInBackground()
	 */
	public void pushInBackground(PushCallback<ECHOMemberObject> callback) {
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
	public ECHOMemberObject delete() throws ECHOException {
		doDelete(true, null);
		return this;
	}
	
	
	/*
	 * Implement a Deletebale
	 * @see com.echopf.Deletebale#deleteInBackground()
	 */
	public void deleteInBackground(DeleteCallback<ECHOMemberObject> callback) {
		try {
			doDelete(false, callback);
		} catch (ECHOException e) {
			throw new InternalError();
		}
	}
	
	
	/**
	 * Does Push data to the ECHO server in a background thread.
	 * @param sync : if set TRUE, then the main (UI) thread is waited for complete the pushing in a background thread. 
	 * 				 (a synchronous communication)
	 * @param callback invoked after the pushing is completed
	 * @throws ECHOException
	 */
	protected void doPush(boolean sync, PushCallback<ECHOMemberObject> callback) throws ECHOException {
		try {
			
			JSONObject apiObj  = new JSONObject(this.toString());
			
			// groups
			JSONArray sdk_groups = this.optJSONArray("groups");
			if(sdk_groups != null) {
				
				JSONArray api_groups = new JSONArray();
				for (int i = 0; i < sdk_groups.length(); i++) {
					JSONObject sdk_group = sdk_groups.optJSONObject(i);
					if(sdk_group == null) throw new IllegalStateException("Invalid data format in a group.");
					
					String refid = sdk_group.optString("refid");
					if(refid.isEmpty()) throw new IllegalStateException("Invalid data format in a group.");
						
					api_groups.put(refid);
				}
				
				apiObj.put("groups", api_groups);
			}
			
			super.doPush(apiObj, sync, callback);

		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	@Override
	protected void copyData(JSONObject data) throws ECHOException {

		try {
			
			// groups
			JSONArray api_groups = data.optJSONArray("groups");
			if(api_groups == null) throw new ECHOException(0, "The copying data is not acceptable. That is why a groups field is not specified.");
			
			JSONArray sdk_groups = new JSONArray();
	
			for(int i = 0; i < api_groups.length(); i++) {
				JSONObject group = api_groups.optJSONObject(i);
				if(group == null) throw new ECHOException(0, "The copying data is not acceptable.");
				
				String refid = group.optString("refid");
				if(refid.isEmpty()) throw new ECHOException(0, "The copying data is not acceptable. That is why a refid is not specified.");

				ECHOMembersGroupObject obj = new ECHOMembersGroupObject(instanceId, refid, group);
				sdk_groups.put(obj);
			}
			data.put("groups", sdk_groups);
			
			
			super.copyData(data);
				
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
}
