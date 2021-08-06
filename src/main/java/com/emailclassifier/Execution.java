/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.emailclassifier;

import com.emailclassifier.Form.Console;
import static com.emailclassifier.Training.formatter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
/**
*
* @author Nguyễn Đăng Hậu - N18DCCN060
* @author Nguyễn Thành Phong - N18DCCN147
*/
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
    public static File TrainingResult; // đường dẫn file traing
    public static File folder_test; // đường dân folder chứa email cần test
    public static File path_result_test; // đường dẫn lưu file kết quả
    public static Boolean stop = false; // trạng thái của tiến trình
    public static Boolean isSaveData = false; // Cờ để kiểm tra có lưu lại kết quả tìm được vào file tập huấn hay ko


    /**********************************************************************
      * Đọc nội dung email từ file và tách các từ đưa vào 1 mảng chứa.
      * @param targetEmail đây là file email
      * @return [ doc , lap , tu , do , hanh , phuc ]
      * @throws IOException kết quả lỗi
      **********************************************************************/
    public static Set<String> extractContentFromEmail(File targetEmail) throws IOException
    {
        String contentTargetEmail = FileUtils.readFileToString(targetEmail, "UTF-16");
        Set<String> wordsFromContent = Training.seperateSentenceToWords(contentTargetEmail);
        return wordsFromContent;
    }


    // Hàm chính để run chương trình
    public static boolean run(Console console) throws FileNotFoundException, IOException, ClassNotFoundException {
        stop = false;
        // 1. Nạp dữ liệu từ file traing đã train trước đó
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(TrainingResult));
        spamKeywordCollection = (ArrayList<Set<String>>) inputStream.readObject();
        nonspamKeywordCollection = (ArrayList<Set<String>>) inputStream.readObject();
        inputStream.close();

        // 2.Nạp file Email cần kiểm tra
        File[] filenames = folder_test.listFiles((FileFilter) HiddenFileFilter.VISIBLE);
        for(File oneFile : filenames)
        {
            // 2.1 lặp từng file và đọc nội dung file
            // nếu có trạng thái tạm ngừng thì thoát chương trình
            if(stop) return false;
            
            // 2.2. Khai báo danh sách các từ có trong 1 mail hiện tại
            Set<String> wordsFromContent = extractContentFromEmail(oneFile);
            ArrayList<String> words = new ArrayList<>(wordsFromContent);
            
            // 2.3.Khai bao Lớp NaiveBayes và tính toán các xác suất
            NaiveBayes bayes = new NaiveBayes(spamKeywordCollection.size(), nonspamKeywordCollection.size());
            
            // tính xác xuất đây là email spam - P(spam)
            bayes.calculateSpamRate();
            // tính xác xuất đây là email nonspam - P(nonespam)
            bayes.calculateNonSpamRate(); 
            
            
            console.printOnConsole("\n" + formatter.format(new Date()));
            console.printOnConsole("\n\tWord\t\tSpam(%)\t\tNon-spam(%)\n");
            //2.4. Duyet mang tu vung de tinh xac xuat
            
            for(String word: words)
            {
                // nếu có trạng thái tạm ngừng thì thoát chương trình
                if(stop) return false;
                // 2.4.1 tính tần suất của từ này xuất hiện trong tất cả các từ spam đã training =  P(xi|spam)
                double spamWordPercent = bayes.percentWordAppear(word, spamKeywordCollection);
                
                // 2.4.1 tính tần suất của từ này xuất hiện trong tất cả các từ none spam đã training =  P(xi|nonspam)
                double nonspamWordPercent = bayes.percentWordAppear(word, nonspamKeywordCollection);

                // 2.4.2 Kiem tra 1 tu vung. Neu la khong bang tan suat hien tai -> cap nhat Rate len
                if( spamWordPercent != bayes.getSpamPercent() || nonspamWordPercent != bayes.getNonspamPercent() )
                {
                    String textSpamPercent = String.format("%.2f", spamWordPercent*100),
                           textNonspamPercent = String.format("%.2f", nonspamWordPercent*100);
                   
                    console.printOnConsole("\n\t"+word+"\t\t"+textSpamPercent+"\t\t"+textNonspamPercent);
                    
                    bayes.setSpamRate(bayes.getSpamRate() * (spamWordPercent));
                    bayes.setNonSpamRate(bayes.getNonSpamRate() * (nonspamWordPercent));
                }
                // 2.4.3 quay lại bước 2.4.1 cho đến hết danh sách thoát ra bước 2.5
            }
            
            /// 2.5. So sanh spamRate & nonSpamRate->Ket luan
            console.printOnConsole("\nConclusion");
            console.printOnConsole("\nSpam rate : " + bayes.getSpamRate());
            console.printOnConsole("\nNonspam rate : " +  bayes.getNonSpamRate());

            if(bayes.getSpamRate() > bayes.getNonSpamRate())
            {
                console.printOnConsole("\nThis is spam Email");
                if(path_result_test != null){
                    writeToResult(oneFile.getName() + " - This is spam Email\n");
                }
                if(isSaveData){
                    // 2.6 nhập nội dung mới vào file training
                    spamKeywordCollection.add(wordsFromContent);
                }
            }
            else
            {
                console.printOnConsole("\nThis is non-spam Email");
                if(path_result_test != null){
                    writeToResult(oneFile.getName() + " - This is non-spam Email\n");
                }
                if(isSaveData){
                    // 2.6 nhập nội dung mới vào file training
                    nonspamKeywordCollection.add(wordsFromContent);
                }
            }
            console.printOnConsole("\n--------------------------------------------\n");
            // 2.7 quay lại bước 2.1 cho đến hết khi đó thoát ra bước 3
        }

        if(isSaveData){
            // 3. Cap nhat du lieu moi cho file huan luyen
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(TrainingResult));
            out.writeObject(spamKeywordCollection);
            out.writeObject(nonspamKeywordCollection);
            out.close();
        }
        return true;
    }
    
    private static void writeToResult(String contents){
        File f = new File(path_result_test.getAbsoluteFile() + "/result.txt");
        if(!f.exists()){
            f = new File(path_result_test.getAbsoluteFile(), "/result.txt");
        }
        
        try {
            //PrintWriter out = new PrintWriter("filename.txt");
            FileUtils.writeStringToFile(f, contents, Charset.forName("UTF-8"), true);
        } catch (IOException ex) {
            Logger.getLogger(Execution.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
