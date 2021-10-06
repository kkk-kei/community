package community.mapper;

import community.dto.QuestionDTO;
import community.pojo.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface QuestionMapper {
    int insertQuestion(Question question);
    List<Question> findQuestionList();

    @Select("select * from question")
    List<Question> getAll();
}
