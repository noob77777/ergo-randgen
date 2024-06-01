# ergo-randgen | SigmaRand
Commit Reveal Random Generation for Ergo - ERGOHACKVIII

## Background
Generating randomness on any blockchain is challenging because every node must come to the same conclusion on the state of the blockchain. Naive approaches to generate randomness can be manipulated by miners or observant attackers. Insecure randomness can be exploited by attackers to gain an unfair advantage in games, lotteries, or any other contracts that rely on random number generation.

## Problem Statement
We need a secure random number generation service for Ergo with the following properties
1. The scheme should be secure.
2. The number generated should be equally unpredictable for all participants, i.e. no participant should have an “upper hand”.
3. All participants must agree on the same random number generated.

## Scope
This document describes how the “Commit-Reveal” scheme can be used to solve the problem described above and dives deep into how to implement the protocol for Ergo.

## On-Chain Randomness
For some cases it can be sufficient to use pseudo-random numbers or getting a random number from a trusted source. However, there are use cases ​​where a seemingly random number that can be predicted is simply not good enough. Examples: 
1. Games and gambling: Randomness is often used in contracts for games and gambling. For example, a blockchain-based poker game might use randomness to shuffle the deck of cards.
2. NFTs: Randomness required to create/distribute rare NFTs.

## Commit-Reveal Protocol
So how do we generate a random number on-chain that is equally unpredictable for all parties involved? Well, we use the Commit-Reveal protocol.
Commit-Reveal is a multi-party scheme for generating random numbers. It has two phases: commit and reveal.

**Commit:** Participants generate a random seed and calculate the corresponding hash value. They also submit a commitment that contains the hash of their answer and the random seed value. The smart contract stores this commitment on the blockchain.

**Reveal:** Participants reveal their answer and the seed value.

Let’s take a look at how it works:
1. Side A generates a random number, randomA
2. Side A sends a message with the hash of that number, hash(randomA). This commits Side A to the value randomA, because while no one can guess the value of randomA, once side A provides it everyone can check that its value is correct
3. Side B sends a message with another random number, randomB
4. Side A reveals the value of randomA in a third message
5. Both sides accept that the random number is randomA ^ randomB, the exclusive or (XOR) of the two values
The advantage of XOR here is that it is determined equally by both sides, so neither can choose an advantageous “random” value.

![commit-reveal](https://github.com/noob77777/ergo-randgen/assets/42897033/db871454-d589-44a3-bef6-701076e8b892)

One key observation here is participant A knows the random number before participant B. When randomB is committed, participant A already knows randomA and can compute randomA ^ randomB. Participant A may choose to not reveal randomA, if the computed random number is not favorable for participant A. To prevent this from happening we must ensure that participant A locks some amount of token or ergs as a pledge that is refunded/used only after randomA is revealed. 

Once the random number is committed on-chain we can use the value for our dApp. The next section details how such a protocol can be implemented on Ergo.

## Implementation - Overview
To start with, let us model the steps in terms of transactions. We will be referring to participant A as the client and participant B as the server for the rest of this document. In the start state, the client has a token which needs to be tagged with a randomly generated number. That is, after the protocol has been executed successfully, we must lock the token in a box which has a user-defined guard script and a random number in one of the registers(for this implementation we will always use R4) of the box.

![overview drawio](https://github.com/noob77777/ergo-randgen/assets/42897033/7042d6cb-4cef-44a5-a3f5-4367ee4d797d)


It can also be the case that after the client submits the token and hash(randomA) as the first step, the server doesn’t act on it. For such cases it must be possible to refund the token, but only after a set threshold time (say 60 blocks or 2 hours). Once the server has revealed randomB, the token can be locked to the contract defined by the application by revealing randomA. Once the token is locked in the contract, it is the application's responsibility to use the token and the random number in R4 for further processing.

## Implementation - Design
Overall we have the following components. The protocol needs 3 transactions to generate the random number.
1. Hash Transaction - Commits client hash value on chain
2. Commit Transaction - Commits server generated random number on chain
3. Reveal Transaction - Client reveals random number from Hash Transaction

To simplify using the random number generation we will expose 3 APIs to the client.
1. RegisterRandomNumberGenerationTask
2. GetRandomNumberGenerationStatus
3. RevealRandomNumber
Here we have proposed a cloud-native solution to implement the same.

### POST RegisterRandomNumberGenerationTask
The client provides the hash for its part of the random number generation process, the final locking contract address and the tokenId it should lock in the final contract.
```
RegisterRandomNumberGenerationTaskRequest
{
	address: string;
	randomHash: string;
	lockingContractAddress: string;
	lockingTokenId: string;
	lockingTokenAmount: number;
}

RegisterRandomNumberGenerationTaskResponse
{
	taskId: string;
	unsignedTransaction: UnsignedTransaction;
  tastStatus: COMMIT_IN_PROGRESS;
}
```

Once a register call is made, the service will start the CommitRandomNumber step function workflow and return an unsigned transaction for the client. The workflow is responsible for committing server generated random value on-chain.

![commit](https://github.com/noob77777/ergo-randgen/assets/42897033/a3c650c2-82a7-4246-bf12-e59d862f853e)

### GET GetRandomNumberGenerationStatus
The client can query the task_id returned and check for current task status. This will be required to know when the client can reveal the random seed.

```
GetRandomNumberGenerationStatusResponse
{
	taskId: string;
	lockingContractAddress: string;
	lockingTokenId: string;
	lockingTokenAmount: number;
	hashBoxId?: string;
	commitBoxId?: string;
	revealBoxId?: string;
  taskStatus: NOT_STARTED|COMMIT_IN_PROGRESS|COMMITTED|REVEAL_IN_PROGRESS|COMPLETED;
}
```
When the task status is in “COMMITTED” state, it means that the CommitRandomNumber workflow is complete. The client must now reveal its part of the random number.

![getstatus](https://github.com/noob77777/ergo-randgen/assets/42897033/c627b44e-ebce-43eb-9895-863682ff8fb4)

### POST RevealRandomNumber
The client must reveal its part of the random number to complete the protocol. Once the random number generated at the client end is revealed, the token and the random number are locked in the specified locking contract address.

```
RevealRandomNumberRequest
{
  address: string;	
  taskId: string;
  random: number;
}

RevealRandomNumberResponse
{
	taskId: string;
	unsignedTransaction: UnsignedTransaction;
  tastStatus: REVEAL_IN_PROGRESS;
}
```

![reveal](https://github.com/noob77777/ergo-randgen/assets/42897033/f29225e4-9f1d-46ff-a337-82ce986d13dc)

The design proposed here uses all serverless components, i.e. lambdas and step functions. The reasoning behind this is getting billed per execution and managed auto scaling. This allows us to easily extend this to an external-client facing service. It is possible to charge external users per random number generated with minimal changes to this architecture.

## Implementation - Contracts
```
object Contract {
  val HASH_UTXO_SCRIPT: String =
    s"""
       |{
       |  val clientPK = _CLIENT_PK
       |  val serverPK = _SERVER_PK
       |  val deadline = _DEADLINE
       |  val runtimePropositionBytes = _RUNTIME_PROPOSITION_BYTES
       |  val tokenId = _TOKEN_ID
       |  val tokenAmount = _TOKEN_AMOUNT
       |
       |  val commitBox = OUTPUTS(0)
       |  val refund = HEIGHT > deadline
       |  val propositionCondition = runtimePropositionBytes == commitBox.propositionBytes
       |  val sizeCondition = INPUTS.size == 1 && OUTPUTS.size == 2
       |  val tokenCondition = commitBox.tokens(0)._1 == tokenId && commitBox.tokens(0)._2 == tokenAmount
       |  clientPK && sigmaProp(refund) || serverPK && sigmaProp(propositionCondition && tokenCondition && sizeCondition)
       |}
       |""".stripMargin
  val COMMIT_UTXO_SCRIPT: String =
    s"""
       |{
       |  val runtimePropositionBytes = _RUNTIME_PROPOSITION_BYTES
       |  val tokenId = _TOKEN_ID
       |  val tokenAmount = _TOKEN_AMOUNT
       |
       |  val commitBox = INPUTS(0)
       |  val revealBox = OUTPUTS(0)
       |  val hashCondition = commitBox.R4[Coll[Byte]].get == blake2b256(revealBox.R4[Coll[Byte]].get)
       |  val commitCondition = commitBox.R5[Coll[Byte]].get == revealBox.R5[Coll[Byte]].get
       |  val propositionCondition = runtimePropositionBytes == revealBox.propositionBytes
       |  val sizeCondition = INPUTS.size == 1 && OUTPUTS.size == 2
       |  val tokenCondition = revealBox.tokens(0)._1 == tokenId && revealBox.tokens(0)._2 == tokenAmount
       |  sigmaProp(hashCondition && commitCondition && propositionCondition && tokenCondition && sizeCondition)
       |}
       |""".stripMargin
}
```

## Example: Opening a “Pack” of NFTs
Let’s walk through an example problem that uses this service. We have a “pack” token when “opened” will redeem a fixed number of random NFTs of varying “rarity”. To model the problem, let us assume there is a pool of NFT’s and the number of NFT’s of each type is proportional to its rarity.
To redeem the pack token, the dApp first generates a random number randomA and makes a RegisterRandomNumberGenerationTask API call. The transaction locks the pack token and hash(randomA) in a UTXO. Our service then generates randomB and spends this UTXO in the Commit Transaction.
The dApp knows when the Commit Transaction is confirmed/committed using the  GetRandomNumberGenerationStatus API. Once the task is in COMMITTED the dApp reveals randomA using RevealRandomNumber API. The server then locks the pack token and randomA ^ randomB in the dApp specified contract completing the random number generation part.
The dApp can now use this random number generated and the pack token to send the end user the required NFTs.

## Summary
In this document, we have outlined the commit-reveal protocol and how it can be implemented in a cloud-native environment. The implementation proposed here is not specific to any specific dApp use-case and can be easily reused across multiple applications that need a secure random number in its execution. We have also shown how the proposed architecture can be extended easily to an external-client facing service that can support per request billing.

## Example

**IMPORTANT: CLIENT SIDE RANDOM SHOULD CONTAIN 32 CHARACTERS**

### 1. Register random number generation task
```
curl -X POST https://67zt8ejryg.execute-api.us-east-2.amazonaws.com/beta/random-number/register -d '{
  "address": "9i6UmaoJKWHgWkuq1EJUoYu2hrkRkxAYwQjDotHRHfGrBo16Rss",
  "lockingContractAddress": "9i6UmaoJKWHgWkuq1EJUoYu2hrkRkxAYwQjDotHRHfGrBo16Rss",
  "lockingTokenId": "d71693c49a84fbbecd4908c94813b46514b18b67a99952dc1e6e4791556de413",
  "lockingTokenAmount": 1,
  "randomHash": "54e15ad6e8b896861bd7f4fddce11c9a94d4a6286004d4d3ab6b3664b361ef35"
}'
```

Example Transaction:
```
https://explorer.ergoplatform.com/en/transactions/1a5ddc9aa0775f60d262a8315b04110316e513b7acbcc856432261bcbc491935
```

### 2. The server then commits its part of the random.
Checking status
```
curl https://67zt8ejryg.execute-api.us-east-2.amazonaws.com/beta/random-number/task/6e506e12-068b-44c5-a947-70e160d68b74
```

```
{
  "taskId": "6e506e12-068b-44c5-a947-70e160d68b74",
  "lockingContractAddress": "9i6UmaoJKWHgWkuq1EJUoYu2hrkRkxAYwQjDotHRHfGrBo16Rss",
  "lockingTokenId": "d71693c49a84fbbecd4908c94813b46514b18b67a99952dc1e6e4791556de413",
  "lockingTokenAmount": 1,
  "hashBoxId": "d29d557cc22f70dfc903c6885337ef14c9c890e6dbf0e7b0f9c8dec55cc5e91e",
  "commitBoxId": "4915d463d29c94ade359e9cee332c592173022840bba76179ba7d19dc3cb3ec5",
  "revealBoxId": "null",
  "taskStatus": "COMMITTED"
}
```

Example Transaction:
```
https://explorer.ergoplatform.com/en/transactions/4298401f83007eb6fa825f47a8edc699270a8af10c7a6c0993eaeab83e1ffbde
```

### Commit Workflow
![image](https://github.com/noob77777/ergo-randgen/assets/42897033/d6debb56-fd5b-4584-a835-16e9a6b7bf80)

### 3. Reveal Hash
```
curl -X POST https://67zt8ejryg.execute-api.us-east-2.amazonaws.com/beta/random-number/reveal -d '{
  "address": "9i6UmaoJKWHgWkuq1EJUoYu2hrkRkxAYwQjDotHRHfGrBo16Rss",
  "taskId": "6e506e12-068b-44c5-a947-70e160d68b74",
  "random": "36845d2ac36444a35f314f001a93e708"
}'
```

Example Transaction:
```
https://explorer.ergoplatform.com/en/transactions/079b8f0deef478f29c308875ac950627dbeaf7978ab19b8624a7c5e79d8689d7
```

Random is committed in R6 of generated output box.

### Reveal Workflow
![image](https://github.com/noob77777/ergo-randgen/assets/42897033/11213fa6-ff29-49bb-9876-843564edb014)


### Completed Task
```
{
  "taskId": "6e506e12-068b-44c5-a947-70e160d68b74",
  "lockingContractAddress": "9i6UmaoJKWHgWkuq1EJUoYu2hrkRkxAYwQjDotHRHfGrBo16Rss",
  "lockingTokenId": "d71693c49a84fbbecd4908c94813b46514b18b67a99952dc1e6e4791556de413",
  "lockingTokenAmount": 1,
  "hashBoxId": "d29d557cc22f70dfc903c6885337ef14c9c890e6dbf0e7b0f9c8dec55cc5e91e",
  "commitBoxId": "4915d463d29c94ade359e9cee332c592173022840bba76179ba7d19dc3cb3ec5",
  "revealBoxId": "f35e89ca9c5d270bf163a01ba44970f7ce68f132d3a0ec627fa600a30b5d5fd4",
  "taskStatus": "COMPLETED"
}
```

### Random Generated
![image](https://github.com/noob77777/ergo-randgen/assets/42897033/525092b7-0d5b-4626-aee9-273ffc844898)

```
R4 - clientSide random
R5 - serverSide random
R6 - R4 xor R5
```

### Fin.
