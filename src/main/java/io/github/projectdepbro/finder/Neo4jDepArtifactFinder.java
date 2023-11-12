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

package io.github.projectdepbro.finder;

import io.github.projectdepbro.domain.DepArtifact;
import io.github.projectdepbro.node.DepArtifactNode;
import io.github.projectdepbro.node.DepGroupNode;
import io.github.projectdepbro.repository.DepArtifactNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Neo4jDepArtifactFinder implements DepArtifactFinder {

    private final DepArtifactNodeRepository repository;

    @Override
    public boolean existsByComposeId(String group, String artifact) {
        String id = asComposeId(group, artifact);
        return repository.existsById(id);
    }

    @Override
    public Optional<DepArtifact> findByComposeId(String group, String artifact) {
        String id = asComposeId(group, artifact);
        return repository.findById(id)
                .map(this::mapNodeToDomain);
    }

    @Override
    public Page<DepArtifact> findPageByParentComposeId(String group, Pageable pageable) {
        return repository.findAllByGroup(group, pageable)
                .map(this::mapNodeToDomain);
    }

    @Override
    public Page<DepArtifact> findByQuery(String query, Pageable pageable) {
        String preparedQuery = query.trim().toLowerCase();
        return repository.findAllByQuery(preparedQuery, pageable)
                .map(this::mapNodeToDomain);
    }

    private String asComposeId(String group, String artifact) {
        return group + ":" + artifact;
    }

    private DepArtifact mapNodeToDomain(DepArtifactNode artifact) {
        DepGroupNode group = artifact.getGroup();
        return new DepArtifact(group.getName(), artifact.getName());
    }

}
