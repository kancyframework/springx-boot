package com.kancy.tester.domain;

import lombok.Data;

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
}
