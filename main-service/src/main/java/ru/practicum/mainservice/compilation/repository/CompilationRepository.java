package ru.practicum.mainservice.compilation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.practicum.mainservice.compilation.model.entity.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    Page<Compilation> getCompilationByPinned(@Param("pinned") Boolean pinned, Pageable pageable);
}
