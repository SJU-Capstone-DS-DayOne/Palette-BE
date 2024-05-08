package kr.ac.sejong.ds.palette.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonController {
    @GetMapping("/")
    public ResponseEntity<?> mainPage(){
        return ResponseEntity.ok().build();
    }
}
