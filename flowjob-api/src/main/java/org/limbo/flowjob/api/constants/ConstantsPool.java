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

package org.limbo.flowjob.api.constants;

import java.time.LocalDateTime;

/**
 * @author Devil
 * @since 2022/12/3
 */
public interface ConstantsPool {

    int UNKNOWN = 0;

    // =========== plan status ==================
    int PLAN_SCHEDULING = 10;
    int PLAN_DISPATCHING = 20;
    int PLAN_EXECUTING = 30;
    int PLAN_EXECUTE_SUCCEED = 40;
    int PLAN_EXECUTE_FAILED = 50;
    // =========== plan status ==================

    // =========== job status ==================
    int JOB_SCHEDULING = 10;
//    int JOB_DISPATCHING = 20;
    int JOB_EXECUTING = 30;
    int JOB_EXECUTE_SUCCEED = 40;
    int JOB_EXECUTE_FAILED = 50;
    // =========== job status ==================


    // =========== task status ==================
    int TASK_SCHEDULING = 10;
    int TASK_DISPATCHING = 20;
    int TASK_EXECUTING = 30;
    int TASK_EXECUTE_SUCCEED = 40;
    int TASK_EXECUTE_FAILED = 50;
    // =========== task status ==================

}
