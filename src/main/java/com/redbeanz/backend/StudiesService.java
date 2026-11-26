package com.redbeanz.backend;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class StudiesService {
    private final StudiesRepository studiesRepository;

    public StudiesService(StudiesRepository repository) {
        this.studiesRepository = repository;
    }


    @Transactional(readOnly = true)
    public Optional<StudiesResponse> find(Long id) {
        Optional<Studies> result = studiesRepository.findById(id);
        if (result.isPresent()) {
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
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    public List<StudiesResponse> findAll() {
        List<Studies> studiess = studiesRepository.findAll();
        List<StudiesResponse> responses = new ArrayList<>();
        for (Studies studies : studiess) {
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
    public StudiesResponse update(Long id, StudiesRequest request) {
        Optional<Studies> result = studiesRepository.findById(id);
        if (result.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Studies studies = result.get();

//
        studies.setTitle(request.getTitle());
        studies.setDescription(request.getDescription());

        return new StudiesResponse(studies);
    }

}