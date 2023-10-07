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

import io.github.projectdepbro.controller.response.GAVResponse;
import io.github.projectdepbro.domain.GAV;
import io.github.projectdepbro.finder.GAVFinder;
import io.github.projectdepbro.registrar.GAVRegistrar;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gavs/{group}/{artifact}/{version}")
public class GAVController {

    private final GAVFinder finder;
    private final GAVRegistrar registrar;

    @GetMapping
    public ResponseEntity<GAVResponse> getOne(
            @PathVariable String group,
            @PathVariable String artifact,
            @PathVariable String version
    ) {
        GAV gav = getGAV(group, artifact, version);
        GAVResponse response = GAVResponse.of(gav);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createOneWithDependencies(
            @PathVariable String group,
            @PathVariable String artifact,
            @PathVariable String version,
            @RequestBody Set<String> dependencyIds
    ) {
        GAV gav = GAV.of(group, artifact, version);
        Set<GAV> dependencies = dependencyIds.stream()
                .map(GAV::ofId)
                .collect(Collectors.toSet());
        registrar.registerWithDependencies(gav, dependencies);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/dependents")
    public ResponseEntity<Set<GAVResponse>> getDependents(
            @PathVariable String group,
            @PathVariable String artifact,
            @PathVariable String version
    ) {
        GAV gav = getGAV(group, artifact, version);
        Set<GAV> dependents = finder.findDependents(gav);
        Set<GAVResponse> responses = dependents.stream()
                .map(GAVResponse::of)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/dependencies")
    public ResponseEntity<Set<GAVResponse>> getDependencies(
            @PathVariable String group,
            @PathVariable String artifact,
            @PathVariable String version
    ) {
        GAV gav = getGAV(group, artifact, version);
        Set<GAV> dependencies = finder.findDependencies(gav);
        Set<GAVResponse> responses = dependencies.stream()
                .map(GAVResponse::of)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(responses);
    }

    private GAV getGAV(String group, String artifact, String version) {
        return finder.findGAV(group, artifact, version)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "GAV not found by group='" + group +
                        "', artifact='" + artifact +
                        "' and version='" + version +
                        "'"));
    }

}
