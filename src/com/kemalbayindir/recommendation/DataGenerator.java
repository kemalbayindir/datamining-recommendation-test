package com.kemalbayindir.recommendation;

import java.util.*;

/**
 * Created by kbayindir on 5.12.2015.
 */
public class DataGenerator {

    public DataGenerator() { }

    public List<Set<Integer>> genRandomVisitor(int rowCount, int minVisitorCount, int maxVisitorCount) {
        List<Set<Integer>> result = new ArrayList<>();
        Random random = new Random();
        for (int r = 0; r < rowCount; r++) {
            int visitorCount = (random.nextInt(maxVisitorCount - minVisitorCount) + 1) + minVisitorCount;
            // DEBUG ::::: System.out.println();
            int visitedCount = 0;
            Set<Integer> visitedIds = new HashSet<>();
            while (visitedCount < visitorCount) {
                int visitedId = random.nextInt(Constants.ITEM_COUNT);
                if (!visitedIds.contains(visitedId)) {
                    visitedIds.add(visitedId);
                    visitedCount++;
                }
            }
            result.add(visitedIds);
            // DEBUG ::::: for (Integer id : visitedIds)
            // DEBUG :::::     System.out.print(id + "|");
        }
        // DEBUG ::::: System.out.println();
        // DEBUG ::::: System.out.println("-->" + result.size());
        // DEBUG ::::: System.out.println();

        return result;
    }

}
