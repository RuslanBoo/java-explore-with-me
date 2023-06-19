package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.CompilationMapper;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.error.exception.DataNotFoundException;
import ru.practicum.events.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.service.EventService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventService eventService;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;

    public CompilationDto prepareDto(Compilation compilation) {
        CompilationDto compilationDto = compilationMapper.toDto(compilation);
        Set<Event> events = compilation.getEvents();

        if (events != null && !events.isEmpty()) {
            compilationDto.setEvents(compilation.getEvents().stream().map(eventMapper::toShortDto).collect(Collectors.toList()));
        } else {
            compilationDto.setEvents(new ArrayList<>());
        }

        return compilationDto;
    }

    public Compilation prepareDao(NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationMapper.fromDto(newCompilationDto);
        Set<Integer> eventIds = newCompilationDto.getEvents();

        if (eventIds != null && !eventIds.isEmpty()) {
            compilation.setEvents(eventService.findAllById(eventIds));
        } else {
            compilation.setEvents(new HashSet<>());
        }

        return compilation;
    }

    public Compilation findById(int compilationId) {
        return compilationRepository.findById(compilationId)
                .orElseThrow(() -> new DataNotFoundException(Compilation.class.getName(), compilationId)
        );
    }

    public CompilationDto getById(int compId) {
        Compilation result = findById(compId);

        return prepareDto(result);
    }

    public List<CompilationDto> getAll(Boolean pinned, int from, int size) {
        List<Compilation> result = compilationRepository.findAllByPinned(pinned, from, size);

        return result.stream()
                .map(this::prepareDto)
                .collect(Collectors.toList());
    }

    public CompilationDto save(NewCompilationDto newCompilationDto) {
        Compilation compilation = prepareDao(newCompilationDto);

        return prepareDto(compilationRepository.save(compilation));
    }

    public CompilationDto update(int compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = findById(compId);
        compilationMapper.update(updateCompilationRequest, compilation);
        Set<Integer> eventIds = updateCompilationRequest.getEvents();

        if (eventIds != null && !eventIds.isEmpty()) {
            compilation.setEvents(eventService.findAllById(eventIds));
        }

        return prepareDto(compilationRepository.save(compilation));
    }

    public void delete(int compId) {
        Compilation compilation = findById(compId);
        compilationRepository.delete(compilation);
    }
}
