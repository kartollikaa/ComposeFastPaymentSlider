# ComposeFastPaymentSlider

Демо реализации слайдера быстрой оплаты («pull to pay») на Jetpack Compose. Это переписанная и упрощённая версия компонента, который используется в рабочем проекте — здесь он собран в виде самостоятельной песочницы, чтобы можно было покрутить состояния и параметры в одном маленьком приложении.

## Что внутри

- `FastPaymentScreen` — экран-демо с настройками: цена, ошибка, количество товаров, время приготовления, тёмная тема, idle-подсказка.
- `fastpayment/compose/FastPaymentButton` — сам слайдер с измерителем (`FastPaymentMeasurer`) и набором контейнеров для разных состояний (idle / pull-to-pay / paying / sync / error).
- `fastpayment/compose/FastPaymentDraggableState` — обёртка над `AnchoredDraggableState` с прогрессом и якорями `Start` / `End`.
- `fastpayment/haptic` — `ProgressVibrator` и `DistanceDetector`, которые управляют тактильной отдачей по мере того, как пользователь тянет ползунок. Вибрация включается только в `IDLE`, и только когда ползунок уже доехал до начала — подробности в комментариях `handleVibrationEnabled`.
- `placeholder/` — мини-копия placeholder/shimmer из Accompanist, чтобы не тащить лишних зависимостей.
- `staff/` — небольшие хелперы: универсальный `Image` для drawable/ImageVector/painter, `getString` через `LocalContext`.

## Стек

Kotlin, Jetpack Compose, Material3, `kotlinx.collections.immutable`. `compileSdk` 35, `minSdk` 26.

## Как запустить

Открыть в Android Studio и запустить модуль `app` на устройстве/эмуляторе с Android 8.0+. На экране доступны переключатели для всех состояний, поэтому залезать в код, чтобы посмотреть конкретный кейс, не нужно.
