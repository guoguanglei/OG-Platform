/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.web.server.push.rest;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.opengamma.web.server.push.analytics.DependencyGraphRequest;

/**
 * TODO this is an awful name
 */
// TODO will @Path("depgraphs") work here?
public interface DependencyGraphOwnerResource {

  @POST
  @Path("depgraphs")
  public abstract Response openDependencyGraph(@Context UriInfo uriInfo, DependencyGraphRequest request);

  @Path("depgraphs/{graphId}")
  public AbstractGridResource getDependencyGraph(@PathParam("graphId") String graphId);

  // TODO this won't work. need to return a DependencyGraphGridResource from getDependencyGraph and have a @DELETE method on that
  @DELETE
  @Path("depgraphs/{graphId}")
  public void closeDependencyGraph(@PathParam("graphId") String graphId);
}
