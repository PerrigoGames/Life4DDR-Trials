package com.perrigogames.life4trials.ui.rankdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.perrigogames.life4trials.R
import com.perrigogames.life4trials.data.BaseRankGoal
import com.perrigogames.life4trials.data.RankEntry
import com.perrigogames.life4trials.db.GoalStatus
import com.perrigogames.life4trials.life4app
import com.perrigogames.life4trials.view.RankHeaderView
import kotlinx.android.synthetic.main.fragment_rank_details.view.*

class RankDetailsFragment(private val rankEntry: RankEntry,
                          private val options: RankGoalsAdapter.Options,
                          private val navigationListener: RankHeaderView.NavigationListener? = null,
                          private val goalListListener: OnGoalListInteractionListener?) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_rank_details, container, false)

        if (options.showHeader) {
            (view.stub_rank_header.inflate() as RankHeaderView).let {
                it.rank = rankEntry.rank
                it.navigationListener = navigationListener
            }
        }
        view.fragment_rank_details.apply {
            adapter = RankGoalsAdapter(rankEntry, options, context.life4app.ladderManager, goalListListener)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

        return view
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    interface OnGoalListInteractionListener {
        fun onGoalStateChanged(item: BaseRankGoal, goalStatus: GoalStatus)

        fun onRankSubmitClicked()
    }

    companion object {
        fun newInstance(rankEntry: RankEntry,
                        options: RankGoalsAdapter.Options = RankGoalsAdapter.Options(),
                        navigationListener: RankHeaderView.NavigationListener? = null,
                        goalListListener: OnGoalListInteractionListener? = null) =
            RankDetailsFragment(rankEntry, options, navigationListener, goalListListener)
    }
}
