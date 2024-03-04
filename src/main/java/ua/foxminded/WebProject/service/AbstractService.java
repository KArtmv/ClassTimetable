package ua.foxminded.WebProject.service;

import java.io.Serializable;
import java.util.Optional;

public interface AbstractService<T, ID extends Serializable> {
    T getById(ID id);
}
