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

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dotpolimi
 */
public class RegexReader {

    public static List<String> readRegex(String fileName, String group) throws IOException {

        return readSerializedTrees(fileName, group, "regex");
    }

    public static List<String> readReplacement(String fileName, String group) throws IOException {

        return readSerializedTrees(fileName, group, "replacement");
    }

    private static List<String> readSerializedTrees(String fileName, String group, String field) throws IOException {

        File f = new File(fileName);
        if (!f.exists() || f.isDirectory())
            return new ArrayList<>();

        JsonArray groupObject = read(fileName, group);
        List<String> treeList = new ArrayList<>();

        if (groupObject != null) {
            groupObject.getAsJsonArray().forEach(el -> treeList.add(el.getAsJsonObject().getAsJsonPrimitive(field).getAsString()));
        }

        return treeList;
    }

    private static JsonArray read(String fileName, String group) throws IOException {

        try (FileInputStream fis = new FileInputStream(fileName); InputStreamReader isr = new InputStreamReader(fis)) {

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
            return gson.fromJson(json, JsonObject.class).getAsJsonArray(group);

        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw ioe;
        }

    }

}
