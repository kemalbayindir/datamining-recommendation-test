package com.kemalbayindir.recommendation;

import java.util.*;

/**
 * Created by Kemal BAYINDIR on 5/12/2015.
 */
public class RecommendationDataManager {

    private List<Set<Integer>> trainingSet;
    private List<Set<Integer>> notVisitedTrainingSet;
    private Map<Integer, List<Integer>> itemVisitorsMap = new HashMap<>();
    private List<Set<Integer>> validationSet;
    private List<Set<Integer>> testSet;

    private Set<Integer> currentSet;
    private Set<Integer> virtualSet;

    public RecommendationDataManager(List<Set<Integer>> userVisits) {

        trainingSet     = new ArrayList<>();
        validationSet   = new ArrayList<>();
        testSet         = new ArrayList<>();

        virtualSet = new HashSet<>();
        for (int i = 0; i < Constants.ITEM_COUNT; i++)
            virtualSet.add(i);

        for (Set<Integer> visitedIds : userVisits) {

            currentSet = visitedIds;

            // %80 nini al
            trainingSet.add(getRandomlySet(80));

            // kalan?n %50 si
            validationSet.add(getRandomlySet(50));

            // kalan? al
            testSet.add(currentSet);

        }

        Helpers.dumpSetForGraph("Training Set", trainingSet);
        Helpers.dumpSetForGraph("Validation Set", validationSet);
        Helpers.dumpSetForGraph("Test Set", testSet);

        notVisitedTrainingSet = new ArrayList<>();
        for (Set<Integer> visitedIds : trainingSet) {
            Set<Integer> newSet = new HashSet<>();
            newSet.addAll(virtualSet);
            newSet.removeAll(visitedIds);
            notVisitedTrainingSet.add(newSet);
        }

        // bir item hangi userlar taraf?ndan clicklenmi?
        itemVisitorsMap = new HashMap<>();
        for (int userId = 0; userId < Constants.ITEM_COUNT; userId++) {
            List<Integer> userList = new ArrayList<>();
            for (int rowId = 0; rowId < trainingSet.size(); rowId++)
                for (Integer subSet : trainingSet.get(rowId))
                    if (subSet.equals(userId))
                        userList.add(rowId);

            itemVisitorsMap.put(userId, userList);
        }
    }

    public Set<Integer> getRandomlySet(int subSetPercent) {
        Set<Integer> result = new HashSet<>();
        Random rnd = new Random();

        int counter = 0;
        int subSetSize = currentSet.size() * subSetPercent / 100;
        List<Integer> sourceArr = new ArrayList<>();
        sourceArr.addAll(currentSet);

        while (counter < subSetSize) {
            int selectedIndex = rnd.nextInt(currentSet.size() - 1);
            Integer obj = sourceArr.get(selectedIndex);
            result.add(obj);
            sourceArr.remove(obj);
            currentSet.remove(obj);
            counter++;
        }

        return result;
    }

    public void dumpTrainingSet()   { dump(trainingSet);    }
    public void dumpValidationSet() { dump(validationSet); }
    public void dumpTestSet()       { dump(testSet);        }

    public void dump(List<Set<Integer>> list) {
        for (Set<Integer> item : list)
            System.out.println(item.size() + "\t" + Arrays.toString(item.toArray()));
    }

    public List<Set<Integer>> getTrainingSet() {
        return trainingSet;
    }

    public List<Set<Integer>> getValidationSet() {
        return validationSet;
    }

    public Set<Integer> getNotVisitedTrainingItemByUserIndex(int userIndex) {
        return notVisitedTrainingSet.get(userIndex);
    }

    public List<Integer> getTrainingSetVisitorsByItemIndex(int itemIndex) {
        return itemVisitorsMap.get(itemIndex);
    }

    public List<Set<Integer>> getTestSet() {
        return testSet;
    }

}
