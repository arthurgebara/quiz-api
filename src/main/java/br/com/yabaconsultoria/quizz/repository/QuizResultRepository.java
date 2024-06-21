package br.com.yabaconsultoria.quizz.repository;

import br.com.yabaconsultoria.quizz.model.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
}