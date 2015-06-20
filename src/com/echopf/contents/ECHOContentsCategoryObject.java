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
 * {@.en An ECHOContentsCategoryObject is a particular category object.}
 * {@.ja カテゴリオブジェクト。{@link com.echopf.contents.ECHOContentsCategoriesMap}の構成要素。}
 */
public class ECHOContentsCategoryObject extends ECHODataObject<ECHOContentsCategoryObject> 
										implements  Fetchable<ECHOContentsCategoryObject>, 
													Pushable<ECHOContentsCategoryObject>, 
													Deletable<ECHOContentsCategoryObject>,
													TreeNodeable<ECHOContentsCategoryObject> {

	private ECHOContentsCategoryObject newParent = null;
	
	
	/* Begin constructors */
	
	/**
	 * {@.en Constructs a new ECHOContentsCategoryObject.}
	 * {@.ja 新しいカテゴリをオブジェクトとして生成します。}
	 * 
	 * @param instanceId
	 * 		{@.en the reference ID of the instance to which this object belongs}
	 * 		{@.ja 新しいカテゴリを所属させるインスタンスのID}
	 */
	public ECHOContentsCategoryObject(String instanceId) {
		super(instanceId, "categories");
	}

	/**
	 * {@.en Constructs a new ECHOContentsCategoryObject based on an existing one on the remote server.}
	 * {@.ja 既存のカテゴリをオブジェクトとして生成します。}
	 * 
	 * @param instanceId
	 * 		{@.en the reference ID of the instance to which this object has belonged}
	 * 		{@.ja 既存カテゴリが所属するインスタンスのID}
	 * @param refid
	 * 		{@.en the reference ID of the existing one}
	 * 		{@.ja 既存カテゴリのID}
	 */
	public ECHOContentsCategoryObject(String instanceId, String refid) {
		super(instanceId, "categories", refid);
	}

	/**
	 * Constructs a new ECHOContentsCategoryObject based on an existing one on the remote server.
	 * @param instanceId the reference ID of the instance to which this object has belonged
	 * @param refid the reference ID of the existing one
	 * @param data : a source JSONObject to copy
	 */
	public ECHOContentsCategoryObject(String instanceId, String refid, JSONObject data) throws ECHOException {
		this(instanceId, refid);
		copyData(data);
	}

	/* End constructors */
	
	
	/*
	 * Implement Fetchable
	 * @see com.echopf.Fetchable#fetch()
	 */
	public ECHOContentsCategoryObject fetch() throws ECHOException {
		doFetch(true, null);
		return this;
	}
	
	
	/*
	 * Implement Fetchable
	 * @see com.echopf.Fetchable#fetchInBackground()
	 */
	public void fetchInBackground(FetchCallback<ECHOContentsCategoryObject> callback) {
		try {
			doFetch(false, callback);
		} catch (ECHOException e) {
			throw new InternalError();
		}
	}
	

	/*
	 * Implement Pushable
	 * @see com.echopf.Pushable#push()
	 */
	public ECHOContentsCategoryObject push() throws ECHOException {
		doPush(true, null);
		return this;
	}
	

	/*
	 * Implement Pushable
	 * @see com.echopf.Pushable#pushInBackground()
	 */
	public void pushInBackground(PushCallback<ECHOContentsCategoryObject> callback) {
		try {
			doPush(false, callback);
		} catch (ECHOException e) {
			throw new InternalError();
		}
	}

	
	/*
	 * Implement Deleteable
	 * @see com.echopf.Deleteable#delete()
	 */
	public ECHOContentsCategoryObject delete() throws ECHOException {
		doDelete(true, null);
		return this;
	}

	
	/*
	 * Implement Deleteable
	 * @see com.echopf.Deleteable#deleteInBackground()
	 */
	public void deleteInBackground(DeleteCallback<ECHOContentsCategoryObject> callback) {
		try {
			doDelete(false, callback);
		} catch (ECHOException e) {
			throw new InternalError();
		}
	}

	
	/*
	 * Implement TreeNodeable
	 * @see com.echopf.TreeNodeable#setNewParent()
	 */
	public void setNewParent(ECHOContentsCategoryObject newParent) {
		this.newParent = newParent;
	}

	
	@Override
	protected JSONObject buildRequestContents() throws ECHOException {
		JSONObject obj = super.buildRequestContents();
		
		try {
			
			if(this.newParent != null) {
				String new_parent_refid = this.newParent.getRefid();
				
				if(!new_parent_refid.isEmpty()) {
					obj.put("parent_refid", new_parent_refid);
					this.newParent = null;
				}
			}
			
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		return obj;
	}

	
	@Override
	protected void copyData(JSONObject data) throws ECHOException {
		if(data == null) throw new IllegalArgumentException("argument `data` must not be null.");

		JSONArray categories = data.optJSONArray("categories");
		
		JSONObject category = null;
		if(categories != null) { // if the data is a tree format
			category = categories.optJSONObject(0);
		}else{ // the data is a category object
			category = data;
		}
		
		if(category == null) throw new ECHOException(0, "Invalid data type for data-field `categories`.");
		
		category.remove("children");
		super.copyData(category);
	}
}
