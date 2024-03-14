package ua.foxminded.WebProject.service;

import java.io.Serializable;

public interface AbstractService<T, ID extends Serializable> {
    T getById(ID id);
}
