package com.example.task_manger.util.mapper;

import com.example.task_manger.tasks.model.Task;
import com.example.task_manger.tasks.request.TaskRequest;
import com.example.task_manger.tasks.response.TaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ClassMapper {

    ClassMapper INSTANCE = Mappers.getMapper( ClassMapper.class );


    //entity to Dto
    TaskResponse entityToDto(Task task);

    //DTO to entity
    Task DtoToEntity(TaskRequest request);

}