package com.example.clickhouseDemo.repository;

import com.example.clickhouseDemo.dto.SearchDto;
import com.example.clickhouseDemo.dto.WeblogResponseDto;
import com.example.clickhouseDemo.entity.Weblog;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ClickhouseRepository {

    private final JdbcTemplate jdbcTemplate;

    public ClickhouseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Weblog weblog) {
        String sql = "insert into weblog (*) values (?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql, weblog.getIp(), weblog.getTimestamp(), weblog.getMethod(),
                weblog.getUrl(), weblog.getProtocol(), weblog.getStatus(), weblog.getSize(), weblog.getRef(), weblog.getAgent());
    }

    //페이징 처리 및 사용자가 입력한 개수만큼 데이터를 가져옴
    public List<WeblogResponseDto> getList(SearchDto searchDto) {
        int offset = searchDto.getPage() * searchDto.getDataCnt();
        String sql = """
                       select * from weblog
                       order by timestamp DESC
                       limit ? offset ?
                       """;
        return jdbcTemplate.query(sql,
                new Object[]{searchDto.getDataCnt(), offset}, this::mapperWeblogResponseDto);
    }

    public List<WeblogResponseDto> findByStatus(SearchDto searchDto) {
        int offset = searchDto.getPage() * searchDto.getDataCnt();
        String sql = """
                       select * from weblog
                       where status = ?
                       order by timestamp DESC
                       limit ? offset ?
                       """;
        return jdbcTemplate.query(sql,
                new Object[]{searchDto.getKeyword(), searchDto.getDataCnt(), offset}, this::mapperWeblogResponseDto);
    }

    private WeblogResponseDto mapperWeblogResponseDto(ResultSet rs, int rowNum) throws SQLException {
        WeblogResponseDto dto = new WeblogResponseDto();
        dto.setIp(rs.getString("ip"));
        dto.setTimestamp(rs.getString("timestamp"));
        dto.setMethod(rs.getString("method"));
        dto.setUrl(rs.getString("url"));
        dto.setProtocol(rs.getString("protocol"));
        dto.setStatus(rs.getString("status"));
        dto.setSize(rs.getString("size"));
        dto.setRef(rs.getString("ref"));
        dto.setAgent(rs.getString("agent"));
        return dto;
    }
}
