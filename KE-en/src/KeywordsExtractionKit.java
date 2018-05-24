import java.io.File;  
import java.io.InputStreamReader;
import java.io.BufferedReader;  
import java.io.FileInputStream;  
import java.util.*;

import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;

public class KeywordsExtractionKit {
	Set<String> stopWords = new HashSet<String>();
	
	void loadStopWords(String path) {
		try {
			File myfile = new File(path);
			InputStreamReader reader = new InputStreamReader
					(new FileInputStream(myfile),"gbk");
			BufferedReader br = new BufferedReader(reader);
			String temp = new String();
			temp = br.readLine();
			while(temp!=null) {
				stopWords.add(temp);
				temp = br.readLine();
			}
		}
		catch(Exception e) {
			System.out.println(e.toString());
			System.exit(0);
		}
		
	}
	
	public  StringBuffer readFromTxt(String path) {
		try {
			File myfile = new File(path);
			//输入流对象的reader
			InputStreamReader reader = new InputStreamReader
					(new FileInputStream(myfile),"gbk");
			//将文件内容转化为计算机可识别的内容
			BufferedReader br = new BufferedReader(reader);
			StringBuffer content=new StringBuffer();
			String temp = new String();
			temp = br.readLine();
			while(temp!=null) {
				content.append(temp);
				temp=br.readLine();
			}
			return content;
		}
		catch(Exception e) {
			System.out.println(e.toString());
			return new StringBuffer("File not exists.");
		}

	}	
	
	public String[] contentSplit(StringBuffer content) {
		String cont = content.toString();
		//将连续的多个非字母符号转化为单个','
		String regex0 = "[\\s\\p{Punct}]{2,}";
		cont = cont.replaceAll(regex0, ",");
		//System.out.println(cont);
		//将文本内容分离成一个个单词
		String regex = "[\\s,.\"?!]";
		String[] result = cont.split(regex);
		return result;
	}
	
	public String[] chineseContentSplit(StringBuffer content) {
		String text = content.toString();
		List<String> result = new ArrayList<>();
		//System.out.println(text);
		StanfordCoreNLP pipeline = new StanfordCoreNLP("StanfordCoreNLP-chinese.properties");
			
        Annotation doc = new Annotation(text);
        pipeline.annotate(doc);
        
        //根据标点符号，进行句子的切分，每一个句子被转化为一个CoreMap的数据结构，保存了句子的信息()
        List<CoreMap> sentences = doc.get(CoreAnnotations.SentencesAnnotation.class);
        
        //从CoreMap 中取出CoreLabel List ,打印
        for (CoreMap sentence : sentences){
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)){
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                result.add(word);
            }
        }
        String[] str=result.toArray(new String[result.size()]);
        return str;
	}
	
	private class ValueComparator implements Comparator<Map.Entry<String, Integer>>{
		public int compare(Map.Entry<String, Integer> m,Map.Entry<String, Integer> n) {
			return n.getValue()-m.getValue();
		}
	}
	
	public void topK(String[] words,int k,boolean stopwords) {
		 /* k:找到前k个高频词
		  * stopwords:是否去除停用词
		  */
		Map<String,Integer> cnt_words = new HashMap<String,Integer>();
		for(int i=0;i<words.length;i++) {
			if(stopwords&&stopWords.contains(words[i]))
				continue;
			if(cnt_words.containsKey(words[i])) {
				cnt_words.put(words[i],cnt_words.get(words[i])+1);
			}
			else {
				cnt_words.put(words[i], 1);
			}
		}
		
		List<Map.Entry<String, Integer>> list = new ArrayList<>();
		list.addAll(cnt_words.entrySet());
		ValueComparator vc = new ValueComparator();
		Collections.sort(list,vc);
		Iterator<Map.Entry<String,Integer>> it=list.iterator();
		for(int i=0;i<k;i++) {
			System.out.println(it.next());
		}
	}
	
	public void KE(String filepath,int k,boolean usestopwords) {
		StringBuffer cont = readFromTxt(filepath);
		String[] cont2 = contentSplit(cont);
		loadStopWords("stop_words.txt");
		topK(cont2,k,usestopwords);
	}
	
	public void KE_cn(String filepath,int k,boolean usestopwords) {
		StringBuffer cont = readFromTxt(filepath);
		String[] cont2 = chineseContentSplit(cont);
		loadStopWords("cn_stop_words.txt");
		topK(cont2,k,usestopwords);
	}
	
}
