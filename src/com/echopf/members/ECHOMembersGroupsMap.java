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

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * {@.en An ECHOMembersGroupsMap is an member's group map.}
 * {@.ja ツリー構造をもったグループマップ。ノードとして{@link com.echopf.members.ECHOMembersGroupObject}を持つ。}
 */
public class ECHOMembersGroupsMap extends ECHOTreeMap<ECHOMembersGroupObject, ECHOMembersGroupsMap>
									implements  Fetchable<ECHOMembersGroupsMap> {

	
	/* Begin constructors */

	/**
	 * {@.en Constructs a new ECHOMembersGroupsMap based on an existing *entire* one on the ECHO server.}
	 * {@.ja ルートから最下層までを対象とした全グループマップオブジェクトを生成します。}
	 * 
	 * @param instanceId
	 * 		{@.en the reference ID of the instance to which the group map has belonged}
	 * 		{@.ja グループが所属するメンバーインスタンスのID}
	 */
	public ECHOMembersGroupsMap(String instanceId) {
		super(instanceId, "groups");
	}

	
	/**
	 * {@.en Constructs a new ECHOMembersGroupsMap based on an existing *sub* one on the ECHO server.}
	 * {@.ja 特定のノードをルートとしたサブグループマップオブジェクトを生成します。}
	 * 
	 * @param instanceId
	 * 		{@.en the reference ID of the instance to which the group map has belonged}
	 * 		{@.ja グループが所属するメンバーインスタンスのID}
	 * @param refid
	 * 		{@.en the reference ID of the root node of the existing sub one}
	 * 		{@.ja サブカテゴリマップのルートとなるグループのID}
	 */
	public ECHOMembersGroupsMap(String instanceId, String refid) {
		super(instanceId, "groups", refid);
	}
	
	
	/**
	 * Constructs a new ECHOMembersGroupsMap linking a ECHOMembersGroupObject
	 *   based on an existing *entire* one on the ECHO server.
	 * @param instanceId the reference ID of the instance to which the group map has belonged
	 * @param data a copying tree map by JSONObject
	 * @throws ECHOException 
	 */
	public ECHOMembersGroupsMap(String instanceId, JSONObject data) throws ECHOException {
		super(instanceId, "groups", data);
	}
	
	
	/**
	 * Constructs a new ECHOMembersGroupsMap linking a ECHOMembersGroupObject
	 *   based on an existing *sub* one on the ECHO server.
	 * @param instanceId the reference ID of the instance to which the group map has belonged
	 * @param refid the reference ID of the root node of the existing sub one
	 * @param data a copying tree map by JSONObject
	 * @throws ECHOException 
	 */
	public ECHOMembersGroupsMap(String instanceId, String refid, JSONObject data) throws ECHOException {
		super(instanceId, "groups", refid, data);
	}

	/* End constructors */

	
	/*
	 * Implement a Fetchable
	 * @see com.echopf.Fetchable#fetch()
	 */
	public ECHOMembersGroupsMap fetch() throws ECHOException {
		doFetch(true, null);
		return this;
	}
	
	
	/*
	 * Implement a Fetchable
	 * @see com.echopf.Fetchable#fetchInBackground()
	 */
	public void fetchInBackground(FetchCallback<ECHOMembersGroupsMap> callback) {
		try {
			doFetch(false, callback);
		} catch (ECHOException e) {
			throw new InternalError();
		}
	}
	
	
	
	/**
	 * Copies data from the specified json object
	 * @throws ECHOException 
	 */
	@Override
	protected void copyData(JSONObject data) throws ECHOException {
		
		JSONArray groups = data.optJSONArray("groups");
		if(groups == null) throw new ECHOException(0, "The copying data is not acceptable. That is why a groups field is not specified.");

			if(is_subtree) {
				JSONObject obj = groups.optJSONObject(0);
				if(obj == null) throw new ECHOException(0, "The copying data is not acceptable.");
				
				JSONArray jsonChildren = obj.optJSONArray("children");
				
				String refid = obj.optString("refid");
				if(refid.isEmpty()) throw new ECHOException(0, "The copying data is not acceptable. That is why a refid is not specified.");
				
				this.node = new ECHOMembersGroupObject(instanceId, refid, obj);
				this.children = children(jsonChildren);
			}else{
				this.children = children(groups);
			}
	}
	

	/**
	 * Builds children recursively
	 * @throws ECHOException 
	 */
	private List<ECHOMembersGroupsMap> children(JSONArray groups) throws ECHOException {
		List<ECHOMembersGroupsMap> children = new ArrayList<ECHOMembersGroupsMap>();
		
		if(groups == null || groups.length() == 0) return children;
		
		for(int i = 0; i < groups.length(); i++) {
			JSONObject obj = groups.optJSONObject(i);
			if(obj == null) throw new ECHOException(0, "The copying data is not acceptable.");
			
			String refid = obj.optString("refid");
			if(refid.isEmpty()) throw new ECHOException(0, "The copying data is not acceptable. That is why a refid is not specified.");

			ECHOMembersGroupsMap map;
			try {
				map = new ECHOMembersGroupsMap(instanceId, refid, new JSONObject("{\"groups\":[" + obj.toString() + "]}"));
			} catch (JSONException e) {
				throw new ECHOException(0, "The copying data is not acceptable.");
			}
			
			children.add(map);
		}
		
		return children;
	}
}
