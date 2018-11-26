package com.libwave.desktop.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.libwave.desktop.domain.Config;

public interface ConfigDao extends PagingAndSortingRepository<Config, Integer> {
}
