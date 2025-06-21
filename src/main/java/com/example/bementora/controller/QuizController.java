package com.example.bementora.controller;
import com.example.bementora.dto.request.QuizCreationRequest;
import com.example.bementora.dto.response.QuizResponse;
import com.example.bementora.service.QuizService;
import com.example.bementora.util.InstructorUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/instructor/quizzes")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('INSTRUCTOR')")
public class QuizController {
    private final QuizService quizService;
    private final InstructorUtil instructorUtil;

    @PostMapping
    public ResponseEntity<QuizResponse> createQuiz(
            @Valid @RequestBody QuizCreationRequest request,
            Authentication authentication) {

        log.info("Received request to create quiz for curriculum: {}", request.getCurriculumId());

        UUID instructorId = instructorUtil.extractInstructorId(authentication);
        QuizResponse response = quizService.createQuiz(request, instructorId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/curriculum/{curriculumId}")
    public ResponseEntity<List<QuizResponse>> getQuizzesByCurriculum(
            @PathVariable UUID curriculumId,
            Authentication authentication) {

        log.info("Received request to get quizzes for curriculum: {}", curriculumId);

        UUID instructorId = instructorUtil.extractInstructorId(authentication);
        List<QuizResponse> responses = quizService.getQuizzesByCurriculum(curriculumId, instructorId);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<QuizResponse> getQuiz(
            @PathVariable UUID quizId,
            Authentication authentication) {

        log.info("Received request to get quiz: {}", quizId);

        UUID instructorId = instructorUtil.extractInstructorId(authentication);
        QuizResponse quiz = quizService.getQuizById(quizId, instructorId);

        return ResponseEntity.ok(quiz);
    }

    @PutMapping("/{quizId}")
    public ResponseEntity<QuizResponse> updateQuiz(
            @PathVariable UUID quizId,
            @Valid @RequestBody QuizCreationRequest request,
            Authentication authentication) {

        log.info("Received request to update quiz: {}", quizId);

        UUID instructorId = instructorUtil.extractInstructorId(authentication);
        QuizResponse response = quizService.updateQuiz(quizId, request, instructorId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{quizId}")
    public ResponseEntity<String> deleteQuiz(
            @PathVariable UUID quizId,
            Authentication authentication) {

        log.info("Received request to delete quiz: {}", quizId);

        UUID instructorId = instructorUtil.extractInstructorId(authentication);
        quizService.deleteQuiz(quizId, instructorId);

        return ResponseEntity.ok("Quiz deleted successfully");
    }
}
