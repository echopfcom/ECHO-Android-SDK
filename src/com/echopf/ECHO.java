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

import java.lang.String;

import android.content.Context;

/**
 * A global configuration handler in the SDK.
 */
public class ECHO {

    public static String secureDomain = null;
    public static String appId = null;
    public static String appKey = null;
    public static String accessToken = null;
    public static Context context = null;
    
    /**
     * {@.en Initializes the configuration.
     * 			This method must be called before using the SDK.}
     * {@.ja SDKを初期化します。 SDKを使用する前に必ず呼び出してください。}
     * 
     * @param context
     * 		{@.en the context of the Android Activity}
     * 		{@.ja 呼び出し元アクティビティのコンテキスト(this)}
     * @param secureDomain
     * 		{@.en ECHO account ID (e.g. hogehoge.echopf.com)}
     * 		{@.ja アカウントID（セキュアドメイン）}
     * @param appId
     * 		{@.en application ID.}
     * 		{@.ja アプリケーションID}
     * @param appKey
     * 		{@.en application key.}
     * 		{@.ja アプリケーションキー}
     */
    public static void initialize(Context context, String secureDomain, String appId, String appKey) {
        if(context == null) throw new IllegalArgumentException("argument `context` must not be null.");
        if(secureDomain == null) throw new IllegalArgumentException("argument `secureDomain` must not be null.");
        if(appId == null) throw new IllegalArgumentException("argument `appId` must not be null.");
        if(appKey == null) throw new IllegalArgumentException("argument `appKey` must not be null.");

        ECHO.context = context;
        ECHO.secureDomain = secureDomain;
        ECHO.appId = appId;
        ECHO.appKey = appKey;
    }
}



