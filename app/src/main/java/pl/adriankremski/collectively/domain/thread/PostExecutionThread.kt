package pl.adriankremski.collectively.domain.thread

import io.reactivex.Scheduler

interface PostExecutionThread {
    val scheduler: Scheduler
}
