package com.yushun.recommender.model.common.mongoEntity.movie;

/**
 * <p>
 * Movie Actor Model
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-22
 */

public class MovieActor {
    private Integer actorId;

    private String actorName;

    private String actorLink;

    public Integer getActorId() {
        return actorId;
    }

    public void setActorId(Integer actorId) {
        this.actorId = actorId;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getActorLink() {
        return actorLink;
    }

    public void setActorLink(String actorLink) {
        this.actorLink = actorLink;
    }
}
