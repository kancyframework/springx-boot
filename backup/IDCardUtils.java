package com.github.kancyframework.springx.utils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;
import java.util.Random;
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
    private static String[] districtList;
    private static int currYear = 1994;
    private static int minAge = 18;
    private static int maxAge = 60;
    private static Properties p = new Properties();
    static {
        try {
            currYear = Calendar.getInstance().get(1);
            p.load(IDCardUtils.class.getClassLoader().getResourceAsStream("district.dat"));
            districtList = (String[]) p.keySet().toArray(new String[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static String create() {
        String district = getdistrictcode();
        String birthday = getBirthday();
        String randomNumber = getRandomNumber();
        String checkCode = getCheckCode(district + birthday + randomNumber);
        return district + birthday + randomNumber + checkCode;
    }

    public static String create(String district) {
        return create(district, getBirthday(), getRandomNumber());
    }

    public static String create(String district, String birthday) {
        return create(district, birthday, getRandomNumber());
    }

    public static String create(String district, String birthday, String randomNumber) {
        if ((district.length() != 6) || (birthday.length() != 8) || (randomNumber.length() != 3) ||
                (!executeRegMatcher("^([1-9]\\d{0,}|0)$", district + birthday + randomNumber))) return null;
        return district + birthday + randomNumber + getCheckCode(new StringBuilder(String.valueOf(district)).append(birthday).append(randomNumber).toString());
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
        return zfill(String.valueOf(new Random().nextInt(999) + 1), 3);
    }

    public static String getBirthday() {
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

    /**
     * 解析身份证号居住地址
     * @param cid
     * @return
     */
    public static String parseAddress(String cid) {
        String addressCode ;
        String address="未知";
        try {
            //取省份
            addressCode = String.format("%s%s", cid.substring(0,2), "0000");
            if(p.containsKey(addressCode)) {
                address = p.get(addressCode).toString();
                //取市
                addressCode = String.format("%s%s", cid.substring(0, 4), "00");
                if (p.containsKey(addressCode)) {
                    address += p.get(addressCode).toString();
                    //取具体地址
                    addressCode = cid.substring(0, 6);
                    if (p.containsKey(addressCode) && !String.format("%s%s", cid.substring(0, 4) , "00").equals(addressCode)) {
                        address+=p.get(addressCode).toString();
                    }
                }
            }
            return address;
        }catch (Exception e){
            System.err.println("身份证地址码信息有误："+e);
        }
        return address;
    }
}
