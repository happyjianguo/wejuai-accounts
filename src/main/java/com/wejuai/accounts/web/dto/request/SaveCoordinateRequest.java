package com.wejuai.accounts.web.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author ZM.Wang
 */
public class SaveCoordinateRequest {

    @NotBlank
    @Pattern(regexp = "^-?[1-9]\\d*(.[0-9]{1,2})?$", message = "只能有两位小数")
    private String x;

    @NotBlank
    @Pattern(regexp = "^-?[1-9]\\d*(.[0-9]{1,2})?$", message = "只能有两位小数")
    private String y;

    private String name;

    public SaveCoordinateRequest() {
    }

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    public String getName() {
        return name;
    }
}
