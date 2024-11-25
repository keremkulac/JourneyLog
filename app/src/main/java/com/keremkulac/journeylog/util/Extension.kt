package com.keremkulac.journeylog.util

import com.keremkulac.journeylog.data.local.model.AverageFuelPriceEntity
import com.keremkulac.journeylog.domain.model.AverageFuelPrice

fun AverageFuelPrice.toAverageFuelPriceEntity() = AverageFuelPriceEntity(
    id = 0,
    title = title,
    price = price
)