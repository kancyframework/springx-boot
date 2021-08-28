package com.kancy.tester.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Province
 *
 * @author huangchengkang
 * @date 2021/8/28 12:38
 */
@NoArgsConstructor
@Data
public class Province implements Serializable {
    private String provinceCode;
    private String provinceName;
    private Map<String, City> citys = new HashMap<>(36);

    public Province(String provinceName) {
        this.provinceName = provinceName;
    }

    @Override
    public String toString() {
        return provinceName;
    }

    public String show() {
        final StringBuffer sb = new StringBuffer("Province{");
        sb.append("provinceCode='").append(provinceCode).append('\'');
        sb.append(", provinceName='").append(provinceName).append('\'');
        sb.append(", citySize='").append(citys.size()).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
