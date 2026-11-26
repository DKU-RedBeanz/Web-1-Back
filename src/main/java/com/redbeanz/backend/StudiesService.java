package com.redbeanz.backend;

import java.util.ArrayList;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
public class StudiesService {
    private final StudiesRepository studiesRepository;

    public StudiesService(StudiesRepository repository) {
        this.studiesRepository = repository;
    }


    @Transactional(readOnly = true)
    public Optional<StudiesResponse> find(Long id) {
        Optional<Studies> result = studiesRepository.findById(id);
        if (result.isEmpty()) {
            return Optional.empty();
        }
        Studies studies = result.get();
        StudiesResponse response = new StudiesResponse(
                studies.getId(), studies.getTitle(),
                studies.getSummary(), studies.getDescription(),
                studies.getGoal(), studies.getLevel(), studies.getTime(),
                studies.getMode(), studies.getRegion(), studies.getLocation(),
                studies.getMember_count(), studies.getCapacity(),
                studies.getLeader_nickname(), studies.getCreatedAt().toString()
        );
        return Optional.of(response);
    }

    @Transactional(readOnly = true)
    public List<StudiesResponse> findAll() {
        List<Studies> studiesList = studiesRepository.findAll();
        List<StudiesResponse> responses = new ArrayList<>();
        for (Studies studies : studiesList) {
            StudiesResponse response = new StudiesResponse(
                    studies.getId(), studies.getTitle(),
                    studies.getSummary(), studies.getDescription(),
                    studies.getGoal(), studies.getLevel(), studies.getTime(),
                    studies.getMode(), studies.getRegion(), studies.getLocation(),
                    studies.getMember_count(), studies.getCapacity(),
                    studies.getLeader_nickname(), studies.getCreatedAt().toString()
            );
            responses.add(response);
        }
        return responses;
    }

    @Transactional
    public StudiesResponse create(StudiesCreateRequest request) {
        Studies studies = new Studies();
        studies.setTitle(request.getTitle());
        studies.setSummary(request.getSummary());
        studies.setDescription(request.getDescription());
        studies.setGoal(request.getGoal());
        studies.setLevel(request.getLevel());
        studies.setTime(request.getTime());
        studies.setMode(request.getMode());
        studies.setRegion(request.getRegion());
        studies.setLocation(request.getLocation());
        studies.setCapacity(request.getCapacity());
        studies.setLeader_nickname(request.getLeader_nickname());
        Studies saved = studiesRepository.saveAndFlush(studies);
        StudiesResponse response = new StudiesResponse(
                saved.getId(), saved.getTitle(),
                saved.getSummary(), saved.getDescription(),
                saved.getGoal(), saved.getLevel(), saved.getTime(),
                saved.getMode(), saved.getRegion(), saved.getLocation(),
                saved.getMember_count(), saved.getCapacity(),
                saved.getLeader_nickname(), saved.getCreatedAt().toString()
        );
        return response;
    }

    @Transactional
    public Optional<StudiesResponse> update(Long id, StudiesUpdateRequest request) {
        Optional<Studies> result = studiesRepository.findById(id);
        if (result.isEmpty()) {
            return Optional.empty();
        }
        Studies studies = result.get();
        studies.setTitle(request.getTitle());
        studies.setDescription(request.getDescription());
        StudiesResponse response = new StudiesResponse(
                studies.getId(), studies.getTitle(),
                studies.getSummary(), studies.getDescription(),
                studies.getGoal(), studies.getLevel(), studies.getTime(),
                studies.getMode(), studies.getRegion(), studies.getLocation(),
                studies.getMember_count(), studies.getCapacity(),
                studies.getLeader_nickname(), studies.getCreatedAt().toString()
        );
        return Optional.of(response);
    }

    @Transactional
    public boolean delete(Long id) {
        Optional<Studies> result = studiesRepository.findById(id);
        if (result.isEmpty()) {
            return false;
        }
        Studies studies = result.get();
        studiesRepository.delete(studies);
        return true;
    }
}