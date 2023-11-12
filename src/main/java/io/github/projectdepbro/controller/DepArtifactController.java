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

import io.github.projectdepbro.domain.DepArtifact;
import io.github.projectdepbro.payload.DepArtifactResponse;
import io.github.projectdepbro.service.DepArtifactService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class DepArtifactController {

    private final DepArtifactService service;

    @GetMapping("/{group}/artifacts")
    public ResponseEntity<Page<String>> getAll(
            @PathVariable String group,
            @PageableDefault Pageable pageable
    ) {
        return service.findPage(group, pageable)
                .map(page -> page.map(DepArtifact::artifact))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{group}/artifacts/{artifact}")
    public ResponseEntity<DepArtifactResponse> getOne(
            @PathVariable String group,
            @PathVariable String artifact
    ) {
        return service.findOne(group, artifact)
                .map(DepArtifactResponse::of)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/artifacts")
    public ResponseEntity<Page<DepArtifactResponse>> search(
            @RequestParam @NotBlank(message = "Query must not be blank") String query,
            @PageableDefault Pageable pageable
    ) {
        Page<DepArtifactResponse> page = service.search(query, pageable)
                .map(DepArtifactResponse::of);
        return ResponseEntity.ok(page);
    }

}
