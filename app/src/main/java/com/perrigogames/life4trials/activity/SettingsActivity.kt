package com.perrigogames.life4trials.activity

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SENDTO
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.preference.*
import com.perrigogames.life4trials.*
import com.perrigogames.life4trials.R
import com.perrigogames.life4trials.data.LadderRank
import com.perrigogames.life4trials.data.TrialRank
import com.perrigogames.life4trials.data.TrialSession
import com.perrigogames.life4trials.event.LadderRanksReplacedEvent
import com.perrigogames.life4trials.event.LocalUserInfoUpdatedEvent
import com.perrigogames.life4trials.event.TrialListReplacedEvent
import com.perrigogames.life4trials.event.TrialListUpdatedEvent
import com.perrigogames.life4trials.util.NotificationUtil
import com.perrigogames.life4trials.util.SharedPrefsUtil
import com.perrigogames.life4trials.util.openWebUrlFromRes


class SettingsActivity : AppCompatActivity(), SettingsFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager.beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return false
    }

    override fun onFragmentEntered(title: String) {
        supportActionBar?.title = title
    }

    abstract class BaseSettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

        protected val ladderManager get() = context!!.life4app.ladderManager
        protected val trialManager get() = context!!.life4app.trialManager
        protected val playerManager get() = context!!.life4app.playerManager
        protected val songDataManager get() = context!!.life4app.songDataManager
        private var listener: SettingsFragmentListener? = null

        override fun onPause() {
            super.onPause()
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }

        override fun onResume() {
            super.onResume()
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
            listener!!.onFragmentEntered(fragmentName() ?: context!!.getString(R.string.action_settings))
        }

        override fun onAttach(context: Context) {
            super.onAttach(context)
            try {
                listener = context as SettingsFragmentListener
            } catch (e: ClassCastException) {
                throw ClassCastException("$context must implement OnFragmentInteractionListener")
            }
        }

        override fun onDetach() {
            super.onDetach()
            listener = null
        }

        abstract fun fragmentName(): String?

        protected fun preference(key: String) = preferenceScreen.findPreference<Preference>(key)!!

        protected inline fun preferenceListener(key: String, crossinline action: (Preference) -> Boolean) {
            preference(key).onPreferenceClickListener = Preference.OnPreferenceClickListener { action(it) }
        }

        protected inline fun preference(target: PreferenceGroup = preferenceScreen, block: Preference.() -> Unit) =
            target.addPreference(Preference(context).apply(block))

        protected inline fun switch(target: PreferenceGroup = preferenceScreen, block: SwitchPreference.() -> Unit) =
            target.addPreference(SwitchPreference(context).apply(block))

        protected inline fun checkBox(target: PreferenceGroup = preferenceScreen, block: CheckBoxPreference.() -> Unit) =
            target.addPreference(CheckBoxPreference(context).apply(block))

        protected inline fun category(key: String,
                                      title: String,
                                      target: PreferenceGroup = preferenceScreen,
                                      block: PreferenceCategory.() -> Unit) =
            PreferenceCategory(context).also {
                it.key = key
                it.title = title
                target.addPreference(it)
                it.apply(block)
            }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) = Unit
    }

    class SettingsFragment : BaseSettingsFragment() {
        override fun fragmentName() = null

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            (preference(SettingsKeys.KEY_IMPORT_GAME_VERSION) as DropDownPreference).apply {
                summary = songDataManager.getIgnoreList(value).name
                entries = songDataManager.ignoreListTitles.toTypedArray()
                entryValues = songDataManager.ignoreListIds.toTypedArray()
            }
            preferenceListener(SettingsKeys.KEY_IMPORT_VIEW_LIST) {
                startActivity(Intent(context, BlockListCheckActivity::class.java))
                true
            }
            preferenceListener(SettingsKeys.KEY_IMPORT_DATA) {
                ladderManager.showImportFlow(activity!!)
                true
            }

            preferenceListener(SettingsKeys.KEY_SHOP_LIFE4) {
                (activity as SettingsActivity).openWebUrlFromRes(R.string.url_shop_life4)
                true
            }
            preferenceListener(SettingsKeys.KEY_SHOP_DANGERSHARK) {
                (activity as SettingsActivity).openWebUrlFromRes(R.string.url_shop_dangershark)
                true
            }
            preferenceListener(SettingsKeys.KEY_FEEDBACK) {
                (context as AppCompatActivity).startActivity(Intent(ACTION_SENDTO, Uri.parse(
                    "mailto:life4@perrigogames.com?subject=${Uri.encode(getString(R.string.life4_feedback_email_subject))}")))
                true
            }
            preferenceListener(SettingsKeys.KEY_FIND_US_TWITTER) {
                (activity as SettingsActivity).openWebUrlFromRes(R.string.url_twitter)
                true
            }
            preferenceListener(SettingsKeys.KEY_CREDITS) {
                (context as AppCompatActivity).startActivity(Intent(context, CreditsActivity::class.java))
                true
            }

            preference(SettingsKeys.KEY_DEBUG).isVisible = BuildConfig.DEBUG

            preference {
                key = "version_info"
                title = "Version ${BuildConfig.VERSION_NAME}${
                    if (BuildConfig.DEBUG) " (${BuildConfig.VERSION_CODE})"
                    else ""}"
            }
        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
            if (key != null) {
                when (key) {
                    SettingsKeys.KEY_IMPORT_GAME_VERSION -> {
                        songDataManager.invalidateIgnoredIds()
                        findPreference<DropDownPreference>(key)?.let {
                            it.summary = songDataManager.getIgnoreList(it.value).name
                        }
                        Life4Application.eventBus.post(LadderRanksReplacedEvent())
                    }
                    SettingsKeys.KEY_INFO_IMPORT -> findPreference<EditTextPreference>(key)?.let {
                        it.text?.let { text -> playerManager.importPlayerInfo(text) }
                        it.text = null
                    }
                }
            }
        }
    }

    class UserInfoSettingsFragment : BaseSettingsFragment() {
        override fun fragmentName() = context!!.getString(R.string.edit_user_info)

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.user_info_preferences, rootKey)

            (preference(SettingsKeys.KEY_INFO_RANK) as DropDownPreference).apply {
                summary = LadderRank.parse(value?.toLongOrNull())?.toString() ?: getString(R.string.none)
                entries = LadderRank.values().map { it.toString() }.toMutableList().apply {
                    add(0, "NONE")
                }.toTypedArray()
                entryValues = LadderRank.values().map { it.stableId.toString() }.toMutableList().apply {
                    add(0, "")
                }.toTypedArray()
            }

            preference(SettingsKeys.KEY_INFO_NAME).summary =
                SharedPrefsUtil.getUserString(context!!, SettingsKeys.KEY_INFO_NAME)
            preference(SettingsKeys.KEY_INFO_RIVAL_CODE).summary =
                SharedPrefsUtil.getUserString(context!!, SettingsKeys.KEY_INFO_RIVAL_CODE)
            preference(SettingsKeys.KEY_INFO_TWITTER_NAME).summary =
                SharedPrefsUtil.getUserString(context!!, SettingsKeys.KEY_INFO_TWITTER_NAME)

            preferenceListener(SettingsKeys.KEY_SUBMISSION_NOTIFICAION_TEST) {
                NotificationUtil.showUserInfoNotifications(context!!, 1579)
                true
            }
        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
            if (key != null) {
                when (key) {
                    SettingsKeys.KEY_INFO_NAME, SettingsKeys.KEY_INFO_RIVAL_CODE, SettingsKeys.KEY_INFO_TWITTER_NAME -> {
                        findPreference<EditTextPreference>(key)?.let { it.summary = it.text }
                        Life4Application.eventBus.post(LocalUserInfoUpdatedEvent())
                    }
                    SettingsKeys.KEY_INFO_RANK -> {
                        findPreference<DropDownPreference>(key)?.let {
                            LadderRank.parse(it.value.toLongOrNull()).let { rank ->
                                it.summary = rank?.toString() ?: getString(R.string.none)
                                ladderManager.setUserRank(rank)
                            }
                        }
                    }
                }
            }
        }
    }

    class TrialSettingsFragment : BaseSettingsFragment() {
        override fun fragmentName() = context!!.getString(R.string.trial_settings)

        private val listUpdateListener: (Preference) -> Boolean = {
            Life4Application.eventBus.post(TrialListUpdatedEvent())
            true
        }
        private val listReplaceListener: (Preference) -> Boolean = {
            Life4Application.eventBus.post(TrialListReplacedEvent())
            true
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.trial_preferences, rootKey)

            preferenceListener(SettingsKeys.KEY_LIST_SHOW_EX, listUpdateListener)
            preferenceListener(SettingsKeys.KEY_LIST_SHOW_EX_REMAINING, listUpdateListener)
            preferenceListener(SettingsKeys.KEY_LIST_TINT_COMPLETED, listUpdateListener)
            preferenceListener(SettingsKeys.KEY_LIST_HIGHLIGHT_NEW, listReplaceListener)
        }
    }

    class ClearDataFragment : BaseSettingsFragment() {
        override fun fragmentName() = context!!.getString(R.string.clear_data)

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.clear_data_preferences, rootKey)
            preferenceListener(SettingsKeys.KEY_LADDER_CLEAR) {
                ladderManager.clearGoalStates(context!!)
                true
            }
            preferenceListener(SettingsKeys.KEY_RECORDS_CLEAR) {
                trialManager.clearRecords(context!!)
                true
            }
            preferenceListener(SettingsKeys.KEY_SONG_RESULTS_CLEAR) {
                ladderManager.clearSongResults(context!!)
                true
            }
            preferenceListener(SettingsKeys.KEY_REFRESH_SONG_DB) {
                ladderManager.refreshSongDatabase(context!!)
                true
            }
        }
    }

    class DebugSettingsFragment : BaseSettingsFragment() {
        override fun fragmentName() = "Debug Settings"

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.debug_preferences, rootKey)

            val context = preferenceManager.context

            preferenceListener(SettingsKeys.KEY_DEBUG_INDUCE_CRASH) { throw IllegalAccessException() }
            preferenceListener(SettingsKeys.KEY_DEBUG_NOTIF_PLACEMENT) {
                NotificationUtil.showPlacementNotification(context!!, LadderRank.values().random())
                true
            }
            preferenceListener(SettingsKeys.KEY_DEBUG_NOTIF_LADDER_RANK) {
                NotificationUtil.showLadderRankChangedNotification(context!!, LadderRank.values().random())
                true
            }
            preferenceListener(SettingsKeys.KEY_DEBUG_NOTIF_TRIAL_RANK) {
                NotificationUtil.showTrialRankChangedNotification(context!!, trialManager.trials.random(), TrialRank.values().random())
                true
            }
            preferenceListener(SettingsKeys.KEY_DEBUG_NOTIF_A20) {
                songDataManager.onA20RequiredUpdate(context!!)
                true
            }
            preferenceListener(SettingsKeys.KEY_DEBUG_LEADERBOARD) {
                (context as AppCompatActivity).startActivity(Intent(context, LadderLeaderboardActivity::class.java))
                true
            }
            preferenceListener(SettingsKeys.KEY_DEBUG_DATA_DUMP) {
                songDataManager.dumpData()
                true
            }
            preferenceListener(SettingsKeys.KEY_DEBUG_SONG_RECORDS) {
                (context as AppCompatActivity).startActivity(Intent(context, SongRecordsListCheckActivity::class.java))
                true
            }
        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) = Unit
    }

    class DebugTrialRanksFragment : BaseSettingsFragment() {
        override fun fragmentName() = "Trial Ranks"

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.empty_preferences, rootKey)
            val ranksList = TrialRank.values().map { it.toString() }.toMutableList()
            ranksList.add(0, "NONE")
            val ranksArray = ranksList.toTypedArray()
            context!!.life4app.trialManager.trials.filter { it.goals != null && it.goals.isNotEmpty() }.forEach { trial ->
                preferenceScreen.addPreference(DropDownPreference(context).apply {
                    key = "$SettingsKeys.KEY_DEBUG_RANK_PREFIX${trial.id}"
                    title = trial.name
                    icon = ContextCompat.getDrawable(context, trial.jacketResId(context!!))
                    summary = trialManager.getRankForTrial(trial.id)?.toString() ?: "NONE"
                    entries = ranksArray
                    entryValues = ranksArray
                })
            }
        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
            if (key != null) {
                when {
                    key.startsWith(SettingsKeys.KEY_DEBUG_RANK_PREFIX) -> findPreference<DropDownPreference>(key)?.let { it ->
                        val rank = TrialRank.parse(it.entry.toString())
                        val trial = trialManager.findTrial(it.key.substring(SettingsKeys.KEY_DEBUG_RANK_PREFIX.length))!!
                        val session = TrialSession(trial, if (trial.isEvent) null else rank).apply { goalObtained = (rank != null) }
                        trialManager.saveRecord(session)
                        it.summary = rank?.toString() ?: "NONE"
                    }
                }
            }
        }
    }
}

interface SettingsFragmentListener {
    fun onFragmentEntered(title: String)
}