package com.example.bementora.service;

import com.example.bementora.dto.request.QuizCreationRequest;
import com.example.bementora.dto.response.QuizResponse;

import java.util.List;
import java.util.UUID;

public interface QuizService {
    QuizResponse createQuiz(QuizCreationRequest request, UUID instructorId);

    QuizResponse getQuizById(UUID quizId, UUID instructorId);

    List<QuizResponse> getQuizzesByCurriculum(UUID curriculumId, UUID instructorId);

    QuizResponse updateQuiz(UUID quizId, QuizCreationRequest request, UUID instructorId);

    void deleteQuiz(UUID quizId, UUID instructorId);
}
