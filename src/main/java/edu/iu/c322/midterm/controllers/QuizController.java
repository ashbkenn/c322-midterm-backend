package edu.iu.c322.midterm.controllers;

import edu.iu.c322.midterm.model.Question;
import edu.iu.c322.midterm.model.Quiz;
import edu.iu.c322.midterm.repository.FileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/quizzes")
public class QuizController {
    private final FileRepository fileRepository;

    public QuizController(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @PostMapping
    public ResponseEntity<Integer> addQuiz(@RequestBody Quiz quiz) {
        try {
            int id = fileRepository.addQuiz(quiz);
            return new ResponseEntity<>(id, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllQuizzes() {
        try {
            return new ResponseEntity<>(fileRepository.findAllQuizzes(), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuizById(@PathVariable int id) {
        try {
            Quiz quiz = fileRepository.getQuiz(id);
            if (quiz != null) {
                quiz.setQuestions(fileRepository.find(quiz.getQuestionIds()));
                return new ResponseEntity<>(quiz, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuiz(@PathVariable int id, @RequestBody Quiz updatedQuiz) {
        try {
            boolean result = fileRepository.updateQuiz(id, updatedQuiz);
            if (result) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
