package com.day.palette.domain.usecase

import com.day.palette.domain.model.Holiday
import com.day.palette.domain.repository.RemoteRepository
import com.day.palette.domain.utils.Errors
import com.day.palette.domain.utils.GenericResult
import javax.inject.Inject

class GetWorldWideHolidaysUseCase @Inject constructor(private val remoteRepository: RemoteRepository) {
    suspend fun execute(): GenericResult<ArrayList<Holiday>, Errors.Network> {
        return remoteRepository.getWorldWideHolidays()
    }
}