package com.example.test.Service.Impl;

import com.example.test.Entity.Type;
import com.example.test.Exception.TypeNotFoundException;
import com.example.test.Repository.TypeRepository;
import com.example.test.Service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    TypeRepository typeRepository;
    @Override
    public List<Type> listAll() {
        return (List<Type>) typeRepository.findAll();
    }

    @Override
    public boolean checkType(String name) {
        return typeRepository.existsByName(name);
    }

    @Override
    public Type getType(Integer id) throws TypeNotFoundException {
        Optional<Type> result = typeRepository.findById(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new TypeNotFoundException("Could not find any type with ID " + id);
    }

    @Override
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

    @Override
    public void deleteType(Integer id) {
        typeRepository.delete(typeRepository.getOne(id));
    }

    @Override
    public List<Type> findAllByTypeName(String keyword) {
        return typeRepository.findAllByNameContaining(keyword);
    }
}
