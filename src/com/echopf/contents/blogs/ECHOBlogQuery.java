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

package com.echopf.contents.blogs;

import com.echopf.*;

import java.util.List;

import org.json.JSONObject;


/**
 * {@.en An ECHOBlogQuery contains query methods to operate a blog instance.}
 * {@.ja ブログインスタンスを操作するクエリ。}
 */
public class ECHOBlogQuery {
	
	/**
	 * Disable constructor since this is an utility class.
	 */
	private ECHOBlogQuery() {} 
	

	/**
	 * {@.en Finds entries from a blog archive by synchronous communication.}
	 * {@.ja 同期通信による記事検索。}
	 * 
	 * @param instanceId
	 * 		{@.en the reference ID of the finding target instance}
	 * 		{@.ja 検索対象ブログインスタンスのID}
	 * @param params
	 * 		{@.en to control the output entries by JSONObject}
	 * 		{@.ja <a href="http://echopf.com/docs/restapi/list">リスト制御パラメータ</a>}
	 * @throws ECHOException 
	 */
	public static List<ECHOEntryObject> find(String instanceId, JSONObject params) throws ECHOException {
		return doFind(true, null, instanceId, params);
	}

	
	/**
	 * {@.en Finds entries from a blog archive in a background thread.}
	 * {@.ja 非同期通信による記事検索。検索完了後に指定したコールバックをメインスレッドで実行します。}
	 * 
	 * @param instanceId
	 * 		{@.en the reference ID of the finding target instance}
	 * 		{@.ja 検索対象ブログインスタンスのID}
	 * @param params
	 * 		{@.en to control the output entries by JSONObject}
	 * 		{@.ja <a href="http://echopf.com/docs/restapi/list">リスト制御パラメータ</a>}
	 * @param callback
	 * 		{@.en invoked after the finding is completed}
	 * 		{@.ja 検索完了後に実行するコールバックを指定します。}
	 */
	public static void findInBackground(String instanceId, JSONObject params, FindCallback<ECHOEntryObject> callback) {
		try {
			doFind(false, callback, instanceId, params);
		} catch (ECHOException e) {
			throw new InternalError();
		}
	}
	

	/**
	 * Does Find entries from a blog archive
	 * @param sync if set TRUE, then the main (UI) thread is waited for complete the finding in a background thread. 
	 * 				 (a synchronous communication)
	 * @param callback invoked after the finding is completed
	 * @param instanceId the reference ID of the finding target instance
	 * @param params to control the output entries by JSONObject
	 * @throws ECHOException
	 */
	protected static List<ECHOEntryObject> doFind(boolean sync, final FindCallback<ECHOEntryObject> callback, 
												final String instanceId, final JSONObject params) throws ECHOException {

		return ECHOQuery.doFind(sync, "entries", "archive", ECHOEntryObject.class, callback, instanceId, params);
	}
		
}
