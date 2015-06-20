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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * An ECHOContentsObject is an abstract contents object.
 */
public abstract class ECHOContentsObject<S extends ECHOContentsObject<S>> extends ECHODataObject<S> {

	
	/* Begin constructors */

	
	/**
	 * Constructs a new ECHOContentsObject.
	 * 
	 * @param instanceId the reference ID of the instance to which this object belongs
	 * @param resourceType the type of this object
	 */
	protected ECHOContentsObject(String instanceId, String resourceType) {
		super(instanceId, resourceType);
	}

	
	/**
	 * Constructs a new ECHOContentsObject based on an existing one on the ECHO server.
	 * 
	 * @param instanceId the reference ID of the instance to which this object has belonged
	 * @param resourceType the type of this object
	 * @param refid the reference ID of the existing one
	 */
	protected ECHOContentsObject(String instanceId, String resource_path, String refid) {
		super(instanceId, resource_path, refid);
	}

	
	/**
	 * Constructs a new ECHOContentsObject based on an existing one on the ECHO server.
	 * 
	 * @param instanceId the reference ID of the instance to which this object has belonged
	 * @param resourceType the type of this object
	 * @param refid the reference ID of the existing one
	 * @param data : a copying contents object by JSONObject
	 */
	protected ECHOContentsObject(String instanceId, String resourceType, String refid, JSONObject data) throws ECHOException {
		this(instanceId, refid);
		copyData(data);
	}

	
	/* End constructors */
	

	@Override
	protected JSONObject buildRequestContents() throws ECHOException {
		JSONObject obj = super.buildRequestContents();
		
		try {
			
			// categories
			JSONArray sdk_categories = obj.optJSONArray("categories");
			
			if(sdk_categories != null) {
				JSONArray api_categories = new JSONArray();
				
				for (int i = 0; i < sdk_categories.length(); i++) {
					JSONObject sdk_category = sdk_categories.optJSONObject(i);
					if(sdk_category == null)  throw new IllegalStateException("Invalid data format in a category.");
					
					String refid = sdk_category.optString("refid");
					if(refid.isEmpty()) throw new IllegalStateException("Invalid data format in a category.");
						
					api_categories.put(refid);
				}
			
				obj.put("categories", api_categories);
			}
			
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		return obj;
	}


	@Override
	protected void copyData(JSONObject data) throws ECHOException {

		try {
			// categories
			JSONArray api_categories = data.optJSONArray("categories");
			if(api_categories == null) throw new ECHOException(0, "The copying data is not acceptable. That is why a categories field is not specified.");
			
			JSONArray sdk_categories = new JSONArray();
			for(int i = 0; i < api_categories.length(); i++) {
				JSONObject category = api_categories.optJSONObject(i);
				if(category == null) throw new ECHOException(0, "The copying data is not acceptable.");
				
				String refid = category.optString("refid");
				if(refid.isEmpty()) throw new ECHOException(0, "The copying data is not acceptable. That is why a refid is not specified.");

				ECHOContentsCategoryObject obj = new ECHOContentsCategoryObject(instanceId, refid, category);
				sdk_categories.put(obj);
			}
			data.put("categories", sdk_categories);
			
			
			super.copyData(data);
				
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

}
