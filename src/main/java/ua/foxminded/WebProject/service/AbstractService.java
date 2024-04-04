package ua.foxminded.WebProject.service;

import java.io.Serializable;
import java.util.List;

public interface AbstractService<T, ID extends Serializable> {
    T getById(ID id);
    List<T> getAll();
}
