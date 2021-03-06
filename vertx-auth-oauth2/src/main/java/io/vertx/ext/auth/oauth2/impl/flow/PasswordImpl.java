/*
 * Copyright 2015 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */
package io.vertx.ext.auth.oauth2.impl.flow;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.oauth2.AccessToken;
import io.vertx.ext.auth.oauth2.impl.AccessTokenImpl;

import static io.vertx.ext.auth.oauth2.impl.OAuth2API.*;

/**
 * @author Paulo Lopes
 */
public class PasswordImpl implements OAuth2Flow {

  private final Vertx vertx;
  private final JsonObject config;

  public PasswordImpl(Vertx vertx, JsonObject config) {
    this.vertx = vertx;
    this.config = config;
  }

  @Override
  public String authorizeURL(JsonObject params) {
    return null;
  }

  /**
   * Returns the Access Token object.
   *
   * @param params - username: A string that represents the registered username.
   *                 password: A string that represents the registered password.
   *                 scope:    A String that represents the application privileges.
   * @param handler - The handler function returning the results.
   */
  @Override
  public void getToken(JsonObject params, Handler<AsyncResult<AccessToken>> handler) {
    params.put("grant_type", "password");
    api(vertx, config, HttpMethod.POST, config.getString("tokenPath"), params, res -> {
      if (res.succeeded()) {
        handler.handle(Future.succeededFuture(new AccessTokenImpl(vertx, config, res.result())));
      } else {
        handler.handle(Future.failedFuture(res.cause()));
      }
    });
  }
}
