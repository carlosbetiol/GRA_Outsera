package com.outsera.goldenraspberryawards.core.helper;

import java.util.List;
import java.util.stream.Collectors;

public class ListHelper {

    public static <T> boolean areListsEqual(List<T> listA, List<T> listB) {
        return listA.size() == listB.size() &&
                listA.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()))
                        .equals(listB.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting())));
    }

}


