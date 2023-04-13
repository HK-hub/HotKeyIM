package com.hk.im.admin.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.hk.im.client.service.CloudFileService;
import com.hk.im.client.service.StorageDirService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.CloudFile;
import com.hk.im.domain.entity.TreeNode;
import com.hk.im.domain.entity.StorageDir;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-06-06
 */
@RestController
@RequestMapping("/educenter/file")
@CrossOrigin
public class FileController {

    @Resource
    private CloudFileService cloudFileService;

    @Resource
    private StorageDirService userDirService;

    //根据名字模糊查询文件
    @PostMapping("findFile/{userId}/{name}")
    public ResponseResult findFile(@PathVariable String name, @PathVariable Long userId) {
        List<CloudFile> fileList = this.cloudFileService.getFindFile(userId, name);
        System.out.println(fileList);
        StorageDir userDir = userDirService.getUserDir(userId);
        TreeNode treeNode = JSON.parseObject(userDir.getDir(), new TypeReference<TreeNode>() {
        });
        List list=new ArrayList();
        findTreeNode(treeNode, name,list);
        //System.out.println(list);
        HashMap<String, Object> map = new HashMap<>();
        map.put("fileList", fileList);
        map.put("list", list);

        return ResponseResult.SUCCESS(map);
    }

    private static void findTreeNode(TreeNode treeNode, String name,List list1) {
        //System.out.println(treeNode);
        //System.out.println(list1);
        List<TreeNode> list=treeNode.getChildrenList();
        //System.out.println(list);
        if (list==null || list.isEmpty()){
            return;
        }
        else {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getName().indexOf(name)>-1){
                    //System.out.println(list.get(i).getChildrenList());
                    list1.add(list.get(i));
                    List<TreeNode> list2=list.get(i).getChildrenList();
                    System.out.println(list2);
                  if (list2.size()>=1){
                      findTreeNode(list.get(i),name,list1);
                  }
                }else{
                    findTreeNode(list.get(i),name,list1);
                }
            }
        }

    }

    //添加文件的接口方法
    @PostMapping("addFile")
    public ResponseResult addFile(@RequestBody CloudFile file) {
            boolean save = this.cloudFileService.save(file);
            if (save) {
                return ResponseResult.SUCCESS(file);
            } else {
                return ResponseResult.FailResponse;
            }

    }


    //查询当前用户下的所有文件
    @GetMapping("getAllFileInfo/{memId}")
    public ResponseResult getAllFileInfo(@PathVariable String memId) {
        //System.out.println(memId);
        List<CloudFile> fileList = this.cloudFileService.getAllFileInfo(memId);
        //System.out.println(files);
        return ResponseResult.SUCCESS(fileList);
    }

    //根据文件id查询文件具体信息
    @GetMapping("getFileInfo/{id}")
    public ResponseResult getfileInfo(@PathVariable String id) {
        //System.out.println(memId);
        List<CloudFile> files = this.cloudFileService.getFileInfo(id);
        //System.out.println(files);
        return ResponseResult.SUCCESS(files);
    }

    //文件重命名
    @PostMapping("updateFile/{id}/{name}")
    public ResponseResult updateFile(@PathVariable String id, @PathVariable String name) {
        QueryWrapper<CloudFile> wrapper=new QueryWrapper<>();
        wrapper.eq("id",id);
        CloudFile one = this.cloudFileService.getOne(wrapper);
        CloudFile file = new CloudFile();
        file.setId(id);
        file.setName(name);
        file.setSize(one.getSize());
        boolean update = this.cloudFileService.updateById(file);
        if (update) {
            return ResponseResult.SUCCESS();
        } else {
            return ResponseResult.FAIL();
        }
    }

    //文件收藏
    @PostMapping("collectFile")
    public ResponseResult collectionFile(@RequestParam("id") String[] id) {
        boolean flag = false;
        for (int i = 0; i < id.length; i++) {
            System.out.println(id[i]);
            CloudFile file = new CloudFile();
            file.setId(id[i]);
            file.setCollection(Boolean.TRUE);
            boolean update = this.cloudFileService.updateById(file);
            if (update) {
                flag = true;
            }
        }
        if (flag) {
            return ResponseResult.SUCCESS();
        } else {
            return ResponseResult.FAIL();
        }
    }

    //文件收藏
    @PostMapping("cancelCollection")
    public ResponseResult cancelCollection(@RequestParam("id") String[] id) {
        boolean flag = false;
        for (int i = 0; i < id.length; i++) {
            System.out.println(id[i]);
            CloudFile file = new CloudFile();
            file.setId(id[i]);
            file.setCollection(Boolean.FALSE);
            boolean update = this.cloudFileService.updateById(file);
            if (update) {
                flag = true;
            }
        }
        if (flag) {
            return ResponseResult.SUCCESS();
        } else {
            return ResponseResult.FAIL();
        }
    }

    //根据当前路径查询所有文件
    @PostMapping("getCurDirFiles/{id}")
    public ResponseResult setDirStruct(@RequestBody String userDir, @PathVariable String id) {
        //System.out.println(userDir);
        List<CloudFile> files = this.cloudFileService.getCurFiles(userDir, id);
        System.out.println(files);
        return ResponseResult.SUCCESS(files);
    }

    //根据当前路径查询所有文件
    @PostMapping("fileMove")
    public ResponseResult fileMove(@RequestBody String movingPath, @RequestParam("id") String[] id) {
        boolean flag = false;
        for (int i = 0; i < id.length; i++) {
            System.out.println(id[i]);
            CloudFile file = new CloudFile();
            file.setId(id[i]);
            file.setFDir(movingPath);
            boolean update = this.cloudFileService.updateById(file);
            if (update) {
                flag = true;
            }
        }
        if (flag) {
            return ResponseResult.SUCCESS();
        } else {
            return ResponseResult.FAIL();
        }
    }
}

