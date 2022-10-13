package ru.practicum.mainservice.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.common.exception.NotFoundException;
import ru.practicum.mainservice.compilation.model.entity.Compilation;
import ru.practicum.mainservice.compilation.repository.CompilationRepository;
import ru.practicum.mainservice.event.model.entity.Event;
import ru.practicum.mainservice.event.service.EventService;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventService eventService;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public Compilation addCompilation(Compilation compilation) {
        log.debug("Добавление подборки {}", compilation);
        return compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public void deleteCompilation(long compId) {
        log.debug("Удаление подборки с compId={}", compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public void pinCompilation(long compId) {
        log.info("Закрепить подборку на главной странице с compId={}", compId);
        Compilation compilation = getCompilationByCompId(compId);
        compilation.setPinned(true);
        compilationRepository.save(compilation);
        log.info("Подборка закреплена");
    }

    @Override
    @Transactional
    public void unPinCompilation(long compId) {
        log.info("Открепить подборку на главной странице с compId={}", compId);
        Compilation compilation = getCompilationByCompId(compId);
        compilation.setPinned(false);
        compilationRepository.save(compilation);
        log.info("Подборка откреплена");
    }

    @Override
    @Transactional
    public void addEventToCompilation(long compId, long eventId) {
        log.info("Добавить событие с eventId={} из подборки с compId={}", eventId, compId);
        Compilation compilation = getCompilationByCompId(compId);
        Event event = eventService.getEventByEventId(eventId);
        compilation.addEvent(event);
        entityManager.detach(compilation);
        compilationRepository.save(compilation);
        log.info("Событие добавлено в подборку");
    }

    @Override
    @Transactional
    public void removeEventFromCompilation(long compId, long eventId) {
        log.info("Удалить событие с eventId={} из подборки с compId={}", eventId, compId);
        Compilation compilation = getCompilationByCompId(compId);
        Event event = eventService.getEventByEventId(eventId);
        compilation.deleteEvent(event);
        entityManager.detach(compilation);
        compilationRepository.save(compilation);
        log.info("Событие удалено из подборки");
    }

    @Override
    public List<Compilation> getCompilations(Boolean pinned, Integer from, Integer size) {
        log.debug("Получение подборок событий:");
        log.info("pinned={}, from={}, size={}", pinned, from, size);
        PageRequest pr = PageRequest.of(from / size, size);
        return compilationRepository.getCompilationByPinned(pinned, pr).toList();
    }

    @Override
    public Compilation getCompilationById(long compId) {
        log.debug("Получение подборки события с compId={}", compId);
        return getCompilationByCompId(compId);
    }

    Compilation getCompilationByCompId(Long compId) {
        return compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Подборки с userId=" + compId + " не существует!")
        );
    }
}
