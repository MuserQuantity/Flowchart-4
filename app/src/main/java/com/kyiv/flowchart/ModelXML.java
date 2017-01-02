package com.kyiv.flowchart;

import android.content.Context;

import com.kyiv.flowchart.block.Block;
import com.kyiv.flowchart.block.BlockType;
import com.kyiv.flowchart.block.Link;
import com.kyiv.flowchart.block.Model;
import com.kyiv.flowchart.block.Rectangle;
import com.kyiv.flowchart.block.Rhomb;
import com.kyiv.flowchart.block.RoundRect;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

class ModelXML {
    private DocumentBuilder builder;
    private static final String BLOCKS = "blocks";
    private static final String BLOCK = "block";
    private static final String ID = "id";
    private static final String X = "x";
    private static final String Y = "y";
    private static final String TYPE = "type";
    private static final String TEXT = "text";
    private static final String TEXT_SIZE = "text_size";
    private static final String LINKED_OUT_WITH = "linked_out_with";
    private static final String LINKED_IN_WITH = "linked_in_with";
    private static final String INDEX_OUT = "index_out";
    private static final String NODE_NAME = "node_name";
    private Context context;

    ModelXML(Context context){
        this.context = context;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private Element blockXML(Document doc, Block block){
        Element eBlock = doc.createElement(BLOCK);

        Element blockId = doc.createElement(ID);
        blockId.appendChild(doc.createTextNode(block.getId() + ""));
        eBlock.appendChild(blockId);

        Element blockX = doc.createElement(X);
        blockX.appendChild(doc.createTextNode(block.getX() + ""));
        eBlock.appendChild(blockX);

        Element blockY = doc.createElement(Y);
        blockY.appendChild(doc.createTextNode(block.getY() + ""));
        eBlock.appendChild(blockY);

        Element blockType = doc.createElement(TYPE);
        blockType.appendChild(doc.createTextNode(block.getBlockType() + ""));
        eBlock.appendChild(blockType);

        Element blockText = doc.createElement(TEXT);
        blockText.appendChild(doc.createTextNode(block.getText() + ""));
        eBlock.appendChild(blockText);

        Element blockTextSize = doc.createElement(TEXT_SIZE);
        blockTextSize.appendChild(doc.createTextNode(block.getTextSize() + ""));
        eBlock.appendChild(blockTextSize);

        if (block.getBlockType() != BlockType.RHOMB) {
            Element blockNodeName = doc.createElement(NODE_NAME);
            blockNodeName.appendChild(doc.createTextNode(block.getNumberNode() + ""));
            eBlock.appendChild(blockNodeName);
        }

        HashMap<Integer, Block> blocksOut = block.getLinkedOutBlocks();
        if (blocksOut != null && blocksOut.size() != 0) {
            Element linkedOutWith = doc.createElement(LINKED_OUT_WITH);
            int currentOutLink = 0;
            int indexOut = 0;
            while (currentOutLink != blocksOut.size()) {
                Block b = blocksOut.get(indexOut);
                if (b != null) {
                    Element id = doc.createElement(ID);
                    id.setAttribute(INDEX_OUT, indexOut + "");
                    id.appendChild(doc.createTextNode(b.getId() + ""));
                    linkedOutWith.appendChild(id);
                    currentOutLink++;
                }
                indexOut++;
            }
            eBlock.appendChild(linkedOutWith);
        }

        List<Block> blocksIn = block.getLinkedInBlocks();
        if (blocksIn != null && blocksIn.size() != 0) {
            Element linkedInWith = doc.createElement(LINKED_IN_WITH);
            for (Block b : blocksIn)
                if (b != null) {
                    Element id = doc.createElement(ID);
                    id.appendChild(doc.createTextNode(b.getId() + ""));
                    linkedInWith.appendChild(id);
                }
            eBlock.appendChild(linkedInWith);
        }

        return eBlock;

    }

    void writeXML(Model model, String fileName) throws TransformerException, IOException {

        Document doc=builder.newDocument();
        Element RootElement=doc.createElement(BLOCKS);

        for (Block block : model.getBlockList()){
            RootElement.appendChild(blockXML(doc, block));
        }

        doc.appendChild(RootElement);
        Transformer t=TransformerFactory.newInstance().newTransformer();
        t.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(fileName)));

    }

    Model readXML(FileInputStream fileInputStream) throws ParserConfigurationException, IOException, SAXException {
        Model model = new Model();
        Block.clear();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource is = new InputSource();
        is.setByteStream(fileInputStream);

        Document doc = db.parse(is);
        NodeList blockNodeList = doc.getElementsByTagName(BLOCK);

        for (int i = 0; i < blockNodeList.getLength(); i++){
            Element element = (Element) blockNodeList.item(i);

            NodeList xNode = element.getElementsByTagName(X);
            int x = Integer.parseInt(getCharacterDataFromElement((Element)xNode.item(0)));


            NodeList yNode = element.getElementsByTagName(Y);
            int y = Integer.parseInt(getCharacterDataFromElement((Element)yNode.item(0)));

            NodeList idNode = element.getElementsByTagName(ID);
            int id = Integer.parseInt(getCharacterDataFromElement((Element)idNode.item(0)));

            NodeList textNode = element.getElementsByTagName(TEXT);
            String text = getCharacterDataFromElement((Element)textNode.item(0));

            NodeList textSizeNode = element.getElementsByTagName(TEXT_SIZE);
            int textSize = Integer.parseInt(getCharacterDataFromElement((Element)textSizeNode.item(0)));

            NodeList type = element.getElementsByTagName(TYPE);
            Block block = null;
            switch(getCharacterDataFromElement((Element) type.item(0))){
                case "RECT":
                    block = new Rectangle(x, y, context.getResources().getColor(R.color.block_color), text, textSize, id);
                    NodeList nodeNameR = element.getElementsByTagName(NODE_NAME);
                    block.setNumberNode(Integer.parseInt(getCharacterDataFromElement((Element)nodeNameR.item(0))));
                    break;
                case "RHOMB":
                    block = new Rhomb(x, y, context.getResources().getColor(R.color.block_color), text, textSize, id);
                    break;
                case "ROUNDRECT":
                    block = new RoundRect(x, y, context.getResources().getColor(R.color.block_color), text, textSize, id);
                    NodeList nodeNameRR = element.getElementsByTagName(NODE_NAME);
                    block.setNumberNode(Integer.parseInt(getCharacterDataFromElement((Element)nodeNameRR.item(0))));
                    break;
            }

            model.addBlock(block);

        }

        //повторний прохід по блокам для встановлення лінків
        for (int i = 0; i < blockNodeList.getLength(); i++){
            Element element = (Element) blockNodeList.item(i);

            NodeList idNode = element.getElementsByTagName(ID);
            int id = Integer.parseInt(getCharacterDataFromElement((Element)idNode.item(0)));

            Element linkedOutWith = (Element)(element.getElementsByTagName(LINKED_OUT_WITH)).item(0);
            if (linkedOutWith != null) {
                NodeList outId = linkedOutWith.getElementsByTagName(ID);
                Block block = model.getBlockById(id);
                for (int index = 0; index < outId.getLength(); index++) {
                    Element out = (Element) outId.item(index);
                    int outIndex = Integer.parseInt(out.getAttribute(INDEX_OUT));
                    int linkegWithId = Integer.parseInt(getCharacterDataFromElement(out));
                    Block inBlock = model.getBlockById(linkegWithId);
                    Link link = new Link(context.getResources().getColor(R.color.block_color));
                    link.setOutIndex(outIndex);
                    link.setInBlock(inBlock);
                    link.setOutBlock(block);
                    model.addLink(link);
                }
            }

        }

        return model;
    }

    private String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "";
    }
}
