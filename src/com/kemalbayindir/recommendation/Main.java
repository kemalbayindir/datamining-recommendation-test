package com.kemalbayindir.recommendation;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Kemal BAYINDIR on 5/12/2015.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        List<Set<Integer>> visits = new ArrayList<>();

        File f = new File(Constants.DATA_PATH + Constants.DATA_FILE_NAME);
        if(f.exists() && !f.isDirectory()) {
            visits = RecommendationDataLoader.loadData(Constants.DATA_FILE_NAME);
        } else {
            // DATA GENERATION
            
            DataGenerator gen = new DataGenerator();
            System.out.println();
            System.out.println("*****************************");
            System.out.println("* 50 (>50 <100)");
            System.out.println("*****************************");
            visits.addAll(gen.genRandomVisitor(50, 50, 100));

            System.out.println();
            System.out.println("*****************************");
            System.out.println("* 100 (>100 <200)");
            System.out.println("*****************************");
            visits.addAll(gen.genRandomVisitor(100, 100, 200));

            System.out.println();
            System.out.println("*****************************");
            System.out.println("* 100 (>500 <1000)");
            System.out.println("*****************************");
            visits.addAll(gen.genRandomVisitor(100, 500, 1000));

            /*System.out.println();
            System.out.println("*****************************");
            System.out.println("* 250 (>200 <500)");
            System.out.println("*****************************");
            visits.addAll(gen.genRandomVisitor(250, 200, 500));
            */
            Helpers.save2File(Helpers.list2String(visits), Constants.DATA_FILE_NAME);

        }

        System.out.println();
        int total = 0;
        for (Set<Integer> row : visits) {
            total += row.size();
        }

        Helpers.out("SPARSITY : " + (double)total / (Constants.ITEM_COUNT * Constants.USER_COUNT), true);

        new Recommendation(visits);
    }
}
