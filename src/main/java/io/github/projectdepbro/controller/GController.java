/*
 * Copyright 2023 The Project DepBro Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.projectdepbro.controller;

import io.github.projectdepbro.controller.response.GAResponse;
import io.github.projectdepbro.controller.response.GResponse;
import io.github.projectdepbro.domain.G;
import io.github.projectdepbro.domain.GA;
import io.github.projectdepbro.finder.GFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gavs/{group}")
public class GController {

    private final GFinder finder;

    @GetMapping
    public ResponseEntity<GResponse> getOne(
            @PathVariable String group
    ) {
        G g = getG(group);
        GResponse response = GResponse.of(g);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/artifacts")
    public ResponseEntity<Set<GAResponse>> getGAs(
            @PathVariable String group
    ) {
        G g = getG(group);
        Set<GA> gas = finder.findGAs(g);
        Set<GAResponse> responses = gas.stream()
                .map(GAResponse::of)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(responses);
    }

    private G getG(String group) {
        return finder.findG(group)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "G not found by group='" + group +
                        "'"));
    }

}
