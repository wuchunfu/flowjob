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

package org.limbo.flowjob.api.param.console;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.limbo.flowjob.api.constants.JobType;
import org.limbo.flowjob.api.constants.TriggerType;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Devil
 * @since 2023/7/12
 */
@Data
public class PlanParam implements Serializable {
    private static final long serialVersionUID = 68689784703620782L;

    /**
     * 计划名称
     */
    @Schema(title = "任务名称")
    private String name;

    /**
     * 计划描述
     */
    @Schema(title = "任务描述")
    private String description;

    /**
     * 触发方式
     * @see TriggerType
     */
    @NotNull
    @Schema(title = "触发方式", implementation = Integer.class)
    private TriggerType triggerType;

    /**
     * 作业计划调度配置参数
     */
    @NotNull
    @Schema(title = "调度配置参数")
    private ScheduleOptionParam scheduleOption;

    @Data
    @EqualsAndHashCode(callSuper = true)
    @Schema(title = "普通任务参数")
    public static class NormalPlanParam extends PlanParam {
        private static final long serialVersionUID = -552043309934945026L;

        /**
         * 作业类型
         * @see JobType
         */
        @NotNull
        @Schema(title = "作业类型")
        private JobType type;

        /**
         * 属性参数
         */
        @Schema(title = "属性参数")
        private Map<String, Object> attributes;

        /**
         * 作业分发重试参数
         */
        @Schema(title = "作业分发重试参数")
        private RetryOptionParam retryOption;

        /**
         * 作业分发配置参数
         */
        @Valid
        @NotNull
        @Schema(title = "作业分发配置参数")
        private DispatchOptionParam dispatchOption;

        /**
         * 执行器名称
         */
        @NotBlank
        @Schema(title = "执行器名称")
        private String executorName;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @Schema(title = "工作流任务参数")
    public static class WorkflowPlanParam extends PlanParam {
        private static final long serialVersionUID = 3674766094102983427L;

        /**
         * 此执行计划对应的所有作业
         */
        @Schema(title = "工作流对应的所有作业")
        private List<@Valid WorkflowJobParam> workflow;
    }

}
