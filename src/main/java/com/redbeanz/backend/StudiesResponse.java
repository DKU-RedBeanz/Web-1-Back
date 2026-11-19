package com.redbeanz.backend;
import lombok.Getter;
@Getter
public class StudiesResponse {
    private final Long id;
    private final String title;
    private final String summary;
    private final String description;
    private final String goal;
    private final String level;
    private final String time;
    private final String mode;
    private final String region;
    private final String location;
    private final Long member_count;
    private final Long capacity;
    private final String leader_nickname;
    private final String created_at;

    public StudiesResponse(
            Long id, String title, String summary, String description,
            String goal, String level, String time, String mode,
            String region, String location, Long member_count,
            Long capacity, String leader_nickname, String created_at) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.description = description;
        this.goal = goal;
        this.level = level;
        this.time = time;
        this.mode = mode;
        this.region = region;
        this.location = location;
        this.member_count = member_count;
        this.capacity = capacity;
        this.leader_nickname = leader_nickname;
        this.created_at = created_at;
    }
}