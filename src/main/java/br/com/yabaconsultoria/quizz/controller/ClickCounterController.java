package br.com.yabaconsultoria.quizz.controller;

import br.com.yabaconsultoria.quizz.service.ClickCounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/click")
public class ClickCounterController {

    @Autowired
    private ClickCounterService clickCounterService;

    @PostMapping("/increment")
    public ResponseEntity<String> incrementClickCount(@RequestBody String pathology) {
        clickCounterService.incrementClickCount(pathology);
        return ResponseEntity.ok("Click count incremented for pathology: " + pathology);
    }
}