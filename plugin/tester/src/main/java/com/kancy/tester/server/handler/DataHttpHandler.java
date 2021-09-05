package com.kancy.tester.server.handler;

import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.utils.BeanUtils;
import com.github.kancyframework.springx.utils.RandomUtils;
import com.github.kancyframework.springx.utils.StringUtils;
import com.kancy.spring.minidb.MapDb;
import com.kancy.tester.domain.BankCard;
import com.kancy.tester.service.BankCardService;
import com.kancy.tester.utils.IDCardUtils;
import com.kancy.tester.utils.MockDataUtils;
import com.kancy.tester.utils.NameUtils;
import com.sun.net.httpserver.HttpExchange;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * IdCardHttpHandler
 *
 * @author huangchengkang
 * @date 2021/8/28 23:09
 */
@Component
public class DataHttpHandler extends BaseHttpHandler{

    @Override
    protected String doGet(HttpExchange httpExchange) {
        Map<String, String> params = queryParamsToMap(httpExchange);

        Map<String, Object> responseMap = new HashMap<>();
        if (Objects.equals(params.getOrDefault("type", "idcard"), "bankcard")){
            BankCardService bankCardService = new BankCardService();
            String cardType = params.getOrDefault("cardType","所有").toUpperCase();
            String bankName = params.getOrDefault("bankName", "所有");

            Object indexKey = null;
            String searchCardType = "";
            if (Objects.equals(cardType,"所有") || (cardType.contains("DEBIT") && cardType.contains("CREDIT"))){
                searchCardType = RandomUtils.nextBoolean() ? "储蓄卡" : "信用卡";
            }else {
                searchCardType = cardType;
            }
            if (Objects.equals(bankName,"所有")){
                indexKey = searchCardType;
            }else {
                indexKey = String.format("%s@%s", bankName, searchCardType);
            }
            BankCard bankCard = bankCardService.generateCard(String.valueOf(indexKey));

            responseMap.putAll(BeanUtils.beanToMap(bankCard.getCardBin()));
            responseMap.put("cardNo", bankCard.getCardNo());
            return StringUtils.toJSONString(responseMap);
        }

        String name = NameUtils.fullName();
        String idCardNo = IDCardUtils.create();
        responseMap.put("name", name);
        responseMap.put("idCardNo", idCardNo);
        responseMap.put("sex", Integer.parseInt(idCardNo.substring(16,17)) % 2 == 0 ? "女" : "男");
        responseMap.put("age", new Date().getYear() + 1900 - Integer.parseInt(idCardNo.substring(6,10)));
        responseMap.put("constellation", IDCardUtils.getConstellationByIdCard(idCardNo));
        responseMap.put("animal", IDCardUtils.getAnimalByIdCard(idCardNo));
        responseMap.put("mobile", MockDataUtils.mobile());
        responseMap.put("email", MockDataUtils.email());
        responseMap.put("residenceAddress", IDCardUtils.getAddress(idCardNo)
                .replace("<html>","")
                .replace("</html>","")
                .replace("<br/>",""));
        responseMap.put("nation", MapDb.getData("defaultNation", "汉"));
        responseMap.put("issue", String.format("%s公安局", IDCardUtils.getCityAndTown(idCardNo)));
        responseMap.put("cardValidDate", String.format("%s公安局",IDCardUtils.getCardValidDate(idCardNo)));
        responseMap.put("birthDay", String.format("%s年%s月%s日",
                Integer.parseInt(idCardNo.substring(6,10)),
                Integer.parseInt(idCardNo.substring(10,12)),
                Integer.parseInt(idCardNo.substring(12,14))));
        return StringUtils.toJSONString(responseMap);
    }

    @Override
    public String getContextPath() {
        return "/data";
    }
}
