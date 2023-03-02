package com.yushun.recommender.algorithm;

import com.yushun.recommender.model.common.User;

/**
 * <p>
 * User Rating Item Vo
 * </p>
 *
 * @author yushun zeng
 * @since 2023-2-4
 */

public class UserRatingItemVo {
    private String userId;

    private String itemId;

    private String rate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    @Override
    public int hashCode() {
        //return id.hashCode
        return itemId != null ? itemId.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }

        if(obj != null && obj.getClass() == UserRatingItemVo.class) {
            UserRatingItemVo obj2 = (UserRatingItemVo) obj;

            return this.getUserId().equals(obj2.getUserId()) && this.getItemId().equals(obj2.getItemId());
        }

        return false;
     }
}
