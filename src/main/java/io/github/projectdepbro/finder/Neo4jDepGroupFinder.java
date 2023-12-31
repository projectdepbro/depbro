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

import io.github.projectdepbro.domain.DepGroup;
import io.github.projectdepbro.node.DepGroupNode;
import io.github.projectdepbro.repository.DepGroupNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Neo4jDepGroupFinder implements DepGroupFinder {

    private final DepGroupNodeRepository repository;

    @Override
    public boolean existsById(String groupId) {
        return repository.existsById(groupId);
    }

    @Override
    public Optional<DepGroup> findById(String groupId) {
        return repository.findById(groupId)
                .map(this::mapNodeToDomain);
    }

    @Override
    public Page<DepGroup> findPage(Pageable pageable) {
        return repository.findAll(pageable)
                .map(this::mapNodeToDomain);
    }

    private DepGroup mapNodeToDomain(DepGroupNode node) {
        return new DepGroup(node.getName());
    }

}
