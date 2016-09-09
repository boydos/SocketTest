package com.example.sockettest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InitConfigureUtils implements InitConts {
 public static boolean isIP(String address) {
     if(address==null || address.length()<7 || address.length()>15 ||"".equals(address.trim())) {
         return false;
     }
     String rexp="([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
     Pattern pat = Pattern.compile(rexp);
     Matcher mat = pat.matcher(address);
     return mat.find();
 }
}
