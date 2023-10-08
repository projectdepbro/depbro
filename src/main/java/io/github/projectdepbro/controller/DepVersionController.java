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

import io.github.projectdepbro.payload.DepVersionResponse;
import io.github.projectdepbro.service.DepVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups/{group}/artifacts/{artifact}/versions")
public class DepVersionController {

    private final DepVersionService service;

    @GetMapping
    public ResponseEntity<Page<DepVersionResponse>> getAll(
            @PathVariable String group,
            @PathVariable String artifact,
            @PageableDefault Pageable pageable
    ) {
        return service.findPage(group, artifact, pageable)
                .map(page -> page.map(DepVersionResponse::of))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{version}")
    public ResponseEntity<DepVersionResponse> getOne(
            @PathVariable String group,
            @PathVariable String artifact,
            @PathVariable String version
    ) {
        return service.findOne(group, artifact, version)
                .map(DepVersionResponse::of)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
