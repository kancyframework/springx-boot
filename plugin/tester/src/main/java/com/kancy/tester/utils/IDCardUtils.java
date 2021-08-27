package com.kancy.tester.utils;

import com.github.kancyframework.springx.utils.DateUtils;
import com.github.kancyframework.springx.utils.RandomUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 身份证
 */
public class IDCardUtils {
    private static final String[] PARITYBIT = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
    private static final int[] POWER = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};
    private static String[] districtList;
    private static int currYear = 1994;
    private static int minAge = 18;
    private static int maxAge = 60;
    private static boolean boy = true;
    private static boolean girl = true;

    private static Properties districtProperties = new Properties();

    static {
        try {
            currYear = Calendar.getInstance().get(1);
            districtProperties.load(IDCardUtils.class.getClassLoader().getResourceAsStream("data/district.properties"));
            districtList = districtProperties.keySet().toArray(new String[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getCardValidDate(String idCardNo){
        String maxYear = DateUtils.getDateStr(LocalDate.now(), "yyyy");
        int year = RandomUtils.nextInt(Integer.parseInt(maxYear) - 21 , Integer.parseInt(maxYear));
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
        items.add((Objects.isNull(city) ? "" : String.valueOf(city)));
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
        String checkCode = getCheckCode(district + birthday + randomNumber);
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
        String districtCode = districtList[new Random().nextInt(districtList.length)].trim();
        while (("".equals(districtCode)) || (districtCode == null))
            districtCode = districtList[new Random().nextInt(districtList.length)].trim();
        return districtCode;
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
