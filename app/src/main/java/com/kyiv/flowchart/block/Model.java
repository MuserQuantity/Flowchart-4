package com.kyiv.flowchart.block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Model {
    private List<Block> blockList;
    private List<Link> linkList;

    public Model(){
        blockList = new ArrayList<>();
        linkList = new ArrayList<>();
    }

    public List<Block> getBlockList(){
        return blockList;
    }

    public List<Link> getLinkList(){
        return linkList;
    }

    public void addBlock(Block block){
        blockList.add(block);
    }

    public void addLink(Link link){
        Block outBlock = link.getOutBlock();//блок, з якого виходить link
        Block inBlock = link.getInBlock();  //блок, в який входить link
        if (outBlock.setLinkedOutBlock(link.getOutIndex(), inBlock)) {
            linkList.add(link);
            inBlock.setLinkedInBlock(outBlock);
        }
    }

    public void removeBlockAndLinks(Block block){
        Iterator iterator = linkList.iterator();
        while(iterator.hasNext()){
            Link link = (Link) iterator.next();
            if (link.getInBlock() == block | link.getOutBlock() == block) {
                if (link.getInBlock() == block)
                    link.getOutBlock().removeLinkedBlock(block);
                else
                    link.getInBlock().removeLinkedBlock(block);
                iterator.remove();
            }
        }
        blockList.remove(block);
    }

    public Block findBlock(float x, float y){
        for (int index = 0; index < blockList.size(); index++){
            Block block = blockList.get(index);
            if (x >= block.getX() - block.getWidth()/2 && x <= block.getX() + block.getWidth()/2 && y >= block.getY() - block.getHeight()/2 && y <= block.getY() + block.getHeight()/2)
                return block;
        }
        return null;
    }
}
