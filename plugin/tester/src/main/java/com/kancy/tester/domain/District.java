package com.kancy.tester.domain;

import com.github.kancyframework.springx.log.Log;
import com.kancy.tester.utils.IDCardUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * District
 *
 * @author huangchengkang
 * @date 2021/8/28 12:40
 */
public class District {

    private static District district = new District();

    static {
        initDistrict();
    }

    public static void main(String[] args) {
        printDistrict();
    }

    private Properties districtProperties = new Properties();
    private Map<String, Province> provinces = new HashMap<>(32);
    private Map<String, City> citys = new HashMap<>(256);
    private Map<String, Town> towns = new HashMap<>(2048);

    private District() {

    }

    public static District getInstance(){
        return district;
    }

    public static void printDistrict() {
        Map<String, Province> provinces = getInstance().getProvinces();
        System.out.println("----------------------行政区划-----------------------------");
        for (Map.Entry<String, Province> provinceEntry : provinces.entrySet()) {
            System.out.println(provinceEntry.getValue().getProvinceName());
            for (Map.Entry<String, City> cityEntry : provinceEntry.getValue().getCitys().entrySet()) {
                System.out.println("\t" + cityEntry.getValue().getCityName());
                for (Map.Entry<String, Town> townEntry : cityEntry.getValue().getTowns().entrySet()) {
                    System.out.println("\t\t" + townEntry.getValue().getTownName());
                }
            }
        }
        System.out.println("----------------------行政区划-----------------------------");
    }

    private static void initDistrict() {
        try {
            district.getDistrictProperties().load(IDCardUtils.class.getClassLoader().getResourceAsStream("data/district.properties"));

            for (Map.Entry<Object, Object> entry : district.getDistrictProperties().entrySet()) {
                String key = entry.getKey().toString();
                String value = entry.getValue().toString();

                if (key.endsWith("0000")){
                    // 省
                    Province province = district.getProvinces().get(key);
                    if (Objects.isNull(province)){
                        province = new Province();
                        district.getProvinces().put(key, province);
                    }
                    province.setProvinceCode(key);
                    province.setProvinceName(value);
                }else if (key.endsWith("00")){
                    // 市
                    // 找到所在的省，将其加入到对应的省
                    String provinceCode = String.format("%s0000", key.substring(0, 2));
                    Province province = district.getProvinces().get(provinceCode);
                    if (Objects.isNull(province)){
                        province = new Province();
                        district.getProvinces().put(provinceCode, province);
                    }

                    City city = district.getCitys().get(key);
                    if (Objects.isNull(city)){
                        city = new City();
                    }

                    city.setProvince(province);
                    city.setCityCode(key);
                    city.setCityName(value);
                    province.getCitys().put(key, city);
                    district.getCitys().put(key, city);
                } else {
                    // 区
                    // 找到所在的市，将其加入到对应的市
                    String cityCode = String.format("%s00", key.substring(0, 4));
                    City city = district.getCitys().get(cityCode);
                    if (Objects.isNull(city)){
                        String provinceCode = String.format("%s0000", key.substring(0, 2));
                        Province province = district.getProvinces().get(provinceCode);
                        if (Objects.isNull(province)){
                            province = new Province();
                            district.getProvinces().put(provinceCode, province);
                        }
                        city = new City();
                        city.setProvince(province);
                        district.getCitys().put(cityCode, city);
                        province.getCitys().put(cityCode, city);
                    }

                    Town town = new Town();
                    town.setCity(city);
                    town.setProvince(city.getProvince());
                    town.setTownCode(key);
                    town.setTownName(value);
                    city.getTowns().put(key, town);
                    district.getTowns().put(key, town);
                }
            }
        } catch (Exception e) {
            Log.error(e.getMessage(), e);
        }
    }

    public Properties getDistrictProperties() {
        return districtProperties;
    }

    public Map<String, Province> getProvinces() {
        return provinces;
    }

    public Map<String, City> getCitys() {
        return citys;
    }

    public Map<String, Town> getTowns() {
        return towns;
    }
}
