package sigmarand.model;

import java.util.List;

public record RegisterRandomNumberGenerationTaskRequest(
        String address,
        String randomHash,
        String lockingContractAddress,
        String lockingTokenId,
        Integer lockingTokenAmount
) {}
