
package it.epicode.ecotrack.services;

import it.epicode.ecotrack.auth.AppUser;
import it.epicode.ecotrack.dto.MissionDto;
import it.epicode.ecotrack.dto.UserFullProgressDto;
import it.epicode.ecotrack.entities.Mission;
import it.epicode.ecotrack.entities.UserMission;
import it.epicode.ecotrack.entities.UserProgress;
import it.epicode.ecotrack.repositories.MissionRepository;
import it.epicode.ecotrack.repositories.UserMissionRepository;
import it.epicode.ecotrack.repositories.UserProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserProgressService {

    private final UserProgressRepository userProgressRepository;
    private final MissionRepository missionRepository;
    private final UserMissionRepository userMissionRepository;


    private MissionDto toMissionDto(Mission mission, boolean completed) {
        MissionDto dto = new MissionDto();
        dto.setId(mission.getId());
        dto.setTitle(mission.getTitle());
        dto.setDescription(mission.getDescription());
        dto.setPoints(mission.getPoints());
        dto.setBadge(mission.getBadge());
        dto.setCompleted(completed);
        return dto;
    }


    private List<MissionDto> getMissionsForUser(AppUser user) {
        List<Mission> missions = missionRepository.findAll();
        return missions.stream().map(mission -> {
            boolean completed = userMissionRepository
                    .findByUserAndMission(user, mission)
                    .map(UserMission::isCompleted)
                    .orElse(false);
            return toMissionDto(mission, completed);
        }).collect(Collectors.toList());
    }


    private UserFullProgressDto toFullProgressDto(UserProgress progress, List<MissionDto> missionsForUser) {
        UserFullProgressDto dto = new UserFullProgressDto();
        dto.setUserId(progress.getUser().getId());
        dto.setScore(progress.getScore());
        dto.setLevel(progress.getLevel());
        dto.setMissions(missionsForUser);

        List<String> badges = missionsForUser.stream()
                .filter(MissionDto::isCompleted)
                .map(MissionDto::getBadge)
                .filter(b -> b != null)
                .distinct()
                .collect(Collectors.toList());
        dto.setBadges(badges);
        return dto;
    }


    @Transactional
    public UserFullProgressDto getFullProgressByUser(Long userId) {
        UserProgress progress = userProgressRepository.findByUser_Id(userId)
                .orElseGet(() -> {
                    UserProgress newProgress = UserProgress.builder()
                            .user(AppUser.builder().id(userId).build())
                            .score(0)
                            .build();
                    return userProgressRepository.save(newProgress);
                });
        AppUser user = progress.getUser();
        List<MissionDto> missionsForUser = getMissionsForUser(user);
        return toFullProgressDto(progress, missionsForUser);
    }


    @Transactional
    public UserFullProgressDto updateScore(Long userId, int pointsToAdd) {
        UserProgress progress = userProgressRepository.findByUser_Id(userId)
                .orElseGet(() -> {
                    UserProgress newProgress = UserProgress.builder()
                            .user(AppUser.builder().id(userId).build())
                            .score(0)
                            .build();
                    return userProgressRepository.save(newProgress);
                });
        progress.setScore(progress.getScore() + pointsToAdd);
        userProgressRepository.save(progress);
        AppUser user = progress.getUser();
        List<MissionDto> missionsForUser = getMissionsForUser(user);
        return toFullProgressDto(progress, missionsForUser);
    }


    @Transactional
    public UserFullProgressDto completeMission(Long userId, Long missionId) {
        AppUser user = AppUser.builder().id(userId).build();
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new RuntimeException("Mission not found"));


        UserMission userMission = userMissionRepository.findByUserAndMission(user, mission)
                .orElse(UserMission.builder().user(user).mission(mission).completed(false).build());


        if (!userMission.isCompleted()) {
            userMission.setCompleted(true);
            userMissionRepository.save(userMission);


            UserProgress progress = userProgressRepository.findByUser_Id(userId)
                    .orElseGet(() -> {
                        UserProgress newProgress = UserProgress.builder()
                                .user(user)
                                .score(0)
                                .build();
                        return userProgressRepository.save(newProgress);
                    });


            progress.setScore(progress.getScore() + mission.getPoints());
            userProgressRepository.save(progress);
        }


        return getFullProgressByUser(userId);
    }

}
