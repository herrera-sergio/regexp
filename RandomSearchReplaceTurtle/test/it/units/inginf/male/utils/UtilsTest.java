/*
 * Copyright (C) 2018 Machine Learning Lab - University of Trieste, 
 * Italy (http://machinelearning.inginf.units.it/)  
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */package it.units.inginf.male.utils;

import it.units.inginf.male.inputs.Bounds;
import it.units.inginf.male.tree.Constant;
import it.units.inginf.male.tree.Node;
import it.units.inginf.male.tree.operator.Concatenator;
import it.units.inginf.male.tree.operator.MatchOneOrMore;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.apache.commons.text.similarity.FuzzyScore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by andrea on 21/11/16.
 */
public class UtilsTest {

    @Test
    public void testcomputeLevenshteinDistance() {
        assertEquals(0,Utils.computeLevenshteinDistance("prova","prova"));
        assertEquals(3,Utils.computeLevenshteinDistance("provaxxx","prova"));
        assertEquals(1,Utils.computeLevenshteinDistance("prova","prov"));
        assertEquals(3,Utils.computeLevenshteinDistance("prova","provaxxx"));
        assertEquals(1,Utils.computeLevenshteinDistance("prov","prova"));
        assertEquals(3,Utils.computeLevenshteinDistance("ov","prova"));
        assertEquals(2,Utils.computeLevenshteinDistance("prova","ova"));
        assertEquals(1,Utils.computeLevenshteinDistance("prova","prxva"));
        assertEquals(0,Utils.computeLevenshteinDistance("",""));

        FuzzyScore fuzzyScore = new FuzzyScore(Locale.ENGLISH);
        long startFuzzy = System.nanoTime();
        System.out.println(fuzzyScore.fuzzyScore("prov", "provaty"));
        long timeFuzzy = System.nanoTime() - startFuzzy;

        long startLevensthein = System.nanoTime();
        System.out.println(Utils.computeLevenshteinDistance("provaxjksdnvjksndjkvnsjknvvjkkbvsdbjvsiosfioahiofhdiohfdifhsdiohfiosdhfiosdhfiohsdiofhsdhijkbvsdjkbvjksdbvjksbvjksbdxxprovaxjksdnvjksndjkvnsjknvvjkkbvsdbjvsiosfioahiofhdiohfdifhsdiohfiosdhfiosdhfiohsdiofhsdhijkbvsdjkbvjksdbvjksbvjksbdxx","provayy"));
        long timeLevensthein = System.nanoTime() - startLevensthein;

        System.out.println("Fuzzy " +  timeFuzzy);
        System.out.println("Levensthein " + timeLevensthein);
        System.out.println("TIME DIFFERENCE " + (timeLevensthein - timeFuzzy));
    }

    @Test
    public void testcomputeLUkkonenDistance() {
        assertEquals(6,Utils.computeUkkonenDistance("Ukkonen","Levenshtein", 6));
        assertEquals(8,Utils.computeUkkonenDistance("Ukkonen","Levenshtein", 10));
        assertEquals(8,Utils.computeUkkonenDistance("Ukkonen","Levenshtein", null));
        assertEquals(Utils.computeUkkonenDistance("Ukkonen","Levenshtein", 10), Utils.computeLevenshteinDistance("Ukkonen","Levenshtein"));
//        assertEquals(3,Utils.computeUkkonenDistance("prova","provaxxx"));
//        assertEquals(1,Utils.computeUkkonenDistance("prov","prova"));
//        assertEquals(3,Utils.computeUkkonenDistance("ov","prova"));
//        assertEquals(2,Utils.computeUkkonenDistance("prova","ova"));
//        assertEquals(1,Utils.computeUkkonenDistance("prova","prxva"));
//        assertEquals(0,Utils.computeUkkonenDistance("",""));



        int better = 0;
        for(int i= 0; i < 300; i++){

            byte[] array = new byte[i+1]; // length is bounded by 7
            new Random().nextBytes(array);
            String s1 = new String(array, StandardCharsets.UTF_8);
            new Random().nextBytes(array);
            String s2 = new String(array, StandardCharsets.UTF_8);

            System.out.println("S1 " + s1 + " S2" + s2);

            long startFuzzy = System.nanoTime();
            System.out.println(Utils.computeUkkonenDistance(s1,s2, i));
            long timeFuzzy = System.nanoTime() - startFuzzy;

            long startLevensthein = System.nanoTime();
            System.out.println(Utils.computeLevenshteinDistance(s1, s2));
            long timeLevensthein = System.nanoTime() - startLevensthein;

            if(timeFuzzy < timeLevensthein){
                better++;
            }
        }

        System.out.println("better "  + better);

        long startFuzzy = System.nanoTime();
        System.out.println(Utils.computeUkkonenDistance("provajkdhusdhvuiduihvuisdbvuibdusihcuisdhcuihsduichuisdhcuihsduichsduihcuisdhcuisdhcuisdhuisdhuicduhhsduihcisdsduiprovajkdhusdhvuiduihvuisdbvuibdusihcuisdhcuihsduichuisdhcuihsduichsduihcuisdhcuisdhcuisdhuisdhuicduhhsduihcisdsdui","provajkdhusdhvuiduihvuisdbvuibdusihcuisdhcuihsduichuisdhcuihsduichsduihcuisdhcuisdhcuisdhuisdhuicduhhsduihcisdsduiprovajkdhusdhvuiduihvuisdbvuibdusihcuisdhcuihsduichuisdhcuihsduichsduihcuisdhcuisdhcuisdhuisdhuicduhhsduihcisdsduiprovaxxxxxxxxxxxxxxxxxxxxxajskhchuighaishchuiahsihaisuh", null));
        long timeFuzzy = System.nanoTime() - startFuzzy;

        long startLevensthein = System.nanoTime();
        System.out.println(Utils.computeLevenshteinDistance("provajkdhusdhvuiduihvuisdbvuibdusihcuisdhcuihsduichuisdhcuihsduichsduihcuisdhcuisdhcuisdhuisdhuicduhhsduihcisdsduiprovajkdhusdhvuiduihvuisdbvuibdusihcuisdhcuihsduichuisdhcuihsduichsduihcuisdhcuisdhcuisdhuisdhuicduhhsduihcisdsdui","provajkdhusdhvuiduihvuisdbvuibdusihcuisdhcuihsduichuisdhcuihsduichsduihcuisdhcuisdhcuisdhuisdhuicduhhsduihcisdsduiprovajkdhusdhvuiduihvuisdbvuibdusihcuisdhcuihsduichuisdhcuihsduichsduihcuisdhcuisdhcuisdhuisdhuicduhhsduihcisdsduiprovaxxxxxxxxxxxxxxxxxxxxxajskhchuighaishchuiahsihaisuh"));
        long timeLevensthein = System.nanoTime() - startLevensthein;

        System.out.println("Ukkonen " +  timeFuzzy);
        System.out.println("Levensthein " + timeLevensthein);
        System.out.println("TIME DIFFERENCE " + (timeLevensthein - timeFuzzy));
    }
    
    @Test
    public void testfindLargestCommonSubstringNew(){
        String first = "ciao 13 2013 4078 40";
        String second = "2013-13xxxxxxxx-40-4078";
        List<Pair<Bounds, Bounds>> expected = Arrays.asList(new Pair<>(new Bounds(5, 7),new Bounds(5, 7)),
                new Pair<>(new Bounds(8, 12),new Bounds(0, 4)),
                new Pair<>(new Bounds(13, 17),new Bounds(19, 23)),
                new Pair<>(new Bounds(18, 20),new Bounds(16, 18))                
                );
        List<Pair<Bounds, Bounds>> outcome = Utils.findLargestCommonSubstringNew(first, second);
        
        assertEquals(expected, outcome);
    }
    
    /**
     * Test of complexityRegex method, of class Utils.
     */
    public void testComplexity() {
        System.out.println("complexity");
        Node node = new Concatenator();
        List<Node> childrens = node.getChildrens();
        childrens.add(new MatchOneOrMore());
        childrens.add(new Constant("ab"));
        childrens.get(0).getChildrens().add(new Constant("\\w"));
        boolean pushGeneralization = true;
        double expResult = 3.4;
        double result = Utils.complexityRegex(node, pushGeneralization);
        assertEquals(expResult, result);
    }

    
}
