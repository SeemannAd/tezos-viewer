import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Serializable
data class Block(
    val cycle: Int?= null,
    val level: Int?= null,
    val hash: String?= null,
    val timestamp: String?= null,
    val proto: Int?= null,
    val payloadRound: Int?= null,
    val blockRound: Int?= null,
    val validations: Int?= null,
    val deposit: Int?= null,
    val rewardDelegated: Int?= null,
    val rewardStakedOwn: Int?= null,
    val rewardStakedEdge: Int?= null,
    val rewardStakedShared: Int?= null,
    val bonusDelegated: Int?= null,
    val bonusStakedOwn: Int?= null,
    val bonusStakedEdge: Int?= null,
    val bonusStakedShared: Int?= null,
    val fees: Int?= null,
    val nonceRevealed: Boolean?= null,
    val proposer: Proposer? = null,
    val producer: Producer? = null,
    val software: Software? = null,
    val lbToggle: Boolean? = null,
    val lbToggleEma: Int?= null,
    val aiToggle: Boolean?= null,
    val aiToggleEma: Int?= null,
    val endorsements: List<Endorsement>?= null,
    val preendorsements: List<Preendorsement>?= null,
    val proposals: List<Proposal>?= null,
    val ballots: List<Ballot>?= null,
    val activations: List<Activation>?= null,
    val doubleBaking: List<DoubleBaking>?= null,
    val doubleEndorsing: List<DoubleEndorsing>?= null,
    val doublePreendorsing: List<DoublePreendorsing>?= null,
    val nonceRevelations: List<NonceRevelation>?= null,
    val vdfRevelations: List<VdfRevelation>?= null,
    val delegations: List<Delegation>?= null,
    val originations: List<Origination>?= null,
    val transactions: List<Transaction>?= null,
    val reveals: List<Reveal>?= null,
    val registerConstants: List<RegisterConstant>?= null,
    val setDepositsLimits: List<SetDepositLimit>?= null,
    val transferTicketOps: List<TransferTicketOp>?= null,
    val txRollupCommitOps: List<TxRollupCommitOp>?= null,
    val txRollupDispatchTicketsOps: List<TxRollupDispatchTicketOp>?= null,
    val txRollupFinalizeCommitmentOps: List<TxRollupFinalizeCommitmentOp>?= null,
    val txRollupOriginationOps: List<TxRollupOriginationOp>?= null,
    val txRollupRejectionOps: List<TxRollupRejectionOp>?= null,
    val txRollupRemoveCommitmentOps: List<TxRollupRemoveCommitmentOp>?= null,
    val txRollupReturnBondOps: List<TxRollupReturnBondOp>?= null,
    val txRollupSubmitBatchOps: List<TxRollupSubmitBatchOp>?= null,
    val increasePaidStorageOps: List<IncreasePaidStorageOp>?= null,
    val updateConsensusKeyOps: List<UpdateConsensusKeyOp>?= null,
    val drainDelegateOps: List<DrainDelegateOp>?= null,
    val srAddMessagesOps: List<SrAddMessageOp>?= null,
    val srCementOps: List<SrCementOp>?= null,
    val srExecuteOps: List<SrExecuteOp>?= null,
    val srOriginateOps: List<SrOriginateOp>?= null,
    val srPublishOps: List<SrPublishOp>?= null,
    val srRecoverBondOps: List<SrRecoverBondOp>?= null,
    val srRefuteOps: List<SrRefuteOp>?= null,
    val stakingOps: List<StakingOp>?= null,
    val setDelegateParametersOps: List<SetDelegateParametersOp>?= null,
    val dalPublishCommitmentOps: List<DalPublishCommitmentOp>?= null,
    val migrations: List<Migration>?= null,
    val revelationPenalties: List<RevelationPenalty>?= null,
    val endorsingRewards: List<EndorsingReward>?= null,
    val autostakingOps: List<AutostakingOp>?= null,
    val quote: Quote?= null,
    val rewardLiquid: Int?= null,
    val bonusLiquid: Int?= null,
    val reward: Int?= null,
    val bonus: Int?= null,
    val priority: Int?= null,
    val baker: Baker? = null,
    val lbEscapeVote: Boolean?= null,
    val lbEscapeEma: Int?= null
){
    val date: String
        get() {
            if(timestamp == null) return ""
            return try {
                val datePattern = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                LocalDateTime
                    .ofInstant(Instant.parse(timestamp), ZoneId.systemDefault())
                    .format(datePattern) ?: ""
            } catch (e:Exception) {
                ""
            }
        }

    val time: String
        get() {
            if(timestamp == null) return ""
            return try {
                val datePattern = DateTimeFormatter.ofPattern("mm:hh a")
                LocalDateTime
                    .ofInstant(Instant.parse(timestamp), ZoneId.systemDefault())
                    .format(datePattern) ?: ""
            } catch (e:Exception) {
                ""
            }
        }
}

@Serializable
data class RegisterConstant(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val hash: String,
    val sender: Delegate,
    val counter: Int,
    val gasLimit: Int,
    val gasUsed: Int,
    val storageLimit: Int,
    val storageUsed: Int,
    val bakerFee: Int,
    val status: String,
    val address: String,
    val value: String?,
    val errors: List<Error>,
    val quote: Quote
)

@Serializable
data class SetDepositLimit(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val hash: String,
    val sender: Delegate,
    val counter: Int,
    val gasLimit: Int,
    val gasUsed: Int,
    val storageLimit: Int,
    val bakerFee: Int,
    val status: String,
    val limit: String,
    val errors: List<Error>,
    val quote: Quote
)

@Serializable
data class TransferTicketOp(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val hash: String,
    val sender: Delegate,
    val counter: Int,
    val gasLimit: Int,
    val gasUsed: Int,
    val storageLimit: Int,
    val storageUsed: Int,
    val bakerFee: Int,
    val storageFee: Int,
    val target: Delegate,
    val ticketer: Delegate,
    val amount: String,
    val entrypoint: String,
    val ticketTransfersCount: Int,
    val contentType: String?,
    val content: String?,
    val status: String,
    val errors: List<Error>,
    val quote: Quote
)

@Serializable
data class TxRollupCommitOp(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val hash: String,
    val sender: Delegate,
    val counter: Int,
    val gasLimit: Int,
    val gasUsed: Int,
    val storageLimit: Int,
    val bakerFee: Int,
    val rollup: Delegate,
    val bond: Int,
    val status: String,
    val errors: List<Error>,
    val quote: Quote
)

@Serializable
data class Proposer(val alias: String, val address: String)

@Serializable
data class Producer(val alias: String, val address: String)

@Serializable
data class Software(val version: String, val date: String)

@Serializable
data class Endorsement(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val hash: String,
    val delegate: Delegate,
    val slots: Int,
    val deposit: Int,
    val rewards: Int,
    val quote: Quote
)

@Serializable
data class Delegate(val alias: String, val address: String)

@Serializable
data class Quote(
    val btc: Int,
    val eur: Int,
    val usd: Int,
    val cny: Int,
    val jpy: Int,
    val krw: Int,
    val eth: Int,
    val gbp: Int
)
// Define other necessary data classes similarly...

// Example for one of the classes
@Serializable
data class Proposal(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val hash: String,
    val period: Period,
    val proposal: ProposalDetail,
    val delegate: Delegate,
    val votingPower: Int,
    val duplicated: Boolean,
    val quote: Quote,
    val rolls: Int
)

@Serializable
data class Baker(
    val alias: String,
    val address: String
)

@Serializable
data class Preendorsement(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val hash: String,
    val delegate: Delegate,
    val slots: Int,
    val quote: Quote
)

@Serializable
data class Ballot(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val hash: String,
    val period: Period,
    val proposal: ProposalDetail,
    val delegate: Delegate,
    val votingPower: Int,
    val vote: String,
    val quote: Quote,
    val rolls: Int
)

@Serializable
data class Activation(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val hash: String,
    val account: Delegate,
    val balance: Int,
    val quote: Quote
)

@Serializable
data class DoubleBaking(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val hash: String,
    val accusedLevel: Int,
    val slashedLevel: Int,
    val accuser: Delegate,
    val reward: Int,
    val offender: Delegate,
    val lostStaked: Int,
    val lostUnstaked: Int,
    val lostExternalStaked: Int,
    val lostExternalUnstaked: Int,
    val stakingUpdatesCount: Int,
    val quote: Quote,
    val roundingLoss: Int,
    val offenderLoss: Int,
    val accuserReward: Int,
    val accuserRewards: Int,
    val offenderLostDeposits: Int,
    val offenderLostRewards: Int,
    val offenderLostFees: Int
)

@Serializable
data class DoubleEndorsing(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val hash: String,
    val accusedLevel: Int,
    val slashedLevel: Int,
    val accuser: Delegate,
    val reward: Int,
    val offender: Delegate,
    val lostStaked: Int,
    val lostUnstaked: Int,
    val lostExternalStaked: Int,
    val lostExternalUnstaked: Int,
    val stakingUpdatesCount: Int,
    val quote: Quote,
    val roundingLoss: Int,
    val offenderLoss: Int,
    val accuserReward: Int,
    val accuserRewards: Int,
    val offenderLostDeposits: Int,
    val offenderLostRewards: Int,
    val offenderLostFees: Int
)

@Serializable
data class DoublePreendorsing(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val hash: String,
    val accusedLevel: Int,
    val slashedLevel: Int,
    val accuser: Delegate,
    val reward: Int,
    val offender: Delegate,
    val lostStaked: Int,
    val lostUnstaked: Int,
    val lostExternalStaked: Int,
    val lostExternalUnstaked: Int,
    val stakingUpdatesCount: Int,
    val quote: Quote,
    val roundingLoss: Int,
    val offenderLoss: Int,
    val accuserReward: Int
)

@Serializable
data class NonceRevelation(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val hash: String,
    val baker: Delegate,
    val sender: Delegate,
    val revealedLevel: Int,
    val revealedCycle: Int,
    val nonce: String,
    val rewardDelegated: Int,
    val rewardStakedOwn: Int,
    val rewardStakedEdge: Int,
    val rewardStakedShared: Int,
    val quote: Quote,
    val rewardLiquid: Int,
    val reward: Int,
    val bakerRewards: Int
)

@Serializable
data class VdfRevelation(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val hash: String,
    val baker: Delegate,
    val cycle: Int,
    val solution: String,
    val proof: String,
    val rewardDelegated: Int,
    val rewardStakedOwn: Int,
    val rewardStakedEdge: Int,
    val rewardStakedShared: Int,
    val quote: Quote,
    val rewardLiquid: Int,
    val reward: Int
)

@Serializable
data class Delegation(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val hash: String,
    val counter: Int,
    val initiator: Delegate,
    val sender: Delegate,
    val senderCodeHash: Int,
    val nonce: Int,
    val gasLimit: Int,
    val gasUsed: Int,
    val storageLimit: Int,
    val bakerFee: Int,
    val amount: Int,
    val stakingUpdatesCount: Int,
    val prevDelegate: Delegate,
    val newDelegate: Delegate,
    val status: String,
    val errors: List<Error>,
    val quote: Quote,
    val unstakedPseudotokens: Int,
    val unstakedBalance: Int,
    val unstakedRewards: Int
)

@Serializable
data class Origination(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val hash: String,
    val counter: Int,
    val initiator: Delegate,
    val sender: Delegate,
    val senderCodeHash: Int,
    val nonce: Int,
    val gasLimit: Int,
    val gasUsed: Int,
    val storageLimit: Int,
    val storageUsed: Int,
    val bakerFee: Int,
    val storageFee: Int,
    val allocationFee: Int,
    val contractBalance: Int,
    val contractManager: Delegate,
    val contractDelegate: Delegate,
    val code: String?,
    val storage: String?,
    val diffs: List<Diff>,
    val status: String,
    val errors: List<Error>,
    val originatedContract: OriginatedContract,
    val tokenTransfersCount: Int,
    val quote: Quote
)

@Serializable
data class Transaction(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val hash: String,
    val counter: Int,
    val initiator: Delegate,
    val sender: Delegate,
    val senderCodeHash: Int,
    val nonce: Int,
    val gasLimit: Int,
    val gasUsed: Int,
    val storageLimit: Int,
    val storageUsed: Int,
    val bakerFee: Int,
    val storageFee: Int,
    val allocationFee: Int,
    val target: Delegate,
    val targetCodeHash: Int,
    val amount: Int,
    val parameter: Parameter,
    val storage: String?,
    val diffs: List<Diff>,
    val status: String,
    val errors: List<Error>,
    val hasInternals: Boolean,
    val tokenTransfersCount: Int,
    val ticketTransfersCount: Int,
    val eventsCount: Int,
    val quote: Quote
)

@Serializable
data class Reveal(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val hash: String,
    val sender: Delegate,
    val counter: Int,
    val gasLimit: Int,
    val gasUsed: Int,
    val storageLimit: Int,
    val bakerFee: Int,
    val status: String,
    val errors: List<Error>,
    val quote: Quote
)

// Supporting classes
@Serializable
data class Error(val type: String)

@Serializable
data class Diff(val bigmap: Int, val path: String, val action: String, val content: Content)

@Serializable
data class Content(val hash: String, val key: String?, val value: String?)

@Serializable
data class OriginatedContract(
    val kind: String,
    val alias: String,
    val address: String,
    val typeHash: Int,
    val codeHash: Int,
    val tzips: List<String>
)

@Serializable
data class Parameter(val entrypoint: String, val value: String?)

@Serializable
data class Period(
    val index: Int,
    val epoch: Int,
    val kind: String,
    val firstLevel: Int,
    val lastLevel: Int
)

@Serializable
data class ProposalDetail(val alias: String, val hash: String)

@Serializable
data class TxRollupDispatchTicketOp(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val hash: String,
    val sender: Delegate,
    val counter: Int,
    val gasLimit: Int,
    val gasUsed: Int,
    val storageLimit: Int,
    val storageUsed: Int,
    val bakerFee: Int,
    val storageFee: Int,
    val rollup: Delegate,
    val status: String,
    val errors: List<Error>,
    val quote: Quote
)

@Serializable
data class TxRollupFinalizeCommitmentOp(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val hash: String,
    val sender: Delegate,
    val counter: Int,
    val gasLimit: Int,
    val gasUsed: Int,
    val storageLimit: Int,
    val bakerFee: Int,
    val rollup: Delegate,
    val status: String,
    val errors: List<Error>,
    val quote: Quote
)

@Serializable
data class TxRollupOriginationOp(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val hash: String,
    val sender: Delegate,
    val counter: Int,
    val gasLimit: Int,
    val gasUsed: Int,
    val storageLimit: Int,
    val bakerFee: Int,
    val allocationFee: Int,
    val rollup: Delegate,
    val status: String,
    val errors: List<Error>,
    val quote: Quote
)

@Serializable
data class TxRollupRejectionOp(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val hash: String,
    val sender: Delegate,
    val counter: Int,
    val gasLimit: Int,
    val gasUsed: Int,
    val storageLimit: Int,
    val bakerFee: Int,
    val rollup: Delegate,
    val committer: Delegate,
    val reward: Int,
    val loss: Int,
    val status: String,
    val errors: List<Error>,
    val quote: Quote
)

@Serializable
data class TxRollupRemoveCommitmentOp(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val hash: String,
    val sender: Delegate,
    val counter: Int,
    val gasLimit: Int,
    val gasUsed: Int,
    val storageLimit: Int,
    val bakerFee: Int,
    val rollup: Delegate,
    val status: String,
    val errors: List<Error>,
    val quote: Quote
)

@Serializable
data class TxRollupReturnBondOp(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val hash: String,
    val sender: Delegate,
    val counter: Int,
    val gasLimit: Int,
    val gasUsed: Int,
    val storageLimit: Int,
    val bakerFee: Int,
    val rollup: Delegate,
    val bond: Int,
    val status: String,
    val errors: List<Error>,
    val quote: Quote
)

@Serializable
data class TxRollupSubmitBatchOp(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val hash: String,
    val sender: Delegate,
    val counter: Int,
    val gasLimit: Int,
    val gasUsed: Int,
    val storageLimit: Int,
    val storageUsed: Int,
    val bakerFee: Int,
    val storageFee: Int,
    val rollup: Delegate,
    val status: String,
    val errors: List<Error>,
    val quote: Quote
)

@Serializable
data class IncreasePaidStorageOp(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val hash: String,
    val sender: Delegate,
    val counter: Int,
    val gasLimit: Int,
    val gasUsed: Int,
    val storageLimit: Int,
    val storageUsed: Int,
    val bakerFee: Int,
    val status: String,
    val contract: Delegate,
    val amount: String,
    val errors: List<Error>,
    val quote: Quote
)

@Serializable
data class UpdateConsensusKeyOp(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val hash: String,
    val sender: Delegate,
    val counter: Int,
    val gasLimit: Int,
    val gasUsed: Int,
    val storageLimit: Int,
    val bakerFee: Int,
    val status: String,
    val activationCycle: Int,
    val publicKey: String,
    val publicKeyHash: String,
    val errors: List<Error>,
    val quote: Quote
)

@Serializable
data class DrainDelegateOp(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val hash: String,
    val delegate: Delegate,
    val target: Delegate,
    val amount: Int,
    val fee: Int,
    val allocationFee: Int,
    val quote: Quote
)

@Serializable
data class SrAddMessageOp(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val hash: String,
    val sender: Delegate,
    val counter: Int,
    val gasLimit: Int,
    val gasUsed: Int,
    val storageLimit: Int,
    val bakerFee: Int,
    val status: String,
    val messagesCount: Int,
    val errors: List<Error>,
    val quote: Quote
)

@Serializable
data class SrCementOp(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val hash: String,
    val sender: Delegate,
    val counter: Int,
    val gasLimit: Int,
    val gasUsed: Int,
    val storageLimit: Int,
    val bakerFee: Int,
    val rollup: Delegate,
    val commitment: Commitment,
    val status: String,
    val errors: List<Error>,
    val quote: Quote
)

@Serializable
data class Commitment(
    val id: Int,
    val initiator: Delegate,
    val inboxLevel: Int,
    val state: String,
    val hash: String,
    val ticks: Int,
    val firstLevel: Int,
    val firstTime: String
)

@Serializable
data class SrExecuteOp(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val hash: String,
    val sender: Delegate,
    val counter: Int,
    val gasLimit: Int,
    val gasUsed: Int,
    val storageLimit: Int,
    val storageUsed: Int,
    val bakerFee: Int,
    val rollup: Delegate,
    val commitment: Commitment,
    val status: String,
    val errors: List<Error>,
    val ticketTransfersCount: Int,
    val quote: Quote
)

@Serializable
data class SrOriginateOp(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val hash: String,
    val sender: Delegate,
    val counter: Int,
    val gasLimit: Int,
    val gasUsed: Int,
    val storageLimit: Int,
    val storageUsed: Int,
    val bakerFee: Int,
    val storageFee: Int,
    val status: String,
    val pvmKind: String,
    val kernel: String,
    val parameterType: String?,
    val genesisCommitment: String,
    val rollup: Delegate,
    val errors: List<Error>,
    val quote: Quote
)

@Serializable
data class SrPublishOp(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val hash: String,
    val sender: Delegate,
    val counter: Int,
    val gasLimit: Int,
    val gasUsed: Int,
    val storageLimit: Int,
    val bakerFee: Int,
    val rollup: Delegate,
    val commitment: Commitment,
    val bond: Int,
    val status: String,
    val errors: List<Error>,
    val quote: Quote
)

@Serializable
data class SrRecoverBondOp(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val hash: String,
    val sender: Delegate,
    val counter: Int,
    val gasLimit: Int,
    val gasUsed: Int,
    val storageLimit: Int,
    val bakerFee: Int,
    val rollup: Delegate,
    val staker: Delegate,
    val bond: Int,
    val status: String,
    val errors: List<Error>,
    val quote: Quote
)

@Serializable
data class SrRefuteOp(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val hash: String,
    val sender: Delegate,
    val counter: Int,
    val gasLimit: Int,
    val gasUsed: Int,
    val storageLimit: Int,
    val bakerFee: Int,
    val rollup: Delegate,
    val game: Game,
    val move: String,
    val gameStatus: String,
    val dissectionStart: Int,
    val dissectionEnd: Int,
    val dissectionSteps: Int,
    val status: String,
    val errors: List<Error>,
    val quote: Quote
)

@Serializable
data class Game(
    val id: Int,
    val initiator: Delegate,
    val initiatorCommitment: Commitment,
    val opponent: Delegate,
    val opponentCommitment: Commitment,
    val initiatorReward: Int,
    val initiatorLoss: Int,
    val opponentReward: Int,
    val opponentLoss: Int
)

@Serializable
data class StakingOp(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val hash: String,
    val sender: Delegate,
    val counter: Int,
    val gasLimit: Int,
    val gasUsed: Int,
    val storageLimit: Int,
    val bakerFee: Int,
    val action: String,
    val requestedAmount: Int,
    val amount: Int,
    val baker: Delegate,
    val stakingUpdatesCount: Int,
    val status: String,
    val errors: List<Error>,
    val quote: Quote,
    val kind: String,
    val pseudotokens: Int,
    val limitOfStakingOverBaking: Int,
    val edgeOfBakingOverStaking: Int,
    val activationCycle: Int
)

@Serializable
data class SetDelegateParametersOp(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val hash: String,
    val sender: Delegate,
    val counter: Int,
    val gasLimit: Int,
    val gasUsed: Int,
    val storageLimit: Int,
    val bakerFee: Int,
    val limitOfStakingOverBaking: Int,
    val edgeOfBakingOverStaking: Int,
    val activationCycle: Int,
    val status: String,
    val errors: List<Error>,
    val quote: Quote
)

@Serializable
data class DalPublishCommitmentOp(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val hash: String,
    val sender: Delegate,
    val counter: Int,
    val gasLimit: Int,
    val gasUsed: Int,
    val storageLimit: Int,
    val bakerFee: Int,
    val slot: Int,
    val commitment: String,
    val status: String,
    val errors: List<Error>,
    val quote: Quote
)

@Serializable
data class Migration(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val kind: String,
    val account: Delegate,
    val balanceChange: Int,
    val storage: String?,
    val diffs: List<Diff>,
    val tokenTransfersCount: Int,
    val quote: Quote
)

@Serializable
data class RevelationPenalty(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val baker: Delegate,
    val missedLevel: Int,
    val loss: Int,
    val quote: Quote,
    val lostReward: Int,
    val lostFees: Int
)

@Serializable
data class EndorsingReward(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val block: String,
    val baker: Delegate,
    val expected: Int,
    val rewardDelegated: Int,
    val rewardStakedOwn: Int,
    val rewardStakedEdge: Int,
    val rewardStakedShared: Int,
    val quote: Quote,
    val rewardLiquid: Int,
    val received: Int
)

@Serializable
data class AutostakingOp(
    val type: String,
    val id: Int,
    val level: Int,
    val timestamp: String,
    val baker: Delegate,
    val action: String,
    val amount: Int,
    val stakingUpdatesCount: Int,
    val quote: Quote,
    val cycle: Int
)
