package com.github.kancyframework.springx.swing.utils;

import com.github.kancyframework.springx.utils.Md5Utils;
import com.github.kancyframework.springx.utils.StringUtils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * PasswordUtils
 *
 * @author kancy
 * @date 2020/6/14 10:37
 */
public class PasswordUtils {

    public static String password(String userName){
        if (StringUtils.isEmpty(userName)){
            return userName;
        }
        List<String> passwords = getPasswords(userName);
        return passwords.get(ThreadLocalRandom.current().nextInt(0, passwords.size()));
    }

    private static List<String> getPasswords(String userName) {
        String encrypt = Md5Utils.md5(String.format("%s%s%s%s", userName.length(), userName,userName, userName.length()));
        int skip = 4;
        int length = 6;
        Set<String> passwords = new HashSet<>();
        char[] chars = encrypt.toCharArray();
        for (int i = 0; i <encrypt.length() ; i++) {
            StringBuilder sb = new StringBuilder();
            int pos = i;
            for (int j = 0; j < length; j++) {
                if (pos >= encrypt.length()){
                    break;
                }
                sb.append(chars[pos]);
                pos += skip;
            }
            if (pos >= encrypt.length()){
                break;
            }
            passwords.add(sb.toString());
        }
        return Collections.unmodifiableList(new ArrayList<>(passwords));
    }

    public static boolean checkPassword(String userName, String password) {
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)){
            return false;
        }
        return getPasswords(userName).contains(password);
    }

}
