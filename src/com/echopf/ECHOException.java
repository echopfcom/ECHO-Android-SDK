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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;


/**
 * An ECHOException represents exceptions or errors occurred in the SDK.
 */
public class ECHOException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private int code;
	private Map<String, ECHOException> details = null;


    	/* Begin error codes */

    	public static int RESOURCE_NOT_FOUND = 110010;
   	public static int NOTFOUND_OR_FORBIDDEN = 110020;
    	public static int METHOD_NOT_ALLOWED = 110030;
	public static int UNSUPPORTED_MEDIA_TYPE = 110040;
	public static int INVALID_JSON_FORMAT = 110050;
	public static int APPID_NOT_SPECIFIED = 100010;
	public static int APPKEY_NOT_SPECIFIED = 100020;
	public static int INVALID_API_APPLICATION = 100030;
	public static int OPERATION_NOT_PERMITTED = 130000;
	public static int AUTHENTICATION_ERROR = 130010;
	public static int ACCESSTOKEN_INCORRECTED_OR_EXPIRED = 130020;
	public static int VALIDATION_ERRORS_OCCURRED = 150000;
	public static int NOT_SET = 150010;
	public static int CONTAINED_RESTRICTED_CHARACTER = 150020;
	public static int TOO_LONG = 150030;
	public static int NON_UNIQUE = 150040;
	public static int CONTAINED_NON_NUMERIC_CHARACTER = 150050;
	public static int INVALID_EMAIL_FORMAT = 150060;
	public static int REFERENCE_LOOPED = 150070;
	public static int INVALID_PHONE_NUMBER_FORMAT = 150080;
	public static int INVALID_DATE_STRING_FORMAT = 150090;
	public static int INVALID_ZIPCODE_FORMAT = 150100;
	public static int REFERENCE_NOT_EXIST = 150110;
	public static int INVALID_PREF_FORMAT = 150120;

	/* End error codes */

	
	/* Begin constructors */

	/**
	 * Constructs a new ECHOException with a particular error code.
	 */
	public ECHOException(int code, String message) {
	    super(message);
	    this.code = code;
	}
	

	/**
	 * Constructs a new ECHOException with detailed information
	 */
	public ECHOException(int code, String message, JSONObject details) {
	    this(code, message);
	    
	    if(details != null) {
		    this.details = new HashMap<String, ECHOException>();
	
		    Iterator<?> iter = details.keys();
			while (iter.hasNext()) {
				String key = (String)iter.next();
				JSONObject anError = details.optJSONObject(key);
				if(anError == null) continue; 
				
				int anErrorCode = anError.optInt("error_code");
				String anErrorMessage = anError.optString("error_message");
				
				this.details.put(key, new ECHOException(anErrorCode, anErrorMessage));
			}
	    }
	}
	

	/**
	 * Constructs a new ECHOException with an external cause
	 */
	public ECHOException(String message, Throwable cause) {
	    super(message, cause);
	}

	
	/**
	 * Constructs a new ECHOException with an external cause
	 */
	public ECHOException(Throwable cause) {
	    super(cause);
	}
	
	/* End constructors */

	
	/**
	 * Gets an error code.
	 */
	public int getCode() {
		return this.code;
	}
	
	
	/**
	 * Gets detail information.
	 */
	public Map<String, ECHOException> getDetails() {
		return this.details;
	}
}
