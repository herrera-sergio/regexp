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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dotpolimi
 */
public class RegexReader {

    public static List<String> readRegex(String fileName, String group) throws IOException {
        JsonArray groupObject = read(fileName, group);
        List<String> regexList = new ArrayList<>();

        if (groupObject != null) {
            groupObject.getAsJsonArray().forEach(el -> regexList.add(el.getAsJsonObject().getAsJsonPrimitive("regex").getAsString()));
        }

        return regexList;
    }

    public static List<String> readReplacement(String fileName, String group) throws IOException {
        JsonArray groupObject = read(fileName, group);
        List<String> replacementList = new ArrayList<>();

        if (groupObject != null) {
            groupObject.getAsJsonArray().forEach(el -> replacementList.add(el.getAsJsonObject().getAsJsonPrimitive("replacement").getAsString()));
        }

        return replacementList;
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
