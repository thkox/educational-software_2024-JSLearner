package eu.tkacas.jslearner.presentation.ui.activity.welcome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.tkacas.jslearner.JSLearner
import eu.tkacas.jslearner.presentation.ui.activity.welcome.navigation.WelcomeNavigation
import eu.tkacas.jslearner.presentation.ui.theme.JSLearnerTheme
import eu.tkacas.jslearner.presentation.viewmodel.viewModelFactory
import eu.tkacas.jslearner.presentation.viewmodel.welcome.ExperienceLevelViewModel
import eu.tkacas.jslearner.presentation.viewmodel.welcome.ExperienceTextViewModel
import eu.tkacas.jslearner.presentation.viewmodel.welcome.ExploringPathViewModel
import eu.tkacas.jslearner.presentation.viewmodel.welcome.LearningReasonViewModel
import eu.tkacas.jslearner.presentation.viewmodel.welcome.auth.LoginViewModel
import eu.tkacas.jslearner.presentation.viewmodel.welcome.auth.SignUpViewModel

class WelcomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JSLearnerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val loginViewModel = viewModel<LoginViewModel>(
                        factory = viewModelFactory {
                            LoginViewModel(
                                loginUseCase = JSLearner.appModule.loginUseCase,
                                getProfileCompletionUseCase = JSLearner.appModule.getProfileCompletionUseCase,
                                validateEmail = JSLearner.appModule.validateEmail,
                                validatePassword = JSLearner.appModule.validatePassword
                            )
                        }
                    )
                    val signUpViewModel = viewModel<SignUpViewModel>(
                        factory = viewModelFactory {
                            SignUpViewModel(
                                signUpUseCase = JSLearner.appModule.signUpUseCase,
                                validateEmail = JSLearner.appModule.validateEmail,
                                validatePassword = JSLearner.appModule.validatePassword,
                                validateFirstName = JSLearner.appModule.validateFirstName,
                                validateLastName = JSLearner.appModule.validateLastName,
                                validateTerms = JSLearner.appModule.validateTerms
                            )
                        }
                    )
                    val experienceLevelViewModel = viewModel<ExperienceLevelViewModel>()
                    val exploringPathViewModel = viewModel<ExploringPathViewModel>(
                        factory = viewModelFactory {
                            ExploringPathViewModel(
                                getCoursesBasedOnExperienceUseCase = JSLearner.appModule.getCoursesBasedOnExperienceUseCase,
                                updateUserProfileUseCase = JSLearner.appModule.updateUserProfileUseCase,
                                setUserStatsUseCase = JSLearner.appModule.setUserStatsUseCase
                            )
                        }
                    )
                    val learningReasonViewModel = viewModel<LearningReasonViewModel>()
                    val experienceTextViewModel = viewModel<ExperienceTextViewModel>()

                    WelcomeNavigation(
                        context = this,
                        authRepository = JSLearner.appModule.authRepository,
                        loginViewModel = loginViewModel,
                        signUpViewModel = signUpViewModel,
                        getProfileCompletionUseCase = JSLearner.appModule.getProfileCompletionUseCase,
                        experienceLevelViewModel = experienceLevelViewModel,
                        exploringPathViewModel = exploringPathViewModel,
                        learningReasonViewModel = learningReasonViewModel,
                        experienceTextViewModel = experienceTextViewModel
                    )
                }
            }
        }
    }
}