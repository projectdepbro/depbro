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

import java.util.Optional;
import java.util.Set;

public interface GAFinder {

    Optional<GA> findGAById(String gaId);

    default Optional<GA> findGA(String group, String artifact) {
        GA ga = GA.of(group, artifact);
        return findGAById(ga.asId());
    }

    Set<GAV> findGAVsById(String gaId);

    default Set<GAV> findGAVs(GA ga) {
        return findGAVsById(ga.asId());
    }

}
