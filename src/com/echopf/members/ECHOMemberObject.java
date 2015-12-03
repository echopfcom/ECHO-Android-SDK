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

import java.text.ParseException;

import com.echopf.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * {@.en An ECHOMemberObject is a particular member object.}
 * {@.ja メンバーオブジェクト。}
 */
public class ECHOMemberObject extends ECHODataObject<ECHOMemberObject>
								implements  Fetchable<ECHOMemberObject>, 
											Pushable<ECHOMemberObject>, 
											Deletable<ECHOMemberObject> {

	/* Begin constructors */

	/**
	 * {@.en Constructs a new ECHOMemberObject.}
	 * {@.ja 新しいメンバーとしてオブジェクトを生成します。}
	 * 
	 * @param instanceId
	 * 		{@.en the reference ID of the instance to which this object has belonged}
	 * 		{@.ja 新しいメンバーを所属させるメンバーインスタンスのID}
	 */
	public ECHOMemberObject(String instanceId) {
		super(instanceId, "member");
	}
	
	
	/**
	 * {@.en Constructs a new ECHOMemberObject based on an existing one on the remote server.}
	 * {@.ja 既存のメンバーとしてオブジェクトを生成します。}
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
	 * Constructs a new ECHOMemberObject based on an existing one on the remote server.
	 * @param instanceId the reference ID of the instance to which this object has belonged
	 * @param refid the reference ID of the existing one
	 * @param data a source JSONObject to copy
	 */
	public ECHOMemberObject(String instanceId, String refid, JSONObject data) throws ECHOException {
		this(instanceId, refid);
		copyData(data);
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

	
	@Override
	protected JSONObject buildRequestContents() {
		JSONObject obj = super.buildRequestContents();
		
		try {

			// readonly field
			obj.remove("last_logined");
			
			// groups
			JSONArray sdk_groups = this.optJSONArray("groups");
			if(sdk_groups != null) {
				
				JSONArray api_groups = new JSONArray();
				for (int i = 0; i < sdk_groups.length(); i++) {
					Object sdk_group = sdk_groups.opt(i);
					
					if(sdk_group instanceof ECHOMembersGroupObject) {
						String refid = ((ECHOMembersGroupObject)sdk_group).getRefid();
						if(!refid.isEmpty()) api_groups.put(refid);
					}
				}
				
				obj.put("groups", api_groups);
			}
			
			// installation
			Object sdk_installation = this.opt("installation");
			if(sdk_installation instanceof ECHOInstallation) {
				obj.put("installation", ((ECHOInstallation) sdk_installation).toJSONObject());
			}
			
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		return obj;
	}
	
	
	@Override
	protected void copyData(JSONObject data) throws ECHOException {
		super.copyData(data);

		try {

			// last_logined
			String lastLogined = this.data.optString("last_logined");
			if(lastLogined == null) throw new ECHOException(0, "Invalid data type for data-field `last_logined`.");
			try {
				this.data.put("last_logined", new ECHODate(lastLogined));
			} catch (ParseException e) {
				if(!lastLogined.equals("0000-00-00 00:00:00")) throw new ECHOException(e);
			}
			
			// groups
			JSONArray api_groups = this.data.optJSONArray("groups");
			if (api_groups == null) {
				
				throw new ECHOException(0, "Invalid data type for data-field `groups`.");
				
			} else {
				
				JSONArray sdk_groups = new JSONArray();
				
				for(int i = 0; i < api_groups.length(); i++) {
					JSONObject group = api_groups.optJSONObject(i);
					if(group == null) throw new ECHOException(0, "Invalid data type for data-field `groups`.");
					
					String refid = group.optString("refid");
					if(refid.isEmpty()) continue;
	
					ECHOMembersGroupObject obj = new ECHOMembersGroupObject(instanceId, refid, group);
					sdk_groups.put(obj);
				}
				
				this.data.put("groups", sdk_groups);
			}
			
			// installation
			JSONObject api_installation = this.data.optJSONObject("installation");
			if(api_installation != null) {
				ECHOInstallation sdk_installation = null;
				try {
					sdk_installation = new ECHOInstallation(api_installation);
					this.data.put("installation", sdk_installation);
				} catch (IllegalStateException ignored) {
					this.data.remove("installation");
				}
			}
				
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
}
