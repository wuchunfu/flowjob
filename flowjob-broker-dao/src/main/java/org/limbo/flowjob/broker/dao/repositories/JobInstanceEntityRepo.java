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

package org.limbo.flowjob.broker.dao.repositories;

import org.limbo.flowjob.api.constants.ConstantsPool;
import org.limbo.flowjob.broker.dao.entity.JobInstanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Devil
 * @since 2022/6/24
 */
public interface JobInstanceEntityRepo extends JpaRepository<JobInstanceEntity, String>, JpaSpecificationExecutor<JobInstanceEntity> {

    @Query(value = "select * from flowjob_job_instance" +
            " where plan_id in :planIds and status = :status and last_report_at >= :lastReportAtStart and last_report_at <= :lastReportAtEnd and job_instance_id > :startId" +
            " order by job_instance_id LIMIT :limit", nativeQuery = true)
    List<JobInstanceEntity> findByExecuteCheck(@Param("planIds") List<String> planIds,
                                               @Param("status") Integer status,
                                               @Param("lastReportAtStart") LocalDateTime lastReportAtStart,
                                               @Param("lastReportAtEnd") LocalDateTime lastReportAtEnd,
                                               @Param("startId") String startId,
                                               @Param("limit") Integer limit);

    @Query(value = "select * from flowjob_job_instance" +
            " where plan_id in :planIds and  status = :status and last_report_at <= :lastReportAt and trigger_at <= :triggerAt and job_instance_id > :startId " +
            " order by job_instance_id LIMIT :limit", nativeQuery = true)
    List<JobInstanceEntity> findInSchedule(@Param("planIds") List<String> planIds,
                                           @Param("lastReportAt") LocalDateTime lastReportAt,
                                           @Param("triggerAt") LocalDateTime triggerAt,
                                           @Param("status") Integer status,
                                           @Param("startId") String startId,
                                           @Param("limit") Integer limit);

    @Query(value = "select * from flowjob_job_instance where plan_instance_id = :planInstanceId and  job_id = :jobId order by trigger_at desc limit 1", nativeQuery = true)
    JobInstanceEntity findByLatest(@Param("planInstanceId") String planInstanceId, @Param("jobId") String jobId);

    @Modifying(clearAutomatically = true)
    @Query(value = "update JobInstanceEntity " +
            " set status = " + ConstantsPool.JOB_EXECUTING + ", agentId = :agentId , startAt = :startAt " +
            " where jobInstanceId = :jobInstanceId and status = " + ConstantsPool.JOB_SCHEDULING)
    int executing(@Param("jobInstanceId") String jobInstanceId, @Param("agentId") String agentId, @Param("startAt") LocalDateTime startAt);

    @Modifying(clearAutomatically = true)
    @Query(value = "update JobInstanceEntity " +
            " set lastReportAt = :lastReportAt " +
            " where jobInstanceId = :jobInstanceId and status = " + ConstantsPool.JOB_EXECUTING)
    int report(@Param("jobInstanceId") String jobInstanceId, @Param("lastReportAt") LocalDateTime lastReportAt);

    @Modifying(clearAutomatically = true)
    @Query(value = "update JobInstanceEntity " +
            "set status = " + ConstantsPool.JOB_EXECUTE_SUCCEED + ", context = :context, endAt = :endAt " +
            "where jobInstanceId = :jobInstanceId and status = " + ConstantsPool.JOB_EXECUTING)
    int success(@Param("jobInstanceId") String jobInstanceId, @Param("endAt") LocalDateTime endAt, @Param("context") String context);

    @Modifying(clearAutomatically = true)
    @Query(value = "update JobInstanceEntity " +
            "set status = " + ConstantsPool.JOB_EXECUTE_FAILED + ", errorMsg =:errorMsg, startAt = :startAt, endAt = :endAt  " +
            " where jobInstanceId = :jobInstanceId and status = :oldStatus")
    int fail(@Param("jobInstanceId") String jobInstanceId,
             @Param("oldStatus") Integer oldStatus,
             @Param("startAt") LocalDateTime startAt,
             @Param("endAt") LocalDateTime endAt,
             @Param("errorMsg") String errorMsg);

}
