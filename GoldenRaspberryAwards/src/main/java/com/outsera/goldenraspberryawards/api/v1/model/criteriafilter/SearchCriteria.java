package com.outsera.goldenraspberryawards.api.v1.model.criteriafilter;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public abstract class SearchCriteria {

    private List<String> search;

    public SearchCriteria parseSearch() {

        if (search == null) {
            return this;
        }

        List<String> parsedSearch = new ArrayList<>();
        for (String element : search) {
            String[] parts = element.split("\\%");
            Arrays.stream(parts)
                    .map(String::trim)
                    .forEach(parsedSearch::add);
        }
        search = parsedSearch;
        return this;
    }

}
