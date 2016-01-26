package com.kemalbayindir.recommendation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Kemal BAYINDIR on 5/12/2015.
 */
public class RecommendationDataLoader {

    public static List<Set<Integer>> loadData(String fileName) {

        List<Set<Integer>> result = new ArrayList<>();

        String dataPath = Constants.DATA_PATH + fileName;

        try(BufferedReader br = new BufferedReader(new FileReader(dataPath))) {

            for(String line; (line = br.readLine()) != null; ) {

                if (line.trim().length() > 0) {
                    String[] parts = line.split(",");

                    Set<Integer> newSet = new HashSet<>();
                    for (int i = 0; i < parts.length; i++)
                        newSet.add(Integer.valueOf(parts[i]));

                    result.add(newSet);
                }
            }

        } catch (Exception ex) {

            System.out.println("File Read Error : " + ex.getMessage());

        }

        return result;
    }
}
