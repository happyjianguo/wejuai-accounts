package com.wejuai.accounts.web.dto.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author ZM.Wang
 */
public class CreateGroupBuyingRequest {

    @ApiModelProperty("如果是修改传团购id")
    private String id;
    @ApiModelProperty("所属爱好的id")
    private String hobbyId;
    @NotBlank(message = "团购标题不能为空")
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("描述")
    private String introduce;
    @Size(min = 1, max = 10, message = "介绍图片最少1张最多10张")
    @ApiModelProperty("图片列表")
    private List<String> images;
    @Min(1)
    @ApiModelProperty("金额")
    private long money;
    @Min(1)
    @ApiModelProperty("库存")
    private long inventory;

    public String getId() {
        return id;
    }

    public CreateGroupBuyingRequest setId(String id) {
        this.id = id;
        return this;
    }

    public String getHobbyId() {
        return hobbyId;
    }

    public CreateGroupBuyingRequest setHobbyId(String hobbyId) {
        this.hobbyId = hobbyId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public CreateGroupBuyingRequest setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getIntroduce() {
        return introduce;
    }

    public CreateGroupBuyingRequest setIntroduce(String introduce) {
        this.introduce = introduce;
        return this;
    }

    public List<String> getImages() {
        return images;
    }

    public CreateGroupBuyingRequest setImages(List<String> images) {
        this.images = images;
        return this;
    }

    public long getMoney() {
        return money;
    }

    public CreateGroupBuyingRequest setMoney(long money) {
        this.money = money;
        return this;
    }

    public long getInventory() {
        return inventory;
    }

    public CreateGroupBuyingRequest setInventory(long inventory) {
        this.inventory = inventory;
        return this;
    }
}
