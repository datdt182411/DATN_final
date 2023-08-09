package com.example.test.Service;


import com.example.test.Entity.Type;
import com.example.test.Exception.TypeNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface TypeService {

    public List<Type> listAll();

    public boolean checkType(String name);

    public Type getType(Integer id) throws TypeNotFoundException;

    public Type saveType(Type type);

    public void deleteType(Integer id);

    public List<Type> findAllByTypeName(String keyword);
}
