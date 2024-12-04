package com.example.clickhouseDemo.service;

import com.example.clickhouseDemo.dto.SearchDto;
import com.example.clickhouseDemo.dto.WeblogResponseDto;
import com.example.clickhouseDemo.entity.Weblog;
import com.example.clickhouseDemo.repository.ClickhouseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


@Service
@Slf4j
public class ClickhouseService {
    private final ObjectMapper objectMapper;
    private final Producer producer;
    private static final Pattern pattern = Pattern.compile("(\\S+) - - \\[(.*?)\\] \"(GET|POST|PUT|DELETE|HEAD|OPTIONS|PATCH) (.*?) (HTTP\\/\\d\\.\\d)\" (\\d{3}) (\\d+) \"(.*?)\" \"(.*?)\" \"(.*?)\"");
    private final ClickhouseRepository clickhouseRepository;

    public ClickhouseService(ObjectMapper objectMapper, Producer producer, ClickhouseRepository clickhouseRepository) {
        this.objectMapper = objectMapper;
        this.producer = producer;
        objectMapper.registerModule(new JavaTimeModule());
        this.clickhouseRepository = clickhouseRepository;
    }

    public static Weblog parseData(String data) {
        Matcher matcher = ClickhouseService.pattern.matcher(data);
        if(matcher.matches()) {
            Weblog weblog = new Weblog();
            weblog.ip = matcher.group(1);
            weblog.timestamp = matcher.group(2);
            weblog.method = matcher.group(3);
            weblog.url = matcher.group(4);
            weblog.protocol = matcher.group(5);
            weblog.status = matcher.group(6);
            weblog.size = matcher.group(7);
            weblog.ref = matcher.group(8);
            weblog.agent = matcher.group(9);
            return weblog;
        }
        else {
            System.out.println("parsing failed " + data);
        }
        return null;
    }

    public ResponseEntity<String> readData() throws IOException {
        Path path = Paths.get(new ClassPathResource("static/access.log").getURI());
        try(Stream<String> lines = Files.lines(path)) {
            lines.map(ClickhouseService::parseData)
                    .filter(Objects::nonNull)
                    .forEach(weblog -> {
                        try {
                            producer.send(weblog);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
            return ResponseEntity.ok("load success");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Page<WeblogResponseDto> search(int page, int size) {
        SearchDto searchDto = new SearchDto();
        searchDto.setPage(page);
        searchDto.setDataCnt(size);
        List<WeblogResponseDto> data = clickhouseRepository.getList(searchDto);
        long totalCnt = 150000;
        return new PageImpl<>(
                data,
                PageRequest.of(page, size),
                totalCnt
        );
    }

    public Page<WeblogResponseDto> findByStatus(int page, int size, String status) {
        SearchDto searchDto = new SearchDto();
        searchDto.setPage(page);
        searchDto.setDataCnt(size);
        searchDto.setKeyword(status);
        List<WeblogResponseDto> data = clickhouseRepository.findByStatus(searchDto);
        long totalCnt = data.isEmpty() ? 0 : 150000;
        if(totalCnt == 0) {
            log.info("no data");
        }
        return new PageImpl<>(
                data,
                PageRequest.of(page, size),
                totalCnt
        );
    }
}
