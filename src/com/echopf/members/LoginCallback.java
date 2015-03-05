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

import com.echopf.ECHOException;


/**
 * A LoginCallback is used to do something after logging-in in a background thread.
 */
public abstract class LoginCallback {

	/**
	 * {@.en Override this method according desired functions after the logging-in.
	 * 			This method is done in the main (UI) thread.}
	 * {@.ja ログイン後にメインスレッドで実行したいコードで上書きしてください。}
	 * 
	 * @param obj
	 * 		{@.en the logged-in member}
	 * 		{@.ja ログインされたメンバーオブジェクト}
	 * @param e
	 * 		{@.en an exception occurred in the logging-in, or null if it succeeded.}
	 * 		{@.ja ログイン中に例外が発生した場合のみセットされ、それ以外はnull}
	 */
	public abstract void done(ECHOMemberObject obj, ECHOException e);
}