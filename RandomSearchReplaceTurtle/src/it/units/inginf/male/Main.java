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
package it.units.inginf.male;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import it.units.inginf.male.configuration.Configuration;
import it.units.inginf.male.configuration.Configurator;
import it.units.inginf.male.conflict.model.ConflictGroup;
import it.units.inginf.male.conflict.model.Regexp;
import it.units.inginf.male.conflict.utils.ConflictReader;
import it.units.inginf.male.conflict.utils.ConflictWriter;
import it.units.inginf.male.conflict.utils.RegexReader;
import it.units.inginf.male.conflict.utils.RegexWriter;
import it.units.inginf.male.outputs.FinalSolution;
import it.units.inginf.male.outputs.Results;
import it.units.inginf.male.strategy.ExecutionStrategy;
import it.units.inginf.male.strategy.impl.CoolTextualExecutionListener;
import it.units.inginf.male.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * @author Marco
 */
public class Main {

    /**
     * @param args the command line argumentsRandomSearchReplaceTurtle_ukkonen_recycle.jar
     */
    public static void main(String[] args) throws Exception {
        Properties prop = null;
        ConflictGroup group = null;

        if (args.length < 2) {
            printUsage();
            System.exit(0);
        }
        //arg[0] grupo

        String groupId = "-1";

        for (int i = 1; i < args.length; i++) {
            groupId = args[i];
            //leggere file di configuration
            try {
                prop = loadProperties(args[0]);
                //Call the createTrainingCVS method
                group = ConflictReader.load(prop.getProperty("conflict_file"), groupId);

                ConflictWriter.write(prop.getProperty("dataset_file"), group);
            } catch (IOException ieo) {

                ieo.printStackTrace();

                System.exit(-1);
            }

            try {
                List<Regexp> regexList = new ArrayList<Regexp>();
                FinalSolution solution1 = getBestSolution(prop, group, groupId);
                FinalSolution solution2 = null;

                if (group.getConflicts().size() >= 3) {
                    group.randomizedConflicts();
                    solution2 = getBestSolution(prop, group, groupId);
//                    System.out.println("Group:"+groupId+"Solution 1: ("+solution1.getRegex()+" - "+solution1.getReplacement()+")");
//                    System.out.println("Group:"+groupId+"Solution 2: ("+solution2.getRegex()+" - "+solution2.getReplacement()+")");

                    if (solution1 != null && solution2 != null) {
                        if (solution1.getRegex().equals(solution2.getRegex())) {
                            Regexp regexp1 = createRegexWithSerializedTree(solution1);
                            regexList.add(regexp1);
                        } else {
                            if (!solution1.getRegex().isEmpty()) {
                                Regexp regexp1 = createRegexWithSerializedTree(solution1);
                                regexList.add(regexp1);
                                if (!solution2.getRegex().isEmpty()) {
                                    Regexp regexp2 = createRegexWithSerializedTree(solution2);
                                    regexList.add(regexp2);
                                }
                            } else if (!solution2.getRegex().isEmpty()) {
                                Regexp regexp2 = createRegexWithSerializedTree(solution2);
                                regexList.add(regexp2);
                            }
                        }
                    }

                    RegexWriter.save(prop.getProperty("regex_file"), group.getGroupID(), regexList);

                    try {
                        RegexWriter.saveTree(prop.getProperty("regex_tree_file"), group.getGroupID(), regexList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Regexp regexp1 = createRegexWithSerializedTree(solution1);
                    regexList.add(regexp1);
                    RegexWriter.save(prop.getProperty("regex_file"), group.getGroupID(), regexList);
                    try {
                        RegexWriter.saveTree(prop.getProperty("regex_tree_file"), group.getGroupID(), regexList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }

    private static Regexp createRegexWithSerializedTree(FinalSolution solution) {

        Regexp regexp = new Regexp(solution.getRegex(), solution.getReplacement());
        try {
            regexp.setSerializedRegexp(Utils.serializeTree(solution.getRegexTree()));
            regexp.setSerializedReplacement(Utils.serializeTree(solution.getReplacementTree()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return regexp;
    }

    private static FinalSolution getBestSolution(Properties prop, ConflictGroup group, String groupId) throws Exception {
        //  System.out.println("Conflict Group: "+group.toString());

        ConflictWriter.write(prop.getProperty("dataset_file"), group);

        //give the path of the modified
        //check if a solution for this group already exists and load the proper configuration
        Configuration configuration;
        if (RegexReader.exists(prop.getProperty("regex_tree_file"), groupId))
            configuration = Configurator.configure(prop.getProperty("regex_configuration_performance"), prop.getProperty("dataset_file"), group, prop);
        else
            configuration = Configurator.configure(prop.getProperty("regex_configuration"), prop.getProperty("dataset_file"), group, prop);


//            System.out.println("Dataset training:"+ configuration.getDatasetContainer().getTrainingDataset().getExamples());
//            System.out.println("Dataset validation:"+ configuration.getDatasetContainer().getValidationDataset().getExamples());
//            System.out.println("Dataset testing:"+ configuration.getDatasetContainer().getTestingDataset().getExamples());

        Logger.getLogger("").addHandler(new FileHandler(new File(configuration.getOutputFolder(), "log.xml").getCanonicalPath()));
        Results results = new Results(configuration);
        results.setMachineHardwareSpecifications(Utils.cpuInfo());

        ExecutionStrategy strategy = configuration.getStrategy();
        long startTime = System.currentTimeMillis();
        strategy.execute(configuration, new CoolTextualExecutionListener(groupId, configuration, results));
        //strategy.execute(configuration, new DefaultExecutionListener());
        if (configuration.getPostProcessor() != null) {
            startTime = System.currentTimeMillis() - startTime;
            configuration.getPostProcessor().elaborate(configuration, results, startTime);
        }

        //create a JSON file if it does not exist, and add the group with the regex and the replacement.
        return results.getBestSolution();
    }

    private static void printUsage() {
        //   System.out.println("Usage: java -jar \"Random_Regex_Turtle.jar\" config_path groups");
    }

    private static Properties loadProperties(String path) throws IOException {
        Properties prop;
        try {
            InputStream input = new FileInputStream(path + "config.properties");
            prop = new Properties();
            prop.load(input);
            return prop;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw ioe;
        }
    }

    private void createTrainingCVS(String group, String outputFile) {
        //Read Json for group

        //write the conflicts in CVS format on the outputFile

    }


}
