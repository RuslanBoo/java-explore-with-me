package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.EndpointHitDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatClientImpl implements StatClient {
    @Value("${stats-server.url}")
    private final String serverURL;
    private final RestTemplate restTemplate;
    private final HttpHeaders headers;

    private void prepareHeader() {
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    @Override
    public ResponseEntity<Object> saveHit(String app, String uri, String ip, LocalDateTime timestamp) {
        prepareHeader();

        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app(app)
                .uri(uri)
                .ip(ip)
                .timestamp(timestamp)
                .build();

        HttpEntity<EndpointHitDto> entity = new HttpEntity<>(endpointHitDto, headers);

        return this.restTemplate.postForEntity(
                serverURL + "/hit",
                entity,
                Object.class);
    }

    @SneakyThrows
    @Override
    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uris) {
        prepareHeader();

        Map<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("end", end);
        params.put("uris", uris);

        HttpEntity request = new HttpEntity(headers);

        return restTemplate.exchange(
                serverURL + "/stats?start={start}&end={end}&uris={uris}",
                HttpMethod.GET,
                request,
                Object.class,
                params
        );
    }
}
