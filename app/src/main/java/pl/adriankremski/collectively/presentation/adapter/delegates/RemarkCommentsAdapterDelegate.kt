package pl.adriankremski.collectively.presentation.adapter.delegates

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates.AbsAdapterDelegate
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.data.model.RemarkComment


class RemarkCommentsAdapterDelegate(viewType: Int): AbsAdapterDelegate<List<Any>>(viewType) {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is RemarkComment
    }

    override fun onBindViewHolder(items: List<Any>, position: Int, holder: RecyclerView.ViewHolder) {
        var remarkComment = items[position] as RemarkComment
        (holder as RemarkCommentRowHolder).setRemarkComment(remarkComment)
    }

    override fun onCreateViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent?.context).inflate(R.layout.view_remark_comment_row, parent, false)
        return RemarkCommentRowHolder(view)
    }

    class RemarkCommentRowHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var authorLabel: TextView = itemView.findViewById(R.id.authorLabel) as TextView
        private var commentLabel: TextView = itemView.findViewById(R.id.commentLabel) as TextView

        init {
        }

        fun setRemarkComment(remarkComment: RemarkComment) {
            authorLabel.text = remarkComment.user.name
            commentLabel.text = remarkComment.text
//            this.remark = remark
//            categoryIcon.setImageDrawable(ContextCompat.getDrawable(itemView.context, remark.category?.name?.iconOfCategory()!!))
//            nameLabel.text = remark.description
//            addressLabel.text = remark.location?.address
        }
    }

}

