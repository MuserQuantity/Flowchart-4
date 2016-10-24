package com.kyiv.flowchart;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.kyiv.flowchart.block.Block;
import com.kyiv.flowchart.block.BlockType;
import com.kyiv.flowchart.block.Model;
import com.kyiv.flowchart.draw.DrawGraph;
import com.kyiv.flowchart.graph.GraphModel;
import com.kyiv.flowchart.graph.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class GraphActivity extends AppCompatActivity {
    private GraphModel graphModel;
    private Model model;
    private DrawGraph drawGraph;
    private FrameLayout frameLayout;
    private int nodeRadius = 50;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        model = (Model) bundle.get("model");
        graphModel = createGraph(model);
        frameLayout = (FrameLayout) findViewById(R.id.frameGraph);
        drawGraph = new DrawGraph(this, graphModel);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        frameLayout.addView(drawGraph, layoutParams);
        int w = drawGraph.getMeasuredWidth();
        final ViewTreeObserver treeObs = frameLayout.getViewTreeObserver();

        treeObs.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int centerX = frameLayout.getWidth()/2;
                int centerY = frameLayout.getHeight()/2;
                int l = frameLayout.getWidth()/2 - nodeRadius;
                List<Node> nodes = graphModel.getNodeList();
                if (!nodes.isEmpty()) {
                    int alpha = 6 / nodes.size();
                    for (int i = 0; i < nodes.size(); i++) {
                        nodes.get(i).setXY((int) (centerX + Math.cos(i * alpha) * l), (int) (centerY + Math.sin(i * alpha) * l));
                    }
                }
            }
        });
    }

    public void onClick(View view){

    }

    private List<String[]> findLinkedNodes(Block block){
        List<String[]> linkedNodes = new ArrayList<>();
        int numberOfOutPoints = block.getNumberOfOutPoint();
        HashMap<Integer, Block> outBlocks = block.getLinkedOutBlocks();
        for (int outPoint = 0; outPoint < numberOfOutPoints; outPoint++){
            Block outBlock = outBlocks.get(outPoint);
            if (outBlock != null) {
                String condition = "";
                if (block.getBlockType() == BlockType.RHOMB) {
                    if (outPoint == 0)
                        condition = "!" + block.getText();
                    else
                        condition = block.getText();
                }
                if (outBlock.getNameNode() != null) {
                    linkedNodes.add(new String[]{condition, outBlock.getNameNode()});
                } else {
                    List<String[]> lNodes = findLinkedNodes(outBlock);
                    for (String[] linkedNode : lNodes) {
                        if (outPoint == 0)
                            linkedNode[0] = condition + " " + linkedNode[0];
                        else
                            linkedNode[0] = condition + linkedNode[0];
                    }
                    linkedNodes.addAll(lNodes);
                }
            }
        }
        return linkedNodes;
    }

    private GraphModel createGraph(Model model){
        GraphModel graphModel = new GraphModel();
        List<Block> blocks = model.getBlockList();
        Iterator iterator = blocks.iterator();
        while(iterator.hasNext()){
            Block block = (Block) iterator.next();
            String nameNode = block.getNameNode();
            String operators = block.getText();
            if (nameNode != null){
                Node newNode = new Node(block.getX(), block.getY(), nodeRadius, getResources().getColor(R.color.block_color), nameNode, operators, 20);
                newNode.setNameNode(nameNode);
                graphModel.addNode(newNode);
            }
        }
        iterator = blocks.iterator();
        while(iterator.hasNext()){
            Block block = (Block) iterator.next();
            String nameNode = block.getNameNode();
            if (nameNode != null){
                Node node = graphModel.findNodeByName(nameNode);
                List<String[]> ln = findLinkedNodes(block);
                for(String[] data : ln){
                    node.setLinkedOutNode(data[0], graphModel.findNodeByName(data[1]));
                }
            }
        }
        return graphModel;
    }
}
