package pl.adriankremski.collectively.presentation.views.remark.comment

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.like.LikeButton
import com.like.OnLikeListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.TheApp
import pl.adriankremski.collectively.data.model.RemarkComment
import pl.adriankremski.collectively.data.repository.RemarksRepository
import pl.adriankremski.collectively.domain.interactor.remark.DeleteRemarkCommentVoteUseCase
import pl.adriankremski.collectively.domain.interactor.remark.SubmitRemarkCommentVoteUseCase
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread
import pl.adriankremski.collectively.presentation.extension.textInInt
import java.text.SimpleDateFormat
import javax.inject.Inject

class RemarkCommentRowHolder(val userId: String, itemView: View) : RecyclerView.ViewHolder(itemView), RemarkCommentMvp.View {
    @Inject
    lateinit var remarksRepository: RemarksRepository

    @Inject
    lateinit var ioThread: UseCaseThread

    @Inject
    lateinit var uiThread: PostExecutionThread

    private lateinit var presenter: RemarkCommentMvp.Presenter

    private var authorLabel: TextView = itemView.findViewById(R.id.authorLabel) as TextView
    private var commentLabel: TextView = itemView.findViewById(R.id.commentLabel) as TextView
    private var dateLabel: TextView = itemView.findViewById(R.id.dateLabel) as TextView

    private var positiveVotesCountLabel: TextView = itemView.findViewById(R.id.positiveVotesCountLabel) as TextView
    private var negativeVotesCountLabel: TextView = itemView.findViewById(R.id.negativeVotesCountLabel) as TextView

    private var voteUpButton: LikeButton = itemView.findViewById(R.id.voteUpButton) as LikeButton
    private var voteDownButton: LikeButton = itemView.findViewById(R.id.voteDownButton) as LikeButton

    private var dateFormat = SimpleDateFormat("HH:mm, dd MMM")

    private var votedUp: Boolean = false
    private var votedDown: Boolean = false
    private var compositeDisposable = CompositeDisposable()

    init {
        TheApp[itemView.context].appComponent?.inject(this)
    }

    fun setRemarkComment(remarkComment: RemarkComment) {
        resetVotes()

        presenter = RemarkCommentPresenter(remarkComment.remarkId, remarkComment.id, this,
                SubmitRemarkCommentVoteUseCase(remarksRepository, ioThread, uiThread),
                DeleteRemarkCommentVoteUseCase(remarksRepository, ioThread, uiThread))

        authorLabel.text = remarkComment.user?.name
        commentLabel.text = remarkComment.text
        dateLabel.text = dateFormat.format(remarkComment.creationDate())

        showPositiveVotes(remarkComment.positiveVotesCount())
        showNegativeVotes(remarkComment.negativeVotesCount())

        if (remarkComment.userVotedPositively(userId)) {
            showUserVotedPositively()
        } else if (remarkComment.userVotedNegatively(userId)) {
            showUserVotedNegatively()
        }

        compositeDisposable = CompositeDisposable()


        var voteUpButtonLikeListener = object : OnLikeListener {
            override fun liked(p0: LikeButton?) {
                voteUpLiked()
                presenter.submitPositiveVote()
            }

            override fun unLiked(p0: LikeButton?) {
                voteUpUnliked()
                presenter.deletePositiveVote()
            }
        }
        voteUpButton.setOnLikeListener(voteUpButtonLikeListener)

        var voteDownButtonLikeListener = object : OnLikeListener {
            override fun liked(p0: LikeButton?) {
                voteDownLiked()
                presenter.submitNegativeVote()
            }

            override fun unLiked(p0: LikeButton?) {
                voteDownUnliked()
                presenter.deleteNegativeVote()
            }
        }
        voteDownButton.setOnLikeListener(voteDownButtonLikeListener)
    }

    fun resetVotes() {
        voteUpButton.setLiked(false)
        positiveVotesCountLabel.setTextColor(ContextCompat.getColor(itemView.context, R.color.font_dark_hint))
        positiveVotesCountLabel.text = "0"

        voteDownButton.setLiked(false)
        negativeVotesCountLabel.setTextColor(ContextCompat.getColor(itemView.context, R.color.font_dark_hint))
        negativeVotesCountLabel.text = "0"
    }


    fun voteUpLiked() {
        var votesCount = positiveVotesCountLabel.textInInt()
        positiveVotesCountLabel.text = Integer.toString(votesCount+1)

        positiveVotesCountLabel.setTextColor(ContextCompat.getColor(itemView.context, R.color.vote_up_remark_color))
        negativeVotesCountLabel.setTextColor(ContextCompat.getColor(itemView.context, R.color.font_dark_hint))

        if (votedDown) {
            voteDownUnliked()
            voteDownButton.setLiked(false)
        }

        votedUp = true
        votedDown = false
    }

    fun voteUpUnliked() {
        var votesCount = positiveVotesCountLabel.textInInt()
        positiveVotesCountLabel.text = Integer.toString(votesCount-1)

        positiveVotesCountLabel.setTextColor(ContextCompat.getColor(itemView.context, R.color.font_dark_hint))

        votedUp = false
    }

    fun voteDownLiked() {
        var votesCount = negativeVotesCountLabel.textInInt()
        negativeVotesCountLabel.text = Integer.toString(votesCount+1)

        positiveVotesCountLabel.setTextColor(ContextCompat.getColor(itemView.context, R.color.font_dark_hint))
        negativeVotesCountLabel.setTextColor(ContextCompat.getColor(itemView.context, R.color.vote_down_remark_color))

        if (votedUp) {
            voteUpUnliked()
            voteUpButton.setLiked(false)
        }

        votedDown = true
        votedUp = false
    }

    fun voteDownUnliked() {
        var votesCount = negativeVotesCountLabel.textInInt()
        negativeVotesCountLabel.text = Integer.toString(votesCount-1)

        negativeVotesCountLabel.setTextColor(ContextCompat.getColor(itemView.context, R.color.font_dark_hint))

        votedDown = false
    }

    override fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun showPositiveVotes(positiveVotesCount: Int) {
        positiveVotesCountLabel.text = positiveVotesCount.toString()
    }

    fun showNegativeVotes(negativeVotesCount: Int) {
        negativeVotesCountLabel.text = negativeVotesCount.toString()
    }

    fun showUserVotedPositively() {
        votedUp = true
        votedDown = false

        voteUpButton.setLiked(true)
        positiveVotesCountLabel.setTextColor(ContextCompat.getColor(itemView.context, R.color.vote_up_remark_color))

        voteDownButton.setLiked(false)
        negativeVotesCountLabel.setTextColor(ContextCompat.getColor(itemView.context, R.color.font_dark_hint))
    }

    fun showUserVotedNegatively() {
        votedDown = true
        votedUp = false

        voteDownButton.setLiked(true)
        negativeVotesCountLabel.setTextColor(ContextCompat.getColor(itemView.context, R.color.vote_down_remark_color))

        voteUpButton.setLiked(false)
        positiveVotesCountLabel.setTextColor(ContextCompat.getColor(itemView.context, R.color.font_dark_hint))
    }
}