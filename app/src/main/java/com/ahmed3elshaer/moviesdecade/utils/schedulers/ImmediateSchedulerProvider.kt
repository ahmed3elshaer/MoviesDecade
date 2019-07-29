package com.ahmed3elshaer.moviesdecade.utils.schedulers

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 * Implementation of the [BaseSchedulerProvider] making all [Scheduler]s immediate.
 */
class ImmediateSchedulerProvider : BaseSchedulerProvider {
  override fun computation(): Scheduler = Schedulers.trampoline()

  override fun io(): Scheduler = Schedulers.trampoline()

  override fun ui(): Scheduler = Schedulers.trampoline()
}
