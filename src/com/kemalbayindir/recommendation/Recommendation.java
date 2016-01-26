package com.kemalbayindir.recommendation;

import java.util.*;

/**
 * Created by Kemal BAYINDIR on 5/12/2015.
 */
public class Recommendation {

    private double[][] similarityMat;
    private RecommendationDataManager dm;

    public Recommendation(List<Set<Integer>> userVisits) {

        // split data -> training, validation, test
        dm = new RecommendationDataManager(userVisits);
        // DEBUG ::::: dm.dumpTrainingSet();
        // DEBUG ::::: dm.dumpValidationSet();
        // DEBUG ::::: dm.dumpTestSet();

        // create user similarity matrix via jaccard similarity
        similarityMat = new double[dm.getTrainingSet().size()][dm.getTrainingSet().size()];
        for (int x = 0; x < dm.getTrainingSet().size(); x++) {
            for (int y = 0; y < dm.getTrainingSet().size(); y++) {

                similarityMat[x][y] = 0;
                if (x != y)
                    similarityMat[x][y] = RecommendationSimilarity.jaccard(dm.getTrainingSet().get(x), dm.getTrainingSet().get(y));

                // DEBUG ::::: System.out.print(similarityMat[x][y] + "\t");
            }
            // DEBUG ::::: System.out.println();
        }

        Helpers.out("***** RECOMMENDATIONS *****", true);

        Double maxRate = 0.0;
        int bestK = 0;
        for (int k = Constants.MIN_K_VALUE; k < Constants.MAX_K_VALUE; k++) {

            Double newRate = getNotVisitedItemRates(k, dm.getValidationSet());
            if (newRate > maxRate) {
                maxRate = newRate;
                bestK = k;
            }

        }

        getNotVisitedItemRates(bestK, dm.getTestSet());
    }

    public Double getNotVisitedItemRates(int K, List<Set<Integer>> set) {

        Double recommendationRateTotal = 0.0;
        Double precisionTotal = 0.0;

        //List<Map<Integer, Double>> result = new ArrayList<>();
        StringBuilder log = new StringBuilder();

        int userCount = dm.getTrainingSet().size();
        for (int userIndex = 0; userIndex < userCount; userIndex++) {
            //System.out.print(".");
            Map<Integer, Double> map = new HashMap<>();

            Set<Integer> notVisitedItemsByUser = dm.getNotVisitedTrainingItemByUserIndex(userIndex);

            // tüm ziyaret edilmemi? itemlar için dön
            for (Integer notVisitedItemIndex : notVisitedItemsByUser) {

                // ziyaret edilmemi?i ziyaret edenleri bul
                List<Integer> visitorsOfNotVisitedItems = dm.getTrainingSetVisitorsByItemIndex(notVisitedItemIndex);

                if (visitorsOfNotVisitedItems.size() > Constants.MIN_VISITOR_TRESHOLD && visitorsOfNotVisitedItems.size() >= K) {

                    // ziyaret edilmemi?i ziyaret edenlerin similaritylerinin ortalamas?n? al
                    List<Double> visitorSimilarities = new ArrayList<>();
                    for (Integer visitorId : visitorsOfNotVisitedItems)
                        visitorSimilarities.add(similarityMat[userIndex][visitorId]);

                    // ziyaretçi sim lerini s?rala
                    Collections.sort(visitorSimilarities, Collections.reverseOrder());

                    Double similarityTotal = 0.0;
                    for (int k = 0; k < K; k++)
                        similarityTotal += visitorSimilarities.get(k);

                    // sim toplamlar?n?n ortalamas?n? al
                    similarityTotal = similarityTotal / Double.valueOf(K);

                    map.put(notVisitedItemIndex, similarityTotal);

                }

            }

            map = Helpers.sortByComparator(map, false);

            // validation/test set ile kar??la?t?rma yap?l?yor.
            int topn = 0;
            Set<Integer> comparer = new HashSet<>();
            for (Map.Entry<Integer, Double> entry : map.entrySet()) {
                if (topn > Constants.TOPN) break;
                comparer.add(entry.getKey());
                topn++;
            }

            comparer.retainAll(set.get(userIndex));
            Double recall = Double.valueOf(comparer.size()) / Double.valueOf(set.get(userIndex).size());
            Double precision = Double.valueOf(comparer.size()) / Constants.TOPN;
            //log.append("for user " + userIndex + ", success recommendation " + comparer.size() + "/" + set.get(userIndex).size() + " = " + recall + "\n");
            System.out.println(K + "\t" + userIndex + "\t" + precision + "\t" + recall);
            recommendationRateTotal += recall;
            precisionTotal += precision;
            //result.add(map);
        }

        Helpers.out(log.toString(), false);
        Double rate = Double.valueOf(recommendationRateTotal) / Double.valueOf(userCount);
        //System.out.println();
        //System.out.println(K + " precision : " + precisionTotal + " recall : " + recommendationRateTotal + " / " + userCount + " = " + String.valueOf(rate));

        //Helpers.dumpMap(result);
        return rate;
    }

    public double[][] getSimilarityMat() {
        return similarityMat;
    }

    public void setSimilarityMat(double[][] similarityMat) {
        this.similarityMat = similarityMat;
    }
}
