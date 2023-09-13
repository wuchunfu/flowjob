/*
 *
 *  * Copyright 2020-2024 Limbo Team (https://github.com/limbo-world).
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * 	http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.limbo.flowjob.worker.demo.executors;

import lombok.extern.slf4j.Slf4j;
import org.limbo.flowjob.worker.core.domain.Task;
import org.limbo.flowjob.worker.core.executor.TaskExecutor;
import org.springframework.stereotype.Component;

/**
 * @author Devil
 * @since 2021/7/28
 */
@Slf4j
@Component
public class HeavyExecutorDemo implements TaskExecutor {

    @Override
    public void run(Task task) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.warn("Heavy success to {}", task.getTaskId());
    }

    @Override
    public String getName() {
        return "heavy";
    }

    @Override
    public String getDescription() {
        return null;
    }

}
