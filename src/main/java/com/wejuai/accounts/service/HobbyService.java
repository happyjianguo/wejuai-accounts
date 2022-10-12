package com.wejuai.accounts.service;

import com.endofmaster.rest.exception.BadRequestException;
import com.wejuai.accounts.repository.HobbyHotRepository;
import com.wejuai.accounts.repository.HobbyRepository;
import com.wejuai.accounts.repository.UserHobbyRepository;
import com.wejuai.dto.response.HobbyInfo;
import com.wejuai.entity.mongo.statistics.HobbyHot;
import com.wejuai.entity.mysql.Hobby;
import com.wejuai.entity.mysql.User;
import com.wejuai.entity.mysql.UserHobby;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ZM.Wang
 */
@Service
public class HobbyService {

    private final HobbyRepository hobbyRepository;
    private final HobbyHotRepository hobbyHotRepository;
    private final UserHobbyRepository userHobbyRepository;

    public HobbyService(HobbyRepository hobbyRepository, HobbyHotRepository hobbyHotRepository, UserHobbyRepository userHobbyRepository) {
        this.hobbyRepository = hobbyRepository;
        this.hobbyHotRepository = hobbyHotRepository;
        this.userHobbyRepository = userHobbyRepository;
    }

    public Hobby getHobby(String hobbyId) {
        if (StringUtils.isNotBlank(hobbyId)) {
            return hobbyRepository.findById(hobbyId).orElseThrow(() -> new BadRequestException("没有找到这个爱好哦，不要填错哦~"));
        } else {
            throw new BadRequestException("爱好是必选的哦~");
        }
    }

    public List<HobbyInfo> getUserHobbies(User user) {
        UserHobby userHobby = getUserHobby(user);
        return userHobby.getHobbies().stream().map(hobby -> {
            HobbyHot hobbyHot = hobbyHotRepository.findByHobbyId(hobby.getId());
            if (hobbyHot == null) {
                hobbyHot = hobbyHotRepository.save(new HobbyHot(hobby.getId()));
            }
            return new HobbyInfo(hobby).setFollowed(hobbyHot.getFollowed());
        }).collect(Collectors.toList());
    }

    @Transactional
    public void updateHobbyOpen(User user, String id, boolean open) {
        Hobby hobby = getHobby(id);
        UserHobby userHobby = getUserHobby(user);
        if (!userHobby.getHobbies().contains(hobby)) {
            throw new BadRequestException("尚未关注这个爱好");
        }
        if (open) {
            if (!userHobby.getOpenHobbies().contains(hobby)) {
                userHobby.addOpenHobby(hobby);
            }
        } else {
            if (userHobby.getOpenHobbies().contains(hobby)) {
                userHobby.reduceOpenHobby(hobby);
            }
        }
    }

    public void checkUserHasHobby(User user, Hobby hobby) {
        UserHobby userHobby = getUserHobby(user);
        if (!userHobby.getHobbies().contains(hobby)) {
            throw new BadRequestException("尚未关注该爱好");
        }
    }

    private UserHobby getUserHobby(User user) {
        UserHobby userHobby = userHobbyRepository.findByUser(user);
        if (userHobby == null) {
            userHobby = userHobbyRepository.save(new UserHobby(user));
        }
        return userHobby;
    }

}
