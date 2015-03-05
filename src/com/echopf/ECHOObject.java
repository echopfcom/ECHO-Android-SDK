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
 * An ECHOObject is an abstract object in ECHO.
 * Particular objects are implemented based on this class.
 * @param <T>
 */
public abstract class ECHOObject {

	protected String instanceId = null;
	protected String resourceType = null;
	protected String refid = null;
	protected final Object lock = new Object();
	
	
	/* Begin constructors */

	/**
	 * Constructs a new ECHOObject.
	 * @param instanceId the reference ID of the instance to which this object belongs
	 * @param resourceType the type of this object
	 */
	protected ECHOObject(String instanceId, String resourceType) {
		this(instanceId, resourceType, null);
	}
	

	/**
	 * Constructs a new ECHOObject based on an existing one on the ECHO server.
	 * @param instanceId the reference ID of the instance to which this object belongs
	 * @param resourceType the type of this object
	 * @param refid : the reference ID of the existing one
	 */
	protected ECHOObject(String instanceId, String resourceType, String refid) {
		super();
		
		this.instanceId = instanceId;
		this.resourceType = resourceType;
		this.refid = refid;
	}

	/* End constructors */
	

	/**
	 * {@.en Gets the current reference ID of this object.}
	 * {@.ja オブジェクトの参照IDを取得する}
	 */
	public String getRefid() {
		return this.refid;
	}
	
	
	/**
	 * Returns the request URL path.
	 */
	protected String getRequestURLPath() {
		if (resourceType == null || resourceType.isEmpty()) 
			throw new IllegalStateException("The resourceType is not specified.");
		
		StringBuilder path = new StringBuilder(instanceId).append("/").append(resourceType);
		if(refid != null && !refid.isEmpty()) path.append("/").append(refid);
		
		return path.toString();
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((instanceId == null) ? 0 : instanceId.hashCode());
		result = prime * result + ((refid == null) ? 0 : refid.hashCode());
		result = prime * result
				+ ((resourceType == null) ? 0 : resourceType.hashCode());
		return result;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ECHOObject other = (ECHOObject) obj;
		if (instanceId == null) {
			if (other.instanceId != null)
				return false;
		} else if (!instanceId.equals(other.instanceId))
			return false;
		if (refid == null) {
			if (other.refid != null)
				return false;
		} else if (!refid.equals(other.refid))
			return false;
		if (resourceType == null) {
			if (other.resourceType != null)
				return false;
		} else if (!resourceType.equals(other.resourceType))
			return false;
		return true;
	}
}
