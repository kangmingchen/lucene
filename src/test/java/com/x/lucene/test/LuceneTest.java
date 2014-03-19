package com.x.lucene.test;

import com.x.lucene.Index;
import com.x.lucene.Searcher;


/**
 * <p>LuceneTest</p>
 * <p>Description:</p>
 * @author chenkangming
 * @date 2014年3月19日
 * @version 1.0
 **/
public class LuceneTest {
	public static void main(String[] args) throws Exception {
		//创建索引
		new Index().createIndex();
		//搜索
		new Searcher().search();
	}
}
