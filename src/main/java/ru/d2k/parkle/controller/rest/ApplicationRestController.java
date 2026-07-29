package ru.d2k.parkle.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.d2k.parkle.controller.ApiRoutes;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiRoutes.API)
public class ApplicationRestController {

    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        return ResponseEntity.ok().build();
    }
}
