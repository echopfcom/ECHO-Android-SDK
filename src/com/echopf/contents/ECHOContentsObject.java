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

import android.os.Parcel;

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
	 * Constructs a new ECHOContentsObject based on an existing one on the remote server.
	 * 
	 * @param instanceId the reference ID of the instance to which this object has belonged
	 * @param resourceType the type of this object
	 * @param refid the reference ID of the existing one
	 */
	protected ECHOContentsObject(String instanceId, String resourceType, String refid) {
		super(instanceId, resourceType, refid);
	}

	
	/**
	 * Constructs a new ECHOContentsObject based on an existing one on the remote server.
	 * 
	 * @param instanceId the reference ID of the instance to which this object has belonged
	 * @param resourceType the type of this object
	 * @param refid the reference ID of the existing one
	 * @param source a source JSONObject to copy
	 */
	protected ECHOContentsObject(String instanceId, String resourceType, String refid, JSONObject source) {
		this(instanceId, resourceType, refid);
		copyData(source);
	}

	
	/* End constructors */
	

	@Override
	protected JSONObject buildRequestContents() {
		JSONObject obj = super.buildRequestContents();
		
		try {
			
			// categories
			JSONArray sdk_categories = obj.optJSONArray("categories");
			if(sdk_categories != null) {
				
				JSONArray api_categories = new JSONArray();
				
				for (int i = 0; i < sdk_categories.length(); i++) {
					Object sdk_category = sdk_categories.opt(i);
					if(sdk_category instanceof ECHOContentsCategoryObject) {
						String refid = ((ECHOContentsCategoryObject)sdk_category).getRefid();
						if(!refid.isEmpty()) api_categories.put(refid);
					}
				}
			
				obj.put("categories", api_categories);
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
			
			// categories
			JSONArray api_categories = source.optJSONArray("categories");
			if (api_categories != null) {
				
				JSONArray sdk_categories = new JSONArray();
				
				for(int i = 0; i < api_categories.length(); i++) {
					JSONObject category = api_categories.optJSONObject(i);
					if(category == null) continue; // skip

					String refid = category.optString("refid");
					if(refid.isEmpty()) continue; // skip

					ECHOContentsCategoryObject obj = new ECHOContentsCategoryObject(instanceId, refid, category);
					sdk_categories.put(obj);
				}
				
				source.put("categories", sdk_categories);
			}
			
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}


		// super
		super.copyData(source);
	}


	/* Begin Parcel methods */
	
    public ECHOContentsObject(Parcel in) {
    	super(in);
     }

	/* End Parcel methods */
}
