package br.com.yabaconsultoria.quizz.controller;


import br.com.yabaconsultoria.quizz.model.Question;
import br.com.yabaconsultoria.quizz.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("allQuestions")
    public ResponseEntity<List<Question>> getAllQuestions(){
        return questionService.getAllQuestions();
    }

    @GetMapping("pathology/{pathology}")
    public ResponseEntity<List<Question>> getQuestionsByPathology(@PathVariable String pathology){

        return questionService.getQuestionsByPathology(pathology);

    }

    @PostMapping("addQuestion")
    public ResponseEntity<String> addQuestion(@RequestBody Question question){
        return questionService.addQuestion(question);
    }

    @PostMapping("addQuestions")
    public ResponseEntity<String> addQuestions(@RequestBody List<Question> questions) {
        return questionService.addQuestions(questions);
    }
}
