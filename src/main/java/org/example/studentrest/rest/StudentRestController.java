package org.example.studentrest.rest;

import jakarta.annotation.PostConstruct;
import org.example.studentrest.entity.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api")
public class StudentRestController {

    private List<Student> students;

    // define to load data only once
    @PostConstruct
    public void loadData() {
        System.out.println("Loading data");

        students = new ArrayList<>();

        students.add(new Student("John", "Doe"));
        students.add(new Student("Teresa", "Torres"));
        students.add(new Student("Elisa", "Aguilar"));

        System.out.println("Data loaded");
    }

    // define endpoint for "api/student/{studentId} - return student at index
    @GetMapping("/student/{studentId}")
    public Student getStudent(@PathVariable int studentId) {

        if ( (studentId < 0) || (studentId >= students.size()) ) {
            throw new StudentNotFoundException("Student id not found - " + studentId);
        }

        return students.get(studentId);
    }

    // define endpoint for "api/students" - return List<Student>
    @GetMapping("/students")
    public List<Student> getStudents() {

        // Jackson will convert List<Student> to JSON array
        return students;
    }

    // Add an exception handler using @ExceptionHandler
    @ExceptionHandler
    public ResponseEntity<StudentErrorResponse> handleException(StudentNotFoundException ex) {

        // create a StudentErrorResponse
        StudentErrorResponse error = new StudentErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(ex.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        // return ResponseEntity
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // add another exception handler ... to catch any exception
    @ExceptionHandler
    public ResponseEntity<StudentErrorResponse> handlerException(Exception ex) {

        // create a StudentErrorResponse
        StudentErrorResponse error = new StudentErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(ex.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
