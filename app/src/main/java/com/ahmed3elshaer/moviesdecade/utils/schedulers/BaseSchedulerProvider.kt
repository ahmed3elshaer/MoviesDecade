package com.ahmed3elshaer.moviesdecade.utils.schedulers

import io.reactivex.Scheduler

/**
 * Allow providing different types of [Scheduler]s.
 */
interface BaseSchedulerProvider {
  fun computation(): Scheduler

  fun io(): Scheduler

  fun ui(): Scheduler
}
