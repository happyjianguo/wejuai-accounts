package com.wejuai.accounts.web.dto.response;

import com.wejuai.entity.mysql.Address;

/**
 * @author ZM.Wang
 */
public class AddressInfo {

    private String id;
    private String phone;
    private String province;
    private String city;
    private String region;

    private String detailed;

    public AddressInfo(Address address) {
        this.id = address.getId();
        this.phone = address.getPhone();
        this.province = address.getProvince();
        this.city = address.getCity();
        this.region = address.getRegion();
        this.detailed = address.getDetailed();
    }

    public String getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getRegion() {
        return region;
    }

    public String getDetailed() {
        return detailed;
    }
}
