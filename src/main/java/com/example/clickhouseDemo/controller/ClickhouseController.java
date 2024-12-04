package com.example.clickhouseDemo.controller;

import com.example.clickhouseDemo.dto.WeblogResponseDto;
import com.example.clickhouseDemo.service.ClickhouseService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ClickhouseController {

    private final ClickhouseService clickhouseService;

    public ClickhouseController(ClickhouseService clickhouseService) {
        this.clickhouseService = clickhouseService;
    }

    @GetMapping("/load")
    public ResponseEntity<String> load() throws IOException {
       return clickhouseService.readData();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<WeblogResponseDto>> search(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<WeblogResponseDto> data = clickhouseService.search(page, size);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/search/status")
    public ResponseEntity<Page<WeblogResponseDto>> searchStatus(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam String status) throws Exception {
        Page<WeblogResponseDto> data = clickhouseService.findByStatus(page, size, status);
        return ResponseEntity.ok(data);
    }
}
