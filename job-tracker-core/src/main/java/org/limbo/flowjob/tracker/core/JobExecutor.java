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

package org.limbo.flowjob.tracker.core;

import org.limbo.flowjob.tracker.core.exceptions.JobExecuteException;

import java.util.Optional;

/**
 * 作业执行器抽象。封装了任务的触发方式：
 * 1. delay
 * 2. fixed interval
 * 3. fixed rate
 * 4. CORN expression
 * 5. DAG schedule
 *
 * @author Brozen
 * @since 2021-05-14
 */
public interface JobExecutor {

    /**
     * 执行作业。
     * @param job 待执行的作业
     * @return 作业执行上下文，当任务不应当被触发时，{@link Optional}中的值为null。
     * @throws JobExecuteException 当作业执行过程中发生异常时，将抛出异常。
     */
    Optional<JobContext> execute(Job job) throws JobExecuteException;

}
