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

package com.echopf.contents.databases;

import com.echopf.Deletable;
import com.echopf.DeleteCallback;
import com.echopf.ECHOException;
import com.echopf.FetchCallback;
import com.echopf.Fetchable;
import com.echopf.PushCallback;
import com.echopf.Pushable;
import com.echopf.contents.*;

import org.json.JSONObject;


/**
 * {@.en An ECHORecordObject is a particular record object.}
 * {@.ja レコードオブジェクト。}
 */
public class ECHORecordObject extends ECHOContentsObject<ECHORecordObject>
									implements  Fetchable<ECHORecordObject>, 
									Pushable<ECHORecordObject>, 
									Deletable<ECHORecordObject> {


	/* Begin constructors */
	
	/**
	 * {@.en Constructs a new ECHORecordObject.}
	 * {@.ja 新しいレコードとしてオブジェクトを生成します。}
	 * 
	 * @param instanceId
	 * 		{@.en the reference ID of the instance to which this object belongs}
	 * 		{@.ja 新しいレコードを所属させるデータベースインスタンスのID}
	 */
	public ECHORecordObject(String instanceId) {
		super(instanceId, "record");
	}
	
	
	/**
	 * {@.en Constructs a new ECHORecordObject based on an existing one on the remote server.}
	 * {@.ja 既存のレコードとしてオブジェクトを生成します。}
	 * 
	 * @param instanceId
	 * 		{@.en the reference ID of the instance to which this object has belonged}
	 * 		{@.ja 既存レコードが所属するブログインスタンスのID}
	 * @param refid
	 * 		{@.en the reference ID of the existing one}
	 * 		{@.ja 既存レコードのID}
	 */
	public ECHORecordObject(String instanceId, String refid) {
		super(instanceId, "record", refid);
	}


	/**
	 * Constructs a new ECHORecordObject based on an existing one on the remote server.
	 * @param instanceId the reference ID of the instance to which this object has belonged
	 * @param refid the reference ID of the existing one
	 * @param data a source JSONObject to copy
	 */
	public ECHORecordObject(String instanceId, String refid, JSONObject data) {
		super(instanceId, "record", refid, data);
	}
	
	/* End constructors */
	
	
	/*
	 * Implement a Fetchable
	 * @see com.echopf.Fetchable#fetch()
	 */
	public ECHORecordObject fetch() throws ECHOException {
		doFetch(true, null);
		return this;
	}
	
	
	/*
	 * Implement a Fetchable
	 * @see com.echopf.Fetchable#fetchInBackground()
	 */
	public void fetchInBackground(FetchCallback<ECHORecordObject> callback) {
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
	public ECHORecordObject push() throws ECHOException {
		doPush(true, null);
		return this;
	}
	

	/*
	 * Implement a Pushable
	 * @see com.echopf.Pushable#pushInBackground()
	 */
	public void pushInBackground(PushCallback<ECHORecordObject> callback) {
		try {
			doPush(false, callback);
		} catch (ECHOException e) {
			throw new InternalError();
		}
	}
	
	
	/*
	 * Implement a Deletebale
	 * @see com.echopf.Deletebale#fetch()
	 */
	public ECHORecordObject delete() throws ECHOException {
		doDelete(true, null);
		return this;
	}
	
	
	/*
	 * Implement a Deletebale
	 * @see com.echopf.Deletebale#deleteInBackground()
	 */
	public void deleteInBackground(DeleteCallback<ECHORecordObject> callback) {
		try {
			doDelete(false, callback);
		} catch (ECHOException e) {
			throw new InternalError();
		}
	}

}
