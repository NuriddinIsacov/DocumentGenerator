package com.example.contractgenerator.api;


import com.example.contractgenerator.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files/v1/")
public class FileController {
    private final FileService fileService;

    @PostMapping( "/process")
    public ResponseEntity<InputStreamResource> copyAndModifyPdf(@RequestBody Map<String, String> placeholders) throws IOException {
        return fileService.copyAndModifyPdf(placeholders);
    }
}
