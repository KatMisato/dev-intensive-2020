package ru.skillbranch.devintensive.viewmodels

import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.repositories.PreferencesRepository

class ProfileViewModel : ViewModel() {
    private val repository: PreferencesRepository = PreferencesRepository
    private val profileData = MutableLiveData<Profile>()
    private val appTheme = MutableLiveData<Int>()
    private val repositoryValid = MutableLiveData<Boolean>()

    init {
        Log.d("M_ProfileViewModel", "init view model")
        profileData.value = repository.getProfile()
        appTheme.value = repository.getAppTheme()
        repositoryValid.value = repository.getRepositoryValid()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("M_ProfileViewModel", "onCleared")
    }

    fun getProfileData(): LiveData<Profile> = profileData

    fun getTheme(): LiveData<Int> = appTheme

    fun isRepositoryValid(): LiveData<Boolean> = repositoryValid

    fun repositoryValidation(repository: String) {
        val x: MatchResult? =
            Regex("^(?:https?://)?(?:www\\.)?(?:github\\.com/)([a-zA-Z_\\d-]+)$").find(repository)

        val isMatches = if (x == null) false
        else x.groupValues[1] !in arrayOf(
            "enterprise",
            "features",
            "topics",
            "collections",
            "trending",
            "events",
            "marketplace",
            "pricing",
            "nonprofit",
            "customer-stories",
            "security",
            "login",
            "join"
        )
        repositoryValid.value = repository.isNotEmpty() && isMatches
    }

    fun saveProfileData(profile: Profile) {
        repository.saveProfile(profile)
        profileData.value = profile
    }

    fun switchTheme() {
        if (appTheme.value == AppCompatDelegate.MODE_NIGHT_YES) {
            appTheme.value = AppCompatDelegate.MODE_NIGHT_NO
        } else {
            appTheme.value = AppCompatDelegate.MODE_NIGHT_YES
        }
        repository.saveAppTheme(appTheme.value!!)
    }
}