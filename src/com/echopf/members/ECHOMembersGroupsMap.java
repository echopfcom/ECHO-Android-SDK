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
 * {@.en An ECHOMembersGroupsMap expresses a hierarchical group map.}
 * {@.ja ツリー構造をもったグループマップ（ノードに{@link com.echopf.members.ECHOMembersGroupObject}を持つ）。}
 */
public class ECHOMembersGroupsMap extends ECHOTreeMap<ECHOMembersGroupObject, ECHOMembersGroupsMap>
									implements  Fetchable<ECHOMembersGroupsMap> {

	
	/* Begin constructors */

	/**
	 * {@.en Constructs a new ECHOMembersGroupsMap based on an existing *entire* one on the remote server.}
	 * {@.ja ルートから最下層までを対象とした全グループマップとしてオブジェクトを生成します。}
	 * 
	 * @param instanceId
	 * 		{@.en the reference ID of the instance to which the group map has belonged}
	 * 		{@.ja グループが所属するメンバーインスタンスのID}
	 */
	public ECHOMembersGroupsMap(String instanceId) {
		super(instanceId, "groups");
	}

	
	/**
	 * {@.en Constructs a new ECHOMembersGroupsMap based on an existing *sub* one on the remote server.}
	 * {@.ja 特定のノードをルートとしたサブグループマップとしてオブジェクトを生成します。}
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
	 *   based on an existing *entire* one on the remote server.
	 * @param instanceId : the reference ID of the instance to which the group map has belonged
	 * @param data a source JSONObject to copy
	 */
	public ECHOMembersGroupsMap(String instanceId, JSONObject data) {
		super(instanceId, "groups", data);
	}
	
	
	/**
	 * Constructs a new ECHOMembersGroupsMap linking a ECHOMembersGroupObject
	 *   based on an existing *sub* one on the remote server.
	 *
	 * @param instanceId : the reference ID of the instance to which the group map has belonged
	 * @param refid the reference ID of the root node of the existing sub one
	 * @param data a source JSONObject to copy
	 */
	public ECHOMembersGroupsMap(String instanceId, String refid, JSONObject data) {
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
	 * Copies data from a JSONObject
	 *
	 * @param source the source JSONObject
	 */
	@Override
	protected void copyData(JSONObject source) {
		if(source == null) throw new IllegalArgumentException("Argument `source` must not be null.");

		JSONArray groups = source.optJSONArray("groups");
		if(groups != null) {

			if (is_subtree) {
				JSONObject obj = groups.optJSONObject(0);
				if (obj == null) return; // skip

				JSONArray jsonChildren = obj.optJSONArray("children");

				String refid = obj.optString("refid");
				if (refid.isEmpty()) return; // skip

				this.node = new ECHOMembersGroupObject(instanceId, refid, obj);
				this.children = children(jsonChildren);
			} else {
				this.children = children(groups);
			}

		}
	}
	

	/**
	 * Builds children recursively
	 */
	private List<ECHOMembersGroupsMap> children(JSONArray source) {
		List<ECHOMembersGroupsMap> children = new ArrayList<ECHOMembersGroupsMap>();
		
		if(source == null || source.length() == 0) return children;
		
		for(int i = 0; i < source.length(); i++) {
			JSONObject obj = source.optJSONObject(i);
			if(obj == null) continue; // skip
			
			String refid = obj.optString("refid");
			if(refid.isEmpty()) continue; // skip

			try {
				ECHOMembersGroupsMap map = new ECHOMembersGroupsMap(instanceId, refid, new JSONObject("{\"groups\":[" + obj.toString() + "]}"));
				children.add(map);
			} catch (JSONException ignored) {
				// skip
			}
		}
		
		return children;
	}
}
