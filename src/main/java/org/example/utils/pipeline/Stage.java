package org.example.utils.pipeline;

@FunctionalInterface
public interface Stage {
    RequestContext process(RequestContext context);

}
