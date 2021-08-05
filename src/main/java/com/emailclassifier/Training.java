/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.emailclassifier;
import com.emailclassifier.Form.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.commons.io.FileUtils;
/**
*
* @author Nguyễn Đăng Hậu - N18DCCN060
* @author Nguyễn Thành Phong - N18DCCN147
*/

public class Training {
    public static File dataSheetSpam;
    public static File dataSheetNoneSpam;
    public static File result_training;

    /**********************************************************************
     * @spamKeywordCollection & @nonspamKeywordCollection la mang chua cac
     * tu khoa spam & non-spam duoc trich xuat tu du lieu mau
     * 2 ArrayList nay co dang [ [collection] , [collection] ,....]
     * 1 collection = [ khong , co ]
     * Ex: @spamKeyCollection = [ [khong,co,gi],[quy,gia,hon],[doc,lap] ]
    **********************************************************************/
    static ArrayList<Set<String>> spamKeywordCollection = new ArrayList<>();
    static ArrayList<Set<String>> nonspamKeywordCollection = new ArrayList<>();

    //Khai báo cấu trúc cho ngày giờ hiện thị
    static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.S");


    /*************************************************
     * Phân tách các từ trong 1 câu.
     * @param sentence la 1 cau hoan chinh
     * input : "I will be back"
     * @return collection = [ I,will,be,back ]
    *************************************************/
    public static Set<String> seperateSentenceToWords(String sentence)
    {
        HashSet<String> collection = new HashSet<>();
        StringTokenizer brokenSentence = new StringTokenizer(sentence," ,.!*\"\'()\n");
        while( brokenSentence.hasMoreTokens() )
        {
            collection.add(brokenSentence.nextToken());
        }
        return collection;
    }



    /*************************************************
     * @param folderPath là đường dẫn của folder chứa file cần đọc nội dung
     * @param KeywordCollection la @spamKeywordCollection or @nonspamKeywordCollection
     * @throws IOException
     * nap cac keyword tu cac email trong database huan luyen vao @KeywordCollection
    *************************************************/
    public static void collectKeywords(File folderPath , ArrayList<Set<String>> KeywordCollection) throws IOException
    {
        // lấy toàn bộ file có trong folder
        File[] filenames = folderPath.listFiles();
        for(File element : filenames)
        {
            // lặp từng file và đọc nội dung file
            String elementContent = FileUtils.readFileToString(element, "UTF-16");
            // lấy từng chữ cho vào collection trong nội dung file
            Set<String> collection = seperateSentenceToWords(elementContent);

            // thêm collection tìm được vào danh sách tổng
            KeywordCollection.add(collection);
        }
    }
    
    // Hàm chính để run chương trình
    public static void run(Console console) throws IOException {
        spamKeywordCollection.clear();
        nonspamKeywordCollection.clear();
        /*1.Nap keyword tu cac email trong các file nội dung cho trước*/
        console.printOnConsole("\n" + formatter.format(new Date()) + "\t Begin training spam text....");
        collectKeywords(dataSheetSpam , spamKeywordCollection);
        console.printOnConsole("\n" + formatter.format(new Date()) + "\t Finish training spam text!");


        console.printOnConsole("\n" + formatter.format(new Date()) + "\t Begin training none spam text....");
        collectKeywords(dataSheetNoneSpam , nonspamKeywordCollection);
        console.printOnConsole("\n" + formatter.format(new Date()) + "\t Finish training none spam text!");

        /*2.Luu 2 KeywordCollection nay vao file @result_training 
        * de chay Execution khong can load lai*/
        console.printOnConsole("\n" + formatter.format(new Date()) + "\t Begin save result...");
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(result_training));
        console.printOnConsole("\n" + formatter.format(new Date()) + "\t Saveing spam keyword to result_trainng.dat...");
        out.writeObject(spamKeywordCollection);

        console.printOnConsole("\n" + formatter.format(new Date()) + "\t Saving none spam keyword to result_trainng.dat...");
        out.writeObject(nonspamKeywordCollection);

        out.close();
        console.printOnConsole("\nFinish training!");

    }

}