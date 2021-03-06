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


/**
 * Declares the interface of delete operations in the SDK.
 */
public interface Deletable <T extends ECHOObject> {
	
	/**
	 * {@.en Deletes an object from the ECHO server by a synchronous communication.}
	 * {@.ja 同期通信による削除。}
	 * 
	 * @throws ECHOException
	 */
	T delete() throws ECHOException;

	/**
	 * {@.en Deletes an object from the ECHO server in a background thread.
	 * 			A callback is done in the main (UI) thread after the deleting.}
	 * {@.ja 非同期通信による削除。削除完了後に指定したコールバックをメインスレッドで実行します。}
	 */
	void deleteInBackground(DeleteCallback<T> callback);
}