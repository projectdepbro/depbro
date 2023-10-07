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

import io.github.projectdepbro.domain.GA;
import io.github.projectdepbro.domain.GAV;
import io.github.projectdepbro.node.GAVNode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Neo4jGAFinder implements GAFinder {

    private final Neo4jTemplate neo4jTemplate;

    @Override
    public Optional<GA> findGAById(String gaId) {
        // language=cypher
        String query = """
                MATCH (d:GAV)
                WHERE d.gavId STARTS WITH $idPrefix
                RETURN d
                LIMIT 1
                """;
        String idPrefix = gaId + ":";
        return neo4jTemplate.findOne(query, Map.of("idPrefix", idPrefix), GAVNode.class)
                .map(node -> GA.ofId(node.getGavId()));
    }

    @Override
    public Set<GAV> findGAVsById(String gaId) {
        // language=cypher
        String query = """
                MATCH (d:GAV)
                WHERE d.gavId STARTS WITH $idPrefix
                RETURN d
                """;
        String idPrefix = gaId + ":";
        List<GAVNode> nodes = neo4jTemplate.findAll(query, Map.of("idPrefix", idPrefix), GAVNode.class);
        return nodes.stream()
                .map(node -> GAV.ofId(node.getGavId()))
                .collect(Collectors.toSet());
    }

}
