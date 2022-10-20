package ru.practicum.mainservice.compilation.service;

import ru.practicum.mainservice.compilation.model.entity.Compilation;

import java.util.List;

public interface CompilationService {
    Compilation addCompilation(Compilation compilation);

    void deleteCompilation(long compId);

    void pinCompilation(long compId);

    void unPinCompilation(long compId);

    void removeEventFromCompilation(long compId, long eventId);

    void addEventToCompilation(long compId, long eventId);

    List<Compilation> getCompilations(Boolean pinned, Integer from, Integer size);

    Compilation getCompilationById(long compId);
}
