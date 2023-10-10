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

package io.github.projectdepbro.service;

import io.github.projectdepbro.domain.DepArtifact;
import io.github.projectdepbro.finder.DepArtifactFinder;
import io.github.projectdepbro.finder.DepGroupFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SimpleDepArtifactService implements DepArtifactService {

    private final DepGroupFinder groupFinder;
    private final DepArtifactFinder artifactFinder;

    @Override
    public Optional<DepArtifact> findOne(String group, String artifact) {
        return artifactFinder.findByComposeId(group, artifact);
    }

    @Override
    public Optional<Page<DepArtifact>> findPage(String group, Pageable pageable) {
        if (groupFinder.existsById(group)) {
            Page<DepArtifact> page = artifactFinder.findPageByParentComposeId(group, pageable);
            return Optional.of(page);
        }
        return Optional.empty();
    }

}
