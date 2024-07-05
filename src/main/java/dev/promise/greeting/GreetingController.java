package dev.promise.greeting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class GreetingController {

    @Autowired
    private GreetingService greetingService;
    @GetMapping("/hello")
    public ResponseEntity<Map<String, String>> greeting(@RequestParam(name = "visitor_name") String visitorName){
        return ResponseEntity.ok(greetingService.greet(visitorName));
    }
}