package server.utility;

import common.data.Flat;

import java.util.Comparator;

public class SortByHouse implements Comparator<Flat> {
    public int compare(Flat first, Flat second) {
        if (first.getHouse() == null) {
            return -1;
        }
        if (second.getHouse() == null) {
            return 1;
        }
        return first.getHouse().compareTo(second.getHouse());
    }
}
