package community.mapper;

import community.model.Question;
import community.model.QuestionExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuestionExtMapper {
    int increaseView(Question record);
    int increaseComment(Question record);
    List<Question> selectRelated(Question record);
    List<Question> selectBySearch(@Param("search") String search);
}