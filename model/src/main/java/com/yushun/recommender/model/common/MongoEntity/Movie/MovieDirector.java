package com.yushun.recommender.model.common.MongoEntity.Movie;

/**
 * <p>
 *  Movie Director Model
 * </p>
 *
 * @author yushun zeng
 * @since 2023-1-22
 */

public class MovieDirector {
    private Integer directorId;

    private String directorName;

    private String directorLink;

    public Integer getDirectorId() {
        return directorId;
    }

    public void setDirectorId(Integer directorId) {
        this.directorId = directorId;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public String getDirectorLink() {
        return directorLink;
    }

    public void setDirectorLink(String directorLink) {
        this.directorLink = directorLink;
    }
}
