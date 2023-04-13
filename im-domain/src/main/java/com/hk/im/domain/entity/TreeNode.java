package com.hk.im.domain.entity;

import lombok.Data;

import java.util.List;

@Data
public class TreeNode{

    private long id;
    private String name;
    private long parentId;
    private static int idCounter = 0;
    private List<TreeNode> childrenList;

    public TreeNode() {
        this.id = ++idCounter;
    }

    public TreeNode(String name, long parentId) {
        this.name = name;
        this.parentId = parentId;
        this.id = ++idCounter;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }


    public void setChildrenList(List<TreeNode> childrenList) {
        this.childrenList = childrenList;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getParentId() {
        return parentId;
    }


    public List<TreeNode> getChildrenList() {
        return childrenList;
    }
}