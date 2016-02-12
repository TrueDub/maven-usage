package com.castlemon.maven.processing;

import org.springframework.stereotype.Component;

import com.castlemon.maven.domain.RunData;
import com.castlemon.maven.domain.Usage;

@Component
public class StatsGenerator {

    public void generateStats(RunData runData) {
        for (Usage usage : runData.getUsages()) {
            if (runData.getVersionCounts().containsKey(usage.getVersionUsed())) {
                int count = runData.getVersionCounts().get(usage.getVersionUsed());
                runData.getVersionCounts().put(usage.getVersionUsed(), ++count);
            } else {
                runData.getVersionCounts().put(usage.getVersionUsed(), 1);
            }
        }
    }

}
