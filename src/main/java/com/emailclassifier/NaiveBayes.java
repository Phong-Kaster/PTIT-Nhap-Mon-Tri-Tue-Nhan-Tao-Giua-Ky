/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.emailclassifier;

import java.util.ArrayList;
import java.util.Set;

/**
 *
 * @author Nguyễn Đăng Hậu - N18DCCN060
 * @author Nguyễn Thành Phong - N18DCCN147
 */
public class NaiveBayes {
    private final int totalCount;
    private final int spamCount;
    private final int nonSpamCount;
    private double spamRate;
    private double nonSpamRate;
    private final double spamPercent;
    private final double nonspamPercent;

    public NaiveBayes(int spamCount, int nonSpamCount) {
        // số lượng từ Spam và từ không Spam
        this.spamCount = spamCount;
        this.nonSpamCount = nonSpamCount;
        this.totalCount = spamCount + nonSpamCount;
            
        //tần suất(xác suất) của 1 từ spam chưa biết
        this.spamPercent = (double)(1/(spamCount + 1));
        //tần suất(xác suất) của 1 từ none-spam chưa biết
        this.nonspamPercent = (double)(1/(nonSpamCount + 1));
    }

    public double getSpamRate() {
        return this.spamRate;
    }

    public void setSpamRate(double spamRate) {
        this.spamRate = spamRate;
    }

    public double getNonSpamRate() {
        return this.nonSpamRate;
    }

    public void setNonSpamRate(double nonSpamRate) {
        this.nonSpamRate = nonSpamRate;
    }


    public double getSpamPercent() {
        return this.spamPercent;
    }

    public double getNonspamPercent() {
        return this.nonspamPercent;
    }
    
    
   
    /**********************************************************************
      * @param x la chu cai duoc kiem tra tan suat voi ArrayList
      * @param KeywordCollection la ArrayList chua tu khoa Spam hoac Non-Spam (là danh sách đã có từ file training)
      * @return tan suat xuat hien cua x trong ArrayList la bao nhieu 
      **********************************************************************/
    public double percentWordAppear(String x , ArrayList<Set<String>> KeywordCollection ) 
    {
        // số lần xuất hiện là 0
        double appealCount = 0;
        for(int i = 0 ; i < KeywordCollection.size() ; i++)
        {
            if( KeywordCollection.get(i).contains(x) )
            {
                //tăng số lần xuất hiện lên 1
                appealCount++;
            }
        }
        // tính tan suat f
        return (appealCount + 1) / (KeywordCollection.size() + 1);
    }
    
    // Tính xác xuất P(spam)
    public void calculateSpamRate(){
        spamRate = (double)(spamCount)/(totalCount);
    }
    
    // Tính xác xuất P(nonespam)
    public void calculateNonSpamRate(){
        nonSpamRate = (double)(nonSpamCount)/(totalCount);
    }
}
