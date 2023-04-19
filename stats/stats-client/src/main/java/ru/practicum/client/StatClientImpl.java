package ru.practicum.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StatClientImpl implements StatClient {
    @Value("${stats-server.url}")
    private final String SERVER_URL;
    private final RestTemplate restTemplate;
    private final HttpHeaders headers;
    private final ObjectMapper objectMapper;

    private void prepareHeader() {
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    @Override
    public void saveHit(String app, String uri, String ip, LocalDateTime timestamp) {
        prepareHeader();

        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app(app)
                .uri(uri)
                .ip(ip)
                .timestamp(timestamp)
                .build();

        HttpEntity<EndpointHitDto> entity = new HttpEntity<>(endpointHitDto, headers);
        ResponseEntity<String> response = this.restTemplate.postForEntity(
                SERVER_URL + "/hit",
                entity,
                String.class);

        if (response.getStatusCode() != HttpStatus.CREATED) {
            throw new RuntimeException("Http request failed");
        }
    }

    @SneakyThrows
    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris) {
        prepareHeader();

        Map<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("end", end);
        params.put("uris", uris);

        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                SERVER_URL + "/stats?start={start}&end={end}&uris={uris}",
                HttpMethod.GET,
                request,
                String.class,
                params
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            return Collections.emptyList();
        }

        return Arrays.asList(objectMapper.readValue(response.getBody(), ViewStatsDto[].class));
    }
}
