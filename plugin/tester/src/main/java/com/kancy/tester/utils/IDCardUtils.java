package com.kancy.tester.utils;

import com.github.kancyframework.springx.utils.DateUtils;
import com.github.kancyframework.springx.utils.ObjectUtils;
import com.github.kancyframework.springx.utils.RandomUtils;
import com.github.kancyframework.springx.utils.StringUtils;
import com.kancy.spring.minidb.MapDb;
import com.kancy.tester.domain.City;
import com.kancy.tester.domain.District;
import com.kancy.tester.domain.Province;
import com.kancy.tester.domain.Town;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 身份证
 */
public class IDCardUtils {
    private static final String[] zoneNum = {"11", "12", "13", "14", "15", "21", "22", "23", "31", "32", "33", "34", "35",
            "36", "37", "41", "42", "43", "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63", "64", "65", "71", "81", "82", "91"};

    private static final String[] PARITYBIT = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
    private static final int[] POWER = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};
    private static int currYear = new Date().getYear() + 1900;
    private static int minAge = 18;
    private static int maxAge = 60;
    private static boolean boy = true;
    private static boolean girl = true;

    private static Province province;
    private static City city;
    private static Town town;

    private static Properties districtProperties;

    static {
        districtProperties = District.getInstance().getDistrictProperties();

        // 初始化
        String idCardAgeRange = MapDb.getData("idCardAgeRange", String.format("%s-%s", minAge, maxAge));
        String[] strings = StringUtils.toArray(idCardAgeRange, "-");
        IDCardUtils.setAgeRange(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]));

        setAddressEnabled(
                District.getInstance().getProvinces().getOrDefault(MapDb.getData("defaultDistrictProvinceCode"), null),
                District.getInstance().getCitys().getOrDefault(MapDb.getData("defaultDistrictCityCode"), null),
                District.getInstance().getTowns().getOrDefault(MapDb.getData("defaultDistrictTownCode"), null)
        );

    }
    public static String getCardValidDate(String idCardNo){
        Integer minYear = Integer.parseInt(idCardNo.substring(6, 10));
        Integer maxYear = Integer.parseInt(DateUtils.getDateStr(LocalDate.now(), "yyyy"));
        int realMinYear = Math.max(maxYear - 21, minYear);

        int year = RandomUtils.nextInt(realMinYear ,maxYear);
        int month = RandomUtils.nextInt(0, 12);
        int day = RandomUtils.nextInt(0, 28);

        return String.format("%s.%02d.%02d-%s.%02d.%02d", year,month, day, year+20, month, day);
    }
    public static String getCityAndTown(String idCardNo){
        Object city = districtProperties.get(String.format("%s00", idCardNo.substring(0, 4)));
        if (Objects.isNull(city)){
            city = districtProperties.get(String.format("%s00", idCardNo.substring(0, 2)));
        }
        Object town = districtProperties.get(idCardNo.substring(0, 6));
        return String.format("%s%s", Objects.isNull(city) || Objects.equals(city, town) ? "" : city,
                Objects.isNull(town) ? "" : town);
    }
    public static String getAddress(String idCardNo){
        Object province = districtProperties.get(String.format("%s0000", idCardNo.substring(0, 2)));
        Object city = districtProperties.get(String.format("%s00", idCardNo.substring(0, 4)));
        Object town = districtProperties.get(idCardNo.substring(0, 6));

        StringBuilder result = new StringBuilder();
        StringBuilder subResult = new StringBuilder();

        List<String> items = new ArrayList<>();
        items.add((Objects.isNull(province) ? "" : String.valueOf(province)));
        if (!Objects.equals(province, city)){
            items.add((Objects.isNull(city) ? "" : String.valueOf(city)));
        }
        items.add((Objects.isNull(town) || Objects.equals(city, town) ? "" : String.valueOf(town)));
        items.add(NameUtils.getRandomChinese(1, 3) + "路");
        items.add(NameUtils.getRandomChinese(1, 3) + "小区");
        items.add(RandomUtils.nextInt(1, 20) + "单元");
        items.add(RandomUtils.nextInt(101, 2500) + "室");

        String line = "";
        for (int i = 0; i < items.size(); i++) {
            line += items.get(i);
            subResult.append(items.get(i));
            if (i+1 < items.size() && line.length() + items.get(i+1).length() > 17){
                subResult.append("<br/>");
                line = "";
            }
        }

        result.append("<html>");
        result.append(subResult.toString());
        result.append("</html>");
        return result.toString();
    }

    public static String create() {
        String district = getdistrictcode();
        String birthday = getBirthday();
        String randomNumber = getRandomNumber();
        String sexCode = getSexCode();
        String checkCode = getCheckCode(district + birthday + randomNumber + sexCode);
        return district + birthday + randomNumber + sexCode + checkCode;
    }

    private static String getSexCode() {
        if (boy && girl){
            return String.valueOf(RandomUtils.nextInt(10));
        }
        if (girl){
            return RandomUtils.nextString("0,2,4,6,8");
        }
        return RandomUtils.nextString("1,3,5,7,9");
    }

    public static void setSexEnabled(boolean boy, boolean girl) {
        IDCardUtils.boy = boy;
        IDCardUtils.girl = girl;
    }

    public static void setAddressEnabled(Province province, City city, Town town) {
        IDCardUtils.province = province;
        IDCardUtils.city = city;
        IDCardUtils.town = town;
    }

    public static void setAgeRange(int minAge, int maxAge) {
        IDCardUtils.minAge = minAge;
        IDCardUtils.maxAge = maxAge;
    }

    private static String getCheckCode(String certid) {
        int sum = 0;
        for (int i = 0; i < certid.length(); i++)
            sum += POWER[i] * (certid.charAt(i) - '0');
        return PARITYBIT[(sum % 11)];
    }

    private static String getRandomNumber() {
        return zfill(String.valueOf(new Random().nextInt(100)), 2);
    }

    private static String getBirthday() {
        Random random = new Random();
        String year = String.valueOf(currYear - random.nextInt(maxAge - minAge) - minAge);
        int iMonth = random.nextInt(12) + 1;
        String month = zfill(String.valueOf(iMonth), 2);
        String day = null;
        if (iMonth == 2)
            day = String.valueOf(random.nextInt(28) + 1);
        else if ((iMonth == 4) || (iMonth == 6) || (iMonth == 9) || (iMonth == 11))
            day = String.valueOf(random.nextInt(30) + 1);
        else
            day = String.valueOf(random.nextInt(31) + 1);
        day = zfill(day, 2);

        return year + month + day;
    }

    public static String getdistrictcode() {
        if (Objects.isNull(province)){
            Object[] towns = District.getInstance().getTowns().keySet().stream().toArray();
            Town town = District.getInstance().getTowns().get(towns[RandomUtils.nextInt(towns.length)]);
            return town.getTownCode();
        } else {
            if (Objects.isNull(city)){
                Object[] citys = province.getCitys().keySet().stream().toArray();
                if (ObjectUtils.isEmpty(citys)){
                    return province.getProvinceCode();
                }
                City city = province.getCitys().get(citys[RandomUtils.nextInt(citys.length)]);
                Object[] towns = city.getTowns().keySet().stream().toArray();
                if (ObjectUtils.isEmpty(towns)){
                    return city.getCityCode();
                }

                Town town = city.getTowns().get(towns[RandomUtils.nextInt(towns.length)]);
                return town.getTownCode();
            } else {
                if (Objects.isNull(town)){
                    Object[] towns = city.getTowns().keySet().stream().toArray();
                    if (ObjectUtils.isEmpty(towns)){
                        return city.getCityCode();
                    }

                    Town town = city.getTowns().get(towns[RandomUtils.nextInt(towns.length)]);
                    return town.getTownCode();
                }else{
                    return town.getTownCode();
                }
            }
        }
    }

    public static boolean isPersonId(String personId) {
        if (((personId.length() != 15) && (personId.length() != 18)) ||
                (!executeRegMatcher("^([1-9]\\d{0,})$", personId.substring(0, personId.length() - 1)))) {
            return false;
        }
        String num = personId.substring(0, 2);
        for (int i = 0; i < zoneNum.length; i++) {
            if (zoneNum[i].equals(num)) {
                break;
            }
            if (i == zoneNum.length - 1) return false;
        }
        String year = personId.length() == 15 ? "19" + personId.substring(6, 8) : personId.substring(6, 10);
        int iyear = Integer.parseInt(year);
        if ((iyear < 1900) || (iyear > currYear)) {
            return false;
        }
        String month = personId.length() == 15 ? personId.substring(8, 10) : personId.substring(10, 12);
        int imonth = Integer.parseInt(month);
        if ((imonth < 1) || (imonth > 12)) {
            return false;
        }
        String day = personId.length() == 15 ? personId.substring(10, 12) : personId.substring(12, 14);
        int iday = Integer.parseInt(day);
        if ((iday < 1) || (iday > 31)) {
            return false;
        }
        if (personId.length() == 18) {
            return getCheckCode(personId.substring(0, 17)).equals(personId.substring(17));
        }
        return true;
    }

    /**
     * 根据身份证解析生肖
     * @param idCard
     * @return
     */
    public static String getAnimalByIdCard(String idCard){
        return getAnimal(Integer.parseInt(idCard.substring(6,10)));
    }


    /**
     * 根据年份解析动物
     * @param year
     * @return
     */
    public static String getAnimal(Integer year){
        if(year<1900){
            return "未知";
        }
        int start = (int) 1900;
        String [] years=new String[]{
                "鼠","牛","虎","兔",
                "龙","蛇","马","羊",
                "猴","鸡","狗","猪"
        };
        return years[(year-start)%years.length];
    }

    /**
     * 通过身份证计算星座
     * @param idCard
     * @return
     */
    public static String getConstellationByIdCard(String idCard) {
        return getConstellation(Integer.parseInt(idCard.substring(10,12)),
                Integer.parseInt(idCard.substring(12,14)));
    }

    /**
     * 通过生日计算星座
     *
     * @param month
     * @param day
     * @return
     */
    public static String getConstellation(int month, int day) {
        int[] dayArr = new int[] { 20, 19, 21, 20, 21, 22, 23,
                23, 23, 24, 23, 22 };
        String[] constellationArr = new String[] { "摩羯座",
                "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座",
                "天蝎座", "射手座", "摩羯座" };

        return day < dayArr[month - 1] ? constellationArr[month - 1]
                : constellationArr[month];
    }

    private static String zfill(String obj, int size) {
        for (int i = obj.length(); i < size; i++)
            obj = "0" + obj;
        return obj;
    }

    private static boolean executeRegMatcher(String reg, String param) {
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(param);
        return m.find();
    }
}
