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
import org.json.JSONObject;


/**
 * {@.en An ECHOContentsCategoriesMap is an contents category map.}
 * {@.ja ツリー構造をもったカテゴリマップ。ノードとして{@link com.echopf.contents.ECHOContentsCategoryObject}を持つ。}
 */
public class ECHOContentsCategoriesMap extends ECHOTreeMap<ECHOContentsCategoryObject> {

	
	/* Begin constructors */

	/**
	 * {@.en Constructs a new ECHOContentsCategoriesMap based on an existing *entire* one on the ECHO server.}
	 * {@.ja ルートから最下層までを対象とした全カテゴリマップオブジェクトを生成します。}
	 * 
	 * @param instanceId
	 * 		{@.en the reference ID of the instance to which the category map has belonged}
	 * 		{@.ja カテゴリが所属するインスタンスのID}
	 */
	public ECHOContentsCategoriesMap(String instanceId) {
		super(instanceId, "categories");
	}

	
	/**
	 * {@.en Constructs a new ECHOContentsCategoriesMap based on an existing *sub* one on the ECHO server.}
	 * {@.ja 特定のノードをルートとしたサブカテゴリマップオブジェクトを生成します。}
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
	 *   based on an existing *entire* one on the ECHO server.
	 *   
	 * @param instanceId : the reference ID of the instance to which the category map has belonged
	 * @param node : a linking ECHOContentsCategoryObject
	 */
	public ECHOContentsCategoriesMap(String instanceId, ECHOContentsCategoryObject node) {
		super(instanceId, "categories", node);
	}
	
	
	/**
	 * Constructs a new ECHOContentsCategoriesMap linking a ECHOContentsCategoryObject
	 *   based on an existing *sub* one on the ECHO server.
	 * @param instanceId : the reference ID of the instance to which the category map has belonged
	 * @param refid the reference ID of the root node of the existing sub one
	 * @param node : a linking ECHOContentsCategoryObject
	 */
	public ECHOContentsCategoriesMap(String instanceId, String refid, ECHOContentsCategoryObject node) {
		super(instanceId, "categories", refid, node);
	}

	/* End constructors */
	
	
	/**
	 * Copies data from the specified json object
	 * @throws ECHOException 
	 */
	@Override
	protected void copyData(JSONObject data) throws ECHOException {
		
		JSONArray categories = data.optJSONArray("categories");
		if(categories == null) throw new ECHOException(0, "The copying data is not acceptable. That is why a categories field is not specified.");

			if(is_subtree) {
				JSONObject obj = categories.optJSONObject(0);
				if(obj == null) throw new ECHOException(0, "The copying data is not acceptable.");
				JSONArray children = obj.optJSONArray("children");
				String refid = obj.optString("refid");
				if(refid.isEmpty()) throw new ECHOException(0, "The copying data is not acceptable. That is why a refid is not specified.");
				
				this.node = new ECHOContentsCategoryObject(instanceId, refid, obj);
				this.children = children(children);
			}else{
				this.children = children(categories);
			}
	}
	

	/**
	 * Builds children recursively
	 * @throws ECHOException 
	 */
	private List<ECHOContentsCategoriesMap> children(JSONArray categories) throws ECHOException {
		List<ECHOContentsCategoriesMap> children = new ArrayList<ECHOContentsCategoriesMap>();
		
		if(categories == null || categories.length() == 0) return children;
		
		for(int i = 0; i < categories.length(); i++) {
			JSONObject obj = categories.optJSONObject(i);
			if(obj == null) throw new ECHOException(0, "The copying data is not acceptable.");
			
			String refid = obj.optString("refid");
			if(refid.isEmpty()) throw new ECHOException(0, "The copying data is not acceptable. That is why a refid is not specified.");

			ECHOContentsCategoryObject node = new ECHOContentsCategoryObject(instanceId, refid, obj);			
			ECHOContentsCategoriesMap map = new ECHOContentsCategoriesMap(instanceId, refid, node);
			map.children = children(obj.optJSONArray("children"));
			
			children.add(map);
		}
		
		return children;
	}
}