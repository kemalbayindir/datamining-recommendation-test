package com.kemalbayindir.recommendation;

import java.io.*;
import java.util.*;

/**
 * Created by Kemal BAYINDIR on 5/12/2015.
 */
public class Helpers {

    public static void out(String msg, boolean sysout) {
        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Constants.DATA_PATH + "out-log.txt", true)))) {
            out.println(msg);
            if (sysout)
                System.out.println(msg);
        }catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }

    public static Map<Integer, Double> sortByComparator(Map<Integer, Double> unsortMap, final boolean asc) {

        List<Map.Entry<Integer, Double>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
            public int compare(Map.Entry<Integer, Double> o1,
                               Map.Entry<Integer, Double> o2)
            {
                if (asc) {
                    return o1.getValue().compareTo(o2.getValue());
                } else {
                    return o2.getValue().compareTo(o1.getValue());
                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<Integer, Double> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public static void save2File(String str, String fileName) {
        try {
            File fout = new File(Constants.DATA_PATH + fileName);
            FileOutputStream fos = new FileOutputStream(fout);
            OutputStreamWriter osw = new OutputStreamWriter(fos);

            osw.write(str);
            osw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String list2String(List<Set<Integer>> list) {
        StringBuilder result = new StringBuilder();
        for (Set<Integer> set : list) {
            result.append("\n");
            for (Integer subSet : set)
                result.append(subSet + ",");
        }
        return result.toString();
    }

    public static <T> void dumpList(List<T> list) {
        for (T item : list)
            System.out.println(item);
    }

    public static void dumpSetForGraph(String title, List<Set<Integer>> set) {
        System.out.println(title);
        for (int i = 0; i < set.size(); i++)
            System.out.println((i + 1) + "\t" + set.get(i).size());
    }

}
