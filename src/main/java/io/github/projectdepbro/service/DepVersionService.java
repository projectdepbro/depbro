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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

public interface DepVersionService {

    void register(String group, String artifact, String version, Set<String> dependencyIds);

    Optional<DepVersion> findOne(String group, String artifact, String version);

    Optional<Page<DepVersion>> findPage(String group, String artifact, Pageable pageable);

    Optional<Set<DepVersion>> findDependencies(String group, String artifact, String version);

    Optional<Set<DepVersion>> findUsages(String group, String artifact, String version);

}
