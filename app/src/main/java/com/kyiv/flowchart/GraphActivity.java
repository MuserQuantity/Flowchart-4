package com.kyiv.flowchart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kyiv.flowchart.block.Block;
import com.kyiv.flowchart.block.BlockType;
import com.kyiv.flowchart.block.Model;
import com.kyiv.flowchart.draw.DrawGraph;
import com.kyiv.flowchart.graph.GraphModel;
import com.kyiv.flowchart.graph.Node;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    private String fileName;

    public static final int OPENFILE = 0;
    public static final int DIALOG_SAVEFILE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case OPENFILE:
                if (resultCode == RESULT_OK){
                    String filePath = data.getStringExtra("filePath");
                    try {
                        loadFromFile(filePath);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

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
        switch(view.getId()){
            case R.id.openButton:
                Intent intent = new Intent(this, OpenFileActivity.class);
                intent.putExtra("nameFilter", ".graph");
                startActivityForResult(intent, OPENFILE);
                break;
            case R.id.saveButton:
                if (fileName != null) {
                    saveInFile(fileName);
                    showMessage("Файл " + fileName + " успіщно збережено");
                }
                else{
                    showDialog(DIALOG_SAVEFILE);
                }
                break;
            case R.id.closeButton:
                graphModel = new GraphModel(this);
                drawGraph.setModel(graphModel);
                fileName = null;
                break;
            case R.id.tableButton:
                Intent intentTable = new Intent(this, TableActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("graphModel", graphModel);
                intentTable.putExtras(bundle);
                startActivity(intentTable);
                break;
        }
    }

    public void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        switch (id) {

            case DIALOG_SAVEFILE:
                adb.setTitle("Зберегти файл?");
                final EditText etFileName = new EditText(GraphActivity.this);
                etFileName.setHint("Назва файлу");
                if (fileName != null)
                    etFileName.setText(fileName);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                etFileName.setLayoutParams(lp);
                adb.setView(etFileName);
                adb.setPositiveButton("Зберегти", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fileName = etFileName.getText().toString();

                        if (!fileName.equals("")) {
                            saveInFile(fileName);
                            model.changeWasSaved();
                        }
                        else {
                            showMessage("Error! Try again");
                        }
                    }
                });
                adb.setNegativeButton("Не зберігати", null);

        }
        return adb.create();
    }

    public boolean saveInFile(String fileName) {
        try {
            if (!fileName.contains("."))
                fileName += ".graph";
            FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(graphModel);
            os.close();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void loadFromFile(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        fileName = file.getName();
        FileInputStream fis = openFileInput(fileName);
        ObjectInputStream is = null;
        try {
            is = new ObjectInputStream(fis);
            graphModel = (GraphModel) is.readObject();
            List<Node> nodes = graphModel.getNodeList();
            int maxNumber = Integer.MIN_VALUE;
            for (Node node : nodes)
                if (node.getNumberNode() > maxNumber)
                    maxNumber = node.getNumberNode();
            graphModel.setLastNodeNumber(maxNumber);
            is.close();
            fis.close();
            drawGraph.setModel(graphModel);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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
                        condition = " !" + block.getText() + " ";
                    else
                        condition = " " + block.getText() + " ";
                }
                if (outBlock.getNumberNode() != -1) {
                    linkedNodes.add(new String[]{condition, outBlock.getNumberNode() + ""});
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
        GraphModel graphModel = new GraphModel(this);
        List<Block> blocks = model.getBlockList();
        Iterator iterator = blocks.iterator();
        while(iterator.hasNext()){
            Block block = (Block) iterator.next();
            int numberNode = block.getNumberNode();
            String operators = block.getText();
            if (numberNode != -1){
                Node newNode = new Node(block.getX(), block.getY(), nodeRadius, getResources().getColor(R.color.block_color), numberNode, operators, 20);
                newNode.setNumberNode(numberNode);
                graphModel.addNode(newNode);
            }
        }
        iterator = blocks.iterator();
        while(iterator.hasNext()){
            Block block = (Block) iterator.next();
            int numberNode = block.getNumberNode();
            if (numberNode != -1){
                Node node = graphModel.findNodeByNumber(numberNode);
                List<String[]> ln = findLinkedNodes(block);
                for(String[] data : ln){
                    Node node2 = graphModel.findNodeByNumber(Integer.parseInt(data[1]));
                    graphModel.linkedNodes(node, node2, data[0]);
                }
            }
        }
        int sizeCode = (int)(Math.log(graphModel.getNodeList().size())/Math.log(2)) + 1;
        int[] code = new int[sizeCode];
        for (int i = 0; i < sizeCode; i++){
            code[i] = 0;
        }
        graphModel.setCodeSize(sizeCode);
        graphModel.setNodeCode(graphModel.findNodeByNumber(-1), code);
        return graphModel;
    }
}
