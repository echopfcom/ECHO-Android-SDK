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

package com.echopf;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * An ECHOACLEntry expresses an ACL entry composing an ECHOACLObject.
 */
public class ECHOACLEntry {
	
	public boolean get = false;
	public boolean list = false;
	public boolean edit = false;
	public boolean delete = false;
	

	/**
	 * Constructs a new ECHOACLEntry.
	 * 
	 * @param get
	 * @param list
	 * @param edit
	 * @param delete
	 */
	public ECHOACLEntry(boolean get, boolean list, boolean edit, boolean delete) {
		this.get = get;
		this.list = list;
		this.edit = edit;
		this.delete = delete;
	}

	
	/**
	 * Converts this object into an JSONObject.
	 * 
	 * @return the formatted JSONObject
	 */
	public JSONObject toJSONObject() {
		JSONObject json = new JSONObject();
		
		try {
			json.put("get", this.get);
			json.put("list", this.list);
			json.put("edit", this.edit);
			json.put("delete", this.delete);
			
			return json;
		} catch (JSONException e) {
		}

		return null;
	}


	/**
	 * Encodes this object as a compact JSON string.
	 */
	public String toString() {
		return this.toJSONObject().toString();
	}
}
