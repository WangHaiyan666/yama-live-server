package com.newverse.yama.live.domain.service;

/**
 * @author liangyu
 * @version 1.0
 * @since 2022/10/16 14:14
 */

public interface FileUploadService {

    /**
     * @param bucketName bucket
     * @param keyName    fileKey
     * @return signedUrl
     */
    String preSignedFileUrl(String bucketName, String keyName);

    /**
     * @param bucketName  bucket
     * @param keyName     fileKey
     * @param contentType content type
     * @return signedUrl
     */
    String preSignedFileUrl(String bucketName, String keyName, String contentType);
}
