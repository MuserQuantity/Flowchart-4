package com.kyiv.flowchart.graph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import com.kyiv.flowchart.R;
import com.kyiv.flowchart.block.Link;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GraphModel implements Serializable {
    private List<LinkGraph> links = new ArrayList<>();
    private List<Node> nodes = new ArrayList<>();
    private HashMap<String, int[]> nodeCodes = new HashMap<>();
    private int codeSize = 0;
    private int blockColor;
    private int lastNodeNumber;
    public GraphModel(Context context) {
        this.blockColor = context.getResources().getColor(R.color.block_color);
    }

    public boolean addNode(Node newNode){
        for (Node node : nodes)
            if (node.getNameNode().equals(newNode.getNameNode()))
                return false;
        nodes.add(newNode);
        lastNodeNumber = newNode.getNumberNode();
        return true;
    }

    public Node findNodeByNumber(int numberNode){
        Node resNode = nodes.get(0);
        if (numberNode == -1){
            for (Node n : nodes){
                if (n.getNumberNode() < resNode.getNumberNode())
                    resNode = n;
            }
            return resNode;
        }
        else
            for (Node node : nodes){
                if (node.getNumberNode() == numberNode){
                    return node;
                }
            }
        return null;
    }

    public int[] change01(int[] code, int index){
        int[] newCode = new int[code.length];
        for (int i = 0; i < code.length; i++)
            newCode[i] = code[i];
        if (newCode[index] == 0)
            newCode[index] = 1;
        else
            newCode[index] = 0;
        return newCode;
    }

    public String intArrayToString(int[] arr){
        String result = "";
        for (int i : arr){
            result += i;
        }
        return result;
    }

    public void setCodeSize(int codeSize) {
        this.codeSize = codeSize;
    }

    private List<Integer> findDifferences(int[] arr1, int[] arr2){
        List<Integer> differences = new ArrayList<>();
        if (arr1.length != arr2.length)
            return differences;
        for (int i = 0; i < arr1.length; i++)
            if (arr1[i] != arr2[i])
                differences.add(i);
        return differences;
    }

    public void incCodeSize(){
        nodeCodes = new HashMap<>();
        codeSize++;
        for (Node node1 : nodes) {
            int[] codeNode1 = node1.getCode();
            if (codeNode1 != null) {
                int[] newCode = new int[codeNode1.length + 1];
                newCode[0] = 0;
                for (int j = codeNode1.length - 1; j >= 0; j--)
                    newCode[j + 1] = codeNode1[j];
                node1.setCode(newCode);
                nodeCodes.put(intArrayToString(newCode), newCode);
            }
        }
    }

    public void setLastNodeNumber(int lastNodeNumber){
        this.lastNodeNumber = lastNodeNumber;
    }

    public boolean isErrors(){
        for (Node node : nodes){
            for (Node outNode : node.getLinkedOutNodes()){
                if (findDifferences(node.getCode(), outNode.getCode()).size() > 1 || findDifferences(node.getCode(), outNode.getCode()).size() < 1){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean setNodeCode(Node node, int[] code){
        node.setCode(code);
        nodeCodes.put(intArrayToString(code), code);
        for (Node n : node.getLinkedOutNodes()) {
            if (n.getCode() == null) {
                boolean findFreeCode = false;
                while (!findFreeCode) {
                    for (int j = codeSize - 1; j >= 0; j--) {
                        int[] newCode = change01(code, j);
                        if (!nodeCodes.containsKey(intArrayToString(newCode))) {
                            setNodeCode(n, newCode);
                            findFreeCode = true;
                            break;
                        }
                    }
                    if (!findFreeCode) {
                        int[] newCode = new int[code.length + 1];
                        for (int i = 0; i < code.length; i++)
                            newCode[i + 1] = code[i];
                        newCode[0] = 0;
                        code = newCode;
                        incCodeSize();
                    }
                }
            }
            else {
                boolean needChangeOne = false;
                List<Integer> diff = findDifferences(node.getCode(), n.getCode()); //node -> n
                LinkGraph N_NodeLink = getLinkWith(n, node);
                LinkGraph Node_NLink = getLinkWith(node, n);
                int[] newCode = null;
                while(true)
                if (diff.size() > 1){
                    boolean findFreeCode = false;
                    if (needChangeOne) {
                        diff = findDifferences(node.getCode(), n.getCode());
                        diff.add(0, 0);
                    }
                    for (int i = 0; i < diff.size(); i++){
                        newCode = change01(node.getCode(), diff.get(i));
                        if (!nodeCodes.containsKey(intArrayToString(newCode))){
                            findFreeCode = true;
                            break;
                        }
                    }
                    if (findFreeCode){
                        lastNodeNumber++;
                        Node node1 = new Node(0, 0, Node.getRadius(), blockColor, lastNodeNumber, "", 20);
                        node.removeOutNode(n);
                        node.addLinkedOutNode(node1);
                        node1.addLinkedOutNode(n);
                        nodes.add(node1);
                        if(N_NodeLink != null){
                            n.removeOutNode(node);
                            n.addLinkedOutNode(node1);
                            node1.addLinkedOutNode(node);
                            N_NodeLink.setInNode(node1);
                            LinkGraph newLink = new LinkGraph("");
                            newLink.setInNode(node);
                            newLink.setOutNode(node1);
                        }
                        Node_NLink.setOutNode(node1);
                        LinkGraph newLink = new LinkGraph("");
                        newLink.setOutNode(node);
                        newLink.setInNode(node1);
                        links.add(newLink);
                        setNodeCode(node1, newCode);
                        break;
                    }else{
                        incCodeSize();
                        needChangeOne = true;
                    }
                    if (!needChangeOne)
                        break;
                }
                else
                    break;
            }
        }
        return false;

    }

    public Node findNode(int x, int y){
        for (Node node : nodes){
            if ((x >= node.getX() - node.getRadius() && x <= node.getX() + node.getRadius())
                    && (y >= node.getY() - node.getRadius() && y <= node.getY() + node.getRadius()))
                return node;
        }
        return null;
    }

    public List<LinkGraph> getLinks() {
        return links;
    }

    public void drawLinks(Canvas canvas){
        for(LinkGraph link : links)
            link.draw(canvas);
    }

    public LinkGraph getLinkWith(Node node1, Node node2){
        for (LinkGraph link : links){
            if (link.getInNode() == node2 && link.getOutNode() == node1)
                return link;
        }
        return null;
    }

    public void linkedNodes(Node node1, Node node2, String condition){
        node1.addLinkedOutNode(node2);
        node2.addLinkedInNode(node1);
        LinkGraph newLink = new LinkGraph(condition);
        LinkGraph l;
        if((l = getLinkWith(node1, node2)) != null){
            l.setMudslide(-10);
            l.setColor(Color.RED);
            newLink.setMudslide(+10);
        }
        newLink.setInNode(node2);
        newLink.setOutNode(node1);
        newLink.setCondition(condition);
        links.add(newLink);

    }

    public List<Node> getNodeList(){
        return nodes;
    }
}