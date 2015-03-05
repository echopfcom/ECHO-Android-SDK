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
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import com.echopf.members.ECHOMemberObject;
import com.echopf.members.ECHOMembersGroupObject;

/**
 * An ECHOACLObject expresses ACL to an object.
 */
public class ECHOACLObject {

	private ECHOACLEntry all = null;
	private Map<String/* :memberInstanceId */, ECHOACLEntry> allMembers = new HashMap<String, ECHOACLEntry>();
	private Map<ECHOMembersGroupObject, ECHOACLEntry> specificGroups = new HashMap<ECHOMembersGroupObject, ECHOACLEntry>();
	private Map<ECHOMemberObject, ECHOACLEntry> specificMembers = new HashMap<ECHOMemberObject, ECHOACLEntry>();

	/* Begin constructors */

	/**
	 * Constructs a new ECHOACLObject.
	 */
	public ECHOACLObject() {
		super();
	}

	/**
	 * Constructs a new ECHOACLObject with JSONObject.
	 *
	 * @param aclObj the source JSONObject
	 */
	public ECHOACLObject(JSONObject aclObj) throws ECHOException {
		super();
		copyData(aclObj);
	}

	/* End constructors */

	
	/* Begin this.all setter/getter */

	/**
	 * {@.en Gets the ACL entry for all the visitors.}
	 * {@.ja すべての訪問者に与えられているACLを取得。}
	 * 
	 * @return ECHOACLEntry
	 */
	public ECHOACLEntry getEntryForAll() {
		return this.all;
	}

	/**
	 * {@.en Sets an ACL entry for all the visitors.}
	 * {@.ja すべての訪問者に対するACLをセット。}
	 * 
	 * @param entry
	 */
	public void putEntryForAll(ECHOACLEntry entry) {
		this.all = entry;
	}

	/**
	 * {@.en Resets the ACL entry for all the visitors.}
	 * {@.ja すべての訪問者に対するACLをリセット。}
	 */
	public void resetEntryForAll() {
		this.all = null;
	}

	
	/* Begin this.allMembers setter/getter */

	/**
	 * {@.en Gets the ACL entry for all the members belonged to a specific member instance.}
	 * {@.ja 特定のインスタンスに所属するメンバーに与えられているACLを取得。}
	 * 
	 * @param memberInstanceId
	 * 		{@.en the reference ID of the member instance}
	 * 		{@.ja メンバーインスタンスID}
	 * @return ECHOACLEntry
	 */
	public ECHOACLEntry getEntryForAllMembers(String memberInstanceId) {
		return allMembers.get(memberInstanceId);
	}

	/**
	 * {@.en Sets an ACL entry for all the members belonged to a specific member instance.}
	 * {@.ja 特定のインスタンスに所属するメンバーに対するACLをセット。}
	 * 
	 * @param memberInstanceId
	 * 		{@.en the reference ID of the member instance}
	 * 		{@.ja メンバーインスタンスID}
	 * @param entry
	 */
	public void putEntryForAllMembers(String memberInstanceId, ECHOACLEntry entry) {
		allMembers.put(memberInstanceId, entry);
	}

	/**
	 * {@.en Resets the ACL entry for all the members belonged to a specific member instance.}
	 * {@.ja 特定のインスタンスに所属するメンバーに対するACLをリセット。}
	 * 
	 * @param memberInstanceId
	 * 		{@.en the reference ID of the member instance}
	 * 		{@.ja メンバーインスタンスID}
	 */
	public void resetEntryForAllMembers(String memberInstanceId) {
		allMembers.remove(memberInstanceId);
	}

	
	/* Begin this.specificGroups setter/getter */

	/**
	 * {@.en Gets the ACL entry for all the members belonged to a specific group.}
	 * {@.ja 特定のグループに所属するメンバーに与えられているACLを取得。}
	 * 
	 * @param group
	 * 		{@.en the target ECHOMembersGroupObject}
	 * 		{@.ja 対象となるグループオブジェクト}
	 */
	public ECHOACLEntry getEntryForSpecificGroup(ECHOMembersGroupObject group) {
		if(group == null || group.instanceId.isEmpty() ||  group.refid.isEmpty())
			throw new IllegalArgumentException("Invalid data type for argument `group`.");
		
		return specificGroups.get(group);
	}

	/**
	 * {@.en Sets the ACL entry for all the members belonged to a specific group.}
	 * {@.ja 特定のグループに所属するメンバーに対するACLをセット。}
	 * 
	 * @param group
	 * 		{@.en the target ECHOMembersGroupObject}
	 * 		{@.ja 対象となるグループオブジェクト}
	 * @param entry
	 */
	public void putEntryForSpecificGroup(ECHOMembersGroupObject group, ECHOACLEntry entry) {
		if(group == null || group.instanceId.isEmpty() ||  group.refid.isEmpty()) 
			throw new IllegalArgumentException("Invalid data type for argument `group`.");

		specificGroups.put(group, entry);
	}

	/**
	 * {@.en Resets the ACL entry for all the members belonged to a specific group.}
	 * {@.ja 特定のグループに所属するメンバーに対するACLをリセット。}
	 * 
	 * @param group
	 * 		{@.en the target ECHOMembersGroupObject}
	 * 		{@.ja 対象となるグループオブジェクト}
	 */
	public void resetEntryForSpecificGroup(ECHOMembersGroupObject group) {
		if(group == null || group.instanceId.isEmpty() ||  group.refid.isEmpty()) 
			throw new IllegalArgumentException("Invalid data type for argument `group`.");
		
		specificGroups.remove(group);
	}

	
	/* Begin this.specificMembers setter/getter */

	/**
	 * {@.en Gets the ACL entry for a specific member.}
	 * {@.ja 特定のメンバーに与えられているACLを取得。}
	 * 
	 * @param member
	 * 		{@.en the target ECHOMemberObject}
	 * 		{@.ja 対象となるメンバーオブジェクト}
	 */
	public ECHOACLEntry getEntryForSpecificMember(ECHOMemberObject member) {
		if(member == null || member.instanceId.isEmpty() ||  member.refid.isEmpty()) 
			throw new IllegalArgumentException("Invalid data type for argument `member`.");
		
		return specificMembers.get(member);
	}

	/**
	 * {@.en Sets the ACL entry for a specific member.}
	 * {@.ja 特定のメンバーに対するACLをセット。}
	 * 
	 * @param member
	 * 		{@.en the target ECHOMemberObject}
	 * 		{@.ja 対象となるメンバーオブジェクト}
	 * @param entry
	 */
	public void putEntryForSpecificMember(ECHOMemberObject member, ECHOACLEntry entry) {
		if(member == null || member.instanceId.isEmpty() ||  member.refid.isEmpty()) 
			throw new IllegalArgumentException("Invalid data type for argument `member`.");
		
		specificMembers.put(member, entry);
	}

	/**
	 * {@.en Resets the ACL entry for a specific member.}
	 * {@.ja 特定のメンバーに対するACLをリセット。}
	 * 
	 * @param member
	 * 		{@.en the target ECHOMemberObject}
	 * 		{@.ja 対象となるメンバーオブジェクト}
	 */
	public void resetEntryForSpecificMember(ECHOMemberObject member) {
		if(member == null || member.instanceId.isEmpty() ||  member.refid.isEmpty()) 
			throw new IllegalArgumentException("Invalid data type for argument `member`.");
		
		specificMembers.remove(member);
	}
	
	
	/**
	 * Converts this object into an acceptable JSONObject for the API.
	 * 
	 * @return the formatted JSONObject
	 */
	public JSONObject toJSONObject() {
		JSONObject jsonACL = new JSONObject();

		try {
			
			// all visitors
			jsonACL.put("*", echoACLEntryToJSONObject(all));

			// all logged-in members
			for (Map.Entry<String, ECHOACLEntry> e : allMembers.entrySet()) {
				if(e.getValue() == null) continue;
				String memberInstanceId = e.getKey();
				
				JSONObject ACLsForAmemberInstance = jsonACL.optJSONObject(memberInstanceId);
				if(ACLsForAmemberInstance == null) {
					ACLsForAmemberInstance = new JSONObject();
					jsonACL.put(memberInstanceId, ACLsForAmemberInstance);
				}
				ACLsForAmemberInstance.put("*", echoACLEntryToJSONObject(e.getValue()));
			}
			
			// specific groups
			for (Entry<ECHOMembersGroupObject, ECHOACLEntry> e : specificGroups.entrySet()) {
				if(e.getValue() == null) continue;
				ECHOMembersGroupObject group = e.getKey();
				ECHOACLEntry entry = e.getValue();
				
				JSONObject ACLsForAmemberInstance = jsonACL.optJSONObject(group.instanceId);
				if(ACLsForAmemberInstance == null) {
					ACLsForAmemberInstance = new JSONObject();
					jsonACL.put(group.instanceId, ACLsForAmemberInstance);
				}
				
				JSONObject ACLForAgroup = ACLsForAmemberInstance.optJSONObject("groups");
				if(ACLForAgroup == null) {
					ACLForAgroup = new JSONObject();
					ACLsForAmemberInstance.put("groups", ACLForAgroup);
				}
				
				ACLForAgroup.put(group.refid, echoACLEntryToJSONObject(entry));
			}
			
			// specific members
			for (Entry<ECHOMemberObject, ECHOACLEntry> e : specificMembers.entrySet()) {
				if(e.getValue() == null) continue;
				ECHOMemberObject member = e.getKey();
				ECHOACLEntry entry = e.getValue();
				
				JSONObject ACLsForAmemberInstance = jsonACL.optJSONObject(member.instanceId);
				if(ACLsForAmemberInstance == null) {
					ACLsForAmemberInstance = new JSONObject();
					jsonACL.put(member.instanceId, ACLsForAmemberInstance);
				}
				
				JSONObject ACLForAgroup = ACLsForAmemberInstance.optJSONObject("members");
				if(ACLForAgroup == null) {
					ACLForAgroup = new JSONObject();
					ACLsForAmemberInstance.put("members", ACLForAgroup);
				}
				
				ACLForAgroup.put(member.refid, echoACLEntryToJSONObject(entry));
			}

		} catch (JSONException e) {
			throw new IllegalStateException(e);
		}

		return jsonACL;
	}

	
	/**
	 * Encodes this object as a compact JSON string.
	 *
	 * @return the formatted JSON string
	 */
	public String toString() {
		return toJSONObject().toString();
	}


	/**
	 * Copies data from a JSONObject.
	 *
	 * @param aclObj the source JSONObject
	 * @throws ECHOException
	 */
	protected void copyData(JSONObject aclObj) throws ECHOException {
		if(aclObj == null) throw new IllegalArgumentException("argument `aclObj` must not be null.");

		Iterator<String> iter = aclObj.keys();
		while (iter.hasNext()) {
			String key = iter.next();
			JSONObject val = aclObj.optJSONObject(key);
			if(val == null) throw new ECHOException(0, "Invalid data type for aclObj-field `" + key + "`.");
			
			if (key.equals("*")) { // for all visitors
				putEntryForAll(jsonObjectToECHOACLEntry(val));
				
			} else { // the key is memberInstanceId

				Iterator<String> iter2 = val.keys();
				while (iter2.hasNext()) {
					String key2 = iter2.next();
					JSONObject val2 = val.optJSONObject(key2);
					if(val2 == null) throw new ECHOException(0, "Invalid data type for aclObj-field `" + key + "`.");
						
					if(key2.equals("*")) { // for all members
						putEntryForAllMembers(key, jsonObjectToECHOACLEntry(val2));
					}else { // for a specified member or group
						Iterator<String> iter3 = val2.keys();
						while (iter3.hasNext()) {
							String key3 = iter3.next();
							JSONObject val3 = val2.optJSONObject(key3);
							if(val3 == null) throw new ECHOException(0, "Invalid data type for aclObj-field `" + key + "`.");

							if(key2.equals("members")) {
								putEntryForSpecificMember(new ECHOMemberObject(key, key3), jsonObjectToECHOACLEntry(val3));
							}else if(key2.equals("groups")) {
								putEntryForSpecificGroup(new ECHOMembersGroupObject(key, key3), jsonObjectToECHOACLEntry(val3));
							}
						}
					}
				}
			}
		}
	}

	
	/**
	 * Convert from an ECHOACLEntry to a JSONObject
	 * 
	 * @param acl the source ECHOACLEntry
	 * @return JSONObject
	 */
	private JSONObject echoACLEntryToJSONObject(ECHOACLEntry acl) {
		if(acl == null) return null;
			
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("get", acl.get);
			jsonObj.put("list", acl.list);
			jsonObj.put("edit", acl.edit);
			jsonObj.put("delete", acl.delete);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return jsonObj;
	}

	
	/**
	 * Convert from a JSONObject to an ECHOACLEntry
	 * 
	 * @param jsonObj the source JSONObject
	 * @return ECHOACLEntry
	 */
	private ECHOACLEntry jsonObjectToECHOACLEntry(JSONObject jsonObj) {
		boolean get = jsonObj.optBoolean("get");
		boolean list = jsonObj.optBoolean("list");
		boolean edit = jsonObj.optBoolean("edit");
		boolean delete = jsonObj.optBoolean("delete");
		
		return new ECHOACLEntry(get, list, edit, delete);
	}
}
