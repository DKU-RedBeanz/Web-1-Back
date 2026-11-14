package com.redbeanz.backend;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.ArrayList;
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
                    studies.getLeader_nickname(), studies.getCreated_at()
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
                    studies.getLeader_nickname(), studies.getCreated_at()
            );
            responses.add(response);
        }
        return responses;
    }
}