package com.kancy.tester.service;

import com.github.kancyframework.springx.utils.RandomUtils;
import com.kancy.tester.data.CardBinData;
import com.kancy.tester.domain.BankCard;
import com.kancy.tester.domain.CardBin;

/**
 * BankCardService
 *
 * @author huangchengkang
 * @date 2021/8/26 19:16
 */
public class BankCardService {

    public BankCard generateDebitCard(){
        CardBin cardBin = CardBinData.randomDebitCardBin();
        BankCard bankCard = generate(cardBin);
        return bankCard;
    }
    public BankCard generateCreditCard(){
        CardBin cardBin = CardBinData.randomCreditCardBin();
        BankCard bankCard = generate(cardBin);
        return bankCard;
    }
    public BankCard generateCard(String indexkey){
        CardBin cardBin = CardBinData.randomCardBin(indexkey);
        BankCard bankCard = generate(cardBin);
        return bankCard;
    }


    private BankCard generate(CardBin cardBin) {
        Integer cardLength = cardBin.getCardLength();
        int cardBinLength = cardBin.getId().length();
        int randomCharLength = cardLength - cardBinLength - 1;
        if (randomCharLength < 1){
            throw new IllegalArgumentException();
        }
        StringBuilder sb = new StringBuilder(cardBin.getId());
        for (int i = 0; i < randomCharLength; i++) {
            sb.append(RandomUtils.nextInt(0, 10));
        }
        sb.append(getBankCardCheckCode(sb.toString()));
        String cardNo = sb.toString();
        BankCard bankCard = new BankCard();
        bankCard.setCardBin(cardBin);
        bankCard.setCardNo(cardNo);
        return bankCard;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     * @param nonCheckCodeCardId
     * @return
     */
    public char getBankCardCheckCode(String nonCheckCodeCardId){
        if(nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for(int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if(j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');  //这边+'0'，不是拼接，在Java和C#中是8+0的ASCII码得到8在ASCII中的编码值，然后通过(char)转成字符'8'
    }

    public String formatCardNo(BankCard card){
        StringBuilder sb = new StringBuilder();
        String spaceChar = "  ";
        String str = card.getCardNo();
        while (str.length() >=4){
            sb.append(str, 0, 4).append(spaceChar);
            str = str.substring(4);
        }
        sb.append(str);
        return sb.toString();
    }
}
