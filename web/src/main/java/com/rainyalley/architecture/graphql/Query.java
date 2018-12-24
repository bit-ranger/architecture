package com.rainyalley.architecture.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.rainyalley.architecture.model.Author;
import com.rainyalley.architecture.model.Book;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
public class Query implements GraphQLQueryResolver {

    public Author findOneAuthor(Long id) {
        Author a = new Author("aaa", "bbb");
        a.setId(id);
        return a;
    }

    public List<Author> findAllAuthors() {
        return Arrays.asList(findOneAuthor(998L));
    }

    public Long countAuthors() {
        return 1L;
    }

    public List<Book> findAllBooks() {
        return Arrays.asList(
                new Book("book1", "aaa", 888, 998),
                new Book("book2", "bbb", 999, 998));
    }

    public Long countBooks() {
        return 2L;
    }
}
