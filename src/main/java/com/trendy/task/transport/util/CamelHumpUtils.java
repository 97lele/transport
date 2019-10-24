package com.trendy.task.transport.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: lele
 * @date: 2019/10/6 上午10:49
 */
public class CamelHumpUtils {

    private CamelHumpUtils() {
    }

    private static Pattern humpPattern = Pattern.compile("[A-Z]");
    private static Pattern linePattern = Pattern.compile("_(\\w)");

    /**
     * 下滑线转驼峰
     *
     * @param source
     * @return
     */
    public static String toCamelCase(String source) {
        source = source.toLowerCase();
        Matcher matcher = linePattern.matcher(source);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 驼峰转下滑线
     *
     * @param source
     * @return
     */
    public static String humpToLine(String source) {
        Matcher matcher = humpPattern.matcher(source);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        String res=sb.toString();
        return res.startsWith("_")?res.substring(1):res;
    }


}
