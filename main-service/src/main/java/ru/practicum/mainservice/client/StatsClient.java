package ru.practicum.mainservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.mainservice.client.model.EndpointHitDto;
import ru.practicum.mainservice.common.Utility;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.mainservice.common.Utility.checkForNull;

@Slf4j
@Service
public class StatsClient extends BaseClient {
    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatsClient(@Value("${ewm-stats-service.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public void save(HttpServletRequest request) {
        checkForNull(request, "request");
        EndpointHitDto endpointHit = new EndpointHitDto();
        endpointHit.setApp("MainService");
        endpointHit.setUri(request.getRequestURI());
        endpointHit.setIp(request.getRemoteAddr());
        endpointHit.setTimestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER));
        log.info("Регистрация в stats-service количества просмотров api c uri={}", request.getRequestURI());
        post("/hit", endpointHit);
    }

    public ResponseEntity<Object> getStats() {
        log.info("Получение из stats-service количества просмотров api");
        return get("/stats");
    }

    public Integer getStatsEvent(String uri, long eventId) {
        String path = Utility.buildPath("stats", uri, eventId);
        log.info("Получение из stats-service количества просмотров api c path={}", path);
        Integer views = (Integer) get(path).getBody();
        log.info("Количество views={}", views);
        return views;
    }
}
