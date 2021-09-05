package com.kancy.tester.domain;

import lombok.Data;

/**
 * BankCard
 *
 * @author huangchengkang
 * @date 2021/8/27 13:32
 */
@Data
public class BankCard {
    private String cardNo;
    private CardBin cardBin;
}
