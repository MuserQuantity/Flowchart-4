package com.kyiv.flowchart.graph;

import com.kyiv.flowchart.block.Link;

import java.util.ArrayList;
import java.util.List;

public class GraphModel {
    private List<Node> nodes = new ArrayList<>();

    public boolean addNode(Node newNode){
        for (Node node : nodes)
            if (node.getNameNode().equals(newNode.getNameNode()))
                return false;
        nodes.add(newNode);
        return true;
    }

    public Node findNodeByName(String nameNode){
        for (Node node : nodes){
            if (node.getNameNode().equals(nameNode)){
                return node;
            }
        }
        return null;
    }

    public Node findNode(int x, int y){
        for (Node node : nodes){
            if ((x >= node.getX() - node.getRadius() && x <= node.getX() + node.getRadius())
                    && (y >= node.getY() - node.getRadius() && y <= node.getY() + node.getRadius()))
                return node;
        }
        return null;
    }

    public List<Node> getNodeList(){
        return nodes;
    }
}