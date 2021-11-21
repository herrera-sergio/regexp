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
package it.units.inginf.male.tree;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author andrea
 */
/**
 * TODO extends constant instead of modify it? Ask for best design.
*
 */
public class Constant extends AbstractNode implements Leaf {

    private Node parent;
    protected String value;
    private boolean charClass;
    private static Set<String> allowedClasses = new HashSet<String>(Arrays.asList("\\w", "\\d", ".", "\\b", "\\s"));
    private boolean escaped;

    public Constant(){}

    public Constant(String value) {
        this.value = value;
        charClass = allowedClasses.contains(value);
        escaped = value.startsWith("\\");
    }

    @Override
    public int getMinChildrenCount() {
        return 0;
    }

    @Override
    public int getMaxChildrenCount() {
        return 0;
    }

    @Override
    public void describe(StringBuilder builder, DescriptionContext context, RegexFlavour flavour) {
        builder.append(value);
    }

    @Override
    public Leaf cloneTree() {
        Constant clone = new Constant(value);
        return clone;
    }

    @Override
    public Node getParent() {
        return parent;
    }

    @Override
    public void setParent(Node parent) {
        this.parent = parent;
    }

    @Override
    public List<Node> getChildrens() {
        return Collections.<Node>emptyList();
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean isCharacterClass() {
        return charClass;
    }

    @Override
    public boolean isEscaped() {
        return escaped;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Constant other = (Constant) obj;
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isCharClass() {
        return charClass;
    }

    public void setCharClass(boolean charClass) {
        this.charClass = charClass;
    }

    public static Set<String> getAllowedClasses() {
        return allowedClasses;
    }

    public static void setAllowedClasses(Set<String> allowedClasses) {
        Constant.allowedClasses = allowedClasses;
    }

    public void setEscaped(boolean escaped) {
        this.escaped = escaped;
    }
}
