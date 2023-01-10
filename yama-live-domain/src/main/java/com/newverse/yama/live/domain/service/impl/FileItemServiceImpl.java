package com.newverse.yama.live.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.newverse.yama.live.domain.constant.SecurityConstants;
import com.newverse.yama.live.domain.entity.FileItem;
import com.newverse.yama.live.domain.enums.FileStatusEnum;
import com.newverse.yama.live.domain.model.*;
import com.newverse.yama.live.domain.respository.FileItemMapper;
import com.newverse.yama.live.domain.service.AdminService;
import com.newverse.yama.live.domain.service.FileUploadService;
import com.newverse.yama.live.domain.service.IAuthService;
import com.newverse.yama.live.domain.service.IFileItemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.NativeWebRequest;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FileItemServiceImpl extends ServiceImpl<FileItemMapper, FileItem> implements IFileItemService {

    @Value("${aws.s3.file.bucket.name}")
    private String bucketName;

    @Value("${aws.static.host}")
    private String staticHost;

    @Resource
    private FileItemMapper fileItemMapper;

    @Resource
    private FileUploadService fileUploadService;

    @Autowired
    private IAuthService authService;

    @Autowired
    private AdminService adminService;

    @Override
    public R fileListPost(UploadedFileListRequest uploadedFileListRequest, NativeWebRequest request) {

        // 鉴权
        String subscriber = getUserSubscriber(request);
        if(StringUtils.isEmpty(subscriber)){
            return R.error(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        UploadedFileListResponse uploadedFileListResponse = getFileItems(
                subscriber,
                uploadedFileListRequest.getStatus(), uploadedFileListRequest.getPage(),
                uploadedFileListRequest.getSize());

        return R.success("success", uploadedFileListResponse);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R filePreSignedPost(PreSignUploadFileRequest preSignUploadFileRequest, NativeWebRequest request) {
        String subscriber = getUserSubscriber(request);
        if(StringUtils.isEmpty(subscriber)){
            return R.error(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }
        PreSignUploadFileResponse fileResponse = generatePreSignedUrl(subscriber, preSignUploadFileRequest);
        return R.success("success", fileResponse);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R fileUploadSignedPost(UploadSignedFileRequest uploadSignedFileRequest, NativeWebRequest request) {
        // 鉴权
        String subscriber = getUserSubscriber(request);
        if(StringUtils.isEmpty(subscriber)){
            return R.error(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }
        UploadSignedFileResponse fileResponse = changeFileStatus(
                subscriber,
                uploadSignedFileRequest.getFileKey(),
                FileStatusEnum.FINISHED_UPLOAD);
        if (fileResponse == null) {
            return R.error(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return R.success("success", fileResponse);
    }

    public PreSignUploadFileResponse generatePreSignedUrl(String userSubscriber, PreSignUploadFileRequest preSignRequest) {
        QueryWrapper<FileItem> fileItemQueryWrapper = new QueryWrapper<>();
        fileItemQueryWrapper.eq("file_key", preSignRequest.getFileKey());
        FileItem fileItem = fileItemMapper.selectOne(fileItemQueryWrapper);

        if (fileItem == null) {
            String preSignedUrl = fileUploadService.preSignedFileUrl(bucketName, preSignRequest.getFileKey(), preSignRequest.getFileType());
            fileItem = FileItem.builder().userSubscriber(userSubscriber).fileKey(preSignRequest.getFileKey()).signedUrl(preSignedUrl).fileSize(preSignRequest.getFileSize()).fileType(preSignRequest.getFileType()).fileTitle(preSignRequest.getFileName()).uploadState(FileStatusEnum.INITIAL.getState()).createTime(new Date()).modifyTime(new Date()).build();
            fileItemMapper.insert(fileItem);
        }

        PreSignUploadFileResponse response = new PreSignUploadFileResponse();
        response.fileName(fileItem.getFileTitle());
        response.setFileType(fileItem.getFileType());
        response.setFileKey(fileItem.getFileKey());
        response.setPreSignedUrl(fileItem.getSignedUrl());
        return response;
    }

    public UploadSignedFileResponse changeFileStatus(String userSubscriber, String fileKey, FileStatusEnum fileStatus) {
        UpdateWrapper<FileItem> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("upload_state", fileStatus.getState());
        updateWrapper.set("modify_time", new Date());
        updateWrapper.eq("file_key", fileKey);
        updateWrapper.eq("user_subscriber", userSubscriber);

        boolean rowAffect = this.update(updateWrapper);
        if (rowAffect) {
            QueryWrapper<FileItem> fileItemQueryWrapper = new QueryWrapper<>();
            fileItemQueryWrapper.eq("file_key", fileKey);
            FileItem fileItem = fileItemMapper.selectOne(fileItemQueryWrapper);

            UploadSignedFileResponse fileResponse = new UploadSignedFileResponse();
            fileResponse.setFileName(fileItem.getFileTitle());
            fileResponse.setFileType(fileItem.getFileType());
            fileResponse.setFileKey(fileItem.getFileKey());
            fileResponse.setPreSignedUrl(fileItem.getSignedUrl());
            fileResponse.setUrl(staticHost + fileItem.getFileKey());
            fileResponse.setFileStatus(fileItem.getUploadState());
            fileResponse.setCreateTime(fileItem.getCreateTime().getTime());
            return fileResponse;
        }
        return null;
    }

    public UploadedFileListResponse getFileItems(String userSubscriber, Integer uploadStatus, Integer page, Integer size) {
        UploadedFileListResponse response = new UploadedFileListResponse();
        List<FileItem> fileItems = Lists.newArrayList();

        QueryWrapper<FileItem> fileItemQueryWrapper = new QueryWrapper<>();
        Page<FileItem> fileItemPage = new Page<>((page - 1) * size, size);
        fileItemQueryWrapper.eq("user_subscriber", userSubscriber);

        if (uploadStatus != null) {
            fileItemQueryWrapper.eq("upload_state", uploadStatus);
        }
        Page<FileItem> fileItemPageResult =  fileItemMapper.selectPage(fileItemPage, fileItemQueryWrapper);
        if(null != fileItemPageResult && null != fileItemPageResult.getRecords()){

            fileItems = fileItemPageResult.getRecords();
            response.setTotal((int)fileItemPageResult.getTotal());
        }
        if (CollectionUtils.isNotEmpty(fileItems)) {
            response.setUploadedFileList(fileItems.stream().map(fileItem -> {
                UploadedFile file = new UploadedFile();
                file.setId(fileItem.getId());
                file.setName(fileItem.getFileTitle());
                file.setType(fileItem.getFileType());
                file.setSize(fileItem.getFileSize());
                file.setUrl(staticHost + fileItem.getFileKey());
                file.setSignedUrl(fileItem.getSignedUrl());
                file.setStatus(fileItem.getUploadState());
                file.setTimestamp(fileItem.getCreateTime().getTime());
                return file;
            }).collect(Collectors.toList()));
        } else {
            response.setUploadedFileList(Lists.newArrayList());
        }
        return response;
    }

    private String getUserSubscriber(NativeWebRequest request){
        String subscriber = "";
        String fromSource = request.getHeader(SecurityConstants.FROM_SOURCE);
        if(StringUtils.isNotEmpty(fromSource) && "company".equals(fromSource)){
            //企业端校验用户登录
            LoginAdmin loginAdmin = adminService.checkAuth(request);
            if (loginAdmin == null) {
                return subscriber;
            }
            subscriber = loginAdmin.getSubscriber();
        }else{
            // 鉴权
            UserCacheInfo userCacheInfo = authService.checkAuth(request);
            if (userCacheInfo == null) {
                return subscriber;
            }
            subscriber = userCacheInfo.getSubscriber();
        }

        return subscriber;
    }
}
