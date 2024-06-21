package br.com.yabaconsultoria.quizz.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name="question")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String question_title;
    private String category;
    private String pathology;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String rightAnswer;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;




}
