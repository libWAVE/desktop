package com.libwave.desktop.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.libwave.desktop.domain.Track;

public interface TrackDao extends PagingAndSortingRepository<Track, Integer> {
	Track findByUuid(String uuid);
}
