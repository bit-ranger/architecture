package org.sllx.site.core.base;

import org.springframework.hateoas.LinkBuilder;
import org.springframework.web.bind.annotation.ModelAttribute;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Created by sllx on 14-11-4.
 */
public abstract class BaseController extends org.sllx.mvc.Controller {

    protected LinkBuilder selfLinkBuilder = linkTo(this.getClass());

    @ModelAttribute("selfURL")
    protected String selfURL(){
        return selfLinkBuilder.withSelfRel().getHref();
    }

    protected String selfURL(Object slash){
        return selfLinkBuilder.slash(slash).withSelfRel().getHref();
    }

    protected LinkBuilder linkBuilder(Class<?> clazz){
        return linkTo(clazz);
    }

    protected String url(Class<?> clazz){
        return linkBuilder(clazz).withSelfRel().getHref();
    }

    protected String url(Class<?> clazz,Object slash){
        return linkBuilder(clazz).slash(slash).withSelfRel().getHref();
    }
}
