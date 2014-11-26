package org.sllx.site.core.common;

import org.springframework.hateoas.LinkBuilder;
import org.springframework.web.bind.annotation.ModelAttribute;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Created by sllx on 14-11-4.
 */
public abstract class Controller extends org.sllx.mvc.Controller {

    protected LinkBuilder selfLinkBuilder = linkTo(this.getClass());
    @ModelAttribute("selfHref")
    public String getHref(){
        return selfLinkBuilder.withSelfRel().getHref();
    }

}
