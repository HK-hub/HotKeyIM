package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.hk.im.client.service.*;
import com.hk.im.common.consntant.MinioConstant;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.common.resp.ResultCode;
import com.hk.im.common.util.FileUtil;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.entity.*;
import com.hk.im.domain.request.*;
import com.hk.im.domain.response.EditNoteArticleResponse;
import com.hk.im.domain.vo.NoteDetailVO;
import com.hk.im.domain.vo.NoteVO;
import com.hk.im.infrastructure.mapper.NoteMapper;
import com.hk.im.infrastructure.mapstruct.NoteMapStructure;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.lang.annotation.Retention;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author : HK意境
 * @ClassName : NoteServiceImpl
 * @date : 2023/3/27 16:49
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note> implements NoteService {

    @Resource
    private NoteMapper noteMapper;
    @Resource
    private CategoryService categoryService;
    @Resource
    private TagService tagService;
    @Resource
    private UserCollectionService userCollectionService;
    @Resource
    private NoteTagService noteTagService;
    @Resource
    private MinioService minioService;
    @Resource
    private NoteAnnexService noteAnnexService;

    /**
     * 获取用户文集列表
     *
     * @param request
     *
     * @return {@link java.util.List} of {@link Note}
     */
    @Override
    public ResponseResult getNoteArticleList(GetArticleListRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || Objects.isNull(request.getPage()) || Objects.isNull(request.getFindType());
        if (BooleanUtils.isTrue(preCheck)) {
            // 参数校验失败
            return ResponseResult.FAIL().setDataAsMessage("参数校验失败!");
        }

        if (Objects.isNull(request.getUserId())) {
            request.setUserId(UserContextHolder.get().getId());
        }

        // 素材准备
        Integer findType = request.getFindType();
        Long userId = request.getUserId();
        // 查询基础List集合
        List<Note> noteList = Lists.newArrayList();
        if (findType == 1) {
            // 近期编辑: 两个周
            LocalDate startTime = LocalDate.now().minusDays(14);
            noteList = this.noteMapper.selectRecentEditNoteList(request, startTime);
        } else if (findType == 2) {
            // 我的收藏
            List<UserCollection> collectedNoteList = this.userCollectionService.getUserCollectedNoteList(userId);
            // 检查是否有收藏的
            if (CollectionUtils.isNotEmpty(collectedNoteList)) {
                // 提取笔记id
                List<Long> noteIdList = collectedNoteList.stream().map(UserCollection::getCollectibleId).toList();
                // 获取收藏笔记集合
                noteList = this.noteMapper.selectCollectedNoteList(noteIdList, request);
            }
        } else if (findType == 3) {
            // 笔记分类：传入分类id
            noteList = this.noteMapper.selectNoteListByCategory(request);

        } else if (findType == 4) {
            // 笔记标签: 传入标签id
            // 查询出标签-笔记 映射集合
            List<NoteTag> noteTagList = this.noteTagService.getNoteListByTagId(request.getCid());
            // 检查该标签下是否有笔记
            if (CollectionUtils.isNotEmpty(noteTagList)) {
                // 提取 笔记id
                List<Long> noteIdList = noteTagList.stream().map(NoteTag::getNoteId).toList();
                // 查询标签下笔记集合
                noteList = this.noteMapper.selectNoteListByTag(noteIdList, request);
            }
        }


        // 查询笔记分类，标签信息
        List<NoteVO> noteVOList = noteList.stream().map(note -> {
                    Category category = this.categoryService.getNoteCategory(note.getCategoryId());
                    List<Tag> tagList = this.tagService.getNoteTagList(note.getId());
                    return NoteMapStructure.INSTANCE.toVO(note, category, tagList);
                })
                // 排序：按照最新编辑时间排序
                .sorted(Comparator.comparing(NoteVO::getUpdateTime).reversed())
                .toList();

        // 响应数据
        return ResponseResult.SUCCESS(noteVOList);
    }


    /**
     * 编辑文章笔记
     *
     * @param request
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult editNoteArticle(EditArticleRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || Objects.isNull(request.getNoteId()) || Objects.isNull(request.getMdContent())
                || StringUtils.isEmpty(request.getTitle());
        if (BooleanUtils.isTrue(preCheck)) {
            // 参数校验失败
            return ResponseResult.FAIL("笔记文章信息不完整!");
        }

        // 素材
        Long noteId = request.getNoteId();
        Long userId = UserContextHolder.get().getId();
        Long categoryId = request.getCategoryId();

        // 检查文章是否存在
        Note note = this.getById(noteId);
        String summary = Jsoup.parse(request.getContent())
                .text().substring(0, Math.min(20, request.getContent().length()));

        boolean res = false;
        if (Objects.isNull(note)) {
            // 笔记文章不存在，新增文章
            note = new Note()
                    .setAuthorId(userId)
                    .setTitle(request.getTitle())
                    .setStatus(1)
                    .setSummary(summary)
                    .setContent(request.getContent())
                    .setMdContent(request.getMdContent());

            // 确定分类信息
            if (Objects.isNull(categoryId) || 0 == categoryId) {
                // 分类不存在：选择默认分类
                Category defaultCategory = this.categoryService.getNoteDefaultCategory(userId);
                if (Objects.isNull(defaultCategory)) {
                    // 默认分类不存在
                    return ResponseResult.FAIL("抱歉,您未选择文章笔记分类!");
                }

                // 设置分类
                categoryId = defaultCategory.getId();
            }

            note.setCategoryId(categoryId);

            // 保存文章笔记
            res = this.save(note);
        } else {
            // 笔记文章存在：进行更新
            note.setTitle(request.getTitle())
                    .setSummary(summary)
                    .setContent(request.getContent())
                    .setMdContent(request.getMdContent())
                    .setCategoryId(categoryId)
                    .setUpdateTime(LocalDateTime.now());
            res = this.updateById(note);
        }

        // 响应数据
        if (BooleanUtils.isFalse(res)) {
            // 保存，编辑失败
            return ResponseResult.FAIL("保存笔记文章失败!");
        }

        // 保存，编辑成功: 构建响应体
        EditNoteArticleResponse result = new EditNoteArticleResponse()
                .setId(note.getId())
                .setTitle(note.getTitle())
                .setCover(note.getCover())
                .setSummary(summary);
        return ResponseResult.SUCCESS(result);
    }


    /**
     * 上传笔记文章图片
     * @param request
     *
     * @return
     */
    @Override
    public ResponseResult uploadNoteImage(UploadNoteImageRequest request) {

        // 参数校验
        if (Objects.isNull(request) || Objects.isNull(request.getImage()) || Objects.isNull(request.getNoteId())) {
            // 校验失败
            return ResponseResult.FAIL("笔记文集图片上传信息不完整!");
        }

        // 上传
        String url = this.minioService.putNoteImage(request.getImage(), MinioConstant.BucketEnum.Note.getBucket(), request.getNoteId());

        if (StringUtils.isEmpty(url)) {
            // 上传失败
            return ResponseResult.FAIL("图片上传失败!");
        }

        return ResponseResult.SUCCESS(url);
    }

    /**
     * 获取笔记文章
     *
     * @param noteId
     *
     * @return
     */
    @Override
    public ResponseResult getArticleDetailById(Long noteId) {

        Note note = this.getById(noteId);
        if (Objects.isNull(note)) {
            // 笔记文章不存在
            return ResponseResult.FAIL(ResultCode.NO_SUCH_RESOURCE);
        }

        // 组装
        Category category = this.categoryService.getNoteCategory(note.getCategoryId());
        List<Tag> tagList = this.tagService.getNoteTagList(note.getId());
        NoteDetailVO noteVO = NoteMapStructure.INSTANCE.toDetailVO(note, category, tagList);

        // 是否星标
        UserCollection collection = this.userCollectionService.getUserCollectedNoteById(note.getId());
        noteVO.setStared(Objects.nonNull(collection));

        return ResponseResult.SUCCESS(noteVO);
    }


    /**
     * 收藏/取消收藏笔记
     * @param request
     * @return
     */
    @Override
    public ResponseResult asteriskNoteArticle(AsteriskArticleRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || Objects.isNull(request.getArticleId()) || Objects.isNull(request.getType());
        if (BooleanUtils.isTrue(preCheck)) {
            // 校验失败
            return ResponseResult.FAIL();
        }

        // 查询收藏记录是否存在
        Long articleId = request.getArticleId();
        UserCollection collectedNote = null;

        // 根据操作进行业务处理
        Integer type = request.getType();

        boolean res = false;
        if (1 == type) {
            // 收藏
            collectedNote = this.userCollectionService.collectTheNoteArticle(articleId);
            res = Objects.nonNull(collectedNote);

        } else if (2 == type) {
            // 取消收藏
            res = this.userCollectionService.removeTheCollectedNote(articleId);
        }

        // 操作是否成功
        return ResponseResult.SUCCESS(res);
    }


    /**
     * 为笔记文章设置标签
     * @param request
     * @return
     */
    @Override
    public ResponseResult tagNoteArticle(TagArticleRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || Objects.isNull(request.getArticleId()) || Objects.isNull(request.getTags());
        if (BooleanUtils.isFalse(preCheck)) {
            // 参数校验失败
            return ResponseResult.FAIL();
        }

        // 素材
        Long articleId = request.getArticleId();
        List<Long> tags = request.getTags();

        // 查询笔记文章目前标签集合
        List<Tag> currentNoteTags = this.tagService.getNoteTagList(articleId);
        Set<Long> currentNoteTagIdList = currentNoteTags.stream().map(Tag::getId).collect(Collectors.toSet());

        // 解析标签集合，进行设置标签: 两个标签集合并集，再去除已经有的，就是需要设置的
        for (Long tagId : tags) {
            // 当前标签是否保存tag
            if (!currentNoteTagIdList.contains(tagId)) {
                // 不包含，设置标签
                this.tagService.addTagToArticle(tagId, articleId);
            }
        }

        // 添加成功
        return ResponseResult.SUCCESS();
    }

    /**
     * 讲笔记文章删除放入回收站
     * @param articleId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult putNoteToRecycleBin(Long articleId) {

        // 参数校验
        if (Objects.isNull(articleId)) {
            // 文章为选择
            return ResponseResult.FAIL("请选择需要删除的文章!");
        }

        // 校验当前登录用户是否有权利删除
        Long userId = UserContextHolder.get().getId();

        // 查询笔记文章
        Note note = this.getById(articleId);
        if (!Objects.equals(note.getAuthorId(), userId)) {
            // 不是作者
            return ResponseResult.FAIL("抱歉，你无权删除此文章!");
        }

        // 删除文章
        boolean update = this.lambdaUpdate()
                .eq(Note::getId, articleId)
                .eq(Note::getAuthorId, userId)
                .set(Note::getDeleted, Boolean.TRUE)
                .set(Note::getStatus, 3)
                .update();

        // 响应
        return ResponseResult.SUCCESS(update);
    }

    /**
     * 查看回收站笔记文章
     * @return
     */
    @Override
    public ResponseResult getRecycleBinNoteList() {

        Long userId = UserContextHolder.get().getId();
        List<Note> list = this.lambdaQuery()
                .eq(Note::getAuthorId, userId)
                .eq(Note::getDeleted, true)
                // 状态: 删除
                .eq(Note::getStatus, 3)
                .list();


        return ResponseResult.SUCCESS(list);
    }


    /**
     * 彻底删除笔记文章
     * @param articleId
     * @return
     */
    @Override
    public ResponseResult completelyRemoveNote(Long articleId) {

        // 校验当前登录用户是否有权利删除
        Long userId = UserContextHolder.get().getId();

        // 查询笔记文章
        Note note = this.getById(articleId);
        if (!Objects.equals(note.getAuthorId(), userId)) {
            // 不是作者
            return ResponseResult.FAIL("抱歉，你无权删除此文章!");
        }

        // 彻底删除文章
        boolean remove = this.removeById(articleId);

        return ResponseResult.SUCCESS(remove);
    }


    /**
     * 清空笔记文章列表
     * @return
     */
    @Override
    public ResponseResult cleanRecycleBinNoteList() {

        // 校验当前登录用户是否有权利删除
        Long userId = UserContextHolder.get().getId();

        // 查询回收站列表文章
        List<Note> list = this.lambdaQuery()
                .eq(Note::getAuthorId, userId)
                .eq(Note::getDeleted, true)
                // 状态: 删除
                .eq(Note::getStatus, 3)
                .list();

        // 清空当前用户回收站文章
        List<Long> noteIdList = list.stream().map(Note::getId).toList();
        boolean remove = this.removeBatchByIds(noteIdList);

        return ResponseResult.SUCCESS(remove);
    }


    /**
     * 恢复回收站文章
     * @param articleId
     * @return
     */
    @Override
    public ResponseResult recoverRecycleNote(Long articleId) {

        // 参数校验
        if (Objects.isNull(articleId)) {
            return ResponseResult.FAIL();
        }

        // 恢复
        Long userId = UserContextHolder.get().getId();
        boolean update = this.lambdaUpdate()
                .eq(Note::getId, articleId)
                .eq(Note::getAuthorId, userId)
                .set(Note::getDeleted, Boolean.FALSE)
                .set(Note::getStatus, 1)
                .update();

        return ResponseResult.SUCCESS(update);
    }


    /**
     * 上传笔记文集附件
     * @param request
     * @return
     */
    @Override
    public ResponseResult uploadNoteAnnex(UploadNoteAnnexRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || Objects.isNull(request.getNoteId()) || Objects.isNull(request.getFile());
        if (BooleanUtils.isTrue(preCheck)) {
            // 参数校验失败
            return ResponseResult.FAIL("请选择笔记或附件!");
        }

        // 检查大小：不能超过10MB
        MultipartFile file = request.getFile();
        boolean sizeCheck = FileUtil.checkFileSize(file.getSize(), 10, "M");
        if (BooleanUtils.isFalse(sizeCheck)) {
            // 文件大小超出限制
            return ResponseResult.FAIL("文件大小不能超过10M");
        }

        // 检查附件个数
        Long noteId = request.getNoteId();
        List<NoteAnnex> noteAnnexList =  this.noteAnnexService.getNoteAnnexList(noteId);

        if (noteAnnexList.size() > 10) {
            // 附件数量大于6，不能上传了
            return ResponseResult.FAIL("附件数量超出限制!");
        }

        // 上传
        String url = this.minioService.putNoteAnnex(file, MinioConstant.BucketEnum.File.getBucket(), noteId);
        if (StringUtils.isEmpty(url)) {
            // 上传失败
            return ResponseResult.FAIL("文件上传失败!");
        }

        // 添加附件
        String originalFilename = file.getOriginalFilename();
        NoteAnnex noteAnnex = new NoteAnnex()
                .setNoteId(noteId)
                .setOriginalName(originalFilename)
                .setSize((int) file.getSize())
                .setUrl(url)
                .setSuffix(FilenameUtils.getExtension(originalFilename));
        boolean save = this.noteAnnexService.save(noteAnnex);

        if (BooleanUtils.isFalse(save)) {
            // 保存附件失败
            return ResponseResult.FAIL("保存附件失败!");
        }

        return ResponseResult.SUCCESS(noteAnnex.setUrl(""));
    }


}




