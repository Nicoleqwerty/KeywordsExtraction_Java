import java.util.*;

public class Test {
	public static void main(String args[]) {
		KeywordsExtractionKit kit = new KeywordsExtractionKit();
		//kit.KE("cnn_news.txt", 10, true);
		/*StringBuffer content=kit.readFromTxt("中文新闻.txt");
		String[] words=kit.chineseContentSplit(content);
		kit.topK(words, 10, false);*/
		kit.KE_cn("中文新闻.txt", 10, true);
	}
}
