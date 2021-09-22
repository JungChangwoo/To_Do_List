package org.myapp.mobileprogramming_todolist;

import java.util.Comparator;

public class StartTimeSorter implements Comparator<MainData> {

    @Override
    public int compare(MainData o1, MainData o2) {
        return o1.getStartTime().compareToIgnoreCase(o2.getStartTime());
    }
}
