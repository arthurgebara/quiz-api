package br.com.yabaconsultoria.quizz.repository;

import br.com.yabaconsultoria.quizz.model.ClickCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClickCounterRepository extends JpaRepository<ClickCounter, Long> {
    Optional<ClickCounter> findByPathology(String pathology);
}