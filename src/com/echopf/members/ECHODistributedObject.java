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

import java.text.ParseException;

import android.os.Parcel;

import com.echopf.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * An ECHODistributedObject is an abstract distributed object.
 */
public abstract class ECHODistributedObject<S extends ECHODistributedObject<S>> extends ECHODataObject<S> {

	
	/* Begin constructors */

	/**
	 * Constructs a new ECHODistributedObject.
	 * 
	 * @param instanceId the reference ID of the instance to which this object belongs
	 * @param resourceType the type of this object
	 */
	protected ECHODistributedObject(String instanceId, String resourceType) {
		super(instanceId, resourceType);
	}
	
	
	/**
	 * Constructs a new ECHODistributedObject based on an existing one on the remote server.
	 * 
	 * @param instanceId the reference ID of the instance to which the object has belonged
	 * @param resourceType the type of this object
	 * @param refid the reference ID of the existing one
	 */
	protected ECHODistributedObject(String instanceId, String resourceType, String refid) {
		super(instanceId, resourceType, refid);
	}

	/* End constructors */
	
	
	/**
	 * {@.en Resets the target object.}
	 * {@.ja 送信ターゲットをリセットします。}
	 * @throws ECHOException 
	 */
	public void resetTarget() throws ECHOException {
		this.put("target", new JSONObject());
	}
	
	/**
	 * {@.en Adds all members to the target to send.}
	 * {@.ja すべてのメンバーを送信ターゲットに追加します。}
	 * @throws ECHOException 
	 */
	public void targetAllMembers() throws ECHOException {
		JSONObject targetObj = new JSONObject();
		this.put("target", targetObj);
		
		try {
			targetObj.put("members", "*");
		} catch (JSONException e) {
			throw new ECHOException(e);
		}
	}
	
	/**
	 * {@.en Adds the root group (all groups) to the target to send.}
	 * {@.ja ルートグループ（すべてのグループ）を送信ターゲットに追加します。}
	 * @throws ECHOException 
	 */
	public void targetRootGroup() throws ECHOException {
		JSONObject targetObj = this.optJSONObject("target");
		
		if(targetObj == null) {
			targetObj = new JSONObject();
			this.put("target", targetObj);
		}
		
		try {
			targetObj.put("groups", "*");
		} catch (JSONException e) {
			throw new ECHOException(e);
		}
	}
	
	/**
	 * {@.en Adds a specific member to the target to send.}
	 * {@.ja 特定のメンバーを送信ターゲットに追加します。}
	 * @param member 
	 * 			{@.en the target ECHOMemberObject}
	 * 			{@.ja ターゲットに追加するメンバーオブジェクト}
	 * @throws ECHOException 
	 */
	public void targetSpecificMember(ECHOMemberObject member) throws ECHOException {
		JSONObject targetObj = this.optJSONObject("target");
		if(targetObj == null) {
			targetObj = new JSONObject();
			this.put("target", targetObj);
		}
		
		JSONArray targetMembers = targetObj.optJSONArray("members");
		if(targetMembers == null) {
			targetMembers = new JSONArray();
			try {
				targetObj.put("members", targetMembers);
			} catch (JSONException e) {
				throw new ECHOException(e);
			}
		}
		
		targetMembers.put(member);
	}
	
	/**
	 * {@.en Adds a specific group to the target to send.}
	 * {@.ja 特定のグループを送信ターゲットに追加します。}
	 * @param group
	 * 			{@.en the target ECHOMembersGroupObject}
	 * 			{@.ja ターゲットに追加するグループオブジェクト}
	 * @throws ECHOException 
	 */
	public void targetSpecificGroup(ECHOMembersGroupObject group) throws ECHOException {
		JSONObject targetObj = this.optJSONObject("target");
		if(targetObj == null) {
			targetObj = new JSONObject();
			this.put("target", targetObj);
		}
		
		JSONArray targetGroups = targetObj.optJSONArray("groups");
		if(targetGroups == null) {
			targetGroups = new JSONArray();
			try {
				targetObj.put("groups", targetGroups);
			} catch (JSONException e) {
				throw new ECHOException(e);
			}
		}
		
		targetGroups.put(group);
	}


	@Override
	protected JSONObject buildRequestContents() {
		JSONObject obj = super.buildRequestContents();
		
		try {
			
			// distributed
			Object date = this.opt("distributed");
			if (date instanceof ECHODate) {
				obj.put("distributed", ((ECHODate) date).toStringForECHO());
			}
			
			// target
			JSONObject sdkTarget = this.optJSONObject("target");
			if(sdkTarget != null) {
				JSONObject apiTarget = new JSONObject();
				
				// members
				Object sdkTargetMembers = sdkTarget.opt("members");
				if(sdkTargetMembers != null) {
					
					if (sdkTargetMembers instanceof JSONArray) {
						JSONArray apiTargetMembers = new JSONArray();
						
						JSONArray sdkTargetMembersArray = (JSONArray)sdkTargetMembers;
						for (int i = 0; i < sdkTargetMembersArray.length(); i++) {
							Object sdkTargetMember = sdkTargetMembersArray.opt(i);
							if(sdkTargetMember == null) throw new IllegalStateException("Invalid data type for a target field.");
							
							if(sdkTargetMember instanceof ECHOMemberObject) {
								String refid = ((ECHOMemberObject)sdkTargetMember).getRefid();
								if(refid.isEmpty()) continue; // skip
								
								apiTargetMembers.put(refid);
							} else if(sdkTargetMember.equals("*")) {
								apiTarget.put("members", "*");
								break;
							}
						}

						apiTarget.put("members", apiTargetMembers);
						
					} else if(sdkTargetMembers.equals("*")) {
						apiTarget.put("members", "*");
					}
					
				}
				
				// groups
				Object sdkTargetGroups = sdkTarget.opt("groups");
				if(sdkTargetGroups != null) {
					
					if (sdkTargetGroups instanceof JSONArray) {
						JSONArray apiTargetGroups = new JSONArray();
						
						JSONArray sdkTargetGroupsArray = (JSONArray)sdkTargetGroups;
						for (int i = 0; i < sdkTargetGroupsArray.length(); i++) {
							Object sdkTargetGroup = sdkTargetGroupsArray.opt(i);
							if(sdkTargetGroup == null) throw new IllegalStateException("Invalid data type for a target field.");
							
							if(sdkTargetGroup instanceof ECHOMembersGroupObject) {
								String refid = ((ECHOMembersGroupObject)sdkTargetGroup).getRefid();
								if(refid.isEmpty()) continue; // skip
								
								apiTargetGroups.put(refid);
							} else if(sdkTargetGroup.equals("*")) {
								apiTarget.put("groups", "*");
								break;
							}
						}

						apiTarget.put("groups", apiTargetGroups);
						
					} else if(sdkTargetGroups.equals("*")) {
						apiTarget.put("groups", "*");
					}	
				}
				
				obj.put("target", apiTarget);
			}

		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		return obj;
	}
	
	
	@Override
	protected void copyData(JSONObject source) {
		if(source == null) throw new IllegalArgumentException("Argument `source` must not be null.");

		try {
			
			// distributed
			String distributed = source.optString("distributed");
			if(distributed != null) {
				try {
					source.put("distributed", new ECHODate(distributed));
				} catch (ParseException ignored) {
					// skip
				}
			}
			
			// target
			JSONObject apiTarget = source.optJSONObject("target");
			if (apiTarget != null) {
				JSONObject sdkTarget = new JSONObject();
	
				// members
				Object apiTargetMembers = apiTarget.opt("members");
				if(apiTargetMembers != null) {
					
					if(apiTargetMembers instanceof JSONArray) {
						JSONArray sdkTargetMembers = new JSONArray();
	
						JSONArray apiTargetMembersArray = (JSONArray)apiTargetMembers;
						for (int i = 0; i < apiTargetMembersArray.length(); i++) {
							String refid = apiTargetMembersArray.optString(i);
							if(refid.isEmpty()) continue; // skip
							
							if(refid.equals("*")) {
								sdkTarget.put("members", "*");
								break;
							} else {
								sdkTargetMembers.put(new ECHOMemberObject(this.instanceId, refid));
							}
						}
	
						sdkTarget.put("members", sdkTargetMembers);
						
					} else if(apiTargetMembers.equals("*")) {
						sdkTarget.put("members", "*");
					}
				}
	
				// groups
				Object apiTargetGroups = apiTarget.opt("groups");
				if(apiTargetGroups != null) {
					
					if(apiTargetGroups instanceof JSONArray) {
						JSONArray sdkTargetGroups = new JSONArray();
	
						JSONArray apiTargetGroupsArray = (JSONArray)apiTargetGroups;
						for (int i = 0; i < apiTargetGroupsArray.length(); i++) {
							String refid = apiTargetGroupsArray.optString(i);
							if(refid.isEmpty()) continue; // skip
							
							if(refid.equals("*")) {
								sdkTarget.put("groups", "*");
								break;
							} else {
								sdkTargetGroups.put(new ECHOMembersGroupObject(this.instanceId, refid));
							}
						}
						
						sdkTarget.put("groups", sdkTargetGroups);
						
					} else if(apiTargetGroups.equals("*")) {
						sdkTarget.put("groups", "*");
					}
				}
				
				source.put("target", sdkTarget);
			}
		
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}


		// super
		super.copyData(source);
	}

	
	/* Begin Parcel methods */
	
    public ECHODistributedObject(Parcel in) {
    	super(in);
     }

	/* End Parcel methods */
}
