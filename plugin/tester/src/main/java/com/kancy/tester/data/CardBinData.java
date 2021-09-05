package com.kancy.tester.data;

import com.github.kancyframework.springx.log.Log;
import com.github.kancyframework.springx.utils.IoUtils;
import com.github.kancyframework.springx.utils.RandomUtils;
import com.github.kancyframework.springx.utils.StringUtils;
import com.kancy.tester.domain.CardBin;
import lombok.Data;

import java.util.*;

/**
 * CardBinData
 *
 * @author huangchengkang
 * @date 2021/8/26 17:29
 */
@Data
public class CardBinData {

    private static final Map<String, List<CardBin>> cardBinIndexs = new HashMap<>();

    private static List<String> bankNames;

    static {
        bankNames = StringUtils.toList("中国银行,邮储银行,招商银行,工商银行,中信银行,建设银行,农业银行,交通银行,平安银行,民生银行,广发银行,兴业银行,北京银行,上海银行,华夏银行,浦发银行,光大银行");
        try {
            List<String> allCardBins = IoUtils.readLines(CardBinData.class.getResourceAsStream("/data/cardbin.txt"), "UTF-8");
            for (String cardBinStr : allCardBins) {
                String[] datas = cardBinStr.split(",", 6);
                CardBin cardBin = new CardBin();
                cardBin.setId(datas[0]);
                cardBin.setCardType(datas[1]);
                cardBin.setCardLength(Integer.parseInt(datas[2]));
                cardBin.setCardName(datas[3]);
                cardBin.setBankName(datas[4]);
                cardBin.setBankAbbr(StringUtils.isBlank(datas[5]) ? null : datas[5]);
                if (bankNames.contains(cardBin.getBankName())){
                    createCardBinIndex(String.format("%s@%s", cardBin.getBankName(), cardBin.getCardType()), cardBin);
                }else {
                    createCardBinIndex(String.format("其他银行@%s", cardBin.getCardType()), cardBin);
                }
                createCardBinIndex(cardBin.getCardType(), cardBin);
                createCardBinIndex(cardBin.getId(), cardBin);
            }
        } catch (Exception e) {
            Log.error("加载cardbin数据失败：{}", e.getMessage());
        }
    }

    private static void createCardBinIndex(String key, CardBin cardBin) {
        if (cardBinIndexs.containsKey(key)){
            List<CardBin> cardBins = cardBinIndexs.get(key);
            cardBins.add(cardBin);
        }else {
            List<CardBin> cardBins = new ArrayList<>();
            cardBins.add(cardBin);
            cardBinIndexs.put(key, cardBins);
        }
    }

    public static CardBin randomCardBin(String key){
        List<CardBin> cardBins = cardBinIndexs.get(key);
        if (Objects.isNull(cardBins)){
            return null;
        }
        return cardBins.get(RandomUtils.nextInt(0, cardBins.size()));
    }

    public static List<String> getBankNames() {
        return bankNames;
    }
}
