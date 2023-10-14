package org.service1;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Base64;

@RestController
@RequestMapping("/api/v1")
public class AppController {

    @GetMapping("/status")
    Mono<String> getStatus(@RequestParam(required = false) String status){
        String result = "OK";
        if (status!=null) {
            result=status;
        }
        return Mono.just(result);
    }

    @RequestMapping(value = "/echo/**", consumes = MediaType.ALL_VALUE, produces =
            MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.GET, RequestMethod.POST,
            RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
    public Mono<JsonPayload> echo(@RequestBody(required = false) byte[] rawBody, ServerHttpRequest request) {

        final JsonPayload response = new JsonPayload(
                request.getURI().getScheme(),
                request.getMethod().toString(),
                request.getHeaders().toString(),
                request.getCookies().toString(),
                request.getQueryParams().toString(),
                request.getPath().toString(),
                rawBody != null ? Base64.getEncoder().encodeToString(rawBody) : null

        );
        return Mono.just(response);
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    record JsonPayload (String protocol,
                        String method,
                        String headers,
                        String cookies,
                        String parameters,
                        String path,
                        String body) {

    }

}
