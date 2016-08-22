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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.JSONException;
import org.json.JSONObject;



/**
 * An ECHOFile is the file object.
 */
public class ECHOFile {
	
	private byte[] bytes = null;
	private String fileName = null;
	private String urlPath = null;

	
	/* Begin constructors */

	/**
	 * {@.en Constructs a new ECHOFile with byte array.}
	 * {@.ja バイト配列から新しいファイルオブジェクトを生成します。}
	 */
	public ECHOFile(String name, byte[] bytes) {
		this.fileName = name;
		this.bytes = bytes;
	}
	
	/**
	 * {@.en Constructs a new ECHOFile with a JSONObject got from the server.}
	 * {@.ja サーバーから取得したJSONObjectからファイルオブジェクトを生成します。}
	 */
	public ECHOFile(JSONObject obj) {
		this.fileName = obj.optString("name");
		this.urlPath = obj.optString("url_path");
		
		String bytes = obj.optString("bytes");
		if (!bytes.isEmpty()) {
			this.bytes = bytes.getBytes();
		}
	}

	/* End constructors */

	
	/**
	 * {@.en Gets a file name.}
	 * {@.ja ファイル名を取得します。}
	 */
	public String getFileName() {
		return this.fileName;
	}

	/**
	 * {@.en Gets a remote file data.}
	 * {@.ja サーバー上のリモートファイルデータを取得します。}
	 * @throws ECHOException 
	 */
	public byte[] getRemoteBytes() throws ECHOException {
		
		// Get ready a background thread
	    ExecutorService executor = Executors.newSingleThreadExecutor();
	    Callable<byte[]> communicator = new Callable<byte[]>() {
	    	  @Override
	    	  public byte[] call() throws Exception {
	    		  InputStream is = getRemoteInputStream();
	    		  
	    		  int nRead;
	    		  byte[] data = new byte[16384];
	    		  ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	    		  while ((nRead = is.read(data, 0, data.length)) != -1) {
	    		    buffer.write(data, 0, nRead);
	    		  }
	    		  buffer.flush();

	    		  return buffer.toByteArray();
	    	  }
		};

	    Future<byte[]> future = executor.submit(communicator);
	    
	    try {
	    	return future.get();
	    } catch (InterruptedException e) {
	    	Thread.currentThread().interrupt(); // ignore/reset
	    } catch (ExecutionException e) {
	    	Throwable e2 = e.getCause();
	    	throw new ECHOException(e2);
		}
	    
		return null;
	}
	
	/**
	 * Gets an InputStream to get remote bytes.
	 * @return InputStream
	 * @throws ECHOException
	 */
	public InputStream getRemoteInputStream() throws ECHOException {
		
		final String urlPath = this.urlPath;
		if(urlPath == null) return null;
		
		// final String urlPath = this.urlPath;
		if(urlPath.equals("")) throw new ECHOException(0, "File URL not setted.");
		
		return ECHOQuery.requestRaw(urlPath.substring(1), "GET", null, false);
	}
	
	/**
	 * {@.en Gets a local file data.}
	 * {@.ja セットしたローカルファイルデータを取得します。}
	 * @throws ECHOException 
	 */
	public byte[] getLocalBytes() {
		if(this.bytes == null) return null;
		return this.bytes;
	}

	
	/**
	 * Encodes this object as a compact JSON string.
	 *
	 * @return the formatted JSON string
	 */
	public String toString() {
		JSONObject json = new JSONObject();
		try {
			if(!fileName.isEmpty()) json.put("name", fileName);
			if(!urlPath.isEmpty()) json.put("url_path", urlPath);
			if(bytes != null) json.put("bytes", bytes.toString());
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return json.toString();
	}
}



