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

/**
 * @author dotpolimi
 */
public class Regexp {

    String regexp = null;
    String replacement = null;
    String serializedRegexp;
    String serializedReplacement;

    public Regexp() {
        this.regexp = new String();
        this.replacement = new String();
    }

    public Regexp(String regexp, String replacement) {
        this.regexp = regexp;
        this.replacement = replacement;
    }

    public String getRegexp() {
        return regexp;
    }

    public void setRegexp(String regexp) {
        this.regexp = regexp;
    }

    public String getReplacement() {
        return replacement;
    }

    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }

    public String getSerializedRegexp() {
        return serializedRegexp;
    }

    public void setSerializedRegexp(String serializedRegexp) {
        this.serializedRegexp = serializedRegexp;
    }

    public String getSerializedReplacement() {
        return serializedReplacement;
    }

    public void setSerializedReplacement(String serializedReplacement) {
        this.serializedReplacement = serializedReplacement;
    }
}
