package br.com.yabaconsultoria.quizz.service;

import br.com.yabaconsultoria.quizz.model.*;
import br.com.yabaconsultoria.quizz.repository.QuestionRepository;
import br.com.yabaconsultoria.quizz.repository.QuizRepository;
import br.com.yabaconsultoria.quizz.repository.QuizResultRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class QuizService {

    @Autowired
    QuizRepository quizDao;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    QuizResultRepository quizResultRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.from}")
    private String mailFrom;

    @Value("Conexão Saúde")
    private String mailFromName;

    @Autowired
    private TemplateEngine templateEngine;

    private static final Logger logger = LoggerFactory.getLogger(QuizService.class);

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
        List<Question> questions = questionRepository.findRandomQuestionsByCategory(category, numQ);

        if (questions == null || questions.isEmpty()) {
            return new ResponseEntity<>("No questions found for the given category", HttpStatus.BAD_REQUEST);
        }

        Quiz quizModel = new Quiz();
        quizModel.setTitle(title);
        quizModel.setQuestions(questions);

        quizDao.save(quizModel);

        logger.info("Quiz created with ID: {}", quizModel.getId());
        questions.forEach(question -> logger.info("Question ID: {}", question.getId()));

        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Long id) {
        Optional<Quiz> quiz = quizDao.findById(id);
        List<Question> questionsFromDB = quiz.get().getQuestions();

        List<QuestionWrapper> questionsForUser = new ArrayList<>();
        for (Question q : questionsFromDB) {
            QuestionWrapper qw = new QuestionWrapper(q.getId(), q.getCategory(),q.getPathology(), q.getQuestion_title(), q.getOption1(), q.getOption2(), q.getOption3(), q.getOption4(), q.getRightAnswer());
            questionsForUser.add(qw);
        }
        return new ResponseEntity<>(questionsForUser, HttpStatus.OK);
    }

    public ResponseEntity<Integer> calculateResult(Long id, List<Response> responses, String name, String email) {
        Optional<Quiz> quizOptional = quizDao.findById(id);
        if (!quizOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Quiz quiz = quizOptional.get();
        List<Question> questions = quiz.getQuestions();

        // Mapear perguntas pelo ID para facilitar a correspondência
        Map<Long, Question> questionMap = new HashMap<>();
        for (Question question : questions) {
            questionMap.put(question.getId(), question);
        }

        Map<String, Integer> pathologyCount = new HashMap<>();
        int right = 0;

        for (Response response : responses) {
            Question question = questionMap.get(response.getQuestionId());
            if (question != null && response.getResponse().equals(question.getRightAnswer())) {
                String pathology = question.getPathology();
                pathologyCount.put(pathology, pathologyCount.getOrDefault(pathology, 0) + 1);
                right++;
            }
        }

        String predominantPathology;
        if (!pathologyCount.isEmpty()) {
            predominantPathology = Collections.max(pathologyCount.entrySet(), Map.Entry.comparingByValue()).getKey();
        } else {
            predominantPathology = "N/A"; // Caso não haja respostas corretas
        }
        logger.info("Predominant Pathology: {}", predominantPathology);

        // Salvar resultado no banco de dados
        QuizResult quizResult = new QuizResult();
        quizResult.setQuiz(quiz);
        quizResult.setPredominantPathology(predominantPathology);
        quizResult.setCorrectAnswers(right);
        quizResult.setName(name);
        quizResult.setEmail(email);
        quizResultRepository.save(quizResult);

        // Enviar resultado por e-mail
        try {
            sendEmailResult(name, email, predominantPathology);
        } catch (MessagingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(right, HttpStatus.OK);
    }
    private void sendEmailResult(String name, String email, String predominantPathology) throws MessagingException {
        String subject = "Resultado do Quiz";
        String to = email;

        // Determinar o especialista e o link com base na patologia predominante
        String specialist;
        String link;
        switch (predominantPathology) {
            case "Asma":
            case "DPOC":
                specialist = "pneumologista";
                link = "https://www.doctoralia.com.br/pesquisa?q=Pneumologista&loc=&filters%5Bspecializations%5D%5B%5D=72&filters%5Bonline_only%5D=1";
                break;
            case "Rinossinusite Crônica":
            case "Polipose Nasal":
                specialist = "otorrinolaringologista";
                link = "https://www.doctoralia.com.br/pesquisa?q=Otorrino&loc=&filters%5Bonline_only%5D%5B%5D=true&filters%5Bspecializations%5D%5B%5D=69&sorter=";
                break;
            case "Dermatite Atópica":
                specialist = "dermatologista";
                link = "https://www.doctoralia.com.br/pesquisa?q=Dermatologista&loc=&filters%5Bspecializations%5D%5B%5D=24&filters%5Bonline_only%5D=1";
                break;
            default:
                specialist = "especialista";
                link = "https://www.doctoralia.com.br/";
                break;
        }

        // Configurar o contexto do Thymeleaf
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("specialist", specialist);
        context.setVariable("link", link);

        // Processar o template
        String body = templateEngine.process("emailTemplate", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        helper.setSubject(subject);
        helper.setFrom(new InternetAddress("Conexão Saúde <" + this.mailFrom + ">"));
        helper.setTo(to);
        helper.setText(body, true); // Set to true to enable HTML content

        mailSender.send(message);
    }
}
