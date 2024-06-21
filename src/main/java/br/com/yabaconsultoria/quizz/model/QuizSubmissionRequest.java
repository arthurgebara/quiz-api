package br.com.yabaconsultoria.quizz.model;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizSubmissionRequest {

    private String name;
    private String email;
    private List<Response> responses;
}
