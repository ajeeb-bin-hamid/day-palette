package com.day.palette.home.domain.usecase

import com.day.palette.home.domain.model.Holiday
import com.day.palette.home.domain.repository.HomeRemoteRepository
import com.day.palette.common.domain.utils.Errors
import com.day.palette.common.domain.utils.GenericResult
import javax.inject.Inject

class GetWorldWideHolidaysUseCase @Inject constructor(private val homeRemoteRepository: HomeRemoteRepository) {
    suspend fun execute(): GenericResult<ArrayList<Holiday>, Errors.Network> {
        return homeRemoteRepository.getWorldWideHolidays()
    }
}