package com.hk.im.service.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.CloudFileService;
import com.hk.im.domain.entity.CloudFile;
import com.hk.im.infrastructure.mapper.CloudFileMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-06-06
 */
@Service
public class CloudFileServiceImpl extends ServiceImpl<CloudFileMapper, CloudFile> implements CloudFileService {
    @Override
    public List<CloudFile> getAllFileInfo(String memId) {
        QueryWrapper<CloudFile> wrapper=new QueryWrapper<>();
        wrapper.eq("mem_id",memId);
        //File files = baseMapper.selectById(wrapper);
        List<CloudFile> fileList = baseMapper.selectList(wrapper);
        //System.out.println(files);
        return fileList;
    }

    @Override
    public  List<CloudFile> getFileInfo(String id) {
        QueryWrapper<CloudFile> wrapper=new QueryWrapper<>();
        wrapper.eq("id",id);
        //File files = baseMapper.selectById(wrapper);
        List<CloudFile> files = baseMapper.selectList(wrapper);
        //System.out.println(files);
        return files;
    }

    /**
     * 获取当前目录下的所有文件
     * @param dir
     * @return
     */
    @Override
    public List<CloudFile> getCurFiles(String dir,String id) {
        QueryWrapper<CloudFile> wrapper=new QueryWrapper<>();
        wrapper.eq("f_dir",dir);
        wrapper.eq("mem_id",id);
        //File files = baseMapper.selectById(wrapper);
        List<CloudFile> files = baseMapper.selectList(wrapper);
        return files;
    }

    @Override
    public CloudFile getFiles(String id) {
        QueryWrapper<CloudFile> wrapper=new QueryWrapper<>();
        wrapper.eq("id",id);
        CloudFile file = baseMapper.selectOne(wrapper);
        return file;
    }

    @Override
    public List<CloudFile> getFindFile(Long userId,String name) {

        List<CloudFile> fileList = this.lambdaQuery()
                .eq(CloudFile::getUserId, userId)
                .like(CloudFile::getName, name)
                .list();
        return fileList;
    }

    @Override
    public List<CloudFile> getList(Long userId, String url,int result,String name) {

        List<CloudFile> fileList = this.lambdaQuery()
                .eq(CloudFile::getUserId,userId)
                .like(CloudFile::getFDir,url)
                .list();

        for (int i = 0; i < fileList.size(); i++) {
            String fDir = fileList.get(i).getFDir();
            String[] s=fDir.split("/",-1);
            s[result]=name;
            StringBuffer sb = new StringBuffer();
            for (int j = 1; j < s.length; j++) {
                    sb.append("/").append(s[j]);
            }
            System.out.println(sb.toString());
            fileList.get(i).setFDir(sb.toString());
        }

        System.out.println(fileList);
        return fileList;
    }


}
