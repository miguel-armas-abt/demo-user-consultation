package com.demo.service.entrypoint.user.rest;

import com.demo.commons.restserver.utils.RestServerUtils;
import com.demo.commons.validations.headers.DefaultHeaders;
import com.demo.commons.validations.ParamValidator;

import com.demo.service.entrypoint.user.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserHandler {

  private final ParamValidator paramValidator;
  private final UserService userService;

  public Mono<ServerResponse> findUserById(ServerRequest serverRequest) {
    long userId = Long.parseLong(serverRequest.pathVariable("userId"));

    return paramValidator.validateHeadersAndGet(serverRequest, DefaultHeaders.class)
        .flatMap(tuple -> userService.findUserById(tuple.getValue(), userId))
        .flatMap(response -> ServerResponse.ok()
            .headers(httpHeaders -> RestServerUtils.buildResponseHeaders(serverRequest.headers()).accept(httpHeaders))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(response)));
  }
}
