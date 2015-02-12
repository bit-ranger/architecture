package org.sllx.site.blog.controller;

import org.sllx.core.Assert;
import org.sllx.site.blog.entity.Article;
import org.sllx.site.core.Relation;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.stereotype.Component;

@Component("blogAssembler")
public class BlogAssembler implements ResourceAssembler<Article,Resource<Article>> {

    @javax.annotation.Resource(name = "linkBuilderFactory")
    private ControllerLinkBuilderFactory linkBuilderFactory;

    @Override
    public Resource toResource(Article blog) {
        Assert.notNull(blog);
        ControllerLinkBuilder clb = linkBuilderFactory.linkTo(BlogController.class);
        Link getLink = clb.slash(blog.getArticleid()).withRel(Relation.GET.getName());
        Link listLink = clb.withRel(Relation.LIST.getName());
        Link postLink = clb.withRel(Relation.POST.getName());
        Link deleteLink = clb.slash(blog.getArticleid()).withRel(Relation.DELETE.getName());
        Link putLink = clb.slash(blog.getArticleid()).withRel(Relation.PUT.getName());
        return new Resource(getLink,listLink,postLink,deleteLink,putLink);
    }
}
