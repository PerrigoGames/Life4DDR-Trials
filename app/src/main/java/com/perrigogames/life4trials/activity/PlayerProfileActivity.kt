package com.perrigogames.life4trials.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.perrigogames.life4trials.Life4Application
import com.perrigogames.life4trials.R
import com.perrigogames.life4trials.activity.SettingsActivity.Companion.KEY_INFO_NAME
import com.perrigogames.life4trials.activity.SettingsActivity.Companion.KEY_INFO_RANK
import com.perrigogames.life4trials.data.BaseRankGoal
import com.perrigogames.life4trials.data.LadderRank
import com.perrigogames.life4trials.db.GoalStatus
import com.perrigogames.life4trials.event.LadderRankUpdatedEvent
import com.perrigogames.life4trials.life4app
import com.perrigogames.life4trials.ui.rankdetails.RankDetailsFragment
import com.perrigogames.life4trials.ui.rankdetails.RankGoalsAdapter
import com.perrigogames.life4trials.util.CommonSizes
import com.perrigogames.life4trials.util.SharedPrefsUtil
import com.perrigogames.life4trials.util.openWebUrlFromRes
import kotlinx.android.synthetic.main.activity_player_profile.*
import kotlinx.android.synthetic.main.content_player_profile.*
import kotlinx.android.synthetic.main.item_profile_mode_button.view.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * An Activity that displays the local player's current profile. This includes
 * info like their name and rank, in-progress goals, and navigation buttons to
 * other experiences like Tournaments or Trials.
 */
class PlayerProfileActivity : AppCompatActivity(), RankDetailsFragment.OnGoalListInteractionListener {

    private val ladderManager get() = life4app.ladderManager
    private var rank: LadderRank? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_profile)
        setSupportActionBar(toolbar)

        setupContent()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        SharedPrefsUtil.isPreviewEnabled().let { p ->
            menu.findItem(R.id.action_progress_matrix).isVisible = p
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> startActivity(Intent(this, SettingsActivity::class.java))
            R.id.action_records -> startActivity(Intent(this, TrialRecordsActivity::class.java))
            R.id.action_progress_matrix -> startActivity(MatrixTestActivity.intent(this, LadderRank.DIAMOND1))
            R.id.action_shop -> openWebUrlFromRes(R.string.url_shop)
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Life4Application.eventBus.register(this)
    }

    override fun onDetachedFromWindow() {
        Life4Application.eventBus.unregister(this)
        super.onDetachedFromWindow()
    }

    fun onExtraViewClicked(v: View) {
        when (v.id) {
            R.id.view_mode_button_left -> {
                if (SharedPrefsUtil.isPreviewEnabled()) {
                    startActivity(Intent(this, SingleSongTournamentActivity::class.java))
                }
            }
            R.id.view_mode_button_right -> startActivity(Intent(this, TrialListActivity::class.java))
        }
    }

    override fun onGoalStateChanged(item: BaseRankGoal, goalStatus: GoalStatus) = Unit

    override fun onRankSubmitClicked() = openWebUrlFromRes(R.string.url_standard_submission_form)

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLadderRankModified(e: LadderRankUpdatedEvent) = updatePlayerContent()

    private fun setupContent() {
        if (!SharedPrefsUtil.isPreviewEnabled()) {
            view_mode_button_left.alpha = 0.3f
        }
        view_mode_button_left.text_title.text = getString(R.string.tournaments)
        view_mode_button_left.image_background.apply {
            setImageDrawable(ContextCompat.getDrawable(this@PlayerProfileActivity, R.drawable.ic_tournament))
            setColorFilter(ContextCompat.getColor(this@PlayerProfileActivity, R.color.white))
            setPadding(0, CommonSizes.contentPaddingLarge(resources) + CommonSizes.contentPaddingMed(resources), 0, 0)
        }
        view_mode_button_right.text_title.text = getString(R.string.trials)
        view_mode_button_right.image_background.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.life4_trials_logo_invert))

        updatePlayerContent()
    }

    private fun updatePlayerContent() {
        text_player_name.text = SharedPrefsUtil.getUserString(this, KEY_INFO_NAME)
        rank = LadderRank.parse(SharedPrefsUtil.getUserString(this, KEY_INFO_RANK)?.toLongOrNull())

        if (rank != null) {
            image_rank.visibility = View.VISIBLE
            image_rank.rank = rank
            image_rank.setOnClickListener { startActivity(Intent(this, RankListActivity::class.java)) }

            ladderManager.nextEntry(rank!!)?.let { rankEntry ->
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container_current_goals, RankDetailsFragment.newInstance(rankEntry,
                        RankGoalsAdapter.Options(hideCompleted = true, hideIgnored = false, showHeader = false), null, this))
                    .commitNowAllowingStateLoss()
            }
        } else {
            image_rank.visibility = View.GONE
        }
    }
}
