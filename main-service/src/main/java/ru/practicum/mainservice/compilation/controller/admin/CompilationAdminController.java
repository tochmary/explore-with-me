package ru.practicum.mainservice.compilation.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.compilation.mapper.CompilationMapper;
import ru.practicum.mainservice.compilation.model.dto.CompilationDto;
import ru.practicum.mainservice.compilation.model.dto.NewCompilationDto;
import ru.practicum.mainservice.compilation.model.entity.Compilation;
import ru.practicum.mainservice.compilation.service.CompilationService;
import ru.practicum.mainservice.event.model.entity.Event;
import ru.practicum.mainservice.event.service.EventService;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.mainservice.common.Utility.checkForNull;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class CompilationAdminController {
    private final CompilationService compilationService;
    private final EventService eventService;

    /**
     * Добавления новой подборки
     *
     * @param compilationDto Подборка событий
     * @return CompilationDto Подборка событий:
     */
    @PostMapping
    public CompilationDto saveCompilation(@RequestBody NewCompilationDto compilationDto) {
        log.info("Добавления новой подборки {}", compilationDto);
        checkForNull(compilationDto, "compilationDto");
        List<Event> eventList = compilationDto.getEvents().stream().map(eventService::getEventByEventId).collect(Collectors.toList());
        Compilation compilation = CompilationMapper.toCompilation(compilationDto, eventList);
        compilation = compilationService.addCompilation(compilation);
        return CompilationMapper.toCompilationDto(compilation);
    }

    /**
     * Удаление подборки
     *
     * @param compId id подборки
     */
    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable long compId) {
        log.info("Удаление подборки с compId={}", compId);
        compilationService.deleteCompilation(compId);
    }

    /**
     * Закрепить подборку на главной странице
     *
     * @param compId id подборки
     */
    @PatchMapping("/{compId}/pin")
    public void pinCompilation(@PathVariable long compId) {
        log.info("Закрепить подборку на главной странице с compId={}", compId);
        compilationService.pinCompilation(compId);
    }

    /**
     * Открепить подборку на главной странице
     *
     * @param compId id подборки
     */
    @DeleteMapping("/{compId}/pin")
    public void unPinCompilation(@PathVariable long compId) {
        log.info("Открепить подборку на главной странице с compId={}", compId);
        compilationService.unPinCompilation(compId);
    }

    /**
     * Добавить событие из подборки
     *
     * @param compId  id подборки
     * @param eventId id события
     */
    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable long compId,
                                      @PathVariable long eventId) {
        log.info("Добавить событие с eventId={} из подборки с compId={}", eventId, compId);
        compilationService.addEventToCompilation(compId, eventId);
    }

    /**
     * Удалить событие из подборки
     *
     * @param compId  id подборки
     * @param eventId id события
     */
    @DeleteMapping("/{compId}/events/{eventId}")
    public void removeEventFromCompilation(@PathVariable long compId,
                                           @PathVariable long eventId) {
        log.info("Удалить событие с eventId={} из подборки с compId={}", eventId, compId);
        compilationService.removeEventFromCompilation(compId, eventId);
    }
}