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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.net.ssl.HttpsURLConnection;

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
		
		if(this.urlPath == null) return null;
		
		final String urlPath = this.urlPath;
		if(urlPath.equals("")) throw new ECHOException(0, "File URL not setted.");
		final String secureDomain = ECHO.secureDomain;
		if(secureDomain == null) throw new IllegalStateException("The SDK is not initialized.　Please call `ECHO.initialize()`.");

		String url = new StringBuilder("https://").append(secureDomain).append(urlPath).toString();

		HttpsURLConnection httpClient = null;

		try {
			URL urlConn = new URL(url);
			httpClient = (HttpsURLConnection) urlConn.openConnection();
		} catch (IOException e) {
			throw new ECHOException(e);
		}
		
		final HttpsURLConnection fHttpClient = httpClient;
		
		// Get ready a background thread
	    ExecutorService executor = Executors.newSingleThreadExecutor();
	    Callable<byte[]> communicator = new Callable<byte[]>() {
	    	  @Override
	    	  public byte[] call() throws Exception {
	    		  InputStream is = ECHOQuery.requestRaw(fHttpClient, "GET", null, false);
	    		  
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
		} finally {
			if(httpClient != null) httpClient.disconnect();
		}
	    
		return null;
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
}



