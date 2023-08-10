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

package org.limbo.flowjob.broker.core.agent.rpc;

import org.apache.commons.lang3.BooleanUtils;
import org.limbo.flowjob.api.constants.rpc.HttpAgentApi;
import org.limbo.flowjob.api.dto.ResponseDTO;
import org.limbo.flowjob.api.param.agent.JobSubmitParam;
import org.limbo.flowjob.broker.core.agent.AgentConverter;
import org.limbo.flowjob.broker.core.agent.ScheduleAgent;
import org.limbo.flowjob.broker.core.domain.job.JobInstance;
import org.limbo.flowjob.broker.core.exceptions.RpcException;
import org.limbo.flowjob.broker.core.rpc.AbstractRpc;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * @author Brozen
 * @since 2022-08-26
 */
public class RetrofitHttpAgentRpc extends AbstractRpc implements AgentRpc {

    private final RetrofitAgentApi api;

    public RetrofitHttpAgentRpc(ScheduleAgent agent) {
        super(agent.getId());
        this.api = new Retrofit.Builder()
                .baseUrl(agent.getUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RetrofitAgentApi.class);
    }

    @Override
    public boolean dispatch(JobInstance instance) {
        Boolean result = send(api.dispatch(AgentConverter.toJobDispatchParam(instance)));
        return BooleanUtils.isTrue(result);
    }

    private <T> T send(Call<ResponseDTO<T>> call) {
        return getResponseData(() -> {
            try {
                return call.execute().body();
            } catch (Exception e) {
                throw new RpcException(id(), "http api execute error", e);
            }
        });
    }


    /**
     * Worker HTTP 协议通信接口
     */
    interface RetrofitAgentApi {

        @Headers(
                "Content-Type: application/json"
        )
        @POST(HttpAgentApi.API_JOB_SUBMIT)
        Call<ResponseDTO<Boolean>> dispatch(@Body JobSubmitParam param);

    }

}
