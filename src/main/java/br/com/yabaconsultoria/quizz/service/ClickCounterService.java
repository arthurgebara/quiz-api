package br.com.yabaconsultoria.quizz.service;

import br.com.yabaconsultoria.quizz.model.ClickCounter;
import br.com.yabaconsultoria.quizz.repository.ClickCounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClickCounterService {

    @Autowired
    private ClickCounterRepository clickCounterRepository;

    public void incrementClickCount(String pathology) {
        Optional<ClickCounter> clickCounterOpt = clickCounterRepository.findByPathology(pathology);
        ClickCounter clickCounter;

        if (clickCounterOpt.isPresent()) {
            clickCounter = clickCounterOpt.get();
        } else {
            clickCounter = new ClickCounter();
            clickCounter.setPathology(pathology);
        }

        clickCounter.setClickCount(clickCounter.getClickCount() + 1);
        clickCounterRepository.save(clickCounter);
    }
}
