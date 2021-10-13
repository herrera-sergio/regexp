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
package it.units.inginf.male.conflict.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author dotpolimi
 */
public class ConflictGroup {
    
    private String groupID;
    private List<Conflict> conflicts;
    
    public ConflictGroup() {
        this.groupID = new String();
    }

    public ConflictGroup(String groupID, List<Conflict> conflicts) {
        this.groupID = groupID;
        this.conflicts = conflicts;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public List<Conflict> getConflicts() {
        return conflicts;
    }

    public void setConflicts(List<Conflict> conflicts) {
        this.conflicts = conflicts;
    }

    @Override
    public String toString() {
        return "ConflictGroup{" + "groupID=" + groupID + ", conflicts=" + conflicts + '}';
    }

    public void randomizedConflicts(){
        Collections.shuffle(conflicts);
    }
    
    
}
