package com.daiyanping.cms.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Test2 {

//    public static void main(String[] args) throws Exception {
//        int count = 0;
//        InputStream in = System.in;
//        char c = (char) in.read();
//        while(c != '\n'){
//            if(c == ' ')
//                count = 0;
//            else
//                count++;
//            c = (char) in.read();
//        }
//        System.out.println(count);
//    }

    /**
     * 计算某字符出现次数
     * @param args
     * @throws IOException
     */
//    public static void main(String[] args) throws IOException {
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        char[] chars1 = br.readLine().toLowerCase().toCharArray();
//        char[] chars2 = br.readLine().toLowerCase().toCharArray();
//        int count = 0;
//        for (int i = 0; i < chars1.length; i++) {
//            if ((chars1[i] >= 65 || chars1[i] < 90) && (chars1[i] == chars2[0])) {
//                count++;
//            }
//        }
//        System.out.println(count);
//    }

    // 明明的随机数
//    public static void main(String[] args)throws IOException {
//        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
//        String str;
//        while((str=bf.readLine())!=null) {
//
//            boolean[] stu = new boolean[1001];
//            StringBuilder sb=new StringBuilder();
//            int n = Integer.parseInt(str);
//            for(int i=0;i<n;i++) {
//                stu[Integer.parseInt(bf.readLine())] = true;
//            }
//            for(int i=1;i<1001;i++) {
//                if(stu[i]) {
//                    sb.append(i).append("\n");
//                }
//            }
//            sb.deleteCharAt(sb.length()-1);
//            System.out.println(sb.toString());
//        }
//    }

    // 字符串分隔
//    public static void main(String[] args) throws IOException{
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        String str;
//        while((str = br.readLine())!=null){
//            int len = str.length();
//            int start = 0;
//            while (len >= 8){
//                System.out.println(str.substring(start, start + 8));
//                start += 8;
//                len -= 8;
//            }
//            if (len > 0) {
//                char[] tmp = new char[8];
//                for(int i = 0;i<8;i++){
//                    tmp[i]='0';
//                }
//                for(int i = 0; start < str.length(); i++) {
//                    tmp[i] = str.charAt(start++);
//                }
//                System.out.println(String.valueOf(tmp));
//            }
//        }
//    }

    //
//    public static void main2(String[] args) throws IOException{
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        String str = "";
//        while ((str = br.readLine()) != null) {
//            StringBuilder stringBuilder = new StringBuilder();
//            char[] chars = str.toCharArray();
//            int i = 0;
//            while (i < chars.length) {
//                if (chars[i] >= '0' && chars[i] <= '9') {
//                    stringBuilder.append("*");
//                    stringBuilder.append(chars[i]);
//                    while (++i < chars.length && chars[i] >= '0' && chars[i] <= '9') {
//                        stringBuilder.append(chars[i]);
//                    }
//                    stringBuilder.append("*");
//                } else {
//                    stringBuilder.append(chars[i]);
//                    i++;
//                }
//            }
//        }
//        str = br.readLine();
//
//
//    }

//    public static void main(String[] args) throws IOException{
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        String s = br.readLine();
//        Integer integer = Integer.valueOf(s);
//        int count = 0;
//        List<String> list = new ArrayList<String>();
//        while (count < integer) {
//            String str = br.readLine();
//            list.add(str);
//            count++;
//        }
//        Collections.sort(list);
//        for (String s1 : list) {
//            System.out.println(s1);
//        }
//    }

//    public static void main(String[] args) throws IOException{
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        String s = br.readLine();
//        HashSet<String> strings = new HashSet<>();
//        for (int i = 0; i < s.length(); i++) {
//            int i1 = s.charAt(i);
//            if ((i1 >= 0 || i1 <= 127) && '\n' != i1) {
//                strings.add(i1 + "");
//            }
//
//        }
//        System.out.println(strings.size());
//    }

    // 提取不重复的整数
//    public static void main(String[] args) throws IOException{
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        String s = br.readLine();
//        char c = s.charAt(s.length() - 1);
//        if (c == 0) {
//            return;
//        }
//
//        StringBuilder reverse = new StringBuilder(s).reverse();
//        HashSet<String> strings = new HashSet<>();
//        StringBuilder stringBuilder = new StringBuilder();
//        for (int i = 0; i < reverse.length(); i++) {
//            char c1 = reverse.charAt(i);
//            if (!strings.contains(c1 + "")) {
//                strings.add(c1 + "");
//                stringBuilder.append(c1);
//            }
//
//        }
//
//        System.out.println(stringBuilder);
//    }

    // 计算某字符出现次数
//        public static void main(String[] args) throws IOException{
//            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//            String s = br.readLine();
//            String s1 = br.readLine();
//
//            String s2 = s1.toUpperCase();
//            String s3 = s1.toLowerCase();
//            int count = 0;
//            for (int i = 0; i < s.length(); i++) {
//                char c = s.charAt(i);
//                if (c == s2.charAt(0) || c == s3.charAt(0)) {
//                    count++;
//                }
//            }
//            System.out.println(count);
//        }

//    public static void main(String[] args) throws IOException{
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        String str;
//        while((str = br.readLine())!=null){
//           if (str.length() <=8) {
//               char[] chars = new char[8];
//               for (int i = 0; i < 8; i++) {
//                   if (i <= str.length()) {
//
//                       chars[i] = str.charAt(i);
//                   } else {
//                       chars[i] = '0';
//                   }
//
//               }
//               System.out.println(new String(chars));
//           }
//            if (str.length() > 8) {
//
//                int length = str.length();
//                int count = length / 8;
//                int i2 = length % 8;
//                if (i2 !=0) {
//                    count++;
//                }
//                for (int i = 0; i < count; i++) {
//                    str.substring(i * 8, (i + 1) * 8 - 1);
//                    if (str.length()>)
//                    String substring = str.substring(i, ((i + 1) * 8) - 1);
//
//                }
//                char[] chars = new char[8];
//                for (int i = 0; i < 8; i++) {
//                    if (i <= str.length()) {
//
//                        chars[i] = str.charAt(i);
//                    } else {
//                        chars[i] = '0';
//                    }
//
//                }
//                System.out.println(new String(chars));
//            }
//        }
//    }
    public static void main(String[] args) {
        recursion(5);
    }

    public static int recursion(int num) {
        int sum = 1;
        if (num < 0) {

        }
        if (num == 1) {
            return 1;
        } else {
            sum = num * recursion(num - 1
            );
            return sum;
        }

    }
}
