package com.batch.springBatch.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;

public class DataRangePartitioner implements Partitioner {

	@Value("${app.batch.gridsize}")
	private int gridSize;
	@Value("${app.batch.file.data.min.count}")
	private int min;
	@Value("${app.batch.file.data.max.count}")
	private int max;
	private static final String PARTITION_NAME = "Partition-";

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {

		int targetSize = (max - min) / gridSize + 1; // 2654 per thread processing
		System.out.println("targetSize: " + targetSize + " for total row: " + max);

		Map<String, ExecutionContext> result = new HashMap<>();

		int partitionCount = 0;
		int start = min;
		int end = start + targetSize - 1;// 1+2654-1 = 2654

		/*
		 * dividing the data rows equally among all grids
		 */
		while (start <= max) {
			ExecutionContext value = new ExecutionContext();
			result.put(PARTITION_NAME + partitionCount, value);

			if (end >= max) {
				end = max;
			}
			value.put("minValue", start);// 1
			value.put("maxValue", end);// 2654

			start = start + targetSize;
			end = end + targetSize;

			partitionCount++;
		}

		System.out.println("partition created: " + result.toString());

		return result;
	}

}
