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

import com.echopf.*;
import org.json.JSONObject;


/**
 * {@.en An ECHODatabaseQuery contains query methods to operate a database instance.}
 * {@.ja データベースインスタンスを操作するクエリ。}
 */
public class ECHODatabaseQuery {
	

	/* Disable constructor since this is an utility class. */
	private ECHODatabaseQuery() {}
	
	
	/**
	 * {@.en Finds records from a database archive by synchronous communication.}
	 * {@.ja 同期通信によるレコード検索。}
	 * 
	 * @param instanceId
	 * 		{@.en the reference ID of the finding target instance}
	 * 		{@.ja 検索対象データベースインスタンスのID}
	 * @param params
	 * 		{@.en to control the output records by JSONObject}
	 * 		{@.ja <a href="http://echopf.com/docs/restapi/list">リスト制御パラメータ</a>}
	 * @throws ECHOException 
	 */
	public static ECHOArrayList<ECHORecordObject> find(String instanceId, JSONObject params) throws ECHOException {
		return doFind(true, null, instanceId, params);
	}

	
	/**
	 * {@.en Finds records from a database archive in a background thread.}
	 * {@.ja 非同期通信によるレコード検索。検索完了後に指定したコールバックをメインスレッドで実行します。}
	 * 
	 * @param instanceId
	 * 		{@.en the reference ID of the finding target instance}
	 * 		{@.ja 検索対象データベースインスタンスのID}
	 * @param params
	 * 		{@.en to control the output records by JSONObject}
	 * 		{@.ja <a href="http://echopf.com/docs/restapi/list">リスト制御パラメータ</a>}
	 * @param callback
	 * 		{@.en invoked after the finding is completed}
	 * 		{@.ja 検索完了後に実行するコールバックを指定します。}
	 */
	public static void findInBackground(String instanceId, JSONObject params, FindCallback<ECHORecordObject> callback) {
		try {
			doFind(false, callback, instanceId, params);
		} catch (ECHOException e) {
			throw new InternalError();
		}
	}
	

	/**
	 * Does Find records from a database archive
	 * @param sync : if set TRUE, then the main (UI) thread is waited for complete the finding in a background thread. 
	 * 				 (a synchronous communication)
	 * @param callback invoked after the finding is completed
	 * @param instanceId the reference ID of the finding target instance
	 * @param params to control the output records by JSONObject
	 * @throws ECHOException
	 */
	protected static ECHOArrayList<ECHORecordObject> doFind(boolean sync, final FindCallback<ECHORecordObject> callback, 
												final String instanceId, final JSONObject params) throws ECHOException {

		return ECHOQuery.doFind(sync, "records", "archive", callback, instanceId, params, new ECHODataObjectFactory<ECHORecordObject>() {

			@Override
			public ECHORecordObject create(String instanceId, String refid, JSONObject obj) throws ECHOException {
				return new ECHORecordObject(instanceId, refid, obj);
			}
			
		});
	}
}
