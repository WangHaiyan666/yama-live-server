package com.newverse.yama.live.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.newverse.yama.live.domain.entity.FileItem;
import com.newverse.yama.live.domain.model.PreSignUploadFileRequest;
import com.newverse.yama.live.domain.model.R;
import com.newverse.yama.live.domain.model.UploadSignedFileRequest;
import com.newverse.yama.live.domain.model.UploadedFileListRequest;
import org.springframework.web.context.request.NativeWebRequest;

public interface IFileItemService extends IService<FileItem> {

    R fileListPost(UploadedFileListRequest uploadedFileListRequest, NativeWebRequest request);

    R filePreSignedPost(PreSignUploadFileRequest preSignUploadFileRequest, NativeWebRequest request);

    R fileUploadSignedPost(UploadSignedFileRequest uploadSignedFileRequest, NativeWebRequest request);
}
