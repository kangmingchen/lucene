package com.x.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * <p>
 * Index
 * </p>
 * <p>
 * Description:索引管理
 * </p>
 * 
 * @author chenkangming
 * @date 2014年3月19日
 * @version 1.0
 **/
public class Index {
	/**
	 * 删除索引
	 */
	public void deleteIndex() throws IOException {
		// 目录
		Directory directory = FSDirectory.open(new File("D:" + File.separator + "lucene" + File.separator + "indexDir" + File.separator));
		// 分词器
		Analyzer analyzer = new IKAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_47, analyzer);
		IndexWriter writer = new IndexWriter(directory, iwc);
		try {
			Article info = new Article();
			info.setId(2);
			writer.deleteDocuments(new Term("id", String.valueOf(info.getId())));
		} finally {
			writer.close();
		}
	}

	/**
	 * 更新索引
	 */
	public void updateIndex() throws IOException {
		// 目录
		Directory directory = FSDirectory.open(new File("D:" + File.separator + "lucene" + File.separator + "indexDir" + File.separator));
		// 分词器
		Analyzer analyzer = new IKAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_47, analyzer);
		IndexWriter writer = new IndexWriter(directory, iwc);
		try {
			Document doc = new Document();

			Article info = new Article();
			info.setId(2);
			info.setTitle("全文索引");
			info.setContent("我们主要是做站内搜索（或叫系统内搜索），即对一个系统内的资源进行搜索。如BBS、BLOG中的文章搜索，网上商店中的商品搜索等。");

			writer.updateDocument(new Term("id", info.getId() + ""), doc);
		} finally {
			writer.close();
		}
	}

	/**
	 * 创建索引
	 * 
	 * @throws IOException
	 */
	public void createIndex() throws IOException {

		List<Article> list = new ArrayList<Article>();
		// 模拟一条文章数据
		Article info = new Article();
		info.setId(2);
		info.setTitle("全文索引");
		info.setContent("我们主要是做站内搜索（或叫系统内搜索），即对一个系统内的资源进行搜索。如BBS、BLOG中的文章搜索，网上商店中的商品搜索等。");
		list.add(info);

		info = new Article();
		info.setId(3);
		info.setTitle("你好，世界！");
		info.setContent("IK Analyzer是一个结合词典分词和文法分词的中文分词开源工具包。它使用了全新的正向迭代最细粒度切分算法。");
		list.add(info);

		// 目录
		Directory directory = FSDirectory.open(new File("D:" + File.separator + "lucene" + File.separator + "indexDir" + File.separator));
		// 分词器
		Analyzer analyzer = new IKAnalyzer();

		// 建立索引
		for (Article info1 : list) {
			Document doc = new Document();
			doc.add(new LongField("id", info1.getId(), Store.YES));
			doc.add(new StringField("title", info1.getTitle(), Store.YES));
			doc.add(new TextField("content", info1.getContent(), Store.YES));
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_47, analyzer);
			IndexWriter writer = new IndexWriter(directory, iwc);
			try {
				writer.addDocument(doc);
				writer.commit();
			} finally {
				writer.close();
			}
		}
	}
}
