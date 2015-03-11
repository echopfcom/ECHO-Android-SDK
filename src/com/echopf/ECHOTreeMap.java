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

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.JSONObject;

import android.os.Handler;


/**
 * An ECHOTreeMap is an abstract recursive tree map in ECHO.
 * Particular tree maps (e.g. {@link com.echopf.contents.ECHOContentsCategoriesMap}, {@link com.echopf.members.ECHOMembersGroupsMap})
 * are implemented based on this class.
 */
public abstract class ECHOTreeMap<T extends ECHODataObject<T> & TreeNodeable<T>, S extends ECHOTreeMap<T,S>> extends ECHOObject {
	
	protected T node;
	protected List<S> children;
	protected boolean is_subtree = false;

	
	/* Begin constructors */

	/**
	 * Constructs a new ECHOTreeMap based on an existing *entire* one on the ECHO server.
	 * @param instanceId the reference ID of the instance to which the tree map has belonged
	 * @param resourceType the type of this object
	 */
	protected ECHOTreeMap(String instanceId, String resourceType) {
		super(instanceId, resourceType);
	}
	
	
	/**
	 * Constructs a new ECHOTreeMap based on an existing *sub* one on the ECHO server.
	 * @param instanceId the reference ID of the instance to which the tree map has belonged
	 * @param resourceType the type of this object
	 * @param refid the reference ID of the root node of the existing sub one
	 */
	protected ECHOTreeMap(String instanceId, String resourceType, String refid) {
		super(instanceId, resourceType, refid);
		is_subtree = true;
	}
	
	
	/**
	 * Constructs a new ECHOTreeMap linking a TreeNodable object
	 *   based on an existing *entire* one on the ECHO server.
	 * @param instanceId the reference ID of the instance to which the tree map has belonged
	 * @param resourceType the type of this object
	 * @param node a linking TreeNodeable object
	 */
	protected ECHOTreeMap(String instanceId, String resourceType, T node) {
		super(instanceId, resourceType);
		this.node = node;
	}

	
	/**
	 * Constructs a new ECHOTreeMap linking a TreeNodable object
	 *   based on an existing *sub* one on the ECHO server.
	 * @param instanceId the reference ID of the instance to which the tree map has belonged
	 * @param resourceType the type of this object
	 * @param refid the reference ID of the root node of the existing sub one
	 * @param node a linking TreeNodeable object
	 */
	protected ECHOTreeMap(String instanceId, String resourceType, String refid, T node) {
		this(instanceId, resourceType, refid);
		this.node = node;
	}

	/* End constructors */

	
	/**
	 * {@.en Gets this node object.}
	 * {@.ja ノードのオブジェクトを取得します。}
	 */
	public T getNode() {
		return this.node;
	}


	/**
	 * {@.en Gets lower sub tree maps belonging to this node.}
	 * {@.ja ノードの配下に存在するサブツリー群を取得します。}
	 */
	public List<S> getChildren() {
		return this.children;
	}
	

	/**
	 * Does Fetch data from the ECHO server in a background thread.
	 * @param sync if set TRUE, then the main (UI) thread is waited for complete the fetching in a background thread. 
	 * 		  (a synchronous communication)
	 * @param callback invoked after the fetching is completed
	 * @throws ECHOException 
	 */
	protected void doFetch(final boolean sync, final FetchCallback<S> callback) throws ECHOException {
		final Handler handler = new Handler();

		// Get ready a background thread
	    ExecutorService executor = Executors.newSingleThreadExecutor();
	    Callable<Object> communictor = new Callable<Object>() {

	    	@Override
	    	public Object call() throws ECHOException {

				ECHOException exception = null;
				JSONObject data = null;
				
				try {
		    	
					synchronized (lock) {
						data = ECHOQuery.getRequest(getRequestURLPath());
						copyData(data);
		    		}
					
				} catch(ECHOException e) {
					exception = e;
				}
				
				
				if(sync == false) {
					
					// Execute a callback method in the main (UI) thread.
					if(callback != null) {
						final ECHOException fException = exception;
						handler.post(new Runnable() {
							@Override @SuppressWarnings("unchecked")
							public void run() { 
								callback.done((S) ECHOTreeMap.this, fException);
							}
						});
					}
					
				}else{

					if(exception != null) throw exception;
				}
	    	
	    		return null;
	    	}
	    };
	    
	    Future<Object> future = executor.submit(communictor);

	    if(sync) {
		    try {
		    	future.get();
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
	}

	
	/**
	 * Copies data from the specified json object.
	 * @throws ECHOException 
	 */
	protected abstract void copyData(JSONObject data) throws ECHOException;
	
}
