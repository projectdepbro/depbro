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

import io.github.projectdepbro.domain.DepVersion;
import io.github.projectdepbro.finder.DepArtifactFinder;
import io.github.projectdepbro.finder.DepVersionFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SimpleDepVersionService implements DepVersionService {

    private final DepArtifactFinder artifactFinder;
    private final DepVersionFinder versionFinder;

    @Override
    public Optional<DepVersion> findOne(String group, String artifact, String version) {
        return versionFinder.findByComposeId(group, artifact, version);
    }

    @Override
    public Optional<Page<DepVersion>> findPage(String group, String artifact, Pageable pageable) {
        if (artifactFinder.existsByComposeId(group, artifact)) {
            Page<DepVersion> page = versionFinder.findPageByParentComposeId(group, artifact, pageable);
            return Optional.of(page);
        }
        return Optional.empty();
    }

}
