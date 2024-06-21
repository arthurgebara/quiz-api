package br.com.yabaconsultoria.quizz.model;


import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@RequiredArgsConstructor
@Getter
@Setter
public class Response {

    private Long questionId;
    private String response;

}
