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

package com.echopf.contents;

import com.echopf.*;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * {@.en An ECHOContentsCategoriesMap expresses a hierarchical category map.}
 * {@.ja ツリー構造をもったカテゴリマップ（ノードに{@link com.echopf.contents.ECHOContentsCategoryObject}を持つ）。}
 */
public class ECHOContentsCategoriesMap extends ECHOTreeMap<ECHOContentsCategoryObject, ECHOContentsCategoriesMap>
										implements  Fetchable<ECHOContentsCategoriesMap> {

	
	/* Begin constructors */

	/**
	 * {@.en Constructs a new ECHOContentsCategoriesMap based on an existing *entire* one on the remote server.}
	 * {@.ja ルートから最下層までを対象とした全カテゴリマップとしてオブジェクトを生成します。}
	 * 
	 * @param instanceId
	 * 		{@.en the reference ID of the instance to which the category map has belonged}
	 * 		{@.ja カテゴリが所属するインスタンスのID}
	 */
	public ECHOContentsCategoriesMap(String instanceId) {
		super(instanceId, "categories");
	}

	
	/**
	 * {@.en Constructs a new ECHOContentsCategoriesMap based on an existing *sub* one on the remote server.}
	 * {@.ja 特定のノードをルートとしたサブカテゴリマップとしてオブジェクトを生成します。}
	 * 
	 * @param instanceId
	 * 		{@.en the reference ID of the instance to which the category map has belonged}
	 * 		{@.ja カテゴリが所属するインスタンスのID}
	 * @param refid
	 * 		{@.en the reference ID of the root node of the existing sub one}
	 * 		{@.ja サブカテゴリマップのルートとなるカテゴリのID}
	 */
	public ECHOContentsCategoriesMap(String instanceId, String refid) {
		super(instanceId, "categories", refid);
	}


	/**
	 * Constructs a new ECHOContentsCategoriesMap linking a ECHOContentsCategoryObject.
	 *   based on an existing *entire* one on the remote server.
	 *   
	 * @param instanceId the reference ID of the instance to which the category map has belonged
	 * @param data a source JSONObject to copy
	 * @throws ECHOException 
	 */
	public ECHOContentsCategoriesMap(String instanceId, JSONObject data) throws ECHOException {
		super(instanceId, "categories", data);
	}
	
	
	/**
	 * Constructs a new ECHOContentsCategoriesMap linking a ECHOContentsCategoryObject
	 *   based on an existing *sub* one on the remote server.
	 *
	 * @param instanceId : the reference ID of the instance to which the category map has belonged
	 * @param refid the reference ID of the root node of the existing sub one
	 * @param data a source JSONObject to copy
	 * @throws ECHOException 
	 */
	public ECHOContentsCategoriesMap(String instanceId, String refid, JSONObject data) throws ECHOException {
		super(instanceId, "categories", refid, data);
	}

	/* End constructors */

	
	/*
	 * Implement a Fetchable
	 * @see com.echopf.Fetchable#fetch()
	 */
	public ECHOContentsCategoriesMap fetch() throws ECHOException {
		doFetch(true, null);
		return this;
	}
	
	
	/*
	 * Implement a Fetchable
	 * @see com.echopf.Fetchable#fetchInBackground()
	 */
	public void fetchInBackground(FetchCallback<ECHOContentsCategoriesMap> callback) {
		try {
			doFetch(false, callback);
		} catch (ECHOException e) {
			throw new InternalError();
		}
	}
	
	
	/**
	 * Copies data from a JSONObject.
	 * @throws ECHOException 
	 */
	@Override
	protected void copyData(JSONObject data) throws ECHOException {
		if(data == null) throw new IllegalArgumentException("argument `data` must not be null.");

		JSONArray categories = data.optJSONArray("categories");
		if(categories == null) throw new ECHOException(0, "Invalid data type for data-field `categories`.");

			if(is_subtree) {
				JSONObject obj = categories.optJSONObject(0);
				if(obj == null) throw new ECHOException(0, "Invalid data type for data-field `categories`.");
				JSONArray jsonChildren = obj.optJSONArray("children");

				String refid = obj.optString("refid");
				if(refid.isEmpty()) return;
				
				this.node = new ECHOContentsCategoryObject(instanceId, refid, obj);
				this.children = children(jsonChildren);
			}else{
				this.children = children(categories);
			}
	}
	

	/**
	 * Builds children recursively.
	 * @throws ECHOException 
	 */
	private List<ECHOContentsCategoriesMap> children(JSONArray categories) throws ECHOException {
		List<ECHOContentsCategoriesMap> children = new ArrayList<ECHOContentsCategoriesMap>();
		
		if(categories == null || categories.length() == 0) return children;
		
		for(int i = 0; i < categories.length(); i++) {
			JSONObject obj = categories.optJSONObject(i);
			if(obj == null) throw new ECHOException(0, "Invalid data type for data-field `categories`.");
			
			String refid = obj.optString("refid");
			if(refid.isEmpty()) continue;
		
			ECHOContentsCategoriesMap map;
			try {
				map = new ECHOContentsCategoriesMap(instanceId, refid, new JSONObject("{\"categories\":[" + obj.toString() + "]}"));
			} catch (JSONException e) {
				throw new ECHOException(0, "Invalid data type for data-field `categories`");
			}
			
			children.add(map);
		}
		
		return children;
	}
}
