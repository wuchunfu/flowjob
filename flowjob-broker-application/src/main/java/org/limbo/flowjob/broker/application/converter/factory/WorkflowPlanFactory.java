/*
 * Copyright 2020-2024 Limbo Team (https://github.com/limbo-world).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.limbo.flowjob.broker.application.converter.factory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Setter;
import org.limbo.flowjob.api.param.console.WorkflowJobParam;
import org.limbo.flowjob.broker.core.domain.job.WorkflowJobInfo;
import org.limbo.flowjob.common.utils.attribute.Attributes;
import org.limbo.flowjob.common.utils.dag.DAG;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Brozen
 * @since 2023-08-11
 */
@Component
public class WorkflowPlanFactory {

    @Setter(onMethod_ = @Inject)
    private PlanFactory factory;


    /**
     * 生成更新计划 JobDAG
     */
    public DAG<WorkflowJobInfo> createDAG(List<WorkflowJobParam> jobParams) {
        return new DAG<>(createWorkflowJobs(jobParams));
    }


    /**
     * 生成 DAG 作业实体列表
     */
    public List<WorkflowJobInfo> createWorkflowJobs(List<WorkflowJobParam> params) {
        List<WorkflowJobInfo> jobs = Lists.newArrayList();
        Map<String, Set<String>> parents = new HashMap<>();
        Map<String, Set<String>> children = new HashMap<>();
        for (WorkflowJobParam param : params) {
            jobs.add(createWorkflowJob(param));

            Set<String> childIds = children.computeIfAbsent(param.getId(), _k -> new HashSet<>());
            for (String child : param.getChildren()) {
                childIds.add(child);
                parents.computeIfAbsent(child, _k -> new HashSet<>()).add(param.getId());
            }
        }

        // 填充父子关系
        for (WorkflowJobInfo job : jobs) {
            job.setParentIds(parents.getOrDefault(job.getId(), Sets.newHashSet()));
            job.setChildrenIds(children.getOrDefault(job.getId(), Sets.newHashSet()));
        }

        return jobs;
    }


    /**
     * 生成单个 DAG 作业
     */
    public WorkflowJobInfo createWorkflowJob(WorkflowJobParam param) {
        WorkflowJobInfo jobInfo = new WorkflowJobInfo();
        jobInfo.setId(param.getId());
        jobInfo.setName(param.getName());
        jobInfo.setDescription(param.getDescription());
        jobInfo.setTriggerType(param.getTriggerType());
        jobInfo.setContinueWhenFail(param.isContinueWhenFail());
        jobInfo.setType(param.getType());
        jobInfo.setAttributes(new Attributes(param.getAttributes()));
        jobInfo.setRetryOption(factory.createRetryOption(param.getRetryOption()));
        jobInfo.setDispatchOption(factory.createJobDispatchOption(param.getDispatchOption()));
        jobInfo.setExecutorName(param.getExecutorName());
        return jobInfo;
    }

}
