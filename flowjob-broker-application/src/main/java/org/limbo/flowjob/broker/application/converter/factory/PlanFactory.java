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

import org.apache.commons.collections4.CollectionUtils;
import org.limbo.flowjob.api.param.console.DispatchOptionParam;
import org.limbo.flowjob.api.param.console.PlanParam;
import org.limbo.flowjob.api.param.console.RetryOptionParam;
import org.limbo.flowjob.api.param.console.TagFilterParam;
import org.limbo.flowjob.broker.core.domain.job.JobInfo;
import org.limbo.flowjob.broker.core.worker.dispatch.DispatchOption;
import org.limbo.flowjob.broker.core.worker.dispatch.RetryOption;
import org.limbo.flowjob.broker.core.worker.dispatch.TagFilterOption;
import org.limbo.flowjob.common.utils.attribute.Attributes;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Brozen
 * @since 2023-08-11
 */
@Component
public class PlanFactory {




    /**
     * 生成非 DAG 作业实体
     */
    public JobInfo createJob(PlanParam.NormalPlanParam jobParam) {
        JobInfo jobInfo = new JobInfo();
        jobInfo.setId("0");
        jobInfo.setType(jobParam.getType());
        jobInfo.setExecutorName(jobParam.getExecutorName());
        jobInfo.setAttributes(new Attributes(jobParam.getAttributes()));
        jobInfo.setRetryOption(createRetryOption(jobParam.getRetryOption()));
        jobInfo.setDispatchOption(createJobDispatchOption(jobParam.getDispatchOption()));
        return jobInfo;
    }



    /**
     * 生成作业重试参数
     */
    public RetryOption createRetryOption(RetryOptionParam param) {
        if (param == null) {
            return new RetryOption();
        }
        return RetryOption.builder()
                .retry(param.getRetry())
                .retryInterval(param.getRetryInterval())
                .retryType(param.getRetryType())
                .build();
    }



    /**
     * 生成作业分发参数
     */
    public DispatchOption createJobDispatchOption(DispatchOptionParam param) {
        return DispatchOption.builder()
                .loadBalanceType(param.getLoadBalanceType())
                .cpuRequirement(param.getCpuRequirement())
                .ramRequirement(param.getRamRequirement())
                .tagFilters(createTagFilterOption(param.getTagFilters()))
                .build();
    }



    /**
     * 生成作业的过滤标签
     */
    public List<TagFilterOption> createTagFilterOption(List<TagFilterParam> params) {
        if (CollectionUtils.isEmpty(params)) {
            return Collections.emptyList();
        }
        return params.stream().map(param -> TagFilterOption.builder()
                .tagName(param.getTagName())
                .tagValue(param.getTagValue())
                .condition(param.getCondition())
                .build()).collect(Collectors.toList());
    }

}
