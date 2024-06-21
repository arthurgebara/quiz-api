package br.com.yabaconsultoria.quizz.controller;

import br.com.yabaconsultoria.quizz.model.QuestionWrapper;
import br.com.yabaconsultoria.quizz.model.QuizSubmissionRequest;
import br.com.yabaconsultoria.quizz.model.Response;
import br.com.yabaconsultoria.quizz.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("quiz")
public class QuizController {

    @Autowired
    QuizService quizService;

    @PostMapping("create")
    public ResponseEntity<String> createQuiz(@RequestParam String category, @RequestParam int numQ, @RequestParam String title) {
        return quizService.createQuiz(category, numQ, title);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(@PathVariable Long id) {
        return quizService.getQuizQuestions(id);
    }

    @PostMapping("submit/{id}")
    public ResponseEntity<Integer> submitQuiz(
            @PathVariable Long id,
            @RequestBody QuizSubmissionRequest request) {
        return quizService.calculateResult(id, request.getResponses(), request.getName(), request.getEmail());
    }
}
