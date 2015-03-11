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
	private Map<String, Throwable> details = null;

	public static int RESOURCE_NOT_FOUND = 110010;
	public static int INVALID_JSON_FORMAT = 110050;
	
	
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
		    this.details = new HashMap<String, Throwable>();
	
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
	public Map<String, Throwable> getDetails() {
		return this.details;
	}
}
