package com.example.pastestorage.dto.mapper;

import com.example.pastestorage.dto.response.PageUsersResponseDTO;
import com.example.pastestorage.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(
        componentModel = "spring",
        uses = {UserMapper.class})
public interface PageUsersMapper {
    @Mapping(target = "users", source = "content")
    @Mapping(target = "currentPage", source = "number")
    @Mapping(target = "totalItems", source = "totalElements")
    PageUsersResponseDTO toPageUsersResponseDTO(Page<User> userPage);
}
