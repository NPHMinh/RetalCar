package com.minhnphde180174.fu.hsf301assigment1.service.impl;

import com.minhnphde180174.fu.hsf301assigment1.entity.Producer;
import com.minhnphde180174.fu.hsf301assigment1.repository.ProducerRepository;
import com.minhnphde180174.fu.hsf301assigment1.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProducerService extends BaseService {
    private final ProducerRepository producerRepository;

    @Autowired
    public ProducerService(ProducerRepository producerRepository) {
        this.producerRepository = producerRepository;
    }
    public List<Producer> findAll() {
        return producerRepository.findAll();
    }

    public Producer findById(Long producerId) {
        return producerRepository.findById(producerId).orElse(null);
    }
}
