package server.utility;

import common.data.Flat;

import java.util.Comparator;

public class SortByCoordinates implements Comparator<Flat> {
    @Override
    public int compare(Flat first, Flat second) {
        return first.getCoordinates().compareTo(second.getCoordinates());
    }

}
