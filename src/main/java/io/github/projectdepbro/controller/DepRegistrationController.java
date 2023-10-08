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

import io.github.projectdepbro.service.DepRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/registrations")
public class DepRegistrationController {

    private final DepRegistrationService service;

    @PostMapping("/{group}/{artifact}/{version}")
    public ResponseEntity<?> createOneWithDependencies(
            @PathVariable String group,
            @PathVariable String artifact,
            @PathVariable String version,
            @RequestBody Set<String> dependencyIds
    ) {
        service.register(group, artifact, version, dependencyIds);
        return ResponseEntity.ok().build();
    }

}
