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

import it.units.inginf.male.conflict.model.ConflictGroup;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author dotpolimi
 */
public class ConflictWriter {
    
    public static void write(String fileName, ConflictGroup group) throws IOException {
        FileWriter csvWriter = new FileWriter(fileName);
        csvWriter.append("Conflict");
        csvWriter.append(",");
        csvWriter.append("Resolution");
        csvWriter.append("\n");
        
        if(group.getConflicts().size()==1){
             for (int i=0;i<3;i++) {
                 
                if(group.getConflicts().get(0).getConflict().startsWith("@")&&group.getConflicts().get(0).getResolution().startsWith("@")){
                    String conflict_noAt=group.getConflicts().get(0).getConflict().replace("@", "");
                    String resolution_noAt=group.getConflicts().get(0).getResolution().replace("@", "");
                    csvWriter.append(String.join(",", 
                                             ConflictWriter.escapeSpecialCharacters(conflict_noAt), 
                                             ConflictWriter.escapeSpecialCharacters(resolution_noAt)));
                    csvWriter.append("\n");
                }
                else {
                    csvWriter.append(String.join(",", 
                                             ConflictWriter.escapeSpecialCharacters(group.getConflicts().get(0).getConflict()), 
                                             ConflictWriter.escapeSpecialCharacters(group.getConflicts().get(0).getResolution())));
                    csvWriter.append("\n");
                }
            }
        }
        else if(group.getConflicts().size()==2){
            if((group.getConflicts().get(0).getConflict().startsWith("@")&&group.getConflicts().get(0).getResolution().startsWith("@"))&&
               (group.getConflicts().get(1).getConflict().startsWith("@")&&group.getConflicts().get(1).getResolution().startsWith("@"))){
                
                    String conflict_noAt=group.getConflicts().get(0).getConflict().replace("@", "");
                    String resolution_noAt=group.getConflicts().get(0).getResolution().replace("@", "");
                    csvWriter.append(String.join(",", 
                                             ConflictWriter.escapeSpecialCharacters(conflict_noAt), 
                                             ConflictWriter.escapeSpecialCharacters(resolution_noAt)));
                    csvWriter.append("\n");
                    csvWriter.append(String.join(",", 
                                             ConflictWriter.escapeSpecialCharacters(conflict_noAt), 
                                             ConflictWriter.escapeSpecialCharacters(resolution_noAt)));
                    csvWriter.append("\n");
                    conflict_noAt=group.getConflicts().get(1).getConflict().replace("@", "");
                    resolution_noAt=group.getConflicts().get(1).getResolution().replace("@", "");
                    csvWriter.append(String.join(",", 
                                             ConflictWriter.escapeSpecialCharacters(conflict_noAt), 
                                             ConflictWriter.escapeSpecialCharacters(resolution_noAt)));
                    csvWriter.append("\n");
            } else {
                for (int i=0;i<2;i++) {
                    csvWriter.append(String.join(",", 
                                             ConflictWriter.escapeSpecialCharacters(group.getConflicts().get(0).getConflict()), 
                                             ConflictWriter.escapeSpecialCharacters(group.getConflicts().get(0).getResolution())));
                    csvWriter.append("\n");
                }
                csvWriter.append(String.join(",", 
                                             ConflictWriter.escapeSpecialCharacters(group.getConflicts().get(1).getConflict()), 
                                             ConflictWriter.escapeSpecialCharacters(group.getConflicts().get(1).getResolution())));
                csvWriter.append("\n");
            }
        }
        else if(group.getConflicts().size()>=3){
            int with_at=0;
            for (int i=0;i<group.getConflicts().size();i++) {
                if(group.getConflicts().get(i).getConflict().startsWith("@") && group.getConflicts().get(i).getResolution().startsWith("@")){
                        with_at++;
                }
            }
            
            
            if(with_at/group.getConflicts().size()>.5){
                for (int i=0;i<group.getConflicts().size();i++) {
                    csvWriter.append(String.join(",", 
                                                 ConflictWriter.escapeSpecialCharacters(group.getConflicts().get(i).getConflict().replace("@", "")), 
                                                 ConflictWriter.escapeSpecialCharacters(group.getConflicts().get(i).getResolution().replace("@", ""))));
                    csvWriter.append("\n");
                }
            }else{
                for (int i=0;i<group.getConflicts().size();i++) {
                    csvWriter.append(String.join(",", 
                                                 ConflictWriter.escapeSpecialCharacters(group.getConflicts().get(i).getConflict()), 
                                                 ConflictWriter.escapeSpecialCharacters(group.getConflicts().get(i).getResolution())));
                    csvWriter.append("\n");
                }
            }
        }
        csvWriter.flush();
        csvWriter.close();
    }
    
    public static String escapeSpecialCharacters(String data) {
        //data=data.substring(0,data.length()-1);
        data=data.trim();
       // data=data.replaceAll("\\\\\"" , "\"");
        //data=data.substring(0,data.length()-2)+"\"";
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
    
}
