package com.kancy.tester.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Town
 *
 * @author huangchengkang
 * @date 2021/8/28 12:39
 */
@NoArgsConstructor
@Data
public class Town implements Serializable {
    private Province province;
    private City city;
    private String townCode;
    private String townName;

    public Town(String townName) {
        this.townName = townName;
    }

    @Override
    public String toString() {
        return townName;
    }

    public String show() {
        final StringBuffer sb = new StringBuffer("Town{");
        sb.append("townCode='").append(townCode).append('\'');
        sb.append(", townName='").append(townName).append('\'');
        sb.append(", provinceName='").append(province.getProvinceName()).append('\'');
        sb.append(", cityName='").append(city.getCityName()).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
