package com.oli.saga

import org.koin.logger.SLF4JLogger

enum class SagaStepResult { UNFINISHED, FINISHED, ROLLED_BACK }

abstract class SagaDefinition(
    private val sagaState: SagaState,
    private val logger: SLF4JLogger,
    private val sagaName: String
) {
    protected abstract val saga: Saga

    /**
     * Proceed in the saga by one step.
     * @return CreateOrderSagaResult
     */
    suspend fun step(): SagaStepResult {
        val step = saga.steps[sagaState.currentStep]
        return if (sagaState.rollingBack) {
            // Rollback direction
            step.compensatingTransaction?.invoke()
            logger.debug("$sagaName ${sagaState.sagaId} rolled back to step ${sagaState.currentStep} with description: ${step.description}")
            sagaState.currentStep -= 1

            if (sagaState.currentStep == -1) SagaStepResult.ROLLED_BACK
            else SagaStepResult.UNFINISHED
        } else {
            // Forward direction
            step.transaction?.invoke()
            logger.debug("$sagaName ${sagaState.sagaId} proceeded to step ${sagaState.currentStep} with description: ${step.description}")
            sagaState.currentStep += 1

            if (sagaState.currentStep == saga.steps.size) SagaStepResult.FINISHED
            else SagaStepResult.UNFINISHED
        }
    }
}
