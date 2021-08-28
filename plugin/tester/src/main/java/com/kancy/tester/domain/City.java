package com.kancy.tester.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * City
 *
 * @author huangchengkang
 * @date 2021/8/28 12:39
 */
@NoArgsConstructor
@Data
public class City implements Serializable {
    private Province province;
    private String cityCode;
    private String cityName;
    private Map<String, Town> towns = new HashMap<>(32);

    public City(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return cityName;
    }

    public String show() {
        final StringBuffer sb = new StringBuffer("City{");
        sb.append("cityCode='").append(cityCode).append('\'');
        sb.append(", cityName='").append(cityName).append('\'');
        sb.append(", provinceName='").append(province.getProvinceName()).append('\'');
        sb.append(", townSize='").append(towns.size()).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
