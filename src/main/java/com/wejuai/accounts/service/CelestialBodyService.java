package com.wejuai.accounts.service;

import com.endofmaster.rest.exception.BadRequestException;
import com.endofmaster.rest.exception.ForbiddenException;
import com.wejuai.accounts.repository.CoordinateRepository;
import com.wejuai.accounts.web.dto.request.SaveCoordinateRequest;
import com.wejuai.accounts.web.dto.request.UpdateCoordinateNameRequest;
import com.wejuai.entity.mongo.Coordinate;
import com.wejuai.entity.mysql.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZM.Wang
 */
@Service
public class CelestialBodyService {

    private final CoordinateRepository coordinateRepository;

    public CelestialBodyService(CoordinateRepository coordinateRepository) {
        this.coordinateRepository = coordinateRepository;
    }

    public void addCoordinate(User user, SaveCoordinateRequest request) {
        long count = coordinateRepository.countByUserId(user.getId());
        if (count >= 10) {
            throw new BadRequestException("最多只能收藏十个坐标");
        }
        coordinateRepository.save(new Coordinate(user.getId(),
                Double.parseDouble(request.getX()),
                Double.parseDouble(request.getY()),
                StringUtils.isBlank(request.getName()) ? RandomStringUtils.randomAlphanumeric(12) : request.getName()));
    }

    public void updateCoordinateName(User user, UpdateCoordinateNameRequest request) {
        Coordinate coordinate = coordinateRepository.findById(request.getId()).orElseThrow(() -> new BadRequestException("没有该收藏记录: " + request.getId()));
        if (!StringUtils.equals(coordinate.getUserId(), user.getId())) {
            throw new ForbiddenException("该坐标不属于你");
        }
        coordinateRepository.save(coordinate.setName(request.getName()));
    }

    public List<Coordinate> getCoordinates(String userId) {
        return coordinateRepository.findByUserId(userId);
    }
}
