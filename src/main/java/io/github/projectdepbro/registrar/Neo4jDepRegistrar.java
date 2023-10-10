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

package io.github.projectdepbro.registrar;

import io.github.projectdepbro.domain.DepRegistration;
import io.github.projectdepbro.domain.DepVersion;
import io.github.projectdepbro.node.DepArtifactNode;
import io.github.projectdepbro.node.DepGroupNode;
import io.github.projectdepbro.node.DepVersionNode;
import io.github.projectdepbro.repository.DepVersionNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Neo4jDepRegistrar implements DepRegistrar {

    private final DepVersionNodeRepository repository;

    @Override
    public void register(DepRegistration registration) {
        DepVersionNode node = mapDomainToNodeBuilder(registration.project())
                .dependencies(registration.dependencies().stream()
                        .map(domain -> mapDomainToNodeBuilder(domain).build())
                        .collect(Collectors.toSet()))
                .build();
        repository.save(node);
    }

    private DepVersionNode.DepVersionNodeBuilder mapDomainToNodeBuilder(DepVersion domain) {
        return DepVersionNode.builder()
                .name(domain.version())
                .versionId(domain.group() + ":" + domain.artifact() + ":" + domain.version())
                .artifact(DepArtifactNode.builder()
                        .artifactId(domain.group() + ":" + domain.artifact())
                        .name(domain.artifact())
                        .group(DepGroupNode.builder()
                                .groupId(domain.group())
                                .name(domain.group())
                                .build())
                        .build());
    }

}
