package com.kancy.tester.domain;

import lombok.Data;

import java.util.Objects;

/**
 * CardBin
 *
 * @author huangchengkang
 * @date 2021/8/27 13:32
 */
@Data
public class CardBin{
    private String id;
    private String cardType;
    private Integer cardLength;
    private String cardName;
    private String bankName;
    private String bankAbbr;

    public String showCardName(){
        if (Objects.isNull(cardName) || Objects.equals("--", cardName)){
            return cardType;
        }
        return cardName;
    }
    public String showCardType(){
        if (Objects.isNull(cardType)){
            return "";
        }
        switch (cardType){
            case "储蓄卡" : return "DEBIT CARD";
            case "信用卡" : return "CREDIT CARD";
            case "准贷记卡" : return "QUASI CREDIT CARD";
            case "预付费卡" : return "PREPAID CARD";
        }
        return "";
    }
}
