package com.newverse.yama.live.domain.service.impl;

import com.newverse.yama.live.domain.constant.FileConstant;
import com.newverse.yama.live.domain.exception.SystemException;
import com.newverse.yama.live.domain.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import javax.annotation.Resource;
import java.time.Duration;

/**
 * @author liangyu
 * @version 1.0
 * @since 2022/10/16 14:21
 */
@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${aws.s3.signed.expiration.minutes:10}")
    private Long preSignedExpiration;

    @Resource
    private S3Presigner s3Presigner;

    @Override
    public String preSignedFileUrl(String bucketName, String keyName) {
        return preSignedFileUrl(bucketName, keyName, FileConstant.DEFAULT_CONTENT_TYPE);
    }

    @Override
    public String preSignedFileUrl(String bucketName, String keyName, String contentType) {
        try {
            PutObjectRequest objectRequest = PutObjectRequest.builder().bucket(bucketName).key(keyName).contentType(contentType).build();

            PutObjectPresignRequest preSignRequest =
                    PutObjectPresignRequest.builder()
                            .signatureDuration(Duration.ofMinutes(preSignedExpiration))
                            .putObjectRequest(objectRequest).build();
            PresignedPutObjectRequest preSignedRequest = s3Presigner.presignPutObject(preSignRequest);
            String signedUrl = preSignedRequest.url().toString();
            log.info("get signedUrl success:fileKey-{},signedUrl-{}", keyName, signedUrl);
            return signedUrl;
        } catch (Exception ex) {
            log.error("get signedUrl failed:fileKey-{}", keyName, ex);
            throw new SystemException(ex);
        }
    }
}
