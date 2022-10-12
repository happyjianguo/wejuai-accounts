package com.wejuai.accounts.web;

import com.wejuai.accounts.service.CelestialBodyService;
import com.wejuai.accounts.web.dto.request.SaveCoordinateRequest;
import com.wejuai.accounts.web.dto.request.UpdateCoordinateNameRequest;
import com.wejuai.entity.mongo.Coordinate;
import com.wejuai.entity.mysql.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.validation.Valid;
import java.util.List;

import static com.wejuai.accounts.config.SecurityConfig.SESSION_LOGIN;

/**
 * @author ZM.Wang
 */
@Api(tags = "星球管理")
@RestController
@RequestMapping("/api/celestialBody")
public class CelestialBodyController {

    private final CelestialBodyService celestialBodyService;

    public CelestialBodyController(CelestialBodyService celestialBodyService) {
        this.celestialBodyService = celestialBodyService;
    }

    @ApiOperation("收藏坐标")
    @PostMapping("/coordinate")
    public void addCoordinate(@SessionAttribute(SESSION_LOGIN) User user, @RequestBody @Valid SaveCoordinateRequest request) {
        celestialBodyService.addCoordinate(user, request);
    }

    @ApiOperation("修改坐标名称")
    @PutMapping("/coordinate/name")
    public void updateCoordinate(@SessionAttribute(SESSION_LOGIN) User user, @RequestBody @Valid UpdateCoordinateNameRequest request) {
        celestialBodyService.updateCoordinateName(user, request);
    }

    @ApiOperation("用户收藏坐标列表")
    @GetMapping("/coordinate")
    public List<Coordinate> getCoordinates(@SessionAttribute(SESSION_LOGIN) String userId) {
        return celestialBodyService.getCoordinates(userId);
    }
}
