package com.wejuai.accounts.web;

import com.wejuai.accounts.repository.CityRepository;
import com.wejuai.accounts.repository.ProvinceRepository;
import com.wejuai.accounts.repository.RegionRepository;
import com.wejuai.entity.mongo.City;
import com.wejuai.entity.mongo.Province;
import com.wejuai.entity.mongo.Region;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ZM.Wang
 */
@Api(tags = "基础接口")
@RestController
@RequestMapping("/api/base")
public class BaseController {

    private final ProvinceRepository provinceRepository;
    private final CityRepository cityRepository;
    private final RegionRepository regionRepository;

    public BaseController(ProvinceRepository provinceRepository, CityRepository cityRepository, RegionRepository regionRepository) {
        this.provinceRepository = provinceRepository;
        this.cityRepository = cityRepository;
        this.regionRepository = regionRepository;
    }

    @ApiOperation("省列表")
    @GetMapping("/province")
    public List<Province> getProvinces() {
        return provinceRepository.findAll();
    }

    @ApiOperation("市列表")
    @GetMapping("/city/{pid}")
    public List<City> getCities(@PathVariable String pid) {
        return cityRepository.findByPid(pid);
    }

    @ApiOperation("区列表")
    @GetMapping("/region/{pid}")
    public List<Region> getRegion(@PathVariable String pid) {
        return regionRepository.findByPid(pid);
    }


}
