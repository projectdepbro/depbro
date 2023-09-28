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

import io.github.projectdepbro.domain.GAV;
import io.github.projectdepbro.node.GAVNode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Neo4jGAVRegistrar implements GAVRegistrar {

    private final Neo4jTemplate neo4jTemplate;

    @Override
    public void registerWithDependencies(GAV gav, Set<GAV> dependencies) {
        Set<GAVNode> depNodes = dependencies.stream()
                .map(dep -> nodeBuilder(dep.getId()).build())
                .collect(Collectors.toSet());
        GAVNode gavNode = nodeBuilder(gav.getId())
                .dependencies(depNodes)
                .build();
        neo4jTemplate.save(gavNode);
    }

    private GAVNode.GAVNodeBuilder nodeBuilder(String gavId) {
        return GAVNode.builder().gavId(gavId);
    }

}
