package br.com.yabaconsultoria.quizz.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "click_counter")
@NoArgsConstructor
@AllArgsConstructor
public class ClickCounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String pathology;

    @Column(nullable = false)
    private int clickCount;
}
