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

package io.github.projectdepbro.repository;

import io.github.projectdepbro.node.DepArtifactNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface DepArtifactNodeRepository extends Neo4jRepository<DepArtifactNode, String> {

    // language=cypher
    @Query(
            value = """
                    MATCH (a:DepArtifact)-[ag:INCLUDED_IN]->(g:DepGroup)
                    WHERE g.name = $group
                    RETURN a, ag, g
                    ORDER BY g.name ASC, a.name ASC
                    SKIP $skip
                    LIMIT $limit
                    """,
            countQuery = """
                    MATCH (a:DepArtifact)-[ag:INCLUDED_IN]->(g:DepGroup)
                    WHERE g.name = $group
                    RETURN count(a)
                    """
    )
    Page<DepArtifactNode> findAllByGroup(String group, Pageable pageable);

}
