package org.danilarefy.github.camel.validator;

import org.apache.camel.CamelContext;
import org.apache.camel.component.validator.ValidatorResourceResolverFactory;
import org.w3c.dom.ls.LSResourceResolver;

public class ResourceResolverFactory implements ValidatorResourceResolverFactory {

    @Override
    public LSResourceResolver createResourceResolver(CamelContext camelContext, String rootResourceUri) {
        return new ResourceResolver(camelContext, rootResourceUri);
    }
}
