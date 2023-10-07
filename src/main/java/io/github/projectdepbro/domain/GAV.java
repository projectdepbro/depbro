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

package io.github.projectdepbro.domain;

public interface GAV {

    static GAV of(String group, String artifact, String version) {
        return new GAVersion(group, artifact, version);
    }

    static GAV ofId(String gavId) {
        String[] parts = gavId.split(":");
        return new GAVersion(parts[0], parts[1], parts[2]);
    }

    default String asId() {
        return group() + ":" + artifact() + ":" + version();
    }

    String group();

    String artifact();

    String version();

}
