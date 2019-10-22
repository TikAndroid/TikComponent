package com.tik.android.component.libcommon;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by xiangning on 2018/11/18.
 */
public class StringUtils {

    public static CharSequence getStringOrEmpty(CharSequence s) {
        return s != null ? s : "";
    }

    public static String readString(InputStream is) {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }

    public static int[] indexOf(CharSequence target, String search, boolean ignoreCase) {
        return indexOf(target, search, new IndexOfOptions().setCaseInsensitive(ignoreCase));
    }

    public static List<int[]> indexOfAll(CharSequence target, String search, boolean ignoreCase) {
        return indexOfAll(target, search,
                new IndexOfOptions().setCaseInsensitive(ignoreCase));
    }

    public static int[] indexOfWithReg(CharSequence target, String search) {
        return indexOf(target, search, new IndexOfOptions().setLiteral(false));
    }

    public static List<int[]> indexOfAllWithReg(CharSequence target, String search) {
        return indexOfAll(target, search, new IndexOfOptions().setLiteral(false));
    }

    public static int[] indexOf(CharSequence target, String search, IndexOfOptions options) {
        List<int[]> result = indexOfAll(target, search, options);
        if (result.size() > 0) {
            return result.get(0);
        }

        return null;
    }

    /**
     * List转为String，中间用separator拼接
     * @param list
     * @param separator
     * @return
     */
    public static String listToString(List<String> list, String separator) {
        StringBuilder builder = new StringBuilder();
        String result = "";

        if (null != list) {
            for (String s : list) {
                builder.append(s).append(separator);
            }

            if (builder.length() > 0) {
                result = builder.substring(0, builder.length() - 1);
            }
        }

        return result;
    }


    /**
     * 查找模式在目标字符串中的位置
     * @param target 目标字符串
     * @param search 需要搜索的字串
     *
     * @return 查找到的所有出现位置，每一个位置信息包含了start和end的信息，
     */
    private static List<int[]> indexOfAll(CharSequence target, CharSequence search, IndexOfOptions options) {
        List<int[]> results = new ArrayList<>();

        if (TextUtils.isEmpty(search)) { // search为空时返回0长度的位置
            results.add(new int[] {0, 0});
            return results;
        }

        target = getStringOrEmpty(target);
        if (options == null) {
            options = new IndexOfOptions();
        }

        options.setOnlyFirst(false);

        int flag = 0;
        if (options.caseInsensitive) {
            flag |= Pattern.CASE_INSENSITIVE;
        }
        if (options.literal) {
            flag |= Pattern.LITERAL;
        }

        Pattern pattern = Pattern.compile(search.toString(), flag);
        Matcher matcher = pattern.matcher(target);
        while (matcher.find()) {
            results.add(new int[] {matcher.start(), matcher.end()});
            if (options.onlyFirst) { //只取第一个结果
                break;
            }
        }
        return results;
    }

    private static class IndexOfOptions {

        boolean caseInsensitive = false; // 大小写不敏感
        boolean onlyFirst = true; // 只取第一个结果
        boolean literal = true; // 不启用正则匹配

        public boolean isCaseInsensitive() {
            return caseInsensitive;
        }

        public IndexOfOptions setCaseInsensitive(boolean caseInsensitive) {
            this.caseInsensitive = caseInsensitive;
            return this;
        }

        public boolean isOnlyFirst() {
            return onlyFirst;
        }

        public IndexOfOptions setOnlyFirst(boolean onlyFirst) {
            this.onlyFirst = onlyFirst;
            return this;
        }


        public boolean isLiteral() {
            return literal;
        }

        public IndexOfOptions setLiteral(boolean literal) {
            this.literal = literal;
            return this;
        }
    }

}
