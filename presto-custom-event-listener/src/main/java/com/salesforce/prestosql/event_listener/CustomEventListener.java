package com.salesforce.prestosql.event_listener;

import com.datorama.oss.timbermill.TimberLogger;
import com.datorama.oss.timbermill.TimberLoggerAdvanced;
import com.datorama.oss.timbermill.pipe.TimbermillServerOutputPipeBuilder;
import com.datorama.oss.timbermill.unit.LogParams;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.airlift.log.Logger;
import io.prestosql.spi.PrestoWarning;
import io.prestosql.spi.eventlistener.EventListener;
import io.prestosql.spi.eventlistener.QueryCompletedEvent;
import io.prestosql.spi.eventlistener.QueryContext;
import io.prestosql.spi.eventlistener.QueryCreatedEvent;
import io.prestosql.spi.eventlistener.QueryIOMetadata;
import io.prestosql.spi.eventlistener.QueryMetadata;
import io.prestosql.spi.eventlistener.QueryStatistics;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.MINUTES;

public class CustomEventListener
        implements EventListener
{
    private static final Logger log = Logger.get(CustomEventListener.class);

    private static Cache<String, String> queryIdMap;
    private final CustomEventListenerConfig config;

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

    public CustomEventListener(CustomEventListenerConfig config)
    {
        this.config = config;
        TimberLogger.bootstrap(new TimbermillServerOutputPipeBuilder()
                .timbermillServerUrl(config.getHostname())
                .build());
        queryIdMap = CacheBuilder.newBuilder().expireAfterWrite(60, MINUTES).build();
    }

    @Override
    public void queryCreated(QueryCreatedEvent queryCreatedEvent)
    {
        String queryId = queryCreatedEvent.getMetadata().getQueryId();
        queryIdMap.put(queryId, startQueryTask(queryCreatedEvent.getCreateTime(), queryCreatedEvent.getContext(), queryCreatedEvent.getMetadata()));
    }

    String startQueryTask(Instant createTime, QueryContext context, QueryMetadata metadata)
    {
        return TimberLoggerAdvanced.start("presto_query", null, LogParams.create()
                .context("queryId", metadata.getQueryId())
                .context("transactionId", metadata.getTransactionId().orElse(""))
                .context("principal", context.getPrincipal().orElse(""))
                .context("query", metadata.getQuery())
                .context("uri", metadata.getUri().toString())
                .context("state", metadata.getQueryState())
                .context("createTime", dateFormatter.format(createTime))
                .context("user", context.getUser()));
    }

    @Override
    public void queryCompleted(QueryCompletedEvent queryCompletedEvent)
    {
        QueryMetadata metadata = queryCompletedEvent.getMetadata();

        boolean hadStartTask = true;
        String queryId = metadata.getQueryId();
        String taskId = queryIdMap.getIfPresent(queryId);
        queryIdMap.invalidate(queryId);

        QueryContext context = queryCompletedEvent.getContext();
        if (StringUtils.isEmpty(taskId)) {
            hadStartTask = false;
            taskId = startQueryTask(queryCompletedEvent.getCreateTime(), context, metadata);
        }

        try {
            QueryStatistics statistics = queryCompletedEvent.getStatistics();
            QueryIOMetadata ioMetadata = queryCompletedEvent.getIoMetadata();

            TimberLoggerAdvanced.logParams(taskId, LogParams.create().context("state", metadata.getQueryState())
                    .context("hadStartTask", hadStartTask)
                    .metric("cpuTime", statistics.getCpuTime().toMillis() / 1000)
                    .metric("wallTime", statistics.getWallTime().toMillis() / 1000)
                    .metric("queuedTime", statistics.getQueuedTime().toMillis() / 1000)
                    .metric("resourceWaitingTime", statistics.getResourceWaitingTime().isPresent() ? statistics.getResourceWaitingTime().get().toMillis() / 1000 : 0)
                    .metric("analysisTime", statistics.getAnalysisTime().isPresent() ? statistics.getAnalysisTime().get().toMillis() / 1000 : 0)
                    .metric("peakUserMemoryBytes", statistics.getPeakUserMemoryBytes())
                    .metric("peakTotalNonRevocableMemoryBytes", statistics.getPeakTotalNonRevocableMemoryBytes())
                    .metric("peakTaskUserMemory", statistics.getPeakTaskUserMemory())
                    .metric("peakTaskTotalMemory", statistics.getPeakTaskTotalMemory())
                    .metric("physicalInputBytes", statistics.getPhysicalInputBytes())
                    .metric("physicalInputRows", statistics.getPhysicalInputRows())
                    .metric("internalNetworkBytes", statistics.getInternalNetworkBytes())
                    .metric("internalNetworkRows", statistics.getInternalNetworkRows())
                    .metric("totalBytes", statistics.getTotalBytes())
                    .metric("totalRows", statistics.getTotalRows())
                    .metric("outputRows", statistics.getOutputRows())
                    .metric("outputBytes", statistics.getOutputBytes())
                    .metric("writtenBytes", statistics.getWrittenBytes())
                    .metric("writtenRows", statistics.getWrittenRows())
                    .metric("cumulativeMemory", statistics.getCumulativeMemory())
                    .metric("completedSplits", statistics.getCompletedSplits())
                    .context("isComplete", statistics.isComplete())
                    .context("operatorSummaries", String.join(",", statistics.getOperatorSummaries()))
                    .context("planNodeStatsAndCosts", statistics.getPlanNodeStatsAndCosts().orElse(""))

                    .context("catalog", context.getCatalog())
                    .context("schema", context.getSchema())
                    .context("source", context.getSource())
                    .context("user", context.getUser())
                    .context("source", context.getResourceGroupId().isPresent() ? context.getResourceGroupId().get().getLastSegment() : "")
                    .context("sessionProperties", context.getSessionProperties().entrySet().stream().map(e -> e.getKey() + ":" + e.getValue()).collect(Collectors.joining(",")))

                    .context("warnings", queryCompletedEvent.getWarnings().stream().map(PrestoWarning::toString).collect(Collectors.joining(",")))

                    .context("prepared_query", metadata.getPreparedQuery().orElse(""))
                    .context("payload", metadata.getPayload().orElse(""))
                    .context("plan", metadata.getPlan().orElse(""))

                    .context("createTime", dateFormatter.format(queryCompletedEvent.getCreateTime()))
                    .context("executionStartTime", dateFormatter.format(queryCompletedEvent.getExecutionStartTime()))
                    .context("endTime", dateFormatter.format(queryCompletedEvent.getEndTime()))

                    .context("remoteClientAddress", context.getRemoteClientAddress().orElse(""))
                    .context("userAgent", context.getUserAgent().orElse(""))
                    .context("ioInputTables", ioMetadata.getInputs().stream().map(i ->
                            String.format("%s.%s.%s columns:[%s]",
                                    i.getCatalogName(),
                                    i.getSchema(),
                                    i.getTable(),
                                    String.join(",", i.getColumns()))).collect(Collectors.toList()))
                    .context("ioOutputTables", ioMetadata.getOutput().isPresent() ?
                            String.format("%s.%s.%s isJsonLengthLimitExceeded:[%b]",
                                    ioMetadata.getOutput().get().getCatalogName(),
                                    ioMetadata.getOutput().get().getSchema(),
                                    ioMetadata.getOutput().get().getTable(),
                                    ioMetadata.getOutput().get().getJsonLengthLimitExceeded().orElse(false)) : "")
            );

            if (queryCompletedEvent.getFailureInfo().isPresent()) {
                TimberLoggerAdvanced.logParams(taskId, LogParams.create().context("failure_info", queryCompletedEvent.getFailureInfo().get().getFailuresJson()));
                TimberLoggerAdvanced.error(taskId, new Exception(queryCompletedEvent.getFailureInfo().get().getFailuresJson()));
            }
            else {
                TimberLoggerAdvanced.success(taskId);
            }
        }
        catch (Exception e) {
            log.error("Failed to populate query stats", e);
            TimberLoggerAdvanced.error(taskId, e);
        }
    }
}