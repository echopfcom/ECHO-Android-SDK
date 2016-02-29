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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.JSONObject;

import android.os.Handler;


/**
 * {@.en An ECHOMemberQuery contains query methods to operate a member instance.}
 * {@.ja メンバーインスタンスを操作するクエリ。}
 */
public class ECHOMemberQuery {


	/**
	 * Disable constructor since this is an utility class.
	 */
	private ECHOMemberQuery() {}


	/**
	 * {@.en Finds members from the remote server by synchronous communication.}
	 * {@.ja 同期通信によるメンバー検索。}
	 * 
	 * @param instanceId
	 * 		{@.en the reference ID of the finding target instance}
	 * 		{@.ja 検索対象メンバーインスタンスのID}
	 * @param params
	 * 		{@.en to control the output}
	 * 		{@.ja <a href="http://echopf.com/docs/restapi/list">リスト制御パラメータ</a>}
	 * @param callback
	 * 		{@.en invoked after the finding is completed}
	 * 		{@.ja 検索完了後に実行するコールバックを指定します。}
	 * @throws ECHOException
	 */
	public static ECHOList<ECHOMemberObject> find(String instanceId, JSONObject params) throws ECHOException {
		return doFind(true, null, instanceId, params);
	}


	/**
	 * {@.en Finds members from the remote server in a background thread.}
	 * {@.ja 非同期通信によるメンバー検索。検索完了後に指定したコールバックをメインスレッドで実行します。}
	 * 
	 * @param instanceId
	 * 		{@.en the reference ID of the finding target instance}
	 * 		{@.ja 検索対象メンバーインスタンスのID}
	 * @param params
	 * 		{@.en to control the output}
	 * 		{@.ja <a href="http://echopf.com/docs/restapi/list">リスト制御パラメータ</a>}
	 * @param callback
	 * 		{@.en invoked after the finding is completed}
	 * 		{@.ja 検索完了後に実行するコールバックを指定します。}
	 */
	public static void findInBackground(String instanceId, JSONObject params, FindCallback<ECHOMemberObject> callback) {
		try {
			doFind(false, callback, instanceId, params);
		} catch (ECHOException e) {
			throw new InternalError();
		}
	}


	/**
	 * {@.en Logs-in by synchronous communication.}
	 * {@.ja 同期通信によるログイン。}
	 * 
	 * @param instanceId
	 * 		{@.en the reference ID of the instance to which the logged-in member belong.}
	 * 		{@.ja ログインメンバーが所属するインスタンスのID}
	 * @param login_id
	 *		{@.en login id}
	 *		{@.ja ログインID}
	 * @param password
	 *		{@.en login password}
	 *		{@.ja ログインパスワード}
	 * @throws ECHOException
	 */
	public static ECHOMemberObject login(String instanceId, String login_id, String password) throws ECHOException {
		return doLogin(true, null, instanceId, login_id, password);
	}


	/**
	 * {@.en Logs-in in a background thread.}
	 * {@.ja 非同期通信によるログイン。処理後に指定したコールバックをメインスレッドで実行します。}
	 * 
	 * @param instanceId
	 * 		{@.en the reference ID of the instance to which the logged-in member belong.}
	 * 		{@.ja ログインメンバーが所属するインスタンスのID}
	 * @param login_id
	 *		{@.en login id}
	 *		{@.ja ログインID}
	 * @param password
	 *		{@.en login password}
	 *		{@.ja ログインパスワード}
	 * @param callback invoked after the logging-in is completed
	 */
	public static void loginInBackground(String instanceId, String login_id, String password, LoginCallback callback) {
		try {
			doLogin(false, callback, instanceId, login_id, password);
		} catch (ECHOException e) {
			throw new InternalError();
		}
	}


	/**
	 * {@.en Logs-out by synchronous communication.}
	 * {@.ja 同期通信によるログアウト。}
	 * 
	 * @param instanceId
	 * 		{@.en the reference ID of the instance to which the logged-in member belong.}
	 * 		{@.ja ログインメンバーが所属するインスタンスのID}
	 * @throws ECHOException
	 */
	public static void logout(String instanceId) throws ECHOException {
		doLogout(true, null, instanceId);
	}


	/**
	 * {@.en Logs-out in a background thread.}
	 * {@.ja 非同期通信によるログアウト。処理後に指定したコールバックをメインスレッドで実行します。}
	 * 
	 * @param instanceId
	 * 		{@.en the reference ID of the instance to which the logged-in member belong.}
	 * 		{@.ja ログインメンバーが所属するインスタンスのID}
	 * @param callback invoked after the logging-out is completed
	 */
	public static void logoutInBackground(String instanceId, ResultCallback callback) {
		try {
			doLogout(false, callback, instanceId);
		} catch (ECHOException e) {
			throw new InternalError();
		}
	}


	/**
	 * Does Find members from the remote server
	 * @param sync if set TRUE, then the main (UI) thread is waited for complete the finding in a background thread. 
	 * 				 (a synchronous communication)
	 * @param callback invoked after the finding is completed
	 * @param instanceId the reference ID of the finding target instance
	 * @param params to control the output
	 * @throws ECHOException
	 */
	protected static ECHOList<ECHOMemberObject> doFind(boolean sync, final FindCallback<ECHOMemberObject> callback,
													   final String instanceId, final JSONObject params) throws ECHOException {
		return ECHOQuery.doFind(sync, "members", "list", callback, instanceId, params, new ECHODataObjectFactory<ECHOMemberObject>() {

			@Override
			public ECHOMemberObject create(String instanceId, String refid, JSONObject obj) throws ECHOException {
				return new ECHOMemberObject(instanceId, refid, obj);
			}

		});
	}


	/**
	 * Does Login
	 * @param sync if set TRUE, then the main (UI) thread is waited for complete the logging-in in a background thread. 
	 * 				 (a synchronous communication)
	 * @param callback invoked after the logging-in is completed
	 * @param instanceId the reference ID of the instance to which the logged-in member belong
	 * @param login_id
	 * @param password
	 * @throws ECHOException
	 */
	protected static ECHOMemberObject doLogin(final boolean sync, final LoginCallback callback,
											  final String instanceId, final String login_id, final String password) throws ECHOException {
		final Handler handler = new Handler();

		// Get ready a background thread
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Callable<ECHOMemberObject> communicator = new Callable<ECHOMemberObject>() {

			@Override
			public ECHOMemberObject call() throws ECHOException {

				ECHOException exception = null;
				ECHOMemberObject memberObj = null;

				try {
					JSONObject params = new JSONObject();
					params.put("login_id", login_id); params.put("password", password);
					JSONObject response = ECHOQuery.postRequest(instanceId + "/login", params);
					memberObj = new ECHOMemberObject(instanceId, response.optString("refid"), response);

					//
					ECHO.accessToken = response.optString("access_token");

				} catch (ECHOException e) {
					exception = e;
				} catch (Exception e) {
					exception = new ECHOException(e);
				}



				if(sync == false) {

					// Execute a callback method in the main (UI) thread.
					if(callback != null) {
						final ECHOException fException = exception;
						final ECHOMemberObject fMemberObj = memberObj;

						handler.post(new Runnable() {
							@Override
							public void run() {
								callback.done(fMemberObj, fException);
							}
						});
					}

					return null;

				}else{

					if(exception == null) return memberObj;
					throw exception;
				}
			}
		};

		Future<ECHOMemberObject> future = executor.submit(communicator);

		if(sync) {
			try {
				return future.get();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt(); // ignore/reset
			} catch (ExecutionException e) {
				Throwable e2 = e.getCause();

				if (e2 instanceof ECHOException) {
					throw (ECHOException) e2;
				}

				throw new RuntimeException(e2);
			}
		}

		return null;
	}


	/**
	 * Does Logout
	 * @param sync if set TRUE, then the main (UI) thread is waited for complete the logging-out in a background thread. 
	 * 				 (a synchronous communication)
	 * @param callback invoked after the logging-out is completed
	 * @param instanceId the reference ID of the instance to which the logged-out member belong
	 * @throws ECHOException
	 */
	protected static void doLogout(final boolean sync, final ResultCallback callback, final String instanceId) throws ECHOException {
		final Handler handler = new Handler();

		// Get ready a background thread
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Callable<Void> communicator = new Callable<Void>() {

			@Override
			public Void call() throws ECHOException {

				ECHOException exception = null;

				try {
					ECHOQuery.postRequest(instanceId + "/login", new JSONObject());
				} catch (ECHOException e) {
					exception = e;
				} catch (Exception e) {
					exception = new ECHOException(e);
				}



				if(sync == false) {

					// Execute a callback method in the main (UI) thread.
					if(callback != null) {
						final ECHOException fException = exception;

						handler.post(new Runnable() {
							@Override
							public void run() {
								callback.done(fException);
							}
						});
					}

					return null;

				}else{

					if(exception == null) return null;
					throw exception;
				}
			}
		};

		Future<Void> future = executor.submit(communicator);

		if(sync) {
			try {
				future.get();
				return;
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt(); // ignore/reset
			} catch (ExecutionException e) {
				Throwable e2 = e.getCause();

				if (e2 instanceof ECHOException) {
					throw (ECHOException) e2;
				}

				throw new RuntimeException(e2);
			}
		}

		return;
	}
}