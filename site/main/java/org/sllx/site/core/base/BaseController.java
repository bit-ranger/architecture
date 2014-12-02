package org.sllx.site.core.base;

import org.springframework.hateoas.LinkBuilder;
import org.springframework.web.bind.annotation.ModelAttribute;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Created by sllx on 14-11-4.
 */
public abstract class BaseController extends org.sllx.mvc.Controller {

    protected LinkBuilder selfLinkBuilder = linkTo(this.getClass());
    public String getSelfHref(){
        return selfLinkBuilder.withSelfRel().getHref();
    }

}
