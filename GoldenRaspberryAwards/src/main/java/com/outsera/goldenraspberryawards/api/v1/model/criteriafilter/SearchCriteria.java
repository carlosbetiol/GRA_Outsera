package com.outsera.goldenraspberryawards.api.v1.model.criteriafilter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public abstract class SearchCriteria {

    @Schema(example = "Peter Brown", description = "Filter the words, use % delimiter to return records with both words")
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
