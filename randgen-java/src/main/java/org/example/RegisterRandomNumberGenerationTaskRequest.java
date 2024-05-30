package org.example;

import java.util.List;

public record RegisterRandomNumberGenerationTaskRequest(
        List<String> addresses,
        String randomHash,
        String lockingContractAddress,
        String lockingTokenId,
        Integer lockingTokenAmount
) {}
