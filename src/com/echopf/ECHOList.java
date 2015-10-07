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

import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * {@.en Composes a list with paginate data.}
 * {@.ja ページ送り情報が付属したリストデータを構成します。}
 */
public class ECHOList<S extends ECHODataObject<S>> extends ArrayList<S> {

	private static final long serialVersionUID = 1L;

	private int page;
	private int prevPage;
	private int nextPage;
	private int pageCount;
	private int count;
	private int limit;
	private String order;
	private boolean asc;

	/**
	 * Constructs a new ECHOList.
	 * 
	 * @param data a copying paginate data by JSONObject.
	 * @throws ECHOException 
	 */
	protected ECHOList(JSONObject data) throws ECHOException {
		super();
		copyPaginateData(data);
	}
	
	/**
	 * {@.ja 現在のページ番号を返します。}
	 * {@.en Gets the current page no.}
	 */
	public int getPage() {
		return this.page;
	}

	/**
	 * {@.ja 前のページ番号を返します。}
	 * {@.en Gets the previous page no.}
	 */
	public int getPrevPage() {
		return this.prevPage;
	}

	/**
	 * {@.ja 次のページ番号を返します。}
	 * {@.en Gets the next page no.}
	 */
	public int getNextPage() {
		return this.nextPage;
	}

	/**
	 * {@.ja ページの総数を返します。}
	 * {@.en Gets the total number of pages.}
	 */
	public int getPageCount() {
		return this.pageCount;
	}

	/**
	 * {@.ja 見つかった要素の総数を返します。}
	 * {@.en Gets the total number of found objects.}
	 */
	public int getCount() {
		return this.count;
	}

	/**
	 * {@.ja 1ページあたりの要素数を返します。}
	 * {@.en Gets the number of objects per a page.}
	 */
	public int getLimit() {
		return this.limit;
	}

	/**
	 * {@.ja ソートの基準となるフィールド名を返します。}
	 * {@.en Gets the field name of sorting criteria.}
	 */
	public String getOrder() {
		return this.order;
	}

	/**
	 * {@.ja ソート順が昇順かそうでないかを返します。}
	 * {@.en Gets whether to sort in ascending order.}
	 */
	public boolean isAsc() {
		return this.asc;
	}
	

	/**
	 * Copies paginate data.
	 * @throws ECHOException 
	 */
	protected void copyPaginateData(JSONObject data) throws ECHOException {
		
		try {
			
			this.page = data.getInt("page");
			this.prevPage = data.optInt("prevPage");
			this.nextPage = data.optInt("nextPage");
			this.pageCount = data.getInt("pageCount");
			this.count = data.getInt("count");
			this.limit = data.getInt("limit");
			this.order = data.getString("order");
			this.asc = data.getBoolean("asc");

		} catch (JSONException e) {
			throw new ECHOException(0, "The copying data is not acceptable.");
		}
	}
}
