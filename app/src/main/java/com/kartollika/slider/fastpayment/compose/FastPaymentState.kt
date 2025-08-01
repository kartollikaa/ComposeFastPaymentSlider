package com.kartollika.slider.fastpayment.compose

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import com.kartollika.slider.fastpayment.compose.FastPaymentState.CartStateVO.IDLE
import com.kartollika.slider.fastpayment.compose.FastPaymentState.CartStateVO.PAYING
import com.kartollika.slider.fastpayment.compose.FastPaymentState.CartStateVO.SYNC
import com.kartollika.slider.fastpayment.compose.FastPaymentState.FastPaymentError
import com.kartollika.slider.staff.Image

@Immutable
data class FastPaymentState(
    val id: String = "",
    val totalPrice: String = "",
    val products: ImmutableList<FastPaymentProduct>,
    val cartState: CartStateVO,
    val paymentMethod: CartPaymentMethodVO,
    val loyalty: LoyaltyVO,
    val estimatedOrderTime: EstimatedOrderTimeState,
    val draggableSliderVisible: Boolean,
    val error: FastPaymentError? = null,
    val idleHintVisible: Boolean = false,
    val isGivingMode: Boolean = false,
    val isGiftMode: Boolean = false,
    val cartProductsCount: Int = 0,
) {

  fun idle() = copy(
      cartState = IDLE,
      error = null,
      idleHintVisible = false
  )

  fun paying() = copy(
      cartState = PAYING,
      error = null,
      idleHintVisible = false
  )

  fun error(error: FastPaymentError) = copy(
      cartState = IDLE,
      error = error,
      idleHintVisible = false
  )

  fun sync() = copy(
      cartState = SYNC,
      error = null,
      idleHintVisible = false
  )

  enum class CartStateVO {
    PAYING,
    SYNC,
    ERROR,
    IDLE,
    SYNC_ERROR,
  }

  @Immutable
  data class FastPaymentError(
    val message: String,
  )

  @Immutable
  sealed interface LoyaltyVO {
    @Immutable
    data object NoLoyalty : LoyaltyVO

    @Immutable
    data object Active : LoyaltyVO
  }

  @Immutable
  data class CartPaymentMethodVO(
    val icon: Int,
    val isGPay: Boolean,
    val isFake: Boolean,
    val isNewCard: Boolean,
    val isSavedCard: Boolean,
    val isSberPay: Boolean,
    val isApp2App: Boolean,
    val isSbp: Boolean,
  )

  @Immutable
  sealed interface EstimatedOrderTimeState {
    @Immutable
    data object NoEstimatedOrderTime : EstimatedOrderTimeState

    @Immutable
    data class EstimatedOrderTimeHidden(
      val periodMin: Int = 0,
      val periodMax: Int = 0,
    ) : EstimatedOrderTimeState

    @Immutable
    data class EstimatedOrderTimeAvailable(
        val periodMin: Int = 0,
        val periodMax: Int = 0,
        val estimatedTimeText: String,
        val estimatedOrderTimeType: EstimatedOrderTimeSpeedType,
    ) : EstimatedOrderTimeState

    @Immutable
    enum class EstimatedOrderTimeSpeedType {
      Normal,
      Slow,
    }
  }
}

@Immutable
data class FastPaymentProduct(
  val image: Image,
  val isGiving: Boolean = false,
  val isGift: Boolean = false,
)

fun FastPaymentState.idle() = copy(
    cartState = IDLE,
    error = null
)

fun FastPaymentState.paying() = copy(
    cartState = PAYING,
    error = null
)

fun FastPaymentState.error(error: FastPaymentError) = copy(
    error = error
)

fun FastPaymentState.sync() = copy(
    cartState = SYNC,
    error = null
)