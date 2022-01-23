/*
 * Copyright (C) 2019 dotpolimi
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
package it.units.inginf.male.conflict.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.units.inginf.male.conflict.model.Regexp;
import it.units.inginf.male.utils.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dotpolimi
 */
public class RegexWriter {

    private static final int MAX_RESULT = 8;

    public static void save(String fileName, String group, String regexp, String replacement) throws IOException {
        //ConflictGroup conflictGroup;
        //List<Conflict> list = new ArrayList();

        File tmpDir = new File(fileName);
        if (tmpDir.exists()) {
            update(fileName, group, regexp, replacement);
        } else {
            create(fileName, group, regexp, replacement);
        }
    }


    public static void saveTree(String fileName, String group, List<Regexp> regexps) throws IOException {
        //ConflictGroup conflictGroup;
        //List<Conflict> list = new ArrayList();
        String splitFilename = fileName + group + ".json";
        File tmpDir = new File(splitFilename);
        if (tmpDir.exists()) {
            update(splitFilename, group, regexps, true);
        } else {
            create(splitFilename, group, regexps, true);
        }
    }


    public static void save(String fileName, String group, List<Regexp> regexps) throws IOException {
        //ConflictGroup conflictGroup;
        //List<Conflict> list = new ArrayList();

        File tmpDir = new File(fileName);
        if (tmpDir.exists()) {
            update(fileName, group, regexps, false);
        } else {
            create(fileName, group, regexps, false);
        }
    }

    private static void update(String fileName, String group, String regexp, String replacement) throws IOException {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        JsonObject parent = null;
        try {
            fis = new FileInputStream(fileName);
            isr = new InputStreamReader(fis);
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
            //ConflictGroup group = gson.fromJson(json, ConflictGroup.class);
            parent = gson.fromJson(json, JsonObject.class);
            JsonObject conflict_group = parent.getAsJsonObject(group);
            if (conflict_group != null) {
                parent.getAsJsonObject(group).addProperty("regex", regexp);
                parent.getAsJsonObject(group).addProperty("replacement", replacement);
            } else {
                JsonObject child = new JsonObject();
                child.addProperty("regex", regexp);
                child.addProperty("replacement", replacement);
                parent.add(group, child);
            }
        } catch (IOException ioe) {
            throw ioe;
        } finally {
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }

        // try-with-resources statement based on post comment below :)
        FileWriter file = new FileWriter(fileName);
        try {
            file.write(parent.toString());
//            System.out.println("Successfully Copied JSON Object to File...");
//            System.out.println("\nJSON Object: " + parent);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            file.flush();
            file.close();
        }
    }

    private static void update(String fileName, String group, List<Regexp> regExp, boolean serialized) throws IOException {
        FileInputStream fis = null;
        InputStreamReader isr = null; //new InputStreamReader(fis);
        JsonObject parent;
        try {
            fis = new FileInputStream(fileName);
            isr = new InputStreamReader(fis);
            StringBuilder sb;
            try (BufferedReader bufferedReader = new BufferedReader(isr)) {
                sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
            }

            JsonArray childArray = new JsonArray();
            for (Regexp regexp : regExp) {
                JsonObject child_expresion = new JsonObject();
                child_expresion.addProperty("regex", serialized ? regexp.getSerializedRegexp() : regexp.getRegexp());
                child_expresion.addProperty("replacement", serialized ? regexp.getSerializedReplacement() : regexp.getReplacement());
                childArray.add(child_expresion);
            }

            String json = sb.toString();
            Gson gson = new Gson();
            //ConflictGroup group = gson.fromJson(json, ConflictGroup.class);
            parent = gson.fromJson(json, JsonObject.class);
            JsonArray conflict_group = parent.getAsJsonArray(group);
            if (conflict_group != null) {
                //parent.remove(group);
                parent.add(group, mergeJsonArray(conflict_group, childArray));
            } else {
                parent.add(group, childArray);
            }
        } catch (IOException ioe) {
            throw ioe;
        } finally {
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        // try-with-resources statement based on post comment below :)
        FileWriter file = new FileWriter(fileName);
        try {
            file.write(parent.toString());
//            System.out.println("Successfully Copied JSON Object to File...");
//            System.out.println("\nJSON Object: " + parent);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            file.flush();
            file.close();
        }
    }

    private static void create(String fileName, String group, String regexp, String replacement) throws IOException {
        //ConflictGroup conflictGroup;
        //List<Conflict> list = new ArrayList();

        JsonObject parent = new JsonObject();
        JsonObject child = new JsonObject();
        child.addProperty("regex", regexp);
        child.addProperty("replacement", replacement);
        parent.add(group, child);

        // try-with-resources statement based on post comment below :)
        FileWriter file = new FileWriter(fileName);
        try {
            file.write(parent.toString());
//            System.out.println("Successfully Copied JSON Object to File...");
//            System.out.println("\nJSON Object: " + parent);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            file.flush();
            file.close();
        }
    }

    private static void create(String fileName, String group, List<Regexp> regExp, boolean serialized) throws IOException {
        //ConflictGroup conflictGroup;
        //List<Conflict> list = new ArrayList();

        JsonObject parent = new JsonObject();
        JsonArray childArray = new JsonArray();
        for (Regexp regexp : regExp) {
            JsonObject child_expresion = new JsonObject();
            child_expresion.addProperty("regex", serialized ? regexp.getSerializedRegexp() : regexp.getRegexp());
            child_expresion.addProperty("replacement", serialized ? regexp.getSerializedReplacement() : regexp.getReplacement());
            childArray.add(child_expresion);
        }
        parent.add(group, childArray);

        // try-with-resources statement based on post comment below :)
        FileWriter file = new FileWriter(fileName);
        try {
            file.write(parent.toString());
//            System.out.println("Successfully Copied JSON Object to File...");
//            System.out.println("\nJSON Object: " + parent);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            file.flush();
            file.close();
        }
    }

    private static JsonArray mergeJsonArray(JsonArray conflictGroup, JsonArray newElements) {
        List<Pair<String, String>> existingCrr = fromJsonArrayToCrrList(conflictGroup);

        int i = 0;
        while(existingCrr.size() + newElements.size() > MAX_RESULT) {
            existingCrr.remove(i);
            i++;
        }
        existingCrr.addAll(fromJsonArrayToCrrList(newElements));
        return fromCrrListToJsonArray(existingCrr.stream().distinct().collect(Collectors.toList()));
    }

    private static List<Pair<String, String>> fromJsonArrayToCrrList(JsonArray jsonArray) {
        List<Pair<String, String>> crrList = new ArrayList<>();
        jsonArray.forEach(el -> {
            JsonObject jO = el.getAsJsonObject();
            crrList.add(new Pair<>(jO.getAsJsonPrimitive("regex").getAsString(),
                    jO.getAsJsonPrimitive("replacement").getAsString()));
        });
        return crrList;
    }


    private static JsonArray fromCrrListToJsonArray(List<Pair<String, String>> crrList) {

        JsonArray childArray = new JsonArray();
        crrList.forEach(crr -> {
            JsonObject child_expresion = new JsonObject();
            child_expresion.addProperty("regex", crr.getFirst());
            child_expresion.addProperty("replacement", crr.getSecond());
            childArray.add(child_expresion);
        });
        return childArray;
    }
}
