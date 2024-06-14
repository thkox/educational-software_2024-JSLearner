package eu.tkacas.jslearner.presentation.ui.state

data class LoginFormState (
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val isDataValid: Boolean = false,
    val isDataValidError: String? = null
)