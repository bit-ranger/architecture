package org.sllx.site.core.base;

import org.springframework.hateoas.LinkBuilder;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Created by sllx on 14-11-4.
 */
public abstract class BaseController extends org.sllx.mvc.Controller {

    protected LinkBuilder selfLinkBuilder = linkTo(this.getClass());

    protected String selfURL(){
        return selfLinkBuilder.withSelfRel().getHref();
    }

    protected String selfURL(Object slash, String rel){
        return selfLinkBuilder.slash(slash).withRel(rel).getHref();
    }

    protected LinkBuilder linkBuilder(Class<?> clazz){
        return linkTo(clazz);
    }

    protected String url(Class<?> clazz,String rel){
        return linkBuilder(clazz).withRel(rel).getHref();
    }

    protected String url(Class<?> clazz,Object slash, String rel){
        return linkBuilder(clazz).slash(slash).withRel(rel).getHref();
    }
}
