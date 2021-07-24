package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class Execution {
	/**********************************************************************
	 * @spamKeywordCollection & @nonspamKeywordCollection la mang chua cac
	 * tu khoa spam & non-spam duoc trich xuat tu du lieu mau
	 * 2 ArrayList nay co dang [ [collection] , [collection] ,....]
	 * 1 collection = [ khong , co , quy , gia , hon , doc , lap , tu ,do]
	 * @outcome la file ket qua thu duoc tu huan luyen
	 **********************************************************************/
	static ArrayList<Set<String>> spamKeywordCollection = new ArrayList<>();
	static ArrayList<Set<String>> nonspamKeywordCollection = new ArrayList<>();
	static File outcome = new File("database/outcome/outcome.dat");
	
	
	
	/**********************************************************************
	 * @param x la chu cai duoc kiem tra tan suat voi ArrayList
	 * @param KeywordCollection la ArrayList chua tu khoa Spam hoac Non-Spam
	 * @return tan suat xuat hien cua x trong ArrayList la bao nhieu 
	 **********************************************************************/
	public static double percentWordAppear(String x , ArrayList<Set<String>> KeywordCollection ) 
	{
		double probabilityResult;
		double appealNumber = 0;
		
		for(int i = 0 ; i < KeywordCollection.size() ; i++)
		{
			if( KeywordCollection.get(i).contains(x) )
			{
				appealNumber++;
			}
		}
		
		probabilityResult = (appealNumber + 1) / (KeywordCollection.size() + 1);
		return probabilityResult;
	}
	
	
	
	/**********************************************************************
	 * Doc noi dung Email va phan tach noi dung
	 * thanh 1 mang chua cac tu trong email & cac chu khong trung nhau
	 * @param emailPath
	 * @return [ doc , lap , tu , do , hanh , phuc ]
	 * @throws IOException
	 **********************************************************************/
	public static Set<String> extractContentFromEmail(String emailPath) throws IOException
	{
		File targetEmail = new File(emailPath);
		String contentTargetEmail = FileUtils.readFileToString(targetEmail, "UTF-16");
		Set<String> wordsFromContent = Training.seperateSentenceToWords(contentTargetEmail);
		return wordsFromContent;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		/* 1.Nap file du lieu thu thap tu huan luyen*/
		ObjectInputStream inputStream = new ObjectInputStream(
				new FileInputStream(outcome));
		spamKeywordCollection = (ArrayList<Set<String>>) inputStream.readObject();
		nonspamKeywordCollection = (ArrayList<Set<String>>) inputStream.readObject();
		inputStream.close();
		
		
		
		/* 2.Nap file Email can kiem tra*/
		Set<String> wordsFromContent = extractContentFromEmail("database/test/test (12).txt");
		
		
		
		/* 3.Khai bao spamRate & nonSpamRate de so sanh ti le Spam*/
		double spamRate=(double)(spamKeywordCollection.size())/(spamKeywordCollection.size()+nonspamKeywordCollection.size());
		double nonSpamRate=(double)(nonspamKeywordCollection.size())/(spamKeywordCollection.size()+nonspamKeywordCollection.size());
		
		
		
		/* 4.Su dung thuat toan Naive Bayes de phan tinh tan xuat cac tu xuat hien*/
		ArrayList<String> words = new ArrayList<>(wordsFromContent);
		/* spamPercent & nonspamPercent cho biet ti le % 1 tu la spam(nonspam)*/
		double spamPercent = (double)(1/(spamKeywordCollection.size() + 1));
		double nonspamPercent = (double)(1/(nonspamKeywordCollection.size()+1));
		System.out.println("		Word		Spam(%) 	Non-spam(%)");
		/*Duyet mang tu vung de tinh xac xuat*/
		for(String element: words)
		{
			/*2 bien ben duoi tinh xac suat chu cai spam(nonspam)*/
			double spamWordPercent = percentWordAppear(element,spamKeywordCollection);
			double nonspamWordPercent = percentWordAppear(element,nonspamKeywordCollection);
			
			
			/*2 bien ben duoi de hien trong console cho dep ^^.Ex : 0.12321->12%*/
			double parseSpamPercent = Double.parseDouble(new DecimalFormat("##.##").format(spamWordPercent))*100;
			double parseNonspamPercent = Double.parseDouble(new DecimalFormat("##.##").format(nonspamWordPercent))*100;
			
			
			/*Kiem tra 1 tu vung.Neu la khong bang tan suat hien tai->cap nhat 2 Rate len*/
			if( spamWordPercent != spamPercent || nonspamWordPercent != nonspamPercent )
			{
				System.out.println("		"+element+"		"+parseSpamPercent+"		"+parseNonspamPercent);
				spamRate *= spamWordPercent;
				nonSpamRate *= nonspamWordPercent; 
			}
		}
		
		
		/* 5.So sanh spamRate & nonSpamRate->Ket luan */
		System.out.println("Conclusion");
		System.out.println("Spam rate : " + spamRate);
		System.out.println("Nonspam rate : " + nonSpamRate);
		if(spamRate > nonSpamRate)
		{
			System.out.println("This is spam Email");
		}
		else
		{
			System.out.println("This is non-spam Email");
		}
	}
}
