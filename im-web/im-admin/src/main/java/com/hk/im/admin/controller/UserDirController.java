package com.hk.im.admin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.alibaba.fastjson.TypeReference;
import com.hk.im.client.service.CloudFileService;
import com.hk.im.client.service.StorageDirService;
import com.hk.im.common.error.BaseException;
import com.hk.im.common.error.BusinessException;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.domain.entity.CloudFile;
import com.hk.im.domain.entity.TreeNode;
import com.hk.im.domain.entity.StorageDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/educenter/dir")
@CrossOrigin
public class UserDirController {
    @Resource
    private StorageDirService userDirService;
    @Autowired
    private CloudFileService fileService;

    int result = 1;

    /**
     * 获取当前目录下的文件列表
     */
    @GetMapping("getUserDir/{id}")
    public ResponseResult getDirStruct(@PathVariable Long id) {
        System.out.println(id);
        StorageDir dir = userDirService.getUserDir(id);
        //System.out.println(files);
        return ResponseResult.SUCCESS(dir);
    }

    @PostMapping("setUserDir/{userId}/{name}/{id}")
    public ResponseResult setDirStruct(@PathVariable Long userId, @PathVariable String name, @PathVariable long id) throws BaseException {
        StorageDir userDir = userDirService.getUserDir(userId);
        TreeNode treeNode = JSON.parseObject(userDir.getDir(), new TypeReference<>() {
        });
        //System.out.println(treeNode);
        TreeNode treeNode1 = new TreeNode();
        treeNode1.setName(name + "/");
        treeNode1.setParentId(id);
        treeNode1.setChildrenList(new ArrayList<>());
        insert(treeNode, id, treeNode1);
        System.out.println(treeNode);
        String s = JSONObject.toJSONString(treeNode);
        System.out.println(s);
        userDir.setDir(s);
        //String s=JSONObject.toJSONString(l);
        //treeNode.setChildrenList(list);
        int ret = userDirService.setUserDir(userDir);
        if (ret > 0) {
            return ResponseResult.SUCCESS(userDir);
        }
        //System.out.println(files);
        return ResponseResult.FAIL();
    }


    @PostMapping("deleteDirStruct/{userId}/{id}")
    public ResponseResult deleteDirStruct(@PathVariable(name = "userId") Long userId, @PathVariable long id, @RequestBody String url) {
        StorageDir userDir = userDirService.getUserDir(userId);
        TreeNode treeNode = JSON.parseObject(userDir.getDir(), new TypeReference<>() {
        });
        boolean i = userDirService.deleteStruct(userId, url);
        System.out.println(i);
        if (i) {
            StringBuffer sb = new StringBuffer();
            delete(treeNode, id, sb);
            //System.out.println(sb);
            String s = JSONObject.toJSONString(treeNode);
            userDir.setDir(s);
            userDirService.setUserDir(userDir);
            return ResponseResult.SUCCESS();
        } else {
            return ResponseResult.FAIL();
        }
    }

    public static void insert(TreeNode treeNode, long id, TreeNode newNode) throws BaseException {
        List<TreeNode> list = treeNode.getChildrenList();
        List arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            arrayList.add(list.get(i).getName());
        }
        arrayList.add(newNode.getName());
        System.out.println(arrayList);
        HashSet set = new HashSet<>(arrayList);
        Boolean result = set.size() == arrayList.size() ? true : false;
        if (result == false) {
            System.out.println("名字不可重复");
            throw new BusinessException(ResultCode.SERVER_BUSY);
        }
        if (id == treeNode.getId()) {
            treeNode.getChildrenList().add(newNode);
            return;
        }
        //System.out.println(list.size());
        if (list == null || list.isEmpty()) {
            return;                            //若该结点 的子结点集合为空 返回
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (result == true) {
                    insert(list.get(i), id, newNode);
                }
            }
        }
    }

    public void delete(TreeNode treeNode, long id, StringBuffer sb) {
        sb.append("/").append(treeNode.getName());
        List<TreeNode> list = treeNode.getChildrenList();
        if (list == null || list.isEmpty()) {
            return;
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (id == list.get(i).getId()) {
                    list.remove(i);
                    delete(new TreeNode(), id, sb);
                    break;
                } else {
                    delete(list.get(i), id, sb);
                }
            }
        }
    }


    @PostMapping("updateDirStruct/{userId}/{name}/{id}")
    public ResponseResult updateDirStruct(@PathVariable Long userId, @PathVariable String name, @PathVariable long id, @RequestBody String url) {
        StorageDir userDir = userDirService.getUserDir(userId);
        TreeNode treeNode = JSON.parseObject(userDir.getDir(), new TypeReference<TreeNode>() {
        });
        update(treeNode, id, name, 1);
        System.out.println(result);
        String s = JSONObject.toJSONString(treeNode);
        userDir.setDir(s);
        List<CloudFile> list = fileService.getList(userId, url, result, name);
        for (int i = 0; i < list.size(); i++) {
            String id1 = list.get(i).getId();
            CloudFile file = new CloudFile();
            file.setId(id1);
            file.setSize(list.get(i).getSize());
            file.setFDir(list.get(i).getFDir());
            fileService.updateById(file);
        }
        int i = userDirService.setUserDir(userDir);
        if (i > 0) {
            return ResponseResult.SUCCESS(treeNode);
        } else {
            return ResponseResult.FAIL();
        }
    }

    public void update(TreeNode treeNode, long id, String name, int d) {
        //result=d;
        //System.out.println(result);
        if (treeNode.getId() == id) {
            treeNode.setName(name + "/");
            // System.out.println(d);
            result = d;
            return;
        }
        List<TreeNode> sonList = treeNode.getChildrenList();
        if (sonList == null || sonList.isEmpty()) {
            return;
        } else {
            for (int i = 0; i < sonList.size(); i++) {
                //result++;
                update(sonList.get(i), id, name, d + 1);
            }
        }
    }

}
