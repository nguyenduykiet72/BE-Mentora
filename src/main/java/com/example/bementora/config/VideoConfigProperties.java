package com.example.bementora.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app.video")
@Data
public class VideoConfigProperties {
    private Long maxSize = 2147483648L;
    private List<String> allowedTypes = List.of(
            "video/mp4",
            "video/quicktime",
            "video/x-msvideo",
            "video/x-flv",
            "video/webm"
    );
    private String storagePath = "videos";
}
