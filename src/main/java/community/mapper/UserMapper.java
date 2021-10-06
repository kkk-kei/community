package community.mapper;

import community.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    int insertUser(User user);
    User findByToken(String token);
    User findByID(Integer id);
    List<User> getAll();
}
