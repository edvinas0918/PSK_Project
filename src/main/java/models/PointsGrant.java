package models;

/**
 * Created by Dziugas on 4/25/2016.
 */
public class PointsGrant {
    public Integer memberID;
    public Integer points;
    public String description;

    public PointsGrant(Integer memberID, Integer points, String description){
        this.memberID = memberID;
        this.points = points;
        this.description = description;
    }
}
