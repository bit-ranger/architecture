package com.rainyalley.architecture.graphql;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.rainyalley.architecture.model.Author;
import com.rainyalley.architecture.model.Book;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BookResolver implements GraphQLResolver<Book> {


    public Author getAuthor(Book book) {
        return new Author("bookAuthor", "aaaaaa");
    }
}
