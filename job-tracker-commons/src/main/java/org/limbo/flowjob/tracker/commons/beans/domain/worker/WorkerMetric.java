/*
 * Copyright 2020-2024 Limbo Team (https://github.com/limbo-world).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.limbo.flowjob.tracker.commons.beans.domain.worker;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

/**
 * worker的指标信息，值对象。
 *
 * @author Brozen
 * @since 2021-05-17
 */
@Data
@Setter(AccessLevel.NONE)
public class WorkerMetric {

    /**
     * worker节点ID
     */
    private String id;

    /**
     * worker节点上正在执行中的作业，瞬时态数据，可能并发不安全
     */
    private List<JobDescription> executingJobs;

    /**
     * worker可用的资源
     */
    private WorkerAvailableResource availableResource;

    /**
     * 指标上报时间戳
     */
    private Long timestamp;

    @JsonCreator
    public WorkerMetric(@JsonProperty("id") String id,
                        @JsonProperty("executingJobs") List<JobDescription> executingJobs,
                        @JsonProperty("availableResource") WorkerAvailableResource availableResource,
                        @JsonProperty("timestamp") Long timestamp) {
        this.id = id;
        this.executingJobs = executingJobs;
        this.availableResource = availableResource;
        this.timestamp = timestamp;
    }
}
