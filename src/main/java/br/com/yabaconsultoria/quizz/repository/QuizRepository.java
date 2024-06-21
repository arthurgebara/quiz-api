package br.com.yabaconsultoria.quizz.repository;

import br.com.yabaconsultoria.quizz.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
