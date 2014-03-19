package com.x.lucene;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * <p>
 * Search
 * </p>
 * <p>
 * Description:搜索
 * </p>
 * 
 * @author chenkangming
 * @date 2014年3月19日
 * @version 1.0
 **/
public class Searcher {
	private final static Formatter highlighter_formatter = new SimpleHTMLFormatter("<span class=\"highlight\">", "</span>");

	/**
	 * 搜索
	 * 
	 * @throws ParseException
	 * @throws IOException
	 * @throws org.apache.lucene.queryparser.classic.ParseException
	 */
	public void search() throws ParseException, IOException, org.apache.lucene.queryparser.classic.ParseException {
		// 目录
		Directory directory = FSDirectory.open(new File("D:" + File.separator + "lucene" + File.separator + "indexDir" + File.separator));
		// 分词器
		Analyzer analyzer = new IKAnalyzer();

		String queryString = "算法";
		// 第二个参数指定是在哪个属性中搜索
		QueryParser queryParser = new QueryParser(Version.LUCENE_47, "content", analyzer);
		queryParser.setDefaultOperator(QueryParser.AND_OPERATOR);
		Query query = queryParser.parse(queryString);

		// 进行搜索 得到结果
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);

		TopDocs topDocs = searcher.search(query, 100);
		System.out.println("总记录数：" + topDocs.totalHits);
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;// 结果的数组

		// 处理结果
		List<Article> list = new ArrayList<Article>();
		for (int i = 0, size = scoreDocs.length; i < size; i++) {
			ScoreDoc scoreDoc = scoreDocs[i];
			Document doc = searcher.doc(scoreDoc.doc);

			Article info = new Article();
			info.setId(Integer.parseInt(doc.get("id")));
			info.setTitle(highlight(doc.get("title"), queryString));
			info.setContent(highlight(doc.get("content"), queryString));

			list.add(info);
		}

		for (Article a : list) {
			System.out.println("id >>>> " + a.getId());
			System.out.println("title >>> " + a.getTitle());
			System.out.println("content >>> " + a.getContent());
		}

	}

	/**
	 * 对一段文本执行语法高亮处理
	 * 
	 * @param text
	 *            要处理高亮的文本
	 * @param key
	 *            高亮的关键字
	 * @return 返回格式化后的html文本
	 */
	private static String highlight(String text, String key) {
		if (StringUtils.isBlank(key) || StringUtils.isBlank(text)) {
			return text;
		}

		Analyzer analyzer = new IKAnalyzer();

		String result = null;
		try {
			key = QueryParser.escape(key.trim().toLowerCase());
			QueryScorer scorer = new QueryScorer(new TermQuery(new Term(null, QueryParser.escape(key))));
			Highlighter hig = new Highlighter(highlighter_formatter, scorer);
			TokenStream tokens = analyzer.tokenStream(null, new StringReader(text));
			result = hig.getBestFragment(tokens, text);
		} catch (Exception e) {
			System.out.println(e);
		}
		return (result != null) ? result : text;
	}

}
