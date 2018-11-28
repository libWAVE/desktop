package com.libwave.desktop.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.libwave.desktop.domain.FileToIndex;

public interface FileToIndexDao extends PagingAndSortingRepository<FileToIndex, Integer> {
}
