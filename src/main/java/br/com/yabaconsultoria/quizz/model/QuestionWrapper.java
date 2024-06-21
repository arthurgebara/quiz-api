package br.com.yabaconsultoria.quizz.model;


import lombok.Data;

@Data
public class QuestionWrapper {

    private Long id;
    private String question_title;
    private String pathology;
    private String category;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String rightAnswer;

    public QuestionWrapper(Long id, String question_title, String pathology, String category, String option1, String option2, String option3, String option4, String rightAnswer) {
        this.id = id;
        this.question_title = question_title;
        this.pathology = pathology;
        this.category = category;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.rightAnswer = rightAnswer;
    }
}
