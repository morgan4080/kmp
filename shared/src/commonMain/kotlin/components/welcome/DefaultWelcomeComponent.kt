package components.welcome

import androidx.compose.ui.graphics.painter.Painter
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dev.icerock.moko.resources.compose.painterResource
import prestaCustomer.shared.MR

class DefaultWelcomeComponent (
    componentContext: ComponentContext,
    private val onGetStartedSelected: () -> Unit,
) : WelcomeComponent, ComponentContext by componentContext {
    override val model: Value<WelcomeComponent.Model> =
        MutableValue(WelcomeComponent.Model(
            // create welcome screens here
            items = listOf(
                UserEducationScreens(
                    "Welcome to Presta Customer",
                    "Loan applications made easy with Presta Customer",
                    ""// MR.images.user_education_1
                ),
                UserEducationScreens(
                    "Welcome to Presta Customer",
                    "Loan applications made easy with Presta Customer",
                    ""// MR.images.user_education_2
                ),
                UserEducationScreens(
                    "Welcome to Presta Customer",
                    "Loan applications made easy with Presta Customer",
                    ""// MR.images.user_education_3
                )
            )
        )
    )

    override fun onGetStarted() {
        onGetStartedSelected()
    }
}

