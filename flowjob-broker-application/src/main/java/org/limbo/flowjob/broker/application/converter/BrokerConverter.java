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

package org.limbo.flowjob.broker.application.converter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.limbo.flowjob.api.dto.broker.AvailableWorkerDTO;
import org.limbo.flowjob.api.dto.broker.BrokerDTO;
import org.limbo.flowjob.api.dto.broker.BrokerTopologyDTO;
import org.limbo.flowjob.broker.core.cluster.Node;
import org.limbo.flowjob.broker.core.worker.Worker;

import java.util.Collection;

/**
 * @author Brozen
 * @since 2022-08-12
 */
@Slf4j
public class BrokerConverter {

    public static BrokerTopologyDTO toBrokerTopologyDTO(Collection<Node> nodes) {
        BrokerTopologyDTO brokerTopologyDTO = new BrokerTopologyDTO();
        if (CollectionUtils.isNotEmpty(nodes)) {
            for (Node node : nodes) {
                brokerTopologyDTO.getBrokers().add(new BrokerDTO(node.getHost(), node.getPort()));
            }
        }
        return brokerTopologyDTO;
    }

    public static AvailableWorkerDTO toWorkerDTO(Worker worker) {
        AvailableWorkerDTO workerDTO = new AvailableWorkerDTO();
        workerDTO.setId(worker.getId());
        workerDTO.setProtocol(worker.getUrl().getProtocol());
        workerDTO.setHost(worker.getUrl().getHost());
        workerDTO.setPort(worker.getUrl().getPort());
        return workerDTO;
    }

}
