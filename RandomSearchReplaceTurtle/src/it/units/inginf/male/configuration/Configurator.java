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
 */
package it.units.inginf.male.configuration;

import com.google.gson.Gson;
import it.units.inginf.male.conflict.model.ConflictGroup;
import it.units.inginf.male.utils.Range;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andrea 
 */
public class Configurator {
    public static Configuration configure(String fileName) throws IOException {
        FileInputStream fis = new FileInputStream(new File(fileName));
        InputStreamReader isr = new InputStreamReader(fis);
        StringBuilder sb;
        try (BufferedReader bufferedReader = new BufferedReader(isr)) {
            sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        }
        String json = sb.toString();
        Gson gson = new Gson();
        Configuration configuration = gson.fromJson(json, Configuration.class);
        
        configuration.setup();
        return configuration;
    }
    
    public static Configuration configure(String fileName,String dataSetFileName ,ConflictGroup conflictGroup) throws IOException {
        FileInputStream fis = new FileInputStream(new File(fileName));
        InputStreamReader isr = new InputStreamReader(fis);
        StringBuilder sb;
        try (BufferedReader bufferedReader = new BufferedReader(isr)) {
            sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        }
        String json = sb.toString();
        Gson gson = new Gson();
        Configuration configuration = gson.fromJson(json, Configuration.class);
        //modify the ranges
        List<Range> training = new ArrayList<>();
        List<Range> validation = new ArrayList<>();
        List<Range> testing = new ArrayList<>();
        int trainingUpperlimit=0;
        int validationUpperLimit=0;
        int testingUpperLimit=0;
            
        
        System.out.println("Conflict Group Size: -------------"+conflictGroup.getConflicts().size());
        if(conflictGroup.getConflicts().size()<=3){
            training.add(new Range(0,0));
            configuration.getDatasetContainer().setTraining(training);
            validation.add(new Range(1,1));
            configuration.getDatasetContainer().setValidation(validation);
            testing.add(new Range(2,2));
            configuration.getDatasetContainer().setTesting(testing);
        }
        else if(conflictGroup.getConflicts().size()>3 && conflictGroup.getConflicts().size()<=10){
            if(conflictGroup.getConflicts().size() % 2 == 0){
                trainingUpperlimit=(int) Math.ceil(conflictGroup.getConflicts().size()*.45);
                validationUpperLimit=(int) Math.floor(conflictGroup.getConflicts().size()*.45);
                testingUpperLimit=(int) Math.ceil(conflictGroup.getConflicts().size()*.10);
            }
            else{
                trainingUpperlimit=(int) Math.floor(conflictGroup.getConflicts().size()*.45);
                validationUpperLimit=(int) Math.floor(conflictGroup.getConflicts().size()*.45);
                testingUpperLimit=(int) Math.ceil(conflictGroup.getConflicts().size()*.10);
            }
            
            training.add(new Range(0,trainingUpperlimit-1));
            configuration.getDatasetContainer().setTraining(training);
            validation.add(new Range(trainingUpperlimit,trainingUpperlimit+validationUpperLimit-1));
            configuration.getDatasetContainer().setValidation(validation);
            testing.add(new Range(trainingUpperlimit+validationUpperLimit,(conflictGroup.getConflicts().size()-1)));
            configuration.getDatasetContainer().setTesting(testing);
        
        } else {
            //check the logic for even numbers
            int firstDigit=(conflictGroup.getConflicts().size()/10)%2;
            int lastDigit=(conflictGroup.getConflicts().size())%2;
            int dividedBy10=(conflictGroup.getConflicts().size())%10;
            if(dividedBy10==0){
                firstDigit=((conflictGroup.getConflicts().size()-1)%10)%2;
            }
            
         //   System.out.println("first-"+firstDigit+"last-"+lastDigit);
            
            if(firstDigit==0){
                System.out.print("even-");
                if(lastDigit==0){
               //     System.out.println("-even");
                    trainingUpperlimit=(int) Math.ceil(conflictGroup.getConflicts().size()*.45);
                    validationUpperLimit=(int) Math.floor(conflictGroup.getConflicts().size()*.45);
                    testingUpperLimit=(int) Math.ceil(conflictGroup.getConflicts().size()*.10);
                }
                else{
                //    System.out.println("-odd");
                    trainingUpperlimit=(int) Math.floor(conflictGroup.getConflicts().size()*.45);
                    validationUpperLimit=(int) Math.floor(conflictGroup.getConflicts().size()*.45);
                    testingUpperLimit=(int) Math.ceil(conflictGroup.getConflicts().size()*.10);
                }
            }
            else{
                 System.out.print("odd-");
                if(lastDigit==0){
                    System.out.println("-even");
                    trainingUpperlimit=(int) Math.floor(conflictGroup.getConflicts().size()*.45);
                    validationUpperLimit=(int) Math.floor(conflictGroup.getConflicts().size()*.45);
                    testingUpperLimit=(int) Math.ceil(conflictGroup.getConflicts().size()*.10);
                }
                else{
                    System.out.println("-odd");
                    trainingUpperlimit=(int) Math.ceil(conflictGroup.getConflicts().size()*.45);
                    validationUpperLimit=(int) Math.floor(conflictGroup.getConflicts().size()*.45);
                    testingUpperLimit=(int) Math.ceil(conflictGroup.getConflicts().size()*.10);
                }
            }
            
            training.add(new Range(0,trainingUpperlimit-1));
            configuration.getDatasetContainer().setTraining(training);
            validation.add(new Range(trainingUpperlimit,trainingUpperlimit+validationUpperLimit-1));
            configuration.getDatasetContainer().setValidation(validation);
            testing.add(new Range(trainingUpperlimit+validationUpperLimit,conflictGroup.getConflicts().size()-1));
            configuration.getDatasetContainer().setTesting(testing);
        }
        //end modify the ranges
        configuration.getDatasetContainer().setPath(dataSetFileName);
        configuration.setup();
        return configuration;
    }
}
