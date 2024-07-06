package eu.tkacas.jslearner.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import eu.tkacas.jslearner.data.model.UserFirebase
import eu.tkacas.jslearner.data.model.UserFirestore
import eu.tkacas.jslearner.data.source.remote.FirebaseDataSource
import eu.tkacas.jslearner.data.source.remote.FirestoreDataSource
import eu.tkacas.jslearner.domain.repository.AuthRepository
import eu.tkacas.jslearner.domain.Result
import eu.tkacas.jslearner.domain.model.experience.ExperienceLevel
import eu.tkacas.jslearner.domain.model.learningreason.LearningReason

class AuthRepositoryImpl (
    private val firebaseDataSource: FirebaseDataSource,
    private val firestoreDataSource: FirestoreDataSource
): AuthRepository {
    override val currentUser
        get() = firebaseDataSource.currentUser

    override suspend fun login(email: String, password: String) = firebaseDataSource.login(email, password)

    override suspend fun signup(firstName: String, lastName: String, email: String, password: String): Result<FirebaseUser> {
        val result = firebaseDataSource.signup(email, password)
        if (result is Result.Success) {
            val uid = result.result.uid
            val user = UserFirestore(firstName, lastName, dateRegistered = getCurrentDate(), profileCompleted = false)
            firestoreDataSource.setUserProfile(uid, user)
        }
        return result
    }

    override suspend fun updateUserProfile(learningReason: LearningReason, profileCompleted: Boolean, epxerienceLevel: ExperienceLevel) {
        try {
            val uid = currentUser?.uid ?: return
            // Update the user profile in Firestore
            val user = UserFirestore(learningReason = learningReason, profileCompleted = profileCompleted)
            firestoreDataSource.updateUserProfile(uid, user)
            // Update the user stats in Firebase
            val userStats = UserFirebase(experienceLevel = epxerienceLevel)
            firebaseDataSource.setUserStats(uid, userStats)
        } catch (e: Exception) {
            Log.w("AuthRepositoryImpl", "Error updating user profile.", e)
        }
    }

    override suspend fun checkUserProfileCompletion(): Boolean {
        val uid = currentUser?.uid ?: return false
        val profileCompletion = firestoreDataSource.checkIfProfileCompleted(uid)
        return profileCompletion
    }

    private fun getCurrentDate(): String {
        return System.currentTimeMillis().toString()
    }
    override fun logout() = firebaseDataSource.logout()
}