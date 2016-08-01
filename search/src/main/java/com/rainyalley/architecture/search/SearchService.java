package com.rainyalley.architecture.search;

import java.util.Set;

public interface SearchService {

    void index(String id, String text);

    Set<String> search(String keyword);
}
