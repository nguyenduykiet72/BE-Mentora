package com.example.bementora.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws.s3")
@Data
public class S3ConfigProperties {
    private String accessKey;
    private String secretKey;
    private String bucketName;
    private String region;
    private Long presignedUrlExpiration = 3600L;
    private String objectKeyPrefix = "videos";
}
