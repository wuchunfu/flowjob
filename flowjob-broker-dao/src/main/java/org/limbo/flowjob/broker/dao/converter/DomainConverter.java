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

package org.limbo.flowjob.broker.dao.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.limbo.flowjob.api.constants.AgentStatus;
import org.limbo.flowjob.api.constants.PlanType;
import org.limbo.flowjob.api.constants.ScheduleType;
import org.limbo.flowjob.api.constants.TriggerType;
import org.limbo.flowjob.broker.core.agent.ScheduleAgent;
import org.limbo.flowjob.broker.core.domain.job.WorkflowJobInfo;
import org.limbo.flowjob.broker.core.domain.plan.NormalPlan;
import org.limbo.flowjob.broker.core.domain.plan.Plan;
import org.limbo.flowjob.broker.core.domain.plan.WorkflowPlan;
import org.limbo.flowjob.broker.core.schedule.ScheduleOption;
import org.limbo.flowjob.broker.dao.entity.AgentEntity;
import org.limbo.flowjob.broker.dao.entity.JobInstanceEntity;
import org.limbo.flowjob.broker.dao.entity.PlanEntity;
import org.limbo.flowjob.broker.dao.entity.PlanInfoEntity;
import org.limbo.flowjob.broker.core.domain.job.JobInfo;
import org.limbo.flowjob.broker.core.domain.job.JobInstance;
import org.limbo.flowjob.common.utils.dag.DAG;
import org.limbo.flowjob.common.utils.json.JacksonUtils;
import org.limbo.flowjob.common.utils.time.TimeUtils;

import java.net.URL;
import java.time.Duration;
import java.util.List;

/**
 * 基础信息转换 静态方法
 *
 * @author Devil
 * @since 2022/8/11
 */
@Slf4j
public class DomainConverter {

    public static Plan toPlan(PlanEntity entity, PlanInfoEntity planInfoEntity) {
        Plan plan;
        PlanType planType = PlanType.parse(planInfoEntity.getPlanType());
        if (PlanType.STANDALONE == planType) {
            plan = new NormalPlan(
                    planInfoEntity.getPlanId(),
                    planInfoEntity.getPlanInfoId(),
                    TriggerType.parse(planInfoEntity.getTriggerType()),
                    toScheduleOption(planInfoEntity),
                    JacksonUtils.parseObject(planInfoEntity.getJobInfo(), JobInfo.class)
            );
        } else if (PlanType.WORKFLOW == planType) {
            plan = new WorkflowPlan(
                    planInfoEntity.getPlanId(),
                    planInfoEntity.getPlanInfoId(),
                    TriggerType.parse(planInfoEntity.getTriggerType()),
                    toScheduleOption(planInfoEntity),
                    toJobDag(planInfoEntity.getJobInfo())
            );
        } else {
            throw new IllegalArgumentException("Illegal PlanType in plan:" + entity.getPlanId() + " version:" + entity.getCurrentVersion());
        }
        return plan;
    }

    public static ScheduleOption toScheduleOption(PlanInfoEntity entity) {
        return new ScheduleOption(
                ScheduleType.parse(entity.getScheduleType()),
                entity.getScheduleStartAt(),
                entity.getScheduleEndAt(),
                Duration.ofMillis(entity.getScheduleDelay()),
                Duration.ofMillis(entity.getScheduleInterval()),
                entity.getScheduleCron(),
                entity.getScheduleCronType()
        );
    }

    /**
     * @param dag 节点关系
     * @return job dag
     */
    public static DAG<WorkflowJobInfo> toJobDag(String dag) {
        List<WorkflowJobInfo> jobInfos = JacksonUtils.parseObject(dag, new TypeReference<List<WorkflowJobInfo>>() {
        });
        return new DAG<>(jobInfos);
    }

    public static JobInstanceEntity toJobInstanceEntity(JobInstance jobInstance) {
        JobInfo jobInfo = jobInstance.getJobInfo();
        JobInstanceEntity entity = new JobInstanceEntity();
        entity.setJobId(jobInfo.getId());
        entity.setJobInstanceId(jobInstance.getJobInstanceId());
        entity.setAgentId(jobInstance.getAgentId());
        entity.setRetryTimes(jobInstance.getRetryTimes());
        entity.setPlanInstanceId(jobInstance.getPlanInstanceId());
        entity.setPlanId(jobInstance.getPlanId());
        entity.setPlanInfoId(jobInstance.getPlanVersion());
        entity.setStatus(jobInstance.getStatus().status);
        entity.setTriggerAt(jobInstance.getTriggerAt());
        entity.setStartAt(jobInstance.getStartAt());
        entity.setEndAt(jobInstance.getEndAt());
        entity.setLastReportAt(TimeUtils.currentLocalDateTime());
        return entity;
    }

    public static ScheduleAgent toAgent(AgentEntity entity) {
        return ScheduleAgent.builder()
                .id(entity.getAgentId())
                .status(AgentStatus.parse(entity.getStatus()))
                .rpcBaseUrl(url(entity))
                .build();
    }

    public static URL url(AgentEntity entity) {
        try {
            return new URL(entity.getProtocol(), entity.getHost(), entity.getPort(), "");
        } catch (Exception e) {
            throw new IllegalStateException("parse agent rpc info error", e);
        }
    }

}
