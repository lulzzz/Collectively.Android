package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.presentation.dagger.ActivityScope
import com.noordwind.apps.collectively.presentation.settings.SettingsActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(SettingsModule::class))
interface SettingsComponent {
    fun inject(activity: SettingsActivity)
}
