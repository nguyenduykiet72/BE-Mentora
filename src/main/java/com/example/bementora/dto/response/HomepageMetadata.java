package com.example.bementora.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomepageMetadata {
    private boolean hasNewCourse;
    private boolean hasPopularCourse;
    private boolean hasRecommendedCourse;
    private LocalDateTime lastUpdated;
    private int totalNewCourse;
    private int totalPopularCourse;
    private int totalRecommendedCourse;
}
