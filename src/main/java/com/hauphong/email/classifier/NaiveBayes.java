/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hauphong.email.classifier;

import java.util.ArrayList;
import java.util.Set;

/**
 *
 * @author haunguyendang
 */
public class NaiveBayes {
    private final int totalCount;
    private final int spamCount;
    private final int noneSpamCount;
    private double spamRate;
    private double noneSpamRate;
    private final double spamPercent;
    private final double nonspamPercent;

    public NaiveBayes(int spamCount, int noneSpamCount) {
        // số lượng từ Spam và từ không Spam
        this.spamCount = spamCount;
        this.noneSpamCount = noneSpamCount;
        this.totalCount = spamCount + noneSpamCount;
        
        //tần suất(xác suất) của 1 từ spam chưa biết
        this.spamPercent = (double)(1/(spamCount + 1));
        //tần suất(xác suất) của 1 từ none-spam chưa biết
        this.nonspamPercent = (double)(1/(noneSpamCount + 1));
    }

    public double getSpamRate() {
        return spamRate;
    }

    public void setSpamRate(double spamRate) {
        this.spamRate = spamRate;
    }

    public double getNoneSpamRate() {
        return noneSpamRate;
    }

    public void setNoneSpamRate(double noneSpamRate) {
        this.noneSpamRate = noneSpamRate;
    }


    public double getSpamPercent() {
        return spamPercent;
    }

    public double getNonspamPercent() {
        return nonspamPercent;
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
        this.spamRate = (double)(this.spamCount/this.totalCount);
    }
    
    // Tính xác xuất P(nonespam)
    public void calculateNoneSpamRate(){
        this.noneSpamRate = (double)(this.noneSpamCount/this.totalCount);
    }
}
