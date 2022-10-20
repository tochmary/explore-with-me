package ru.practicum.mainservice.compilation.controller.publicC;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.compilation.mapper.CompilationMapper;
import ru.practicum.mainservice.compilation.model.dto.CompilationDto;
import ru.practicum.mainservice.compilation.model.entity.Compilation;
import ru.practicum.mainservice.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class CompilationController {
    private final CompilationService compilationService;

    /**
     * Получение подборок событий
     *
     * @param pinned искать только закрепленные/не закрепленные подборки
     * @param from   количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size   количество элементов в наборе
     * @return List<CompilationShortDto> список Краткой информации о событии
     */
    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получение подборок событий:");
        log.info("искать только закрепленные/не закрепленные подборки: {}", pinned);
        log.info("количество элементов, которые нужно пропустить для формирования текущего набора: {}", from);
        log.info("количество элементов в наборе: {}", size);
        List<Compilation> compilationList = compilationService.getCompilations(pinned, from, size);
        return CompilationMapper.getCompilationDtoList(compilationList);
    }

    /**
     * Получение подборки событий по его id
     *
     * @param compId id подборки
     * @return CompilationDto Подборка события
     */
    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable long compId) {
        log.info("Получение подборки событий с compId={}", compId);
        Compilation compilation = compilationService.getCompilationById(compId);
        return CompilationMapper.toCompilationDto(compilation);
    }
}