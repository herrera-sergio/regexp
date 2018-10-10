/*
 * Copyright (C) 2015 Machine Learning Lab - University of Trieste, 
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
package it.units.inginf.male.generations;

import it.units.inginf.male.inputs.Context;
import it.units.inginf.male.tree.Leaf;
import it.units.inginf.male.tree.Node;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andrea
 */
public class Full implements Generation {

    int maxDepth;
    Context context;
    
    /**
     * 
     * @param maxDepth
     * @param context
     */
    public Full(int maxDepth, Context context) {
        this.maxDepth = maxDepth;
        this.context = context;
    }

    /**
     * This method return a new primaryPopulation of the desired size. The primaryPopulation
     * is generated by Full algorithm which creates individual with a fixed
     * depth
     * @param popSize the desired primaryPopulation size
     * @param populationID id of subPopulation (in order to acquire NodeFactory)
     * @return a List of Node of size popSize
     */
    @Override
    public List<Node> generate(int popSize, int populationID) {
        List<Node> population = new ArrayList<Node>();

        for (int i = 0; i < popSize;) {
            Node candidate = full(1, populationID);
            if (candidate.isValid()) {
                population.add(candidate);
                i++;
            }
        }

        return population;
    }

    private Node full(int depth, int populationID) {
        Node tree = randomFunction(populationID);
        if (depth >= this.maxDepth - 1) {

            for (int i = tree.getMaxChildrenCount() - tree.getMinChildrenCount(); i < tree.getMaxChildrenCount(); i++) {
                Leaf leaf = randomLeaf(populationID);
                leaf.setParent(tree);
                tree.getChildrens().add(leaf);
            }

        } else {
            for (int i = tree.getMaxChildrenCount() - tree.getMinChildrenCount(); i < tree.getMaxChildrenCount(); i++) {
                Node node = full(depth + 1, populationID);
                node.setParent(tree);
                tree.getChildrens().add(node);
            }
        }
        return tree;
    }

    private Node randomFunction(int populationID) {

        List<Node> functionSet = context.getConfiguration().getSubConfiguration(populationID).getNodeFactory().getFunctionSet();
        return functionSet.get(context.getRandom().nextInt(functionSet.size())).cloneTree();
    }

    private Leaf randomLeaf(int populationID) {
        List<Leaf> terminalSet = context.getConfiguration().getSubConfiguration(populationID).getNodeFactory().getTerminalSet();
        return terminalSet.get(context.getRandom().nextInt(terminalSet.size())).cloneTree();
    }
}
