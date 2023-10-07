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
import io.github.projectdepbro.controller.response.GAVResponse;
import io.github.projectdepbro.domain.GA;
import io.github.projectdepbro.domain.GAV;
import io.github.projectdepbro.finder.GAFinder;
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
@RequestMapping("/api/gavs/{group}/{artifact}")
public class GAController {

    private final GAFinder finder;

    @GetMapping
    public ResponseEntity<GAResponse> getOne(
            @PathVariable String group,
            @PathVariable String artifact
    ) {
        GA ga = getGA(group, artifact);
        GAResponse response = GAResponse.of(ga);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/versions")
    public ResponseEntity<Set<GAVResponse>> getGAVs(
            @PathVariable String group,
            @PathVariable String artifact
    ) {
        GA ga = getGA(group, artifact);
        Set<GAV> gavs = finder.findGAVs(ga);
        Set<GAVResponse> responses = gavs.stream()
                .map(GAVResponse::of)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(responses);
    }

    private GA getGA(String group, String artifact) {
        return finder.findGA(group, artifact)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "GA not found by group='" + group +
                        "' and artifact='" + artifact +
                        "'"));
    }

}
