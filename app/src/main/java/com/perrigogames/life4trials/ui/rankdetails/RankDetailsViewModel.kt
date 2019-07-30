package com.perrigogames.life4trials.ui.rankdetails

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.perrigogames.life4trials.R
import com.perrigogames.life4trials.data.BaseRankGoal
import com.perrigogames.life4trials.data.RankEntry
import com.perrigogames.life4trials.db.GoalStatus
import com.perrigogames.life4trials.db.GoalStatusDB
import com.perrigogames.life4trials.manager.LadderManager
import com.perrigogames.life4trials.view.LadderGoalItemView

/**
 * A [ViewModel] that manages manipulating and displaying a series of [BaseRankGoal]s
 * attributable to a certain rank.
 */
class RankDetailsViewModel(private val context: Context,
                           private val rankEntry: RankEntry,
                           private val options: RankDetailsFragment.Options,
                           private val ladderManager: LadderManager,
                           private val goalListListener: OnGoalListInteractionListener?) : ViewModel() {

    private val activeItems: MutableList<BaseRankGoal> by lazy {
        if (options.hideCompleted) {
            val completedGoals = ladderManager.getGoalStatuses(rankEntry.goals).filter { it.status == GoalStatus.COMPLETE }.map { it.goalId }
            rankEntry.goals.filterNot { completedGoals.contains(it.id.toLong()) }.toMutableList()
        } else rankEntry.goals.toMutableList()
    }
    private val expandedItems = mutableListOf<BaseRankGoal>()
    private val hiddenItemCount get() = ladderManager.getGoalStatuses(activeItems).count { it.status == GoalStatus.IGNORED }
    private var canIgnoreGoals: Boolean = true

    private val goalItemListener = object: LadderGoalItemView.LadderGoalItemListener {

        override fun onStateToggle(itemView: LadderGoalItemView, item: BaseRankGoal, goalDB: GoalStatusDB) {
            ladderManager.setGoalState(goalDB, when(goalDB.status) {
                GoalStatus.COMPLETE -> GoalStatus.INCOMPLETE
                else -> GoalStatus.COMPLETE
            })
            updateVisibility(item, goalDB)
            updateHiddenCount()
            goalListListener?.onGoalStateChanged(item, goalDB.status, 0)
        }

        override fun onIgnoreClicked(itemView: LadderGoalItemView, item: BaseRankGoal, goalDB: GoalStatusDB) {
            ladderManager.setGoalState(goalDB, when(goalDB.status) {
                GoalStatus.IGNORED -> GoalStatus.INCOMPLETE
                else -> GoalStatus.IGNORED
            })
            updateVisibility(item, goalDB)
            updateHiddenCount()
            goalListListener?.onGoalStateChanged(item, goalDB.status, 0)
        }

        override fun onExpandClicked(itemView: LadderGoalItemView, item: BaseRankGoal, goalDB: GoalStatusDB) {
            expandedItems.remove(item) || expandedItems.add(item)
            adapter.notifyItemChanged(activeItems.indexOf(item))
        }

        override fun onLongPressed(itemView: LadderGoalItemView, item: BaseRankGoal, goalDB: GoalStatusDB) {
            Log.v("rank goal", "long pressed")
        }
    }

    private val dataSource = object: RankGoalsAdapter.DataSource {
        override fun getGoals() = activeItems
        override fun isGoalExpanded(item: BaseRankGoal) = expandedItems.contains(item)
        override fun canIgnoreGoals(): Boolean = canIgnoreGoals
        override fun getGoalStatus(item: BaseRankGoal) = ladderManager.getOrCreateGoalStatus(item)
        override fun getGoalProgress(item: BaseRankGoal) = ladderManager.getGoalProgress(item)
    }

    val adapter: RankGoalsAdapter =
        RankGoalsAdapter(rankEntry, dataSource, goalItemListener, goalListListener)

    val hiddenStatusText = MutableLiveData<String>()
    val hiddenStatusVisibility = MutableLiveData<Int>()

    init {
        updateHiddenCount()
    }

    fun updateVisibility(goal: BaseRankGoal, goalDB: GoalStatusDB) {
        if (goalDB.status == GoalStatus.COMPLETE && options.hideCompleted) {
            val index = activeItems.indexOf(goal)
            activeItems.removeAt(index)
            adapter.notifyItemRemoved(index)
            if (activeItems.isEmpty()) {
                adapter.notifyItemInserted(0)
            }
        }
    }

    private fun updateHiddenCount() {
        val canHideTasks = rankEntry.allowedIgnores > 0
        hiddenStatusVisibility.value = if (canHideTasks) View.VISIBLE else View.INVISIBLE
        if (canHideTasks) {
            hiddenStatusText.value = context.getString(R.string.goals_hidden_format, hiddenItemCount, rankEntry.allowedIgnores)
        }
        val prevIgnore = canIgnoreGoals
        canIgnoreGoals = hiddenItemCount < rankEntry.allowedIgnores
        if (prevIgnore != canIgnoreGoals) {
            adapter.notifyDataSetChanged()
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    interface OnGoalListInteractionListener {
        fun onGoalStateChanged(item: BaseRankGoal, goalStatus: GoalStatus, hiddenGoals: Int) {}
        fun onRankSubmitClicked() {}
    }
}
