package ru.practicum.mainservice.compilation.mapper;

import ru.practicum.mainservice.compilation.model.dto.CompilationDto;
import ru.practicum.mainservice.compilation.model.dto.NewCompilationDto;
import ru.practicum.mainservice.compilation.model.entity.Compilation;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static Compilation toCompilation(NewCompilationDto compilationDto) {
        Compilation compilation = new Compilation();
        compilation.setTitle(compilationDto.getTitle());
        compilation.setPinned(compilationDto.getPinned());
        return compilation;
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setPinned(compilation.getPinned());
        return compilationDto;
    }

    public static List<CompilationDto> getCompilationDtoList(List<Compilation> compilationList) {
        return compilationList.stream()
                .map(CompilationMapper::toCompilationDto)
                .sorted(Comparator.comparing(CompilationDto::getId))
                .collect(Collectors.toList());
    }
}
