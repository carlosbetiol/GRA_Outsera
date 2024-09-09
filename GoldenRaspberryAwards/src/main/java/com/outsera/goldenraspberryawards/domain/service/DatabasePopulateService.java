package com.outsera.goldenraspberryawards.domain.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DatabasePopulateService {

    void populateEntities(List<Map<String, Set<Object>>> data);

}
