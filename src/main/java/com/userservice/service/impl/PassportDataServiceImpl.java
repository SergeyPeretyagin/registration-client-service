package com.userservice.service.impl;

import com.userservice.service.PassportDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PassportDataServiceImpl implements PassportDataService {
}
