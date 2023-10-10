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

import io.github.projectdepbro.domain.DepVersion;
import io.github.projectdepbro.node.DepArtifactNode;
import io.github.projectdepbro.node.DepGroupNode;
import io.github.projectdepbro.node.DepVersionNode;
import io.github.projectdepbro.repository.DepVersionNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Neo4jDepVersionFinder implements DepVersionFinder {

    private final DepVersionNodeRepository repository;

    @Override
    public Optional<DepVersion> findByComposeId(String group, String artifact, String version) {
        String id = asComposeId(group, artifact, version);
        return repository.findById(id)
                .map(this::mapNodeToDomain);
    }

    @Override
    public Page<DepVersion> findPageByParentComposeId(String group, String artifact, Pageable pageable) {
        return repository.findAllByGroupAndArtifact(group, artifact, pageable)
                .map(this::mapNodeToDomain);
    }

    private String asComposeId(String groupId, String artifactId, String versionId) {
        return groupId + ":" + artifactId + ":" + versionId;
    }

    private DepVersion mapNodeToDomain(DepVersionNode version) {
        DepArtifactNode artifact = version.getArtifact();
        DepGroupNode group = artifact.getGroup();
        return new DepVersion(group.getName(), artifact.getName(), version.getName());
    }

}
