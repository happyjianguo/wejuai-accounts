package com.wejuai.accounts.web.dto.response;

/**
 * @author ZM.Wang
 * 是否收藏与点赞
 */

public class StarAndCollection {

    private final boolean star;
    private final boolean collection;

    public StarAndCollection(boolean star, boolean collection) {
        this.star = star;
        this.collection = collection;
    }

    public boolean getStar() {
        return star;
    }

    public boolean getCollection() {
        return collection;
    }
}
