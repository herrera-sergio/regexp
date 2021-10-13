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
import it.units.inginf.male.conflict.model.Conflict;
import it.units.inginf.male.conflict.model.ConflictGroup;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.IIOException;

/**
 *
 * @author dotpolimi
 */
public class ConflictReader {
    public static ConflictGroup load(String fileName, String group) throws IOException {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        try{
            ConflictGroup conflictGroup;
            List<Conflict> list = new ArrayList();
            fis = new FileInputStream(new File(fileName));
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
            JsonObject jsonObject= gson.fromJson(json, JsonObject.class);
            JsonArray conflicts=jsonObject.getAsJsonArray(group);
            if (conflicts != null) { 
                int len = conflicts.size();
                for (int i=0;i<len;i++){ 
                    JsonObject obj = conflicts.get(i).getAsJsonObject();
                    list.add(new Conflict(obj.get("conflict").getAsString(), obj.get("resolution").getAsString()));       
                }
            } else{
                throw new IOException("The specified group':"+group+"' was not found in the file:'"+fileName+"'");
            }
            conflictGroup=new ConflictGroup(group,list);
            return conflictGroup;
        }
        catch(IOException ioe){
            throw ioe;
        }
        finally{
            if(isr!=null){
                isr.close();
            }
            if(fis!=null){
                fis.close();
            }
        }
    }
}
