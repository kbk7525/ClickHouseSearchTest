<h2>ClickHouseSearchTest</h2><br>
docker의 clickhouse 적재 후 검색 api 테스트
<h3>흐름</h3>
("/load")<br>
weblog.log file을 읽어와 파싱 -> String 파싱 후 Json으로 형 변환 -> kafka producer로 전달 -> 토픽에 저장 -> consumer에서 꺼내와 clickhouse 테이블에 저장<br>
("/search")<br>
테이블에 적재된 데이터를 가져와서 페이징 처리 키워드 검색까지 추가
