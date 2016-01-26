package com.kemalbayindir.recommendation;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Kemal BAYINDIR on 5/12/2015.
 */
public class RecommendationSimilarity {

    public static double jaccard(Set<Integer> s1, Set<Integer> s2) {
        Set<Integer> intersection = new HashSet<>(s1);
        intersection.retainAll(s2);
        int intersectionLength = intersection.size();

        Set<Integer> s1Clone = new HashSet<>(s1);
        addNoDups(s1Clone, s2);
        int unionLength = s1Clone.size();

        return (double)intersectionLength/(double)unionLength;
    }

    private static void addNoDups(Set<Integer> toAddTo, Set<Integer> iterateOver) {
        for(Integer num: iterateOver){
            if(!toAddTo.contains(num))
                toAddTo.add(num);
        }
    }

}
