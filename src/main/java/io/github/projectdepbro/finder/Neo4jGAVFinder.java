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

import io.github.projectdepbro.domain.GAV;
import io.github.projectdepbro.domain.GAVersion;
import io.github.projectdepbro.node.GAVNode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Neo4jGAVFinder implements GAVFinder {

    private final Neo4jTemplate neo4jTemplate;

    @Override
    public Set<GAV> findUsage(String gavId) {
        String query = "MATCH (:GAV{gavId:\"" + gavId + "\"})-[:USED_IN]->(u:GAV) RETURN u";
        return findGAVes(query);
    }

    @Override
    public Set<GAV> findDependencies(String gavId) {
        String query = "MATCH (:GAV{gavId:\"" + gavId + "\"})<-[:USED_IN]-(d:GAV) RETURN d";
        return findGAVes(query);
    }

    private Set<GAV> findGAVes(String query) {
        List<GAVNode> nodes = neo4jTemplate.findAll(query, GAVNode.class);
        return nodes.stream()
                .map(node -> {
                    String[] parts = node.getGavId().split(":");
                    return GAVersion.builder()
                            .group(parts[0])
                            .artifact(parts[1])
                            .version(parts[2])
                            .build();
                })
                .collect(Collectors.toSet());
    }

}
