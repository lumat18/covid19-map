package com.maps;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DataRepository {
    List<Point> pointList;

    public DataRepository() {
        this.pointList = new ArrayList<>();
    }

    public void addPoint(Point point){
        this.pointList.add(point);
    }

    public List<Point> getPointList() {
        return pointList;
    }
}
