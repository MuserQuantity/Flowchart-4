package com.kyiv.flowchart.block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.Serializable;

public class Model implements Serializable{
    private List<Block> blockList;
    private List<Link> linkList;
    private boolean isChanged = false;

    public Model(){
        blockList = new ArrayList<>();
        linkList = new ArrayList<>();
    }

    public boolean findWarnings(){
        boolean isWarnings = false;
        boolean isStartBlock = false;
        boolean isEndBlock = false;
        for(Link link : linkList){
            if (link.getInBlock() == link.getOutBlock()) {
                link.setWarning(true);
                isWarnings = true;
            }
            else
                link.setWarning(false);
        }
        for (Block block : blockList) {
            if (block.getBlockType() == BlockType.RHOMB){
                if (block.getLinkedInBlocks().size() == 0 | block.getLinkedOutBlocks().size() != 2){
                    block.setWarning(true);
                    isWarnings = true;
                }
                else
                    block.setWarning(false);
            }
            else if (block.getBlockType() == BlockType.ROUNDRECT) {
                if (block.getLinkedInBlocks().size() == 0 & block.getLinkedOutBlocks().size() != 0) {
                    if (isStartBlock){
                        block.setWarning(true);
                        isWarnings = true;
                    }
                    else {
                        isStartBlock = true;
                        block.setWarning(false);
                    }
                }
                else if (block.getLinkedInBlocks().size() != 0 & block.getLinkedOutBlocks().size() == 0){
                    if (isEndBlock){
                        block.setWarning(true);
                        isWarnings = true;
                    }
                    else {
                        isEndBlock = true;
                        block.setWarning(false);
                    }
                }
                else{
                    block.setWarning(true);
                    isWarnings = true;
                }

            }
            else if (block.getLinkedInBlocks().size() == 0 | block.getLinkedOutBlocks().size() == 0){
                block.setWarning(true);
                isWarnings = true;
            }
            else
                block.setWarning(false);
        }
        return isWarnings;
    }

    public boolean isChanged(){
        if (isChanged){
            isChanged = false;
            return true;
        }
        return false;
    }

    public void changed(){
        isChanged = true;
        findWarnings();
    }

    public void changeWasSaved(){
        isChanged = false;
    }

    public List<Block> getBlockList(){
        return blockList;
    }

    public List<Link> getLinkList(){
        return linkList;
    }

    public void addBlock(Block block){
        blockList.add(block);
        changed();
    }

    public void addLink(Link link){
        Block outBlock = link.getOutBlock();//блок, з якого виходить link
        Block inBlock = link.getInBlock();  //блок, в який входить link
        if (outBlock.setLinkedOutBlock(link.getOutIndex(), inBlock)) {
            linkList.add(link);
            inBlock.setLinkedInBlock(outBlock);
        }
        changed();
    }

    public Block getBlockById(int id){
        for (Block block : blockList)
            if (block.getId() == id)
                return block;
        return null;
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
        changed();
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
