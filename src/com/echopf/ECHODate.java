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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/**
 * An ECHODate is the extended Date object for the SDK.
 */
public class ECHODate extends Date {

	private static final long serialVersionUID = 1L;

	/**
	 * {@.en Constructs a new ECHODate.}
	 * {@.ja 日時オブジェクトを現在時刻で生成します。}
	 */
	public ECHODate() {
		super();
	}
	
	
	/**
	 * {@.en Constructs a new ECHODate with an acceptable date string for the API.}
	 * {@.ja APIの仕様に準拠した文字列形式の日時から、日時オブジェクトを生成します。}
	 * @param s an acceptable date string for the API (e.g. "2015-02-20 00:00:00")
	 */
	public ECHODate(String s) throws ParseException {
		super();

		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		try {
			setTime(sdf.parse(s).getTime());
		} catch (ParseException e) {
			throw e;
		}
	}

	
	/**
	 * {@.en Converts this object to an acceptable date string for the API.}
	 * {@.ja APIの仕様に準拠した文字列形式の日時へ変換します。}
	 * @return the formatted date string for the ECHO API.
	 */
	public String toStringForECHO() {

		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		return sdf.format(this);
	}
}



