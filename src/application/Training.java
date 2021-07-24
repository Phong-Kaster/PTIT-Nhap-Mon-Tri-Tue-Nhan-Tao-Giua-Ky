package application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.commons.io.FileUtils;

public class Training {
	static String spamEmailFolder = "database/spam";
	static String nonSpamEmailFolder = "database/nonspam";
	static File outcome = new File("database/outcome/outcome.dat");
	
	
	
	/**********************************************************************
	 * @spamKeywordCollection & @nonspamKeywordCollection la mang chua cac
	 * tu khoa spam & non-spam duoc trich xuat tu du lieu mau
	 * 2 ArrayList nay co dang [ [collection] , [collection] ,....]
	 * 1 collection = [ khong , co ]
	 * Ex: @spamKeyCollection = [ [khong ,co ,gi],[quy,gia,hon],[doc,lap] ]
	 **********************************************************************/
	static ArrayList<Set<String>> spamKeywordCollection = new ArrayList<>();
	static ArrayList<Set<String>> nonspamKeywordCollection = new ArrayList<>();
	
	
	
	/*************************************************
	 * @param sentence la 1 cau hoan chinh
	 * @input : "I will be back"
	 * @return collection = [ I,will,be,back ]
	 *************************************************/
	public static Set<String> seperateSentenceToWords(String sentence)
	{
		HashSet<String> collection = new HashSet<>();
		StringTokenizer brokenSentence = new StringTokenizer(sentence," ,.!*\"\'()");
		
		
		while( brokenSentence.hasMoreTokens() )
		{
			collection.add(brokenSentence.nextToken());
		}
		
		return collection;
	}
	
	
	
	/*************************************************
	 * @param folderPath la duong dan cua folder can doc noi dung
	 * @param KeywordCollection la @spamKeywordCollection or @nonspamKeywordCollection
	 * @throws IOException
	 * nap cac keyword tu cac email trong database huan luyen vao @KeywordCollection
	 *************************************************/
	public static void collectKeywords(String folderPath , ArrayList<Set<String>> KeywordCollection) throws IOException
	{
		File folder = new File(folderPath);
		File[] filenames = folder.listFiles();
		
		for(File element : filenames)
		{
			String elementContent = FileUtils.readFileToString(element, "UTF-16");
			Set<String> collection = seperateSentenceToWords(elementContent);
			
			KeywordCollection.add(collection);
		}
	}
	
	public static void main(String[] args) throws IOException {
		/*1.Nap keyword tu cac email trong database*/
		System.out.println("Begin training");
		collectKeywords(spamEmailFolder , spamKeywordCollection);
		collectKeywords(nonSpamEmailFolder , nonspamKeywordCollection);
		
		/*2.Luu 2 KeywordCollection nay vao file @outcome 
		 * de chay Execution khong can load lai*/
		ObjectOutputStream out = new ObjectOutputStream(
				new FileOutputStream(outcome));
		out.writeObject(spamKeywordCollection);
		out.writeObject(nonspamKeywordCollection);
		out.close();

		System.out.println("Finish training");
		
	}
	
}