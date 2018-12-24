package com.rainyalley.architecture.graphql;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.rainyalley.architecture.model.Author;
import com.rainyalley.architecture.model.Book;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class AuthorResolver implements GraphQLResolver<Author> {

    public Date getCreatedTime(Author author) {
        return author.getCreatedTime();
    }

    public List<Book> getBooks(Author author) {
        return Arrays.asList(
                new Book("authorBook1", "bbb", 888, author.getId()),
                new Book("authorBook2", "ddd", 999, author.getId()));
    }
}
